package ru.galt.app.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity


fun AppCompatActivity.openGalleryIntent(
        selectRequestCode: Int,
        permissionRequestCode: Int,
        dialogTitle: String = "Галерея") {
    if (isPermissionsGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        val intent = Intent().apply {
            type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent,
                dialogTitle), selectRequestCode)
    } else {
        askPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionRequestCode)
    }
}


fun AppCompatActivity.isPermissionsGranted(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        true
    } else {
        checkSelfPermission(permission) ==
                PackageManager.PERMISSION_GRANTED
    }
}


fun AppCompatActivity.askPermissions(permissions: Array<String>, code: Int) {
    ActivityCompat.requestPermissions(this, permissions, code)
}
// getImagePath

fun AppCompatActivity.getPath(uri: Uri) = getImagePath(uri, this)


fun isExternalStorageDocument(uri: Uri) = "com.android.externalstorage.documents" == uri.authority

fun isDownloadsDocument(uri: Uri) = "com.android.providers.downloads.documents" == uri.authority

fun isMediaDocument(uri: Uri) = "com.android.providers.media.documents" == uri.authority

fun isGooglePhotosUri(uri: Uri) = "com.google.android.apps.photos.content" == uri.authority


fun getDataColumn(context: Context?, uri: Uri?, selection: String?,
                  selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = MediaStore.Images.Media.DATA
    try {
        cursor = context?.contentResolver?.query(uri, arrayOf(column), selection, selectionArgs,
                null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return ""
}

@SuppressLint("NewApi")
fun getImagePath(uri: Uri, context: Context?): String? {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    // DocumentProvider
    try {
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                }
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)

            }// DownloadsProvider
            else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        }
        // MediaStore (and general)
        else if ("content".equals(uri.scheme, true)) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.lastPathSegment

            return getDataColumn(context, uri, null, null)
        } else {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            //Cursor cursor = managedQuery(uri, projection, null, null, null);
            val cursor = context?.contentResolver?.query(uri, projection,
                    null, null, null)
            return if (cursor != null) {
                //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
                //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
                val uri = MediaStore.Images.Media.DATA
                val columnIndex = cursor
                        .getColumnIndexOrThrow(uri)
                cursor.moveToFirst()
                val ret = cursor.getString(columnIndex)
                cursor.close()
                ret
            } else {
                ""
            }
        }
    } catch (ignored: Exception) {
        return ""
    }
    return ""
}

//
package com.tony.vkimage.presentation.view.movigViewsLayout.detectors

import android.util.Log
import android.view.MotionEvent


class RotationGestureDetector(listener: OnRotationGestureListener) {

    private val ZERO = 0F
    private val INVALID_POINTER_ID = -1

    private var fX: Float = 0.toFloat()
    private var fY: Float = 0.toFloat()
    private var sX: Float = 0.toFloat()
    private var sY: Float = 0.toFloat()

    private var ptrID1: Int = INVALID_POINTER_ID
    private var ptrID2: Int = INVALID_POINTER_ID
    private var angel: Float = ZERO
    private var prevAngel: Float = ZERO

    private var mListener: OnRotationGestureListener? = listener

    fun getAngle(): Float {
        return angel - prevAngel
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                ptrID1 = event.getPointerId(event.actionIndex)
                ptrID2 = event.getPointerId(event.actionIndex)
                sX = event.getX(event.findPointerIndex(ptrID1))
                sY = event.getY(event.findPointerIndex(ptrID1))
                fX = event.getX(event.findPointerIndex(ptrID2))
                fY = event.getY(event.findPointerIndex(ptrID2))
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                try {
                    ptrID2 = event.getPointerId(event.actionIndex)
                    sX = event.getX(event.findPointerIndex(ptrID1))
                    sY = event.getY(event.findPointerIndex(ptrID1))
                    fX = event.getX(event.findPointerIndex(ptrID2))
                    fY = event.getY(event.findPointerIndex(ptrID2))
                } catch (e: IllegalArgumentException) {
                    Log.d("MEINE","ERRROR")
                }
            }
            MotionEvent.ACTION_MOVE -> if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
                val nfX: Float = event.getX(event.findPointerIndex(ptrID2))
                val nfY: Float = event.getY(event.findPointerIndex(ptrID2))
                val nsX: Float = event.getX(event.findPointerIndex(ptrID1))
                val nsY: Float = event.getY(event.findPointerIndex(ptrID1))

                angel = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY)
                if (mListener != null) {
                    mListener?.onRotation(this)
                }
                prevAngel = angel
            }
            MotionEvent.ACTION_UP -> {
                ptrID1 = INVALID_POINTER_ID
                prevAngel = ZERO
            }
            MotionEvent.ACTION_POINTER_UP -> {
                ptrID2 = INVALID_POINTER_ID
                prevAngel = ZERO
            }
            MotionEvent.ACTION_CANCEL -> {
                prevAngel = ZERO
                ptrID1 = INVALID_POINTER_ID
                ptrID2 = INVALID_POINTER_ID
            }
        }
        return true
    }

    private fun angleBetweenLines(fX: Float, fY: Float, sX: Float, sY: Float, nfX: Float, nfY: Float, nsX: Float, nsY: Float): Float {
        val angle1 = Math.atan2((fY - sY).toDouble(), (fX - sX).toDouble()).toFloat()
        val angle2 = Math.atan2((nfY - nsY).toDouble(), (nfX - nsX).toDouble()).toFloat()

        var angle = Math.toDegrees((angle1 - angle2).toDouble()).toFloat() % 360
        if (angle < -180f) angle += 360.0f
        if (angle > 180f) angle -= 360.0f
        return angle
    }

    interface OnRotationGestureListener {
        fun onRotation(rotationDetector: RotationGestureDetector)
    }
}
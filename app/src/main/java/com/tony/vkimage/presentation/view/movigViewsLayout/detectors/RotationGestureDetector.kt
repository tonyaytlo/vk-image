package com.tony.vkimage.presentation.view.movigViewsLayout.detectors

import android.view.MotionEvent


class RotationGestureDetector(listener: OnRotationGestureListener) {

    companion object {
        private const val ZERO = 0F
        private const val INVALID_POINTER_ID = -1
    }

    private var fX: Float = 0.toFloat()
    private var fY: Float = 0.toFloat()
    private var sX: Float = 0.toFloat()
    private var sY: Float = 0.toFloat()

    private var fPtrId: Int = INVALID_POINTER_ID
    private var sPtrId: Int = INVALID_POINTER_ID
    private var angel: Float = ZERO
    private var prevAngel: Float = ZERO

    private var mListener: OnRotationGestureListener? = listener

    fun getAngle(): Float {
        return angel - prevAngel
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                fPtrId = event.getPointerId(event.actionIndex)
                sPtrId = event.getPointerId(event.actionIndex)
                sX = event.getX(event.findPointerIndex(fPtrId))
                sY = event.getY(event.findPointerIndex(fPtrId))
                fX = event.getX(event.findPointerIndex(sPtrId))
                fY = event.getY(event.findPointerIndex(sPtrId))
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                sPtrId = event.getPointerId(event.actionIndex)
                sX = event.getX(event.findPointerIndex(fPtrId))
                sY = event.getY(event.findPointerIndex(fPtrId))
                fX = event.getX(event.findPointerIndex(sPtrId))
                fY = event.getY(event.findPointerIndex(sPtrId))
            }
            MotionEvent.ACTION_MOVE -> if (fPtrId != INVALID_POINTER_ID && sPtrId != INVALID_POINTER_ID) {
                val nfX: Float = event.getX(event.findPointerIndex(sPtrId))
                val nfY: Float = event.getY(event.findPointerIndex(sPtrId))
                val nsX: Float = event.getX(event.findPointerIndex(fPtrId))
                val nsY: Float = event.getY(event.findPointerIndex(fPtrId))

                angel = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY)
                if (mListener != null) {
                    mListener?.onRotation(this)
                }
                prevAngel = angel
            }
            MotionEvent.ACTION_UP -> {
                fPtrId = INVALID_POINTER_ID
                prevAngel = ZERO
            }
            MotionEvent.ACTION_POINTER_UP -> {
                sPtrId = INVALID_POINTER_ID
                prevAngel = ZERO
            }
            MotionEvent.ACTION_CANCEL -> {
                prevAngel = ZERO
                fPtrId = INVALID_POINTER_ID
                sPtrId = INVALID_POINTER_ID
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
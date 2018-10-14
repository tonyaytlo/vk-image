package com.tony.vkimage.presentation.view.movigViewsLayout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PointF
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import com.tony.tinkoffnews.extension.makeInvisible
import com.tony.tinkoffnews.extension.makeVisible
import com.tony.vkimage.presentation.view.movigViewsLayout.detectors.RotationGestureDetector
import ru.galt.app.extensions.dpToPx

class MovingViewsLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), RotationGestureDetector.OnRotationGestureListener,
        ScaleGestureDetector.OnScaleGestureListener {

    private var trash: TrashFab? = null //TICK mb not-null
    private var selectedView: View? = null

    private val TAG = "MovingViewsLayout"
    private val MIN_ZOOM = 1.0f
    private val MAX_ZOOM = 4.0f
    private val INVALID_POINTER_ID = -1


    private var activePointerId = INVALID_POINTER_ID
    private var container: SparseArray<Parcelable>? = null

    private var lastTouch: PointF? = null
    private var scale = 1.0f
    private var lastScaleFactor = 0f

    private val scaleDetector: ScaleGestureDetector
    private val rotationDetector: RotationGestureDetector

    init {
        container = SparseArray(5)

        rotationDetector = RotationGestureDetector(this)
        scaleDetector = ScaleGestureDetector(context, this)

        trash = TrashFab(context)
        trash?.layoutParams = LayoutParams(LayoutParams(32.dpToPx, 32.dpToPx))
        trash?.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        addView(trash)
        onViewUnselected()
    }


    override fun checkLayoutParams(p: ViewGroup.LayoutParams) =
            super.checkLayoutParams(p) && p is LayoutParams

    override fun generateLayoutParams(attrs: AttributeSet) = LayoutParams(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = View.MeasureSpec.getSize(widthMeasureSpec)
        var height = View.MeasureSpec.getSize(heightMeasureSpec)

        width = Math.max(width, minimumWidth)
        height = Math.max(height, minimumHeight)

        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val mp = view.layoutParams as LayoutParams
            view.measure(View.MeasureSpec.makeMeasureSpec(width - mp.leftMargin - mp.rightMargin, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(height - mp.topMargin - mp.bottomMargin, View.MeasureSpec.AT_MOST))
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(p0: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (selectedView == null) {
            doInitialLayout(l, t, r, b, childCount)
        }
    }

    private fun doInitialLayout(l: Int, t: Int, r: Int, b: Int, count: Int) {
        for (i in 0 until count) {
            val view = getChildAt(i)
            val mp = view.layoutParams as LayoutParams
            val width = view.measuredWidth
            val height = view.measuredHeight

            val left: Int
            val top: Int
            val right: Int
            val bottom: Int

            if (view.visibility != View.GONE && !mp.moved) {
                if (height > b || l + width > r) {
                    Toast.makeText(context, "Couldn't fit a child View, skipping it", Toast.LENGTH_SHORT)
                            .show()
                    Log.d("MEIN", "Couldn't fit a child View, skipping it")
                    continue
                }

                val centerH = (b - t) / 2
                val centerW = (r - l) / 2

                left = centerW - width / 2
                right = centerW + width / 2


                if (view is TrashFab) {
                    val bottomMargin = 16.dpToPx
                    bottom = (b) - bottomMargin - t
                    top = (b - (height)) - bottomMargin - t
                } else {
                    bottom = centerH + height / 2
                    top = centerH - height / 2
                }

                view.layout(left, top, right, bottom)
                mp.left = left.toFloat()
                mp.top = top.toFloat()

            } else if (mp.moved && view !== selectedView) {
                val x1 = Math.round(mp.left)
                val y1 = Math.round(mp.top)
                val x2 = Math.round(mp.left) + width
                val y2 = Math.round(mp.top) + height
                view.layout(x1, y1, x2, y2)
            }
        }
    }

    /**
     * this method can be used to force layout on a child
     * to recalculate its hit-rect,
     * otherwise outline border of the selected child is
     * drawn at the old position
     */
    private fun layoutSelectedChild(lp: LayoutParams) {
        val l = Math.round(lp.left)
        val t = Math.round(lp.top)
        val r = l + selectedView!!.measuredWidth
        val b = t + selectedView!!.measuredHeight
        lp.moved = true
        selectedView!!.layout(l, t, r, b)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        rotationDetector.onTouchEvent(event)
        scaleDetector.onTouchEvent(event)
        moveDetector(event)
        return true
    }

    private fun moveDetector(event: MotionEvent) {
        val x = event.x
        val y = event.y

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                prepareTouch(x, y)
                activePointerId = event.getPointerId(0)
            }
            MotionEvent.ACTION_POINTER_DOWN -> if (selectedView == null) {
                val pointerIndex = event.actionIndex
                val xx = event.getX(pointerIndex)
                val yy = event.getY(pointerIndex)
                prepareTouch(xx, yy)
                if (selectedView != null) {
                    activePointerId = event.getPointerId(pointerIndex)
                }
            }
            MotionEvent.ACTION_MOVE -> if (selectedView != null && lastTouch != null) {

                val lp = selectedView?.layoutParams as LayoutParams

                val pointerIndex = event.findPointerIndex(activePointerId)
                val curX = event.getX(pointerIndex)
                val curY = event.getY(pointerIndex)

                checkTrashTouch(curX, curY)


                val dx = curX - lastTouch!!.x//RRRR
                val dy = curY - lastTouch!!.y

                lp.left += dx
                lp.top += dy

                lastTouch?.x = curX
                lastTouch?.y = curY

                /* layout child otherwise hit-rect is not recalculated */
                layoutSelectedChild(lp)
                invalidate()

            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = event.actionIndex
                val pointerId = event.getPointerId(pointerIndex)
                //change active pointer
                if (pointerId == activePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    if (lastTouch != null && selectedView != null) {
                        lastTouch?.x = event.getX(newPointerIndex)
                        lastTouch?.y = event.getY(newPointerIndex)
                    }
                    activePointerId = event.getPointerId(newPointerIndex)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                activePointerId = INVALID_POINTER_ID
                deleteViewIfNeed(x, y)
                cancelSelect()
            }
            else -> {
                activePointerId = INVALID_POINTER_ID
                cancelSelect()
            }
        }
    }

//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        val x = ev.x
//        val y = ev.y
//        if (ev.actionMasked == MotionEvent.ACTION_MOVE) {
//            prepareTouch(x, y)
//            return true
//        }
//        return false
//    }

    private fun cancelSelect() {
        if (selectedView != null) {
            selectedView?.makeVisible()
            selectedView = null
        }
        onViewUnselected()
    }


    private fun checkTrashTouch(x: Float, y: Float): Boolean {
        val ix = Math.round(x)
        val iy = Math.round(y)
        if (trash != null) {
            val rect = Rect()
            trash!!.getHitRect(rect)
            return if (rect.contains(ix, iy)) {
                trash!!.openTrash()
                selectedView?.alpha = 0.6F
                true
            } else {
                selectedView?.alpha = 1F
                trash!!.closeTrash()
                false
            }
        }
        return false
    }

    private fun deleteViewIfNeed(x: Float, y: Float) {
        if (checkTrashTouch(x, y)) {
            removeView(selectedView)
        }
    }

    private fun prepareTouch(x: Float, y: Float) {
        lastTouch = null
        selectedView = findChildViewInsideTouch(Math.round(x), Math.round(y))
        if (selectedView != null) {
            onViewSelected()
            bringChildToFront(selectedView)
            lastTouch = PointF(x, y)
        }
    }

    private fun onViewSelected() {
        scale = selectedView?.scaleX ?: MIN_ZOOM
        trash?.makeVisible()
    }

    private fun onViewUnselected() {
        trash?.makeInvisible()
    }

    override fun onRotation(rotationDetector: RotationGestureDetector) {
        if (selectedView != null) {
            selectedView?.rotation = selectedView!!.getRotation() - rotationDetector.getAngle() // not replace kotlin
        }
    }

    override fun onScaleBegin(scaleDetector: ScaleGestureDetector?) = true

    override fun onScaleEnd(p0: ScaleGestureDetector?) {}

    override fun onScale(sDetecor: ScaleGestureDetector?): Boolean {
        if (selectedView == null) {
            return false
        }
        val scaleFactor = scaleDetector.scaleFactor
        if (lastScaleFactor == 0f || Math.signum(scaleFactor) == Math.signum(lastScaleFactor)) {
            scale *= scaleFactor
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM))
            lastScaleFactor = scaleFactor
        } else {
            lastScaleFactor = 0f
        }
        selectedView?.setScaleX(scale)
        selectedView?.setScaleY(scale)
        return true
    }


    private fun findChildViewInsideTouch(x: Int, y: Int): View? {
        for (i in childCount - 1 downTo 0) {
            val view = getChildAt(i)
            if (view is TrashFab) {
                continue
            }
            val rect = Rect()
            view.getHitRect(rect)
            if (rect.contains(x, y)) {
                return view
            }
        }
        return null
    }

    class LayoutParams : ViewGroup.MarginLayoutParams {
        internal var left: Float = 0.toFloat()
        internal var top: Float = 0.toFloat()
        internal var moved: Boolean = false

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs)

        constructor(lp: ViewGroup.LayoutParams) : super(lp)

        init {
            left = -1.0f
            top = -1.0f
            moved = false
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        container = state.container
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val lp = view.layoutParams as LayoutParams

            if (view.id != View.NO_ID) {
                val s = container!!.get(view.id) as SavedState
                lp.left = s.left
                lp.top = s.top
                lp.moved = s.movedFlag
                addView(view)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val lp = view.layoutParams as LayoutParams
            view.saveHierarchyState(container)

            if (view.id != View.NO_ID) {
                val s = SavedState(container!!.get(view.id))
                s.left = lp.left
                s.top = lp.top
                s.movedFlag = lp.moved
                container?.put(view.id, s)
            }
        }
        val p = super.onSaveInstanceState()
        val ss = SavedState(p)
        ss.container = container
        return ss
    }

    private class SavedState(p: Parcelable) : View.BaseSavedState(p) {
        internal var left: Float = 0.toFloat()
        internal var top: Float = 0.toFloat()
        internal var movedFlag: Boolean = false
        internal var container: SparseArray<Parcelable>? = null
    }


    override fun toString(): String {
        val out = StringBuilder(128)
        out.append(TAG)
        out.append(" mSelectedChild: ")
        if (selectedView != null) {
            out.append(this.selectedView.toString())
        }
        return out.toString()
    }

}
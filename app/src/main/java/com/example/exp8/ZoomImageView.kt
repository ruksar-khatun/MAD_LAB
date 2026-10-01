package com.example.exp8

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView

class ZoomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val imageMatrixVal = Matrix()
    private val prevMatrix = Matrix()
    private var mode = MODE_NONE

    private val lastTouch = PointF()
    private val startTouch = PointF()
    private var minScale = 1.0f
    private var maxScale = 5.0f
    private var currentScale = 1.0f

    private val matrixValues = FloatArray(9)
    private var viewWidth = 0
    private var viewHeight = 0

    private val scaleDetector: ScaleGestureDetector
    private val gestureDetector: GestureDetector

    companion object {
        private const val MODE_NONE = 0
        private const val MODE_DRAG = 1
        private const val MODE_ZOOM = 2
    }

    init {
        scaleType = ScaleType.MATRIX
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        fitToScreen()
    }

    fun fitToScreen() {
        val currDrawable = drawable ?: return
        val imageWidth = currDrawable.intrinsicWidth
        val imageHeight = currDrawable.intrinsicHeight

        if (imageWidth == 0 || imageHeight == 0 || viewWidth == 0 || viewHeight == 0) return

        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
        val scale = minOf(scaleX, scaleY)

        imageMatrixVal.setScale(scale, scale)

        var redundantYSpace = viewHeight.toFloat() - (scale * imageHeight.toFloat())
        var redundantXSpace = viewWidth.toFloat() - (scale * imageWidth.toFloat())

        redundantYSpace /= 2f
        redundantXSpace /= 2f

        imageMatrixVal.postTranslate(redundantXSpace, redundantYSpace)

        currentScale = 1.0f
        minScale = 1.0f

        imageMatrix = imageMatrixVal
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        post { fitToScreen() }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        post { fitToScreen() }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        post { fitToScreen() }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        scaleDetector.onTouchEvent(event)

        val curr = PointF(event.x, event.y)

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                lastTouch.set(curr)
                startTouch.set(lastTouch)
                mode = MODE_DRAG
                prevMatrix.set(imageMatrixVal)
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == MODE_DRAG) {
                    val deltaX = curr.x - lastTouch.x
                    val deltaY = curr.y - lastTouch.y
                    if (currentScale > minScale) {
                        imageMatrixVal.postTranslate(deltaX, deltaY)
                        constrainTranslation()
                        imageMatrix = imageMatrixVal
                    }
                    lastTouch.set(curr.x, curr.y)
                }
            }
            MotionEvent.ACTION_UP -> {
                performClick()
                mode = MODE_NONE
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mode = MODE_NONE
            }
        }
        return true
    }

    private fun constrainTranslation() {
        imageMatrixVal.getValues(matrixValues)
        val transX = matrixValues[Matrix.MTRANS_X]
        val transY = matrixValues[Matrix.MTRANS_Y]

        val currDrawable = drawable ?: return
        val imageWidth = currDrawable.intrinsicWidth * matrixValues[Matrix.MSCALE_X]
        val imageHeight = currDrawable.intrinsicHeight * matrixValues[Matrix.MSCALE_Y]

        val fixTransX = getFixTrans(transX, viewWidth.toFloat(), imageWidth)
        val fixTransY = getFixTrans(transY, viewHeight.toFloat(), imageHeight)

        if (fixTransX != 0f || fixTransY != 0f) {
            imageMatrixVal.postTranslate(fixTransX, fixTransY)
        }
    }

    private fun getFixTrans(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float

        if (contentSize <= viewSize) {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }

        if (trans < minTrans) return -trans + minTrans
        if (trans > maxTrans) return -trans + maxTrans
        return 0f
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = MODE_ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val origScale = currentScale
            currentScale *= mScaleFactor

            if (currentScale > maxScale) {
                currentScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (currentScale < minScale) {
                currentScale = minScale
                mScaleFactor = minScale / origScale
            }

            val currDrawable = drawable ?: return true
            val imageWidth = currDrawable.intrinsicWidth * currentScale
            val imageHeight = currDrawable.intrinsicHeight * currentScale

            if (imageWidth <= viewWidth || imageHeight <= viewHeight) {
                imageMatrixVal.postScale(mScaleFactor, mScaleFactor, viewWidth / 2f, viewHeight / 2f)
            } else {
                imageMatrixVal.postScale(mScaleFactor, mScaleFactor, detector.focusX, detector.focusY)
            }

            constrainTranslation()
            imageMatrix = imageMatrixVal
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (currentScale > 1.0f) {
                fitToScreen()
            } else {
                imageMatrixVal.postScale(2.5f, 2.5f, e.x, e.y)
                currentScale = 2.5f
                constrainTranslation()
                imageMatrix = imageMatrixVal
            }
            return true
        }
    }
}
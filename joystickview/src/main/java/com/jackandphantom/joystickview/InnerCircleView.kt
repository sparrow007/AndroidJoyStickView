package com.jackandphantom.joystickview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

internal class InnerCircleView : View {
    private var circleRadius = 0f
    private val circlePaint = Paint()
    private var centerPoint = 0f
    private var centerPointX = 0f
    private var centerPointY = 0f
    private var limitRadius = 0f
    private var width = 0
    private var height = 0
    private var changeLayoutManual = false
    private var isInsideCircle = false
    private var bitmap: Bitmap? = null
    var lockCenter = false
    var innerCircleFactor = 0.1f
        private set
    private var outerCircleFactor = 0.4f
    private var strength = 0f
    private var bitmapDrawFactor = 0f
    private var onMoveListener: OnSmallMoveListener? = null

    constructor(context: Context?) : super(context)

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init()
    }

    //Initializing paint methods attributes
    private fun init() {
        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.FILL
        circlePaint.color = Color.BLACK
    }

    /**
     * This view can change it's width and height according to the changeLayoutManual and it calculating the
     * centerPoint which is mid point and both circle in joystick is making the ratio for their respective
     * size
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!changeLayoutManual) {
            width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
            height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
            setMeasuredDimension(width, height)
        } else {
            setMeasuredDimension(width, height)
        }
        val min = min(width, height)
        centerPoint = (min / 2).toFloat()
        centerPointX = centerPoint
        centerPointY = centerPoint
        circleRadius = min * innerCircleFactor
        limitRadius = min * outerCircleFactor
        bitmapDrawFactor = circleRadius * 0.5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerPointX, centerPointY, circleRadius, circlePaint)
        if (bitmap != null)
            canvas.drawBitmap(bitmap!!, centerPointX - bitmapDrawFactor, centerPointY - bitmapDrawFactor, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        angle(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (!lockCenter || strength < 100) {
                    centerPointY = centerPoint
                    centerPointX = centerPoint
                }
                invalidate()
            }
            MotionEvent.ACTION_DOWN -> handleMotion(event.x, event.y)
            MotionEvent.ACTION_MOVE -> handleMotionEvent(event.x, event.y)
        }
        return true
    }

    //Use some maths for make joystick working well
    private fun handleMotionEvent(x: Float, y: Float) {
        val abs = sqrt((x - centerPoint).toDouble().pow(2.0) + (y - centerPoint).toDouble().pow(2.0))
        onMoveListener?.onMove(angle(x, y), getStrength(abs))
        if (abs <= limitRadius) {
            centerPointX = x
            centerPointY = y
            invalidate()
        } else {
            if (isInsideCircle) {
                centerPointX = ((x - centerPoint) * limitRadius / abs + centerPoint).toFloat()
                centerPointY = ((y - centerPoint) * limitRadius / abs + centerPoint).toFloat()
                invalidate()
            }
        }
    }

    private fun angle(x: Float, y: Float): Double {
        val angle = Math.toDegrees(atan2((y - centerPoint).toDouble(), (x - centerPoint).toDouble()))
        return if (angle > 0) 360 - angle else -angle
    }

    private fun getStrength(distanceGiven: Double): Float {
        var distance = distanceGiven
        if (distance > limitRadius) {
            distance = limitRadius.toDouble()
        }
        return (distance / limitRadius * 100).toFloat().also { strength = it }
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        val bitmap: Bitmap
        if (drawable == null) {
            return null
        }
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        if (drawable.intrinsicWidth > 0 && drawable.intrinsicHeight > 0) {
            bitmap = Bitmap.createBitmap(circleRadius.toInt(), circleRadius.toInt(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        return null
    }

    fun setOnMoveListener(onMoveListener: OnSmallMoveListener?) {
        this.onMoveListener = onMoveListener
    }

    private fun handleMotion(x: Float, y: Float) {
        if (sqrt((x - centerPoint).toDouble().pow(2.0) + (y - centerPoint).toDouble().pow(2.0)) <= limitRadius) {
            centerPointX = x
            centerPointY = y
            isInsideCircle = true
            invalidate()
        } else {
            isInsideCircle = false
        }
    }

    /**
     * Getter and Setter Methods
     */
    fun setWidthAndHeight(width: Int, height: Int) {
        changeLayoutManual = true
        this.width = width
        this.height = height
        requestLayout()
    }

    fun setCirclePaintColor(color: Int) {
        circlePaint.color = color
        invalidate()
    }

    fun setImageDrawable(drawable: Drawable?) {
        bitmap = getBitmapFromDrawable(drawable)
        invalidate()
    }

    fun setImageResId(resId: Int) {
        val drawable: Drawable? = try {
            AppCompatResources.getDrawable(context, resId)
        } catch (e: Exception) {
            return
        }
        bitmap = getBitmapFromDrawable(drawable)
        invalidate()
    }

    fun setInnerCircleFactor(innerFactor: Float, outerCircleFactor: Float) {
        innerCircleFactor = innerFactor
        this.outerCircleFactor = outerCircleFactor
        invalidate()
    }
}

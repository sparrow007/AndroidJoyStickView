package com.jackandphantom.joystickview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

internal class OuterCircleView : View {
    private var centerPoint = 0f
    private var circleRadius = 0f
    private val circlePaint = Paint()
    private val borderCirclePaint = Paint()
    private var changeLayoutManual = false
    private var width = 0
    private var height = 0
    var shadowRadius = 9.0f
    var dx = 5.0f
        private set
    var dy = 5.0f
        private set

    //This makes circle smaller as much we want
    private var circleFactor = 0.4f
    private var shadowColor = Color.RED

    constructor(context: Context?) : super(context)

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init()
    }

    /*
     * Initialize paint attributes to add effect on circle
     * */
    private fun init() {
        circlePaint.isAntiAlias = true
        circlePaint.color = Color.WHITE
        circlePaint.style = Paint.Style.FILL
        borderCirclePaint.isAntiAlias = true
        borderCirclePaint.style = Paint.Style.STROKE
        setShadow()
    }

    /*
     * @variable changeLayoutManual means we are changing the width and height of OuterCircleView
     * so when we want to change it's layout size then we only need to make changeLayoutManual to true
     * also calculating the radius and centerPoint for drawing circle
     * */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (changeLayoutManual) {
            setMeasuredDimension(width, height)
        } else {
            width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
            height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
            setMeasuredDimension(width, height)
        }
        val min = min(width, height)
        centerPoint = (min / 2).toFloat()
        circleRadius = min * circleFactor
    }

    /*
     * first circle is works as background for joystick and second one is for border of the first circle
     * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerPoint, centerPoint, circleRadius, borderCirclePaint)
        canvas.drawCircle(centerPoint, centerPoint, circleRadius, circlePaint)
    }

    /*
     * This method will call when we need to change size of this view
     * */
    fun setWidthAndHeight(width: Int, height: Int) {
        changeLayoutManual = true
        this.width = width * 2
        this.height = height * 2
        requestLayout()
    }

    private fun setShadow() {
        setLayerType(LAYER_TYPE_SOFTWARE, borderCirclePaint)
        borderCirclePaint.setShadowLayer(shadowRadius, -dx, dy, shadowColor)
    }

    /**
     * Setter and Getter methods
     */
    fun setBorderStrokeWidth(width: Float) {
        borderCirclePaint.strokeWidth = width
        invalidate()
    }

    fun setShadowRadius(radius: Float) {
        shadowRadius = radius
        setShadow()
        invalidate()
    }

    fun setCircleColor(color: Int) {
        circlePaint.color = color
        invalidate()
    }

    fun setBorderCircleColor(color: Int) {
        borderCirclePaint.color = color
        invalidate()
    }

    fun setShadowDxDy(dx: Float, dy: Float) {
        this.dx = dx
        this.dy = dy
        setShadow()
        invalidate()
    }

    fun setShadowColor(color: Int) {
        shadowColor = color
        setShadow()
        invalidate()
    }

    fun setOuterCircle(circleFactor: Float) {
        this.circleFactor = circleFactor
        requestLayout()
    }

    val outerCircleBorderWidth: Float
        get() = borderCirclePaint.strokeWidth

    fun getShadowRadius(): Float {
        return shadowRadius
    }
}
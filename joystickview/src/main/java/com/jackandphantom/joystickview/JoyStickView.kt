package com.jackandphantom.joystickview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jackandphantom.joystickview.InnerCircleView.OnSMallMoveListener

class JoyStickView : FrameLayout, OnSMallMoveListener {
    private var outerCircleView: OuterCircleView? = null
    private var innerCircleView: InnerCircleView? = null
    private var outerCircleBorderWidth = 0f
    private var outerCircleBorderColor = 0
    private var outerCircleColor = 0
    private var lockCenter = false
    private var innerCircleRadius = 0f
    private var shadowRadius = 0f
    private var dx = 0f
    private var dy = 0f
    private var innerCircleColor = 0
    private var innerCircleImage = 0
    private var shadowColor = 0

    @Suppress("MemberVisibilityCanBePrivate")
    var onMoveListener: OnMoveListener? = null

    override fun onMove(angle: Double, strength: Float) {
        if (onMoveListener != null) {
            onMoveListener!!.onMove(angle, strength)
        }
    }

    interface OnMoveListener {
        fun onMove(angle: Double, strength: Float)
    }

    constructor(context: Context) : super(context)

    //Taking all the inputs from xml if available
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(attrs, R.styleable.JoyStickView, 0, 0).let { a ->
            try {
                outerCircleBorderWidth = a.getFloat(R.styleable.JoyStickView_outerCircleBorderWidth, 0f)
                outerCircleBorderColor = a.getColor(R.styleable.JoyStickView_outerCircleBorderColor, Color.WHITE)
                innerCircleColor = a.getColor(R.styleable.JoyStickView_innerCircleColor, Color.BLACK)
                outerCircleColor = a.getColor(R.styleable.JoyStickView_outerCircleColor, Color.WHITE)
                shadowColor = a.getColor(R.styleable.JoyStickView_shadowColor, Color.BLACK)
                lockCenter = a.getBoolean(R.styleable.JoyStickView_lockCenter, false)
                innerCircleRadius = a.getFloat(R.styleable.JoyStickView_innerCircleRadius, 0.1f)
                shadowRadius = a.getFloat(R.styleable.JoyStickView_shadowRadius, 0f)
                dx = a.getFloat(R.styleable.JoyStickView_shadowDx, 5.0f)
                dy = a.getFloat(R.styleable.JoyStickView_shadowDy, 5.0f)
                innerCircleImage = a.getResourceId(R.styleable.JoyStickView_innerCircleImage, 0)
            } finally {
                a.recycle()
            }
        }
        init()
    }

    /*
    * Only initialize those value which related to paint not to size of OuterCircleView and InnerCircleView
    * if you try then you will get the error
    * */
    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.joystick_layout, this, true)
        outerCircleView = view.findViewById(R.id.outerCircle)
        innerCircleView = view.findViewById(R.id.innerCircle)
        innerCircleView?.setOnMoveListener(this)
        setLockCenter(lockCenter)
        setInnerCircleColor(innerCircleColor)
        setInnerCircleRadius(innerCircleRadius)
        setOuterCircleBorderColor(outerCircleBorderColor)
        setOuterCircleColor(outerCircleColor)
        setOuterCircleBorderStrokeWidth(outerCircleBorderWidth)
        setShadowRadius(shadowRadius)
        setShadowDxAndDy(dx, dy)
        setShadowColor(shadowColor)
    }

    fun setOnMoveListener(onMoveListener: OnMoveListener?) {
        this.onMoveListener = onMoveListener
    }

    /**
     * We re measure the size of the OuterCircleView and InnerCircleView so init() method only initialize the
     * paint related attributes  */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val actualWidth = w / 2
        val actualHeight = h / 2
        outerCircleView!!.setWidthAndHeight(actualWidth, actualHeight)
        innerCircleView!!.setWidthAndHeight(w, h)
        setInnerCircleImageResId(innerCircleImage)
    }

    fun setOuterCircleBorderStrokeWidth(width: Float) {
        outerCircleView!!.setBorderStrokeWidth(width)
    }

    fun setLockCenter(lockCenter: Boolean) {
        innerCircleView!!.lockCenter = lockCenter
    }

    fun setInnerCircleRadius(radius: Float) {
        val outerCircleFactor = 0.5f - radius
        outerCircleView!!.setOuterCircle(outerCircleFactor)
        innerCircleView!!.setInnerCircleFactor(radius, outerCircleFactor)
    }

    fun setInnerCircleColor(color: Int) {
        innerCircleView!!.setCirclePaintColor(color)
    }

    fun setInnerCircleImageDrawable(drawable: Drawable?) {
        innerCircleView!!.setImageDrawable(drawable)
    }

    fun setInnerCircleImageResId(resId: Int) {
        try {
            innerCircleView!!.setImageResId(resId)
        } catch (exp: IllegalArgumentException) {
            exp.printStackTrace()
            innerCircleImage = resId
        }
    }

    fun setShadowRadius(radius: Float) {
        outerCircleView!!.shadowRadius = radius
    }

    fun setShadowColor(color: Int) {
        outerCircleView!!.setShadowColor(color)
    }

    fun setOuterCircleColor(color: Int) {
        outerCircleView!!.setCircleColor(color)
    }

    fun setShadowDxAndDy(dx: Float, dy: Float) {
        outerCircleView!!.setShadowDxDy(dx, dy)
    }

    fun setOuterCircleBorderColor(color: Int) {
        outerCircleView!!.setBorderCircleColor(color)
    }

    fun getLockCenter(): Boolean {
        return innerCircleView!!.lockCenter
    }

    fun getInnerCircleRadius(): Float {
        return innerCircleView!!.innerCircleFactor
    }

    fun getOuterCircleBorderWidth(): Float {
        return outerCircleView!!.outerCircleBorderWidth
    }

    fun getShadowRadius(): Float {
        return outerCircleView!!.shadowRadius
    }

    val shadowDx: Float
        get() = outerCircleView!!.dx
    val shadowDy: Float
        get() = outerCircleView!!.dy
}

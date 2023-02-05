package com.jackandphantom.joystickview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class JoyStickView extends FrameLayout implements InnerCircleView.OnSMallMoveListener {

    private OuterCircleView outerCircleView;
    private InnerCircleView innerCircleView;

    private float outerCircleBorderWidth;
    private int outerCircleBorderColor;
    private int outerCircleColor;
    private boolean lockCenter;
    private float innerCircleRadius;
    private float shadowRadius;
    private float dx, dy;
    private int innerCircleColor;
    private int innerCircleImage;
    private int shadowColor;

    OnMoveListener onMoveListener;

    @Override
    public void onMove(double angle, float strength) {
        if (onMoveListener != null) {
            onMoveListener.onMove(angle, strength);
        }
    }

    public interface OnMoveListener {
        void onMove(double angle, float strength);
    }

    public JoyStickView(@NonNull Context context) {
        super(context);
    }

    public JoyStickView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

   //Taking all the inputs from xml if available
    public JoyStickView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JoyStickView, 0, 0)) {
            try {
                outerCircleBorderWidth = a.getFloat(R.styleable.JoyStickView_outerCircleBorderWidth, 0f);
                outerCircleBorderColor = a.getColor(R.styleable.JoyStickView_outerCircleBorderColor, Color.WHITE);
                innerCircleColor = a.getColor(R.styleable.JoyStickView_innerCircleColor, Color.BLACK);
                outerCircleColor = a.getColor(R.styleable.JoyStickView_outerCircleColor, Color.WHITE);
                shadowColor = a.getColor(R.styleable.JoyStickView_shadowColor, Color.BLACK);
                lockCenter = a.getBoolean(R.styleable.JoyStickView_lockCenter, false);
                innerCircleRadius = a.getFloat(R.styleable.JoyStickView_innerCircleRadius, 0.1f);
                shadowRadius = a.getFloat(R.styleable.JoyStickView_shadowRadius, 0f);
                dx = a.getFloat(R.styleable.JoyStickView_shadowDx, 5.0f);
                dy = a.getFloat(R.styleable.JoyStickView_shadowDy, 5.0f);
                innerCircleImage = a.getResourceId(R.styleable.JoyStickView_innerCircleImage, 0);
            } finally {
                a.recycle();
            }
        }
        init();
    }

    /*
    * Only initialize those value which related to paint not to size of OuterCircleView and InnerCircleView
    * if you try then you will get the error
    * */
    private void init() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.joystick_layout, this, true);

        outerCircleView = view.findViewById(R.id.outerCircle);
        innerCircleView = view.findViewById(R.id.innerCircle);
        innerCircleView.setOnMoveListener(this);
        setLockCenter(lockCenter);
        setInnerCircleColor(innerCircleColor);
        setInnerCircleRadius(innerCircleRadius);
        setOuterCircleBorderColor(outerCircleBorderColor);
        setOuterCircleColor(outerCircleColor);
        setOuterCircleBorderStrokeWidth(outerCircleBorderWidth);
        setShadowRadius(shadowRadius);
        setShadowDxAndDy(dx, dy);
        setShadowColor(shadowColor);
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

   /**
    * We re measure the size of the OuterCircleView and InnerCircleView so init() method only initialize the
    * paint related attributes */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int actualWidth = w/2;
        int actualHeight = h/2;

        outerCircleView.setWidthAndHeight(actualWidth, actualHeight);
        innerCircleView.setWidthAndHeight(w, h);
        setInnerCircleImageResId(innerCircleImage);
    }

    /***  Getter and Setter methods    ****/
    public void setOuterCircleBorderStrokeWidth (float width) {
        outerCircleView.setBorderStrokeWidth(width);
    }

    public void setLockCenter(boolean lockCenter) {
        innerCircleView.setLockCenter(lockCenter);
    }

    public void setInnerCircleRadius(float radius) {
        float outerCircleFactor = 0.5f - radius ;
        outerCircleView.setOuterCircle(outerCircleFactor);
        innerCircleView.setInnerCircleFactor(radius, outerCircleFactor);
    }

    public void setInnerCircleColor(int color) {
        innerCircleView.setCirclePaintColor(color);
    }

    public void setInnerCircleImageDrawable(Drawable drawable) {
        innerCircleView.setImageDrawable(drawable);
    }

    public void setInnerCircleImageResId(int resId) {
        try {
            innerCircleView.setImageResId(resId);
        }catch (IllegalArgumentException exp) {
            exp.printStackTrace();
            this.innerCircleImage = resId;
        }
    }

    public void setShadowRadius(float radius) {
        outerCircleView.setShadowRadius(radius);
    }

    public void setShadowColor (int color) {
        outerCircleView.setShadowColor(color);
    }

    public void setOuterCircleColor(int color) {
        outerCircleView.setCircleColor(color);
    }

    public void setShadowDxAndDy(float dx, float dy) {
        outerCircleView.setShadowDxDy(dx, dy);
    }

    public void setOuterCircleBorderColor(int color) {
        outerCircleView.setBorderCircleColor(color);
    }

    public boolean getLockCenter() {
       return innerCircleView.getLockCenter();
    }

    public float getInnerCircleRadius() {
        return innerCircleView.getInnerCircleFactor();
    }

    public float getOuterCircleBorderWidth() {
        return outerCircleView.getOuterCircleBorderWidth();
    }

    public float getShadowRadius() {
        return outerCircleView.getShadowRadius();
    }

    public float getShadowDx() {
        return outerCircleView.getDx();
    }

    public float getShadowDy() {
        return outerCircleView.getDy();
    }

}

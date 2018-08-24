package com.jackandphantom.joystickview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class OuterCircleView extends View {


    private float centerPoint;
    private float circleRadius;
    private Paint circlePaint = new Paint();
    private Paint borderCirclePaint = new Paint();
    private boolean changeLayoutManual;
    private int width, height;
    private float shadowRadius = 9.0f;
    private float dx = 5.0f, dy = 5.0f;
    //This makes circle smaller as much we want
    private float circleFactor = 0.4f;
    private int shadowColor = Color.RED;
    private int min;


    public OuterCircleView(Context context) {
        super(context);

    }

    public OuterCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public OuterCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*
    * Initialize paint attributes to add effect on circle
    * */

    private void init() {

        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.FILL);

        borderCirclePaint.setAntiAlias(true);
        borderCirclePaint.setStyle(Paint.Style.STROKE);
        setShadow();

    }

    /*
    * @variable changeLayoutManual means we are changing the width and height of OuterCircleView
    * so when we want to change it's layout size then we only need to make changeLayoutManual to true
    * also calculating the radius and centerPoint for drawing circle
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(changeLayoutManual) {
            setMeasuredDimension(width, height);
        } else {
             width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
             height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);
        }

         min = Math.min(width, height);
        centerPoint = min/2;
        circleRadius = min*circleFactor;

    }

   /*
   * first circle is works as background for joystick and second one is for border of the first circle
   * */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(centerPoint, centerPoint, circleRadius, borderCirclePaint);
        canvas.drawCircle(centerPoint , centerPoint, circleRadius , circlePaint);

    }

    /*
    * This method will call when we need to change size of this view
    * */
    public void setWidthAndHeight(int width , int height) {

        changeLayoutManual = true;
        this.width = width*2;
        this.height = height*2;
        requestLayout();
    }

    private  void setShadow() {

        setLayerType(LAYER_TYPE_SOFTWARE, borderCirclePaint);
        borderCirclePaint.setShadowLayer(shadowRadius, -dx, dy, shadowColor);
    }

    /**   Setter and Getter methods         ****/

    void setBorderStrokeWidth (float width) {
        borderCirclePaint.setStrokeWidth(width);
        invalidate();
    }

    void setShadowRadius (float radius) {
        this.shadowRadius = radius;
        setShadow();
        invalidate();
    }

    void setCircleColor(int color) {
        circlePaint.setColor(color);
        invalidate();
    }

    void setBorderCircleColor (int color) {
        borderCirclePaint.setColor(color);
        invalidate();
    }

    void setShadowDxDy(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
        setShadow();
        invalidate();
    }

    void setShadowColor (int color) {
        this.shadowColor = color;
        setShadow();
        invalidate();
    }

    void setOuterCircle(float circleFactor) {
        this.circleFactor = circleFactor;
        requestLayout();
    }

    float getOuterCircleBorderWidth() {
        return borderCirclePaint.getStrokeWidth();
    }

    float getShadowRadius() {
        return shadowRadius;
    }

    float getDx() {
        return dx;
    }

    float getDy() {
        return dy;
    }

}

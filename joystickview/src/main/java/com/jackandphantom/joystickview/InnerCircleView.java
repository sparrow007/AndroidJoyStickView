package com.jackandphantom.joystickview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class InnerCircleView extends View {


    private float circleRadius;
    private Paint circlePaint = new Paint();
    private float centerPoint;
    private float centerPointX, centerPointY;
    private float limitRadius;
    private int width, height;
    private boolean changeLayoutManual = false;
    private boolean isInsideCircle = false;
    private Bitmap bitmap;
    private boolean lockCenter = false;
    private float innerCircleFactor = 0.1f;
    private float outerCircleFactor = 0.4f;
    private float strength;
    private float bitmapDrawFactor;
    private int min;


    OnSMallMoveListener onMoveListener;

    /*
    * Interface called when innercricle or joystick point is moving and this interface will provide the
    * angle and strength
    * **/

    interface OnSMallMoveListener {

        void onMove(double angle, float strength);
    }

    public InnerCircleView(Context context) {
        super(context);

    }

    public InnerCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public InnerCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //Initializing paint methods attributes
    private void init() {

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.BLACK);

    }

    /**
     *  This view can change it's width and height according to the changeLayoutManual and it calculating the
     *  centerPoint which is mid point and both circle in joystick is making the ratio for their respective
     *  size
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!changeLayoutManual) {
             width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
             height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

            setMeasuredDimension(width, height);
        } else {
            setMeasuredDimension(width, height);
        }

       min = Math.min(width, height);
       centerPoint = min/2;
       centerPointX = centerPoint;
       centerPointY = centerPoint;
       circleRadius = min*innerCircleFactor;
       limitRadius = min*outerCircleFactor;
       bitmapDrawFactor =  circleRadius*0.5f;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(centerPointX, centerPointY, circleRadius , circlePaint);
        if (bitmap != null)
        canvas.drawBitmap(bitmap, centerPointX  - bitmapDrawFactor, centerPointY - bitmapDrawFactor, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        angle(event.getX(), event.getY());

        switch (event.getAction()) {

            case MotionEvent.ACTION_UP:

                if (!lockCenter || strength < 100) {
                    centerPointY = centerPoint;
                    centerPointX = centerPoint;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:

                handleMotion(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                handleMotionEvent(event.getX(), event.getY());
                break;
        }

        return true;
    }
    //Use some maths for make joystick working well
    private void handleMotionEvent(float x, float y) {

        double abs = (Math.sqrt(Math.pow((x-centerPoint),2)+ Math.pow((y-centerPoint),2)));

        if (onMoveListener != null) {
            onMoveListener.onMove(angle(x, y), getStrength(abs));
        }

        if (abs  <= limitRadius) {
            centerPointX =  x;
            centerPointY =  y;
            invalidate();

        } else {
            if (isInsideCircle) {
                centerPointX = (float) ((x - centerPoint) * limitRadius / abs + centerPoint);
                centerPointY = (float) ((y - centerPoint) * limitRadius / abs + centerPoint);

                invalidate();
            }
        }

    }

    private double angle(float x, float y) {

        double angle = Math.toDegrees(Math.atan2((y-centerPoint), (x-centerPoint)));
        return angle > 0 ? 360 - angle : (-angle);
    }

    private float getStrength(double distance) {

        if (distance > limitRadius) {
            distance = limitRadius;
        }

        return strength = (float) ((distance/ limitRadius)*100);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {

        Bitmap bitmap;

        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        if (drawable.getIntrinsicWidth() > 0 &&drawable.getIntrinsicHeight() > 0 ) {

           bitmap = Bitmap.createBitmap((int) circleRadius, (int) circleRadius, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0,canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }

        return null;
    }

    public void setOnMoveListener(OnSMallMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;

    }


    private void handleMotion(float x, float y) {

        if ( (Math.sqrt(Math.pow((x-centerPoint),2)+ Math.pow((y-centerPoint),2))) <= limitRadius) {
            centerPointX =  x;
            centerPointY =  y;
            isInsideCircle = true;
            invalidate();

        } else {
            isInsideCircle = false;
        }

    }

    /** Getter and Setter Methods */
    public void setWidthAndHeight(int width , int height) {

        changeLayoutManual = true;
        this.width = width;
        this.height = height;
        requestLayout();
    }



    void setCirclePaintColor (int color) {
        circlePaint.setColor(color);
        invalidate();
    }

    void setImageDrawable (Drawable drawable) {
        bitmap = getBitmapFromDrawable(drawable);
        invalidate();
    }

    void setImageResId (int resId) {

            Drawable drawable;
            try {
                drawable = getContext().getDrawable(resId);
            } catch (Exception e) {
                return;
            }
            bitmap = getBitmapFromDrawable(drawable);
            invalidate();

    }

    void setLockCenter(boolean lockCenter) {
        this.lockCenter = lockCenter;
    }

    void setInnerCircleFactor (float innerFactor, float outerCircleFactor) {

        innerCircleFactor = innerFactor;
        this.outerCircleFactor = outerCircleFactor;
        invalidate();
    }

    boolean getLockCenter() {
        return this.lockCenter;
    }

    float getInnerCircleFactor() {
        return innerCircleFactor;
    }



}

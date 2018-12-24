package io.feeeei.circleseekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class HSVColorPickerView extends View {
    private Paint mPaint;
    private RectF mGrayStokeRectF;
    private Paint colorWheelPaint;    // 绘制色盘
    private Bitmap colorWheelBitmap;    // 彩灯位图
    private Paint mSelectPaint;
    private RectF mSelectRecF;
    private PointF mSelectPoint;
    private int centerX, centerY;
    private int selectWheelRadius;
    private int selectStrokeWidth;
    private int colorWheelRadius;
    private Rect mColorWheelRect;
    private float[] hsv;
    private float[] firstHsv;
    private OnColorChangedListener mChangeListener;

    public HSVColorPickerView(Context context) {
        this(context, null);
    }

    public HSVColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HSVColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectWheelRadius = dip2px(context, 15);
        selectStrokeWidth = dip2px(context, 4);
        hsv = new float[3];

        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dip2px(context, 4));
        mPaint.setColor(Color.parseColor("#DCDCDC"));

        colorWheelPaint = new Paint();
        colorWheelPaint.setAntiAlias(true);//消除锯齿
        colorWheelPaint.setDither(true);//防抖动

        mSelectPaint = new Paint();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mSelectPaint.setAntiAlias(true);//消除锯齿
        mSelectPaint.setDither(true);//防抖动
        mSelectPaint.setStyle(Paint.Style.STROKE);
        mSelectPaint.setStrokeWidth(selectStrokeWidth);
        mSelectPaint.setShadowLayer(dip2px(context, 2), 0, 0, Color.parseColor("#29000000"));
        mSelectPaint.setColor(Color.WHITE);
    }

    public void setOnColorChangeListener(OnColorChangedListener mChangeListener) {
        this.mChangeListener = mChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (firstHsv != null) {
            setColor();
            firstHsv = null;
            return;
        }
        canvas.drawArc(mGrayStokeRectF, 0, 360, false, mPaint);
        canvas.drawBitmap(colorWheelBitmap, null, mColorWheelRect, null);
        mSelectRecF.set(mSelectPoint.x - selectWheelRadius, mSelectPoint.y - selectWheelRadius,
                mSelectPoint.x + selectWheelRadius, mSelectPoint.y + selectWheelRadius);
        canvas.drawArc(mSelectRecF, 0, 360, false, mSelectPaint);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        initPoint(width, height);
    }

    private void initPoint(int width, int height) {
        centerX = width / 2;
        centerY = height / 2;
        colorWheelRadius = width / 2 - dip2px(getContext(), 8);
        mSelectPoint = new PointF(centerX, centerY);

        mColorWheelRect = new Rect(centerX - colorWheelRadius, centerY - colorWheelRadius, centerX + colorWheelRadius, centerY + colorWheelRadius);
        mGrayStokeRectF = new RectF(selectStrokeWidth / 2, selectStrokeWidth / 2, width - selectStrokeWidth / 2, height - selectStrokeWidth / 2);
        mSelectRecF = new RectF(centerX - selectWheelRadius, centerY - selectWheelRadius, centerX + selectWheelRadius, centerY + selectWheelRadius);

        colorWheelBitmap = createColorWheelBitmap(colorWheelRadius * 2, colorWheelRadius * 2);
    }

    private Bitmap createColorWheelBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int colorCount = 12;
        int colorAngleStep = 360 / 12;
        int colors[] = new int[colorCount + 1];
        float hsv[] = new float[]{0f, 1f, 1f};
        for (int i = 0; i < colors.length; i++) {
            hsv[0] = 360 - (i * colorAngleStep) % 360;
            colors[i] = Color.HSVToColor(hsv);
        }
        colors[colorCount] = colors[0];

        SweepGradient sweepGradient = new SweepGradient(width / 2, height / 2, colors, null);
        RadialGradient radialGradient = new RadialGradient(width / 2, height / 2, colorWheelRadius, 0xFFFFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(sweepGradient, radialGradient, PorterDuff.Mode.SRC_OVER);

        colorWheelPaint.setShader(composeShader);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(width / 2, height / 2, colorWheelRadius, colorWheelPaint);
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                setSelectPoint(x, y);
                invalidate();
                setColorChange();
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                setSelectPoint(x, y);
                invalidate();
                if (mChangeListener != null) {
                    int color = getColor();
                    int r = Color.red(color);
                    int g = Color.green(color);
                    int b = Color.blue(color);
                    String hexColor = IntToFormatHexString(r) +
                            IntToFormatHexString(g) +
                            IntToFormatHexString(b);
                    mChangeListener.onColorSelected(r, g, b, hexColor);
                }
                break;
        }
        return true;
    }

    private void setColorChange() {
        if (mChangeListener != null) {
            int color = getColor();
            mChangeListener.onColorChanged(color);
        }
    }

    private void setSelectPoint(float x, float y) {
        double a = Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2);
        if (Math.sqrt(a) <= colorWheelRadius - selectWheelRadius) {
            mSelectPoint.x = x;
            mSelectPoint.y = y;
        } else {
            double n = Math.sqrt(a) / (colorWheelRadius - selectWheelRadius);
            mSelectPoint.x = (float) (((x - centerX) / n) + centerX);
            mSelectPoint.y = (float) (((y - centerY) / n) + centerY);
        }
    }

    public int getColor() {
        hsv[0] = getDegrees();
        double d = Math.pow(mSelectPoint.x - centerX, 2) + Math.pow(mSelectPoint.y - centerY, 2);
        hsv[1] = (float) (Math.sqrt(d) / colorWheelRadius);
        hsv[2] = 1.0f;
        return Color.HSVToColor(hsv);
    }

    public void setColor(int r, int g, int b) {
        Color.colorToHSV(Color.rgb(r, g, b), hsv);
        setColor();
    }

    public void setColor(int hexColor) {
        Color.colorToHSV(hexColor, hsv);
        setColor();
    }

    public void setColor(String hexColor) {
        Color.colorToHSV(Color.parseColor("#" + hexColor), hsv);
        setColor();
    }

    public void setColor(float[] hsv) {
        this.hsv = hsv;
        setColor();
    }

    private void setColor() {
        if (mSelectPoint == null) {
            firstHsv = hsv;
            return;
        }
        int radius = (int) (colorWheelRadius * hsv[1]);
        if (radius > colorWheelRadius - selectWheelRadius) {
            radius = colorWheelRadius - selectWheelRadius;
        }
        mSelectPoint.x = (float) (centerX + radius * Math.cos(Math.toRadians(360 - hsv[0])));
        mSelectPoint.y = (float) (centerY + radius * Math.sin(Math.toRadians(360 - hsv[0])));
        invalidate();
        setColorChange();
    }

    private float getDegrees() {
        float x = mSelectPoint.x - centerX;
        float y = mSelectPoint.y - centerY;
        float a = (float) Math.toDegrees(Math.atan(Math.abs(y) / Math.abs(x)));
        if (x >= 0 && y <= 0) {
            return a;
        }
        if (x <= 0 && y <= 0) {
            return 180 - a;
        }
        if (x <= 0 && y >= 0) {
            return a + 180;
        }
        if (x >= 0 && y >= 0) {
            return 360 - a;
        }
        return a;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private String IntToFormatHexString(int i) {
        return String.format("%02x", i);
    }

    public interface OnColorChangedListener {
        void onColorChanged(int hexColor);

        void onColorSelected(int r, int g, int b, String hexColor);
    }
}


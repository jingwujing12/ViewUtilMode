package com.cchip.eq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.cchip.lib_eq.R;

import java.util.ArrayList;


public class CustomSeekBar extends View {
    private final String TAG = "CustomSeekBar";
    private int width;
    private int startText = 12;
    private int upX = 0;
    private int upY = 0;
    private float moveX = 0;
    private float moveY = 0;
    private int perWidth = 0;
    private float linePadding = 4+5;
    private int mPadding = 10;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint buttonPaint;
    private Canvas canvas;
    private Bitmap bitmap;
    private Bitmap thumb;
    private Bitmap spot;
    private Bitmap spot_on;
    private int hotarea = 100;//点击的热区
    private int cur_sections = 0;
    private ResponseOnTouch responseOnTouch;
    private int bitMapHeight = 38;//第一个点的起始位置起始，图片的长宽是76，所以取一半的距离
    private int textMove = 60;//字与下方点的距离，因为字体字体是40px，再加上10的间隔
    private int[] colors = new int[]{0xffdf5600, 0x33000000};//进度条的橙色,进度条的灰色,字体的灰色
    private int textSize;
    private int circleRadius;
    private int thumbProgress = 0;
    private ArrayList<String> section_title;
    private Bitmap bitmapTemp;
    private Paint mSubBgPaint;
    private Bitmap mBitmapThumb;
    private int bitmapY = -1;
    private int bitmapX;
    private int height;
    private boolean onThouch;
    private float subheight;
    private float moveXfirst;
    private float lastmoveX;
    private int canvasHeight;
    private float lastX;
    private Context mContext;

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getmCenterBackGround() {
        return mCenterBackGround;
    }

    public void setmCenterBackGround(int mCenterBackGround) {
        this.mCenterBackGround = mCenterBackGround;
    }

    private int mTextColor = R.color.textcolor;
    private int mCenterBackGround = R.color.center_background;

    public int getmVerticalCenterLineColor() {
        return mVerticalCenterLineColor;
    }

    public void setmVerticalCenterLineColor(int mVerticalCenterLineColor) {
        this.mVerticalCenterLineColor = mVerticalCenterLineColor;
    }

    public int getmHorizontalCenterLineColor() {
        return mHorizontalCenterLineColor;
    }

    public void setmHorizontalCenterLineColor(int mHorizontalCenterLineColor) {
        this.mHorizontalCenterLineColor = mHorizontalCenterLineColor;
    }

    public int getmOtherLineColor() {
        return mOtherLineColor;
    }

    public void setmOtherLineColor(int mOtherLineColor) {
        this.mOtherLineColor = mOtherLineColor;
    }

    private int mVerticalCenterLineColor = R.color.eq_vertical_center_line;
    private int mHorizontalCenterLineColor = R.color.eq_horizontal_center_line;
    private int mOtherLineColor = R.color.eq_other_line;

    public interface ResponseOnTouch {
        void onTouchResponse(int progress);
    }

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        circleRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xff373737);
        mPaint.setStrokeWidth(3);
        mSubBgPaint = new Paint(Paint.DITHER_FLAG);
        mSubBgPaint.setAntiAlias(true);
        mSubBgPaint.setColor(ContextCompat.getColor(mContext, mCenterBackGround));
        mSubBgPaint.setStrokeWidth(3);
        mTextPaint = new Paint(Paint.DITHER_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(ContextCompat.getColor(mContext, mTextColor));
        buttonPaint = new Paint(Paint.DITHER_FLAG);
        buttonPaint.setAntiAlias(true);

        bitmapTemp= BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_eq_seekbar_thumb);
        postInvalidate();
//        mPadding = bitmapTemp.getHeight()/2;

    }

    private Bitmap getDotBitmap(int width, int height, int color) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        Canvas c = new Canvas(b);
        c.drawCircle(width / 2, height / 2, Math.min(width / 2, height / 2), paint);
        return b;
    }

    /**
     * 实例化后调用，设置bar的段数和文字
     */
    public void initData(ArrayList<String> section) {
        if (section != null) {
            section_title = section;
        } else {
            String[] str = new String[]{"低", "中", "高"};
            section_title = new ArrayList<String>();
            for (int i = 0; i < str.length; i++) {
                section_title.add(str[i]);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        width = bitmapTemp.getWidth();
//        float scaleX = widthSize / 1080;
//        float scaleY = heightSize / 1920;
//        scale = Math.max(scaleX, scaleY);
//        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 62, getResources().getDisplayMetrics());
        setMeasuredDimension(width, heightMeasureSpec);
//        width = width - bitMapHeight / 2;
//        perWidth = (width - section_title.size() * spot.getWidth() - thumb.getWidth() / 2) / (section_title.size() - 1);
//        hotarea = perWidth / 2;
    }

    //    Rect rect = new Rect();
//        paint.getTextBounds(text,0,text.length(), rect);
//    height = rect.height()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw:progress： " + cur_sections);
        canvasHeight = canvas.getHeight();
        height = canvas.getHeight() - mPadding * 2;
        int width = canvas.getWidth();
        subheight = height / 26f;
        float subheights = subheight;
        if (mBitmapThumb == null) {
            if (bitmapTemp == null) {
                bitmapTemp = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_eq_seekbar_thumb);
            }
            mBitmapThumb = MediaUtil.setImgSize(bitmapTemp, bitmapTemp.getWidth(), (subheights * 2));
        }
        int x = 0;
        int text = startText;
        float measureText = mTextPaint.measureText("2");
        float measureHight = (mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top) / 3;
        float measureAddText = mTextPaint.measureText("+");

        float measureSubText = mTextPaint.measureText("-");
        canvas.drawRect(measureText * 2+5, 0, width - measureText * 2-5, canvasHeight, mSubBgPaint);
        for (int i = 1; i < 26; i++) {
            if (Math.abs(text) < 10) {
                x = (int) mTextPaint.measureText("2") / 2+2;
            } else {
                x = 2;
            }
            if (text <= 0) {
                canvas.drawText(-text + "", x, mPadding + i * subheights + measureHight, mTextPaint);
                if (text == -startText) {
                    canvas.drawText("-", (float) (getWidth() - measureText * 1.5), mPadding - measureSubText / 2 + i * subheights + measureHight, mTextPaint);
                }
            } else {
                canvas.drawText(text + "", x, mPadding + i * subheights + measureHight, mTextPaint);
                if (text == startText) {
                    canvas.drawText("+", (float) (getWidth() - measureText * 1.5), mPadding + i * subheights + measureHight - measureSubText / 2, mTextPaint);
                }

            }
            //横线
            if (text == 0) {
                mPaint.setColor(ContextCompat.getColor(mContext, mVerticalCenterLineColor));
                canvas.drawLine(measureText * 2 + linePadding, mPadding + i * subheights, width - measureText * 2 - linePadding, mPadding + i * subheights, mPaint);
            } else {
                mPaint.setColor(ContextCompat.getColor(mContext, mOtherLineColor));
                canvas.drawLine(measureText * 2 + linePadding, mPadding + i * subheights, width - measureText * 2 - linePadding, mPadding + i * subheights, mPaint);
            }
            text -= 1;
        }
        //垂直线
//        mPaint.setColor(ContextCompat.getColor(mContext, mHorizontalCenterLineColor));
        float strokeWidth = mPaint.getStrokeWidth();
//        canvas.drawLine(width / 2 - strokeWidth, 0, width / 2 - strokeWidth, canvasHeight, mPaint);
        mPaint.setColor(ContextCompat.getColor(mContext, mHorizontalCenterLineColor));
//        mPaint.setColor(0xff3f3f3f);
        canvas.drawLine(width / 2, 0, width / 2, canvasHeight, mPaint);
//        mPaint.setColor(ContextCompat.getColor(mContext, mHorizontalCenterLineColor));
//        mPaint.setColor(0xff282828);
//        canvas.drawLine(width / 2 + strokeWidth, 0, width / 2 + strokeWidth, canvasHeight, mPaint);
//        if (bitmapY < 0) {
//            bitmapY = canvasHeight / 2 - mBitmapThumb.getHeight() / 2;
//        }
        bitmapY = mPadding + (int) ((startText - cur_sections) * subheight);
        bitmapX = width / 2 - mBitmapThumb.getWidth() / 2;
        canvas.drawBitmap(mBitmapThumb, bitmapX, bitmapY, buttonPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: " + "ACTION_DOWN");

                moveX = event.getX();
                moveXfirst = event.getX();
                moveY = event.getY();
                onThouch = isOnThouch(moveX, moveY);
                if (onThouch) {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: " + "ACTION_MOVE");
                moveX = event.getX();
                moveY = event.getY();
                lastmoveX = event.getX() - moveXfirst;
                if (onThouch) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (/*moveY <= (height - subheight * 2) &&*/ moveY > (mPadding) && moveY <= (canvasHeight - mPadding - mBitmapThumb.getHeight() / 2)) {
                        int num = (int) ((moveY - mPadding) / subheight);
                        cur_sections = startText - num;
//                        Log.e(TAG, "onTouchEvent: " + (startText - num));
//                        bitmapY = (int) (num * subheight);
                        listenner();
                    } else {
//                        bitmapY = bitmapY;
                    }
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
//                if (lastmoveX > 100) {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                bitmapY=
//                proofLeft(moveX, moveY);
                break;
//            case MotionEvent.ACTION_CANCEL:

            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent: " + "ACTION_UP");
//                if (responseOnTouch != null) {
//                    responseOnTouch.onTouchResponse((startText-num));
                getParent().requestDisallowInterceptTouchEvent(false);
//                }
                onThouch = false;
//                thumb = getDotBitmap(48, 48, Color.BLACK);
//                upX = (int) event.getX();
//                upY = (int) event.getY();
//                responseTouch(upX, upY);

                break;
        }
        invalidate();
        return true;
    }

    private boolean isOnThouch(float moveX, float moveY) {
        return moveY <= (bitmapY + mBitmapThumb.getHeight()) && moveY >= bitmapY && moveX >= bitmapX && moveX <= (bitmapX + mBitmapThumb.getWidth());
    }

    private void proofLeft(float x, float y) {
        if (x < 0) {
            bitmapX = 0;
        } else if (x > (getWidth())) {
            bitmapX = getWidth();
        } else {
            bitmapX = (int) x;
        }
        if (y < 0) {
            bitmapY = 0;
        } else if (y > (height - 0)) {
            bitmapY = height - 0;
        } else {
            bitmapY = (int) y;
        }
    }

    private void responseTouch(int x, int y) {
        if (x <= width - bitMapHeight / 2) {
            int section = (x - 12) / (perWidth + spot.getWidth()) + 1;
            cur_sections = (x - section * spot.getWidth() + perWidth / 2) / perWidth;
        } else {
            cur_sections = section_title.size() - 1;
        }
        invalidate();
    }

    /**
     * @param progress -22--22
     */
    public void setProgress(int progress) {
        if (progress <= startText && (progress >= -startText)) {
            cur_sections = progress;
            invalidate();
        }
    }

    public void listenner() {
        if (responseOnTouch != null) {
            responseOnTouch.onTouchResponse((cur_sections));
        }
    }

    public void setResponseOnTouch(ResponseOnTouch response) {
        responseOnTouch = response;
    }

    public int getProgress() {

        return cur_sections;
    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////        // 请求父控件及祖宗控件不要拦截当前控件的时间
////        if (ev.getRawX() > 0&& ev.getRawX() <300 ) {
////            getParent().requestDisallowInterceptTouchEvent(true);
////        }
////        return super.dispatchTouchEvent(ev);
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            getParent().getParent().requestDisallowInterceptTouchEvent(true);
//        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            int x = (int) ev.getY();
//            if (lastX > x) {
//                // 如果是水平向左滑动，且不能滑动了，则返回给上一层view处理
//                if (!canScrollVertically(1)) {
//                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
//                    return false;
//                }
//            } else if (x > lastX) {
//                // 如果是水平向右滑动，且不能滑动了，则返回给上一层view处理
//                if (!canScrollVertically(-1)) {
//                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
//                    return false;
//                }
//            }
//        }
//        lastX = ev.getX();
//        return super.dispatchTouchEvent(ev);
//    }

}
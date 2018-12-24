package io.feeeei.circleseekbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import io.feeeei.circleseekbar.util.SeekUtil;

/**
 * Created by gaopengfei on 15/11/15.
 */
public class CircleOutSizeSeekBar extends View {

    private static final double RADIAN = 180 / Math.PI;

    private static final String INATANCE_STATE = "state";
    private static final String INSTANCE_MAX_PROCESS = "max_process";
    private static final String INSTANCE_CUR_PROCESS = "cur_process";
    private static final String INSTANCE_REACHED_COLOR = "reached_color";
    private static final String INSTANCE_REACHED_WIDTH = "reached_width";
    private static final String INSTANCE_REACHED_CORNER_ROUND = "reached_corner_round";
    private static final String INSTANCE_UNREACHED_COLOR = "unreached_color";
    private static final String INSTANCE_UNREACHED_WIDTH = "unreached_width";
    private static final String INSTANCE_POINTER_COLOR = "pointer_color";
    private static final String INSTANCE_POINTER_RADIUS = "pointer_radius";
    private static final String INSTANCE_POINTER_SHADOW = "pointer_shadow";
    private static final String INSTANCE_POINTER_SHADOW_RADIUS = "pointer_shadow_radius";
    private static final String INSTANCE_WHEEL_SHADOW = "wheel_shadow";
    private static final String INSTANCE_WHEEL_SHADOW_RADIUS = "wheel_shadow_radius";
    private static final String INSTANCE_WHEEL_HAS_CACHE = "wheel_has_cache";
    private static final String INSTANCE_WHEEL_CAN_TOUCH = "wheel_can_touch";
    private static final String INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE = "wheel_scroll_only_one_circle";

    private Paint mWheelPaint;

    private Paint mReachedPaint;

    private Paint mReachedEdgePaint;

    private Paint mPointerPaint;

    private int mMaxProcess;
    private int mCurProcess;
    private float mUnreachedRadius;
    private int mReachedColor, mUnreachedColor;
    private float mReachedWidth, mUnreachedWidth;
    private boolean isHasReachedCornerRound;
    private int mPointerColor;
    private float mPointerRadius;

    private double mCurAngle;
    private double mCurAngle2;
    private double mCurAngle3;
    private float mWheelCurX, mWheelCurY;

    private boolean isHasWheelShadow, isHasPointerShadow;
    private float mWheelShadowRadius, mPointerShadowRadius;

    private boolean isHasCache;
    private Canvas mCacheCanvas;
    private Bitmap mCacheBitmap;

    private boolean isCanTouch;

    private boolean isScrollOneCircle;

    private float mDefShadowOffset;

    private OnSeekBarChangeListener mChangListener;
    private Bitmap mEndBitmap;
    private Bitmap mStartBitmap;
    private int mStartBitmapid;
    private int mEndBitmapid;
    private Paint mWheelPaint2;
    private Bitmap mDurationBitmap;
    private float mWheelCurX2;
    private float mWheelCurY2;
    private float sUnreachedRadius;
    private Paint mWheelPaint3;
    private float mWheelCurY3;
    private float mWheelCurX3;
    private Bitmap mBitmapDegrees;
    private Bitmap mBitmapDegrees2;
    private float mWheelCurX22;
    private float mWheelCurY22;
    private float mWheelCurX33;
    private float mWheelCurY33;
    private int mTimeColor;
    private int mDurationBitmapid;
    private int mStartBackgroundBitmapid;
    private int mEndBackgroundBitmapid;
    private Bitmap mStartBackgroundBitmap;
    private Bitmap mEndBackgroundBitmap;

    public CircleOutSizeSeekBar(Context context) {
        this(context, null);
    }

    public CircleOutSizeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleOutSizeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(attrs, defStyleAttr);
        initPadding();
        initPaints();

        if (mEndBitmapid != 0) {
            mEndBitmap = BitmapFactory.decodeResource(context.getResources(),
                    mStartBitmapid);
        }
        if (mStartBitmapid != 0) {
            mStartBitmap = BitmapFactory.decodeResource(context.getResources(),
                    mEndBitmapid);
        }
        if (mStartBackgroundBitmapid != 0) {
            mStartBackgroundBitmap = BitmapFactory.decodeResource(context.getResources(),
                    mStartBackgroundBitmapid);
        }
        if (mEndBackgroundBitmapid != 0) {
            mEndBackgroundBitmap = BitmapFactory.decodeResource(context.getResources(),
                    mEndBackgroundBitmapid);
        }
        if (mDurationBitmapid != 0) {
            mDurationBitmap = BitmapFactory.decodeResource(context.getResources(),
                    mDurationBitmapid);
        }
    }

    private void initPaints() {
        mDefShadowOffset = getDimen(R.dimen.def_shadow_offset);
        /**
         * 最外层圆环画笔
         */
        mWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStyle(Paint.Style.STROKE);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        if (isHasWheelShadow) {
            mWheelPaint.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
        }
        /**
         * 第二圆环画笔
         */
        mWheelPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWheelPaint2.setColor(mTimeColor);
        mWheelPaint2.setStyle(Paint.Style.STROKE);
        mWheelPaint2.setStrokeWidth(mUnreachedWidth * 2 / 3);
        if (isHasWheelShadow) {
            mWheelPaint2.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
        }
        /**
         * 最内侧圆环画笔
         */
        mWheelPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWheelPaint3.setColor(mUnreachedColor);
        mWheelPaint3.setStyle(Paint.Style.STROKE);
        mWheelPaint3.setStrokeWidth(mUnreachedWidth / 4);
        if (isHasWheelShadow) {
            mWheelPaint3.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
        }
        /**
         * 选中区域画笔
         */
        mReachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedPaint.setColor(mReachedColor);
        mReachedPaint.setStyle(Paint.Style.STROKE);
        mReachedPaint.setStrokeWidth(mReachedWidth);
        if (isHasReachedCornerRound) {
            mReachedPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        /**
         * 锚点画笔
         */
        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setColor(mPointerColor);
        mPointerPaint.setStyle(Paint.Style.FILL);
        if (isHasPointerShadow) {
            mPointerPaint.setShadowLayer(mPointerShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
        }
        /**
         * 选中区域两头的圆角画笔
         */
        mReachedEdgePaint = new Paint(mReachedPaint);
        mReachedEdgePaint.setStyle(Paint.Style.FILL);
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleSeekBar, defStyle, 0);
        mMaxProcess = a.getInt(R.styleable.CircleSeekBar_wheel_max_process, 100);
        mCurProcess = a.getInt(R.styleable.CircleSeekBar_wheel_cur_process, 0);
        if (mCurProcess > mMaxProcess) mCurProcess = mMaxProcess;
        mReachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color, getColor(R.color.def_reached_color));
        mUnreachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_unreached_color,
                getColor(R.color.def_wheel_color));
        mUnreachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_unreached_width,
                getDimen(R.dimen.def_wheel_width));
        isHasReachedCornerRound = a.getBoolean(R.styleable.CircleSeekBar_wheel_reached_has_corner_round, true);
        mReachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_reached_width, mUnreachedWidth);
        mPointerColor = a.getColor(R.styleable.CircleSeekBar_wheel_pointer_color, getColor(R.color.def_pointer_color));
        mTimeColor = a.getColor(R.styleable.CircleSeekBar_wheel_time_color, getColor(R.color.def_pointer_color));
        mPointerRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_radius, mReachedWidth / 2);
        isHasWheelShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_wheel_shadow, false);
        mStartBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_start_drawable, 0);
        mEndBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_end_drawable, 0);
        mStartBackgroundBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_center_start_drawable, R.drawable.time);
        mEndBackgroundBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_center_end_drawable, R.drawable.time);
        mDurationBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_center_background_drawable, R.drawable.duration_set_button_on);
        if (isHasWheelShadow) {
            mWheelShadowRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_shadow_radius,
                    getDimen(R.dimen.def_shadow_radius));
        }
        isHasPointerShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_pointer_shadow, false);
        if (isHasPointerShadow) {
            mPointerShadowRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_shadow_radius,
                    getDimen(R.dimen.def_shadow_radius));
        }
        isHasCache = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_cache, isHasWheelShadow);
        isCanTouch = a.getBoolean(R.styleable.CircleSeekBar_wheel_can_touch, true);
        isScrollOneCircle = a.getBoolean(R.styleable.CircleSeekBar_wheel_scroll_only_one_circle, false);

        if (isHasPointerShadow | isHasWheelShadow) {
            setSoftwareLayer();
        }
        a.recycle();
    }

    private void initPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingStart = 0, paddingEnd = 0;
        if (Build.VERSION.SDK_INT >= 17) {
            paddingStart = getPaddingStart();
            paddingEnd = getPaddingEnd();
        }
        int maxPadding = Math.max(paddingLeft, Math.max(paddingTop,
                Math.max(paddingRight, Math.max(paddingBottom, Math.max(paddingStart, paddingEnd)))));
        setPadding(maxPadding, maxPadding, maxPadding, maxPadding);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private int getColor(int colorId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getContext().getColor(colorId);
        } else {
            return ContextCompat.getColor(getContext(), colorId);
        }
    }

    private float getDimen(int dimenId) {
        return getResources().getDimension(dimenId);
    }

    private void setSoftwareLayer() {
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        refershUnreachedWidth();
        refershPosition();
    }

    public Bitmap setDegrees(Bitmap bm, float degrees) {
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        return newbm;
    }

    public static Bitmap setImgSize(Bitmap bm, float newWidth, float newHeight) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float left = getPaddingLeft() + mUnreachedWidth / 2;
        float top = getPaddingTop() + mUnreachedWidth / 2;
        float right = canvas.getWidth() - getPaddingRight() - mUnreachedWidth / 2;
        float bottom = canvas.getHeight() - getPaddingBottom() - mUnreachedWidth / 2;
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;

        float wheelRadius = (canvas.getWidth() - getPaddingLeft() - getPaddingRight()) / 2 - mUnreachedWidth / 2;
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        if (isHasCache) {
            if (mCacheCanvas == null) {
                buildCache(centerX, centerY, wheelRadius);
            }
            canvas.drawBitmap(mCacheBitmap, 0, 0, null);
        } else {
            canvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
            canvas.drawCircle(centerX, centerY, wheelRadius - mUnreachedWidth / 2 - mWheelPaint2.getStrokeWidth() - mWheelPaint3.getStrokeWidth() / 2, mWheelPaint3);
            canvas.drawCircle(centerX, centerY, wheelRadius - mUnreachedWidth / 2 - mWheelPaint2.getStrokeWidth() / 2, mWheelPaint2);

        }
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        double mAngle = mCurAngle2 > mCurAngle3 ? (-360 + mCurAngle2) : mCurAngle2;
        //画选中区域
//        canvas.drawArc(new RectF(left, top, right, bottom), -90, (float) mCurAngle, false, mReachedPaint);
        canvas.drawArc(new RectF(left, top, right, bottom), (float) (-90 + mAngle), (float) ((float) mCurAngle3 - (mAngle)), false, mReachedPaint);
//        canvas.drawBitmap(mReachedPaint, );
        if (mStartBitmap == null) {
            //画锚点
//            canvas.drawCircle(mWheelCurX, mWheelCurY, mPointerRadius, mPointerPaint);
        } else {
            canvas.drawBitmap(mStartBitmap, left + (right - left) / 2 - mStartBitmap.getWidth() / 2, top - mStartBitmap.getHeight() / 2, mPointerPaint);
            canvas.drawBitmap(mEndBitmap, mWheelCurX33 - mStartBitmap.getWidth() / 2, mWheelCurY33 - mStartBitmap.getHeight() / 2, mPointerPaint);
        }
//        canvas.drawBitmap(mDurationBitmap, mWheelCurX - mStartBitmap.getWidth() / 2, mWheelCurY - mStartBitmap.getHeight() / 2, mPointerPaint);
//        canvas.drawBitmap(mBackgroundBitmap, centerX -(wheelRadius - mUnreachedWidth), centerY - mBackgroundBitmap.getHeight() / 2, mPointerPaint);
        //画时钟
        Bitmap bitmap = setImgSize(mDurationBitmap, (wheelRadius + mUnreachedWidth * 2), (wheelRadius + mUnreachedWidth * 2));
        canvas.drawBitmap(bitmap, centerX - bitmap.getWidth() / 2, centerY - bitmap.getHeight() / 2, mPointerPaint);

        //画连接线
        Paint mp = mReachedPaint;
        mp.setStrokeWidth(5);
        canvas.drawLine(mWheelCurX3, mWheelCurY3, mWheelCurX33, mWheelCurY33, mp);
        canvas.drawLine(mWheelCurX2, mWheelCurY2, mWheelCurX22, mWheelCurY22, mp);
//        canvas.drawLine(left + (right - left) / 2, calcYLocationInWheel2(-1), centerX, calcYLocationInWheel(-1), mp);
        mp.setStrokeWidth(mReachedWidth);

        //画三角形
        mBitmapDegrees = setDegrees(mStartBackgroundBitmap, (float) (-90 + mCurAngle2));
//        canvas.drawBitmap(mBitmapDegrees, left + (right - left) / 2 - mBitmapDegrees.getWidth() / 2, calcYLocationInWheel2(-1) - mBitmapDegrees.getHeight() / 2, mPointerPaint);
        canvas.drawBitmap(mBitmapDegrees, mWheelCurX2 - mBitmapDegrees.getWidth() / 2, mWheelCurY2 - mBitmapDegrees.getHeight() / 2, mPointerPaint);
        mBitmapDegrees2 = setDegrees(mEndBackgroundBitmap, (float) (-90 + mCurAngle3));
        canvas.drawBitmap(mBitmapDegrees2, mWheelCurX3 - mBitmapDegrees2.getWidth() / 2, mWheelCurY3 - mBitmapDegrees2.getHeight() / 2, mPointerPaint);


    }

    private void buildCache(float centerX, float centerY, float wheelRadius) {
        mCacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheBitmap);

        //画环
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius - 35, mWheelPaint);
    }

    int isStartBitMapTouch = 1;
    int isEndBitMapTouch = 2;
    int isBitMapTouch = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (isCanTouch) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mBitmapDegrees2 != null && SeekUtil.isBitMapTouch(mBitmapDegrees2, x, y, mWheelCurX3, mWheelCurY3)) {
                    isBitMapTouch = isEndBitMapTouch;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (mBitmapDegrees != null && SeekUtil.isBitMapTouch(mBitmapDegrees, x, y, mWheelCurX2, mWheelCurY2)) {
                    isBitMapTouch = isStartBitMapTouch;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    isBitMapTouch = 0;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
//
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                if (isBitMapTouch(mBitmapDegrees, x, y, mWheelCurX2, mWheelCurY2)) {
//                    isBitMapTouch = isStartBitMapTouch;
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else if (isBitMapTouch(mBitmapDegrees2, x, y, mWheelCurX3, mWheelCurY3)) {
//                    isBitMapTouch = isEndBitMapTouch;
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {
////                    isBitMapTouch = 0;
////                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
                getParent().requestDisallowInterceptTouchEvent(true);
                // 通过当前触摸点搞到cos角度值
                float cos = computeCos(x, y);
                // 通过反三角函数获得角度值
                double angle;
                if (x < getWidth() / 2) { // 滑动超过180度
                    angle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
                } else { // 没有超过180度
                    angle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
                }
                if (isScrollOneCircle) {
                    if (mCurAngle > 270 && angle < 90) {
                        mCurAngle = 360;
                        cos = -1;
                    } else if (mCurAngle < 90 && angle > 270) {
                        mCurAngle = 0;
                        cos = -1;
                    } else {
                        mCurAngle = angle;
                    }
                } else {
                    mCurAngle = angle;
                }
                mCurProcess = getSelectedValue();
                refershWheelCurPosition(cos);
                if (isBitMapTouch == isStartBitMapTouch) {
                    mCurAngle2 = mCurAngle;
                    refershWheelCurPosition2(cos, mCurAngle2);
                } else if (isBitMapTouch == isEndBitMapTouch) {
                    mCurAngle3 = mCurAngle;
                    refershWheelCurPosition3(cos, mCurAngle3);
                }

                if (mChangListener != null && (event.getAction() & (MotionEvent.ACTION_MOVE | MotionEvent.ACTION_UP)) > 0) {
                    mChangListener.onChanged(this, mCurProcess);
                }
                invalidate();
//                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getParent().requestDisallowInterceptTouchEvent(false);
                isBitMapTouch = 0;
//                return true;
            }
            return true;
        }
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.onTouchEvent(event);
    }

    private double getCurAngle(float cos, double angle) {
        if (isScrollOneCircle) {
            if (mCurAngle > 270 && angle < 90) {
                mCurAngle = 360;
                cos = -1;
            } else if (mCurAngle < 90 && angle > 270) {
                mCurAngle = 0;
                cos = -1;
            } else {
                mCurAngle = angle;
            }
        } else {
            mCurAngle = angle;
        }
        mCurProcess = getSelectedValue();
        return mCurAngle;
    }

    private boolean isTouch(float x, float y) {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        return Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) < radius * radius;
    }

//    private boolean isBitMapTouch(Bitmap mDurationBitmap, float x, float y, float lastx, float lasty) {
//        int w = mDurationBitmap.getWidth() / 2;
//        int h = mDurationBitmap.getHeight() / 2;
//        return lastx - w <= x && x <= lastx + w && lasty - h <= y && y <= lasty + h;
//    }

    private float getCircleWidth() {
        return Math.max(mUnreachedWidth, Math.max(mReachedWidth, mPointerRadius));
    }

    private void refershUnreachedWidth() {
        mUnreachedRadius = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
        sUnreachedRadius = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mWheelPaint.getStrokeWidth() * 2 - mWheelPaint2.getStrokeWidth() * 2 - mWheelPaint3.getStrokeWidth()) / 2;
//        sUnreachedRadius = mUnreachedRadius - mUnreachedWidth /2 - mUnreachedWidth * 2 / 3-mUnreachedWidth/4 ;
    }

    private void refershWheelCurPosition(double cos) {
        mWheelCurX = calcXLocationInWheel(mCurAngle, cos);
        mWheelCurY = calcYLocationInWheel(cos);
    }

    private void refershWheelCurPosition2(double cos, double mCurAngle) {
        mWheelCurX2 = calcXLocationInWheel2(mCurAngle, cos);
        mWheelCurY2 = calcYLocationInWheel2(cos);
        mWheelCurX22 = calcXLocationInWheel(mCurAngle, cos);
        mWheelCurY22 = calcYLocationInWheel(cos);
    }

    private void refershWheelCurPosition3(double cos, double mCurAngle) {
        mWheelCurX3 = calcXLocationInWheel2(mCurAngle, cos);
        mWheelCurY3 = calcYLocationInWheel2(cos);
        mWheelCurX33 = calcXLocationInWheel(mCurAngle, cos);
        mWheelCurY33 = calcYLocationInWheel(cos);
    }

    private void refershPosition() {
        mCurAngle = (double) mCurProcess / mMaxProcess * 360.0;
        double cos = -Math.cos(Math.toRadians(mCurAngle));
        refershWheelCurPosition(cos);
        mCurAngle2 = mCurAngle;
        mCurAngle3 = mCurAngle;
        refershWheelCurPosition2(cos, mCurAngle);
        refershWheelCurPosition3(cos, mCurAngle);
    }

    private float calcXLocationInWheel(double angle, double cos) {
        if (angle < 180) {
            return (float) (getMeasuredWidth() / 2 + Math.sqrt(1 - cos * cos) * mUnreachedRadius);
        } else {
            return (float) (getMeasuredWidth() / 2 - Math.sqrt(1 - cos * cos) * mUnreachedRadius);
        }
    }

    private float calcYLocationInWheel(double cos) {
        return getMeasuredWidth() / 2 + mUnreachedRadius * (float) cos;
    }

    private float calcXLocationInWheel2(double angle, double cos) {

        if (angle < 180) {
            return (float) (getMeasuredWidth() / 2 + Math.sqrt(1 - cos * cos) * sUnreachedRadius);
        } else {
            return (float) (getMeasuredWidth() / 2 - Math.sqrt(1 - cos * cos) * sUnreachedRadius);
        }
    }

    private float calcYLocationInWheel2(double cos) {

        return getMeasuredWidth() / 2 + sUnreachedRadius * (float) cos;
    }

    /**
     * 拿到倾斜的cos值
     */
    private float computeCos(float x, float y) {
        float width = x - getWidth() / 2;
        float height = y - getHeight() / 2;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INATANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_MAX_PROCESS, mMaxProcess);
        bundle.putInt(INSTANCE_CUR_PROCESS, mCurProcess);
        bundle.putInt(INSTANCE_REACHED_COLOR, mReachedColor);
        bundle.putFloat(INSTANCE_REACHED_WIDTH, mReachedWidth);
        bundle.putBoolean(INSTANCE_REACHED_CORNER_ROUND, isHasReachedCornerRound);
        bundle.putInt(INSTANCE_UNREACHED_COLOR, mUnreachedColor);
        bundle.putFloat(INSTANCE_UNREACHED_WIDTH, mUnreachedWidth);
        bundle.putInt(INSTANCE_POINTER_COLOR, mPointerColor);
        bundle.putFloat(INSTANCE_POINTER_RADIUS, mPointerRadius);
        bundle.putBoolean(INSTANCE_POINTER_SHADOW, isHasPointerShadow);
        bundle.putFloat(INSTANCE_POINTER_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBoolean(INSTANCE_WHEEL_SHADOW, isHasWheelShadow);
        bundle.putFloat(INSTANCE_WHEEL_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBoolean(INSTANCE_WHEEL_HAS_CACHE, isHasCache);
        bundle.putBoolean(INSTANCE_WHEEL_CAN_TOUCH, isCanTouch);
        bundle.putBoolean(INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE, isScrollOneCircle);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INATANCE_STATE));
            mMaxProcess = bundle.getInt(INSTANCE_MAX_PROCESS);
            mCurProcess = bundle.getInt(INSTANCE_CUR_PROCESS);
            mReachedColor = bundle.getInt(INSTANCE_REACHED_COLOR);
            mReachedWidth = bundle.getFloat(INSTANCE_REACHED_WIDTH);
            isHasReachedCornerRound = bundle.getBoolean(INSTANCE_REACHED_CORNER_ROUND);
            mUnreachedColor = bundle.getInt(INSTANCE_UNREACHED_COLOR);
            mUnreachedWidth = bundle.getFloat(INSTANCE_UNREACHED_WIDTH);
            mPointerColor = bundle.getInt(INSTANCE_POINTER_COLOR);
            mPointerRadius = bundle.getFloat(INSTANCE_POINTER_RADIUS);
            isHasPointerShadow = bundle.getBoolean(INSTANCE_POINTER_SHADOW);
            mPointerShadowRadius = bundle.getFloat(INSTANCE_POINTER_SHADOW_RADIUS);
            isHasWheelShadow = bundle.getBoolean(INSTANCE_WHEEL_SHADOW);
            mPointerShadowRadius = bundle.getFloat(INSTANCE_WHEEL_SHADOW_RADIUS);
            isHasCache = bundle.getBoolean(INSTANCE_WHEEL_HAS_CACHE);
            isCanTouch = bundle.getBoolean(INSTANCE_WHEEL_CAN_TOUCH);
            isScrollOneCircle = bundle.getBoolean(INSTANCE_WHEEL_SCROLL_ONLY_ONE_CIRCLE);
            initPaints();
        } else {
            super.onRestoreInstanceState(state);
        }

        if (mChangListener != null) {
            mChangListener.onChanged(this, mCurProcess);
        }
    }

    private int getSelectedValue() {
        return Math.round(mMaxProcess * ((float) mCurAngle / 360));
    }

    public int getCurProcess() {
        return mCurProcess;
    }

    public void setCurProcess(int curProcess) {
        this.mCurProcess = curProcess > mMaxProcess ? mMaxProcess : curProcess;
        if (mChangListener != null) {
            mChangListener.onChanged(this, curProcess);
        }
        refershPosition();
        invalidate();
    }

    public int getMaxProcess() {
        return mMaxProcess;
    }

    public void setMaxProcess(int maxProcess) {
        mMaxProcess = maxProcess;
        refershPosition();
        invalidate();
    }

    public int getReachedColor() {
        return mReachedColor;
    }

    public void setReachedColor(int reachedColor) {
        this.mReachedColor = reachedColor;
        mReachedPaint.setColor(reachedColor);
        mReachedEdgePaint.setColor(reachedColor);
        invalidate();
    }

    public int getUnreachedColor() {
        return mUnreachedColor;
    }

    public void setUnreachedColor(int unreachedColor) {
        this.mUnreachedColor = unreachedColor;
        mWheelPaint.setColor(unreachedColor);
        invalidate();
    }

    public int getTimeBackgroundColor() {
        return mTimeColor;
    }

    public void setTimeBackgroundColor(int unreachedColor) {
        this.mTimeColor = unreachedColor;
        mWheelPaint2.setColor(unreachedColor);
        invalidate();
    }

    public float getReachedWidth() {
        return mReachedWidth;
    }

    public void setReachedWidth(float reachedWidth) {
        this.mReachedWidth = reachedWidth;
        mReachedPaint.setStrokeWidth(reachedWidth);
        mReachedEdgePaint.setStrokeWidth(reachedWidth);
        invalidate();
    }

    public boolean isHasReachedCornerRound() {
        return isHasReachedCornerRound;
    }

    public void setHasReachedCornerRound(boolean hasReachedCornerRound) {
        isHasReachedCornerRound = hasReachedCornerRound;
        mReachedPaint.setStrokeCap(hasReachedCornerRound ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        invalidate();
    }

    public float getUnreachedWidth() {
        return mUnreachedWidth;
    }

    public void setUnreachedWidth(float unreachedWidth) {
        this.mUnreachedWidth = unreachedWidth;
        mWheelPaint.setStrokeWidth(unreachedWidth);
        refershUnreachedWidth();
        invalidate();
    }

    public int getPointerColor() {
        return mPointerColor;
    }

    public void setPointerColor(int pointerColor) {
        this.mPointerColor = pointerColor;
        mPointerPaint.setColor(pointerColor);
    }

    public float getPointerRadius() {
        return mPointerRadius;
    }

    public void setPointerRadius(float pointerRadius) {
        this.mPointerRadius = pointerRadius;
        mPointerPaint.setStrokeWidth(pointerRadius);
        invalidate();
    }

    public boolean isHasWheelShadow() {
        return isHasWheelShadow;
    }

    public void setWheelShadow(float wheelShadow) {
        this.mWheelShadowRadius = wheelShadow;
        if (wheelShadow == 0) {
            isHasWheelShadow = false;
            mWheelPaint.clearShadowLayer();
            mCacheCanvas = null;
            mCacheBitmap.recycle();
            mCacheBitmap = null;
        } else {
            mWheelPaint.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
            setSoftwareLayer();
        }
        invalidate();
    }

    public float getWheelShadowRadius() {
        return mWheelShadowRadius;
    }

    public boolean isHasPointerShadow() {
        return isHasPointerShadow;
    }

    public float getPointerShadowRadius() {
        return mPointerShadowRadius;
    }

    public void setPointerShadowRadius(float pointerShadowRadius) {
        this.mPointerShadowRadius = pointerShadowRadius;
        if (mPointerShadowRadius == 0) {
            isHasPointerShadow = false;
            mPointerPaint.clearShadowLayer();
        } else {
            mPointerPaint.setShadowLayer(pointerShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
            setSoftwareLayer();
        }
        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        mChangListener = listener;
    }

    public interface OnSeekBarChangeListener {
        void onChanged(CircleOutSizeSeekBar seekbar, int curValue);
    }
}

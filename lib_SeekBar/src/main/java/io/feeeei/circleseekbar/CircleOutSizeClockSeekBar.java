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

import static android.content.ContentValues.TAG;

/**
 * Created by gaopengfei on 15/11/15.
 */
public class CircleOutSizeClockSeekBar extends View {

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
    private double mCenterStartCurAngle;
    private double mCenterEndCurAngle;
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
    private float sUnreachedRadius;
    private Paint mWheelPaint3;
    private Bitmap mBitmapDegrees;
    private Bitmap mBitmapDegrees2;
    private float mCenterStartWheelCurX;
    private float mCenterStartWheelCurY;
    private float mCenterEndWheelCurY;
    private float mCenterEndWheelCurX;
    private float mCenterStartOutSizeWheelCurX;
    private float mCenterStartOutSizeWheelCurY;
    private float mCenterEndOutSizeWheelCurX;
    private float mCenterEndOutSizeWheelCurY;
    private float mBitmapStartOutSizeWheelCurX;
    private float mBitmapStartOutSizeWheelCurY;
    private float mBitmapEndOutSizeWheelCurX;
    private float mBitmapEndOutSizeWheelCurY;
    private int mTimeColor;
    private int mDurationBitmapid;
    private int mStartBackgroundBitmapid;
    private int mEndBackgroundBitmapid;
    private Bitmap mStartBackgroundBitmap;
    private Bitmap mEndBackgroundBitmap;
    private double mEndOutSizeCurAngle;
    private double mStartOutSizeCurAngle;
    private Paint mReachedPaintStart;
    private Paint mReachedPaintEnd;
    private int mReachedColorSelect;
    private int mReachedColorStartNormal;
    private int mReachedColorStartSelect;
    private int mReachedColorEndNormal;
    private int mReachedColorEndSelect;
    private int mStartBitmapSelectid;
    private int mEndBitmapSelectid;
    private int mStartBackgroundBitmapSelectid;
    private int mEndBackgroundBitmapSelectid;
    private Context mContext;
    private float mCenterStartCos;
    private double mLastCurAngle;
    private boolean isCenterStartOutTouch;

    private int mCurStartInProcess;
    private int mMaxEndInProcess = 60;
    private int mMaxStartInProcess = 60;
    private int mMaxEndProcess;
    private int mCurEndProcess;
    private int mCurStartProcess;
    private int mMaxStartProcess;
    private int mCurEndInProcess;
    private float mCenterX;
    private float mCenterY;
    private int mGapProcess = 5;
    private boolean isStartProcessSelect = true;
    private boolean isStartSelect = true;
    private boolean isEndSelect = true;
    private boolean isEndProcessSelect = true;

    public CircleOutSizeClockSeekBar(Context context) {
        this(context, null);
    }

    public CircleOutSizeClockSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleOutSizeClockSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs, defStyleAttr);
        initPadding();
        initPaints();
        mEndBitmap = setBitmap(context, mEndBitmapSelectid);
        mStartBitmap = setBitmap(context, mStartBitmapSelectid);
        mStartBackgroundBitmap = setBitmap(context, mStartBackgroundBitmapSelectid);
        mEndBackgroundBitmap = setBitmap(context, mEndBackgroundBitmapSelectid);
        mDurationBitmap = setBitmap(context, mDurationBitmapid);


    }


    public void setStartProcessSelect(boolean isSelect) {
        isStartProcessSelect = isSelect;
        mStartBackgroundBitmap = setBitmap(mContext, !isSelect ? mStartBackgroundBitmapid : mStartBackgroundBitmapSelectid);
        if (mReachedPaintStart != null) {
            mReachedPaintStart.setColor(isSelect ? mReachedColorStartSelect : mReachedColorStartNormal);
        }
    }

    public void setStartSelect(boolean isSelect) {

        mStartBitmap = setBitmap(mContext, !isSelect ? mStartBitmapid : mStartBitmapSelectid);
    }

    public void setSelect(boolean startSelect, boolean endSelect, boolean startProcessSelect, boolean endProcessSelect) {

        isStartSelect = startSelect;
        isEndSelect = endSelect;
        if (mReachedPaint != null) {
//            isCanTouch = !startSelect && !endSelect;
            mReachedPaint.setColor(startSelect && endSelect ? mReachedColorSelect : mReachedColor);
        }
        setStartSelect(startSelect);
        setEndSelect(endSelect);
        setStartProcessSelect(startSelect ? startProcessSelect : startSelect);
        setEndProcessSelect(endSelect ? endProcessSelect : endSelect);
        invalidate();
    }

    public void setEndSelect(boolean isSelect) {
        mEndBitmap = setBitmap(mContext, !isSelect ? mEndBitmapid : mEndBitmapSelectid);
//        mEndBackgroundBitmap = setBitmap(mContext, !isSelect ? mEndBackgroundBitmapid : mEndBackgroundBitmapSelectid);
//        if (mReachedPaintEnd != null) {
//            mReachedPaintEnd.setColor(isSelect ? mReachedColorEndSelect : mReachedColorEndNormal);
//        }
    }

    public void setEndProcessSelect(boolean isSelect) {
        isEndProcessSelect = isSelect;
        mEndBackgroundBitmap = setBitmap(mContext, !isSelect ? mEndBackgroundBitmapid : mEndBackgroundBitmapSelectid);
        if (mReachedPaintEnd != null) {
            mReachedPaintEnd.setColor(isSelect ? mReachedColorEndSelect : mReachedColorEndNormal);
        }
    }

    private Bitmap setBitmap(Context context, int bitmapid) {
        Bitmap bitmap = null;
        if (bitmapid != 0) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    bitmapid);
        }
        return bitmap;
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
        mReachedPaint.setColor(mReachedColorSelect);
        mReachedPaint.setStyle(Paint.Style.STROKE);
        mReachedPaint.setStrokeWidth(mReachedWidth);
        if (isHasReachedCornerRound) {
            mReachedPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        mReachedPaintStart = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedPaintStart.setColor(mReachedColorStartSelect);
        mReachedPaintStart.setStyle(Paint.Style.STROKE);
        mReachedPaintStart.setStrokeWidth(mReachedWidth);
        if (isHasReachedCornerRound) {
            mReachedPaintStart.setStrokeCap(Paint.Cap.ROUND);
        }
        mReachedPaintEnd = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedPaintEnd.setColor(mReachedColorEndSelect);
        mReachedPaintEnd.setStyle(Paint.Style.STROKE);
        mReachedPaintEnd.setStrokeWidth(mReachedWidth);
        if (isHasReachedCornerRound) {
            mReachedPaintEnd.setStrokeCap(Paint.Cap.ROUND);
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
        mMaxStartProcess = a.getInt(R.styleable.CircleSeekBar_wheel_max_start_process, 100);
        mCurStartProcess = a.getInt(R.styleable.CircleSeekBar_wheel_cur_start_process, 0);
        mMaxEndProcess = a.getInt(R.styleable.CircleSeekBar_wheel_max_end_process, 100);
        mCurEndProcess = a.getInt(R.styleable.CircleSeekBar_wheel_cur_end_process, 0);
        mGapProcess = a.getInt(R.styleable.CircleSeekBar_wheel_gap_process, 1);
        if (mCurProcess > mMaxProcess) mCurProcess = mMaxProcess;
        if (mCurStartProcess > mMaxStartProcess) mCurStartProcess = mMaxStartProcess;
        if (mCurEndProcess > mMaxEndProcess) mCurEndProcess = mMaxEndProcess;
        mReachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color, getColor(R.color.def_reached_color));
        mReachedColorSelect = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color_select, getColor(R.color.def_reached_color));
        mReachedColorStartNormal = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color_start_normal, getColor(R.color.def_reached_color));
        mReachedColorStartSelect = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color_start_select, getColor(R.color.def_reached_color));
        mReachedColorEndNormal = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color_end_normal, getColor(R.color.def_reached_color));
        mReachedColorEndSelect = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color_end_select, getColor(R.color.def_reached_color));
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
        mDurationBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_center_background_drawable, R.drawable.duration_set_button_on);
        mStartBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_start_drawable, 0);
        mEndBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_end_drawable, 0);
        mStartBackgroundBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_center_start_drawable, 0);
        mEndBackgroundBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_center_end_drawable, 0);
        mStartBitmapSelectid = a.getResourceId(R.styleable.CircleSeekBar_wheel_start_drawable_select, 0);
        mEndBitmapSelectid = a.getResourceId(R.styleable.CircleSeekBar_wheel_end_drawable_select, 0);
        mStartBackgroundBitmapSelectid = a.getResourceId(R.styleable.CircleSeekBar_wheel_center_start_drawable_select, 0);
        mEndBackgroundBitmapSelectid = a.getResourceId(R.styleable.CircleSeekBar_wheel_center_end_drawable_select, 0);
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
        refershStartOutPosition();
        refershEndOutPosition();
        refershStartInPosition();
        refershEndInPosition();
    }

    public Bitmap setDegrees(Bitmap bm, float degrees) {
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        // 得到新的图片.
        int width = bm.getWidth();
        int height = bm.getHeight();
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
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
        mCenterX = (left + right) / 2;
        mCenterY = (top + bottom) / 2;

        float wheelRadius = (canvas.getWidth() - getPaddingLeft() - getPaddingRight()) / 2 - mUnreachedWidth / 2;
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        if (isHasCache) {
            if (mCacheCanvas == null) {
                buildCache(mCenterX, mCenterY, wheelRadius);
            }
            canvas.drawBitmap(mCacheBitmap, 0, 0, null);
        } else {
            canvas.drawCircle(mCenterX, mCenterY, wheelRadius, mWheelPaint);
            canvas.drawCircle(mCenterX, mCenterY, wheelRadius - mUnreachedWidth / 2 - mWheelPaint2.getStrokeWidth() - mWheelPaint3.getStrokeWidth() / 2, mWheelPaint3);
            canvas.drawCircle(mCenterX, mCenterY, wheelRadius - mUnreachedWidth / 2 - mWheelPaint2.getStrokeWidth() / 2, mWheelPaint2);

        }
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        drawClock(canvas, mCenterX, mCenterY, wheelRadius);
        drawLine(canvas);
        drawAnglebg(canvas, left, top, right, bottom);
        drawBitmap(canvas);


    }

    private void drawAnglebg(Canvas canvas, float left, float top, float right, float bottom) {
        //画选中区域
        double mAngle = 0;
        double subAngle = getSubAngle(mCenterStartCurAngle, mStartOutSizeCurAngle);
        if (subAngle == 0) {
        } else {
            mAngle = mCenterStartCurAngle > mStartOutSizeCurAngle ? (-360 + mCenterStartCurAngle) : mCenterStartCurAngle;
            canvas.drawArc(new RectF(left, top, right, bottom), (float) (-90 + mAngle), (float) ((float) mStartOutSizeCurAngle - (mAngle)), false, mReachedPaintStart);
        }

        mAngle = mStartOutSizeCurAngle >= mEndOutSizeCurAngle ? (-360 + mStartOutSizeCurAngle) : mStartOutSizeCurAngle;
        canvas.drawArc(new RectF(left, top, right, bottom), (float) (-90 + mAngle), (float) ((float) mEndOutSizeCurAngle - (mAngle)), false, mReachedPaint);


        subAngle = getSubAngle(mCenterEndCurAngle, mEndOutSizeCurAngle);
        if (subAngle == 0) {
        } else {
            mAngle = mCenterEndCurAngle > mEndOutSizeCurAngle ? (-360 + mCenterEndCurAngle) : mCenterEndCurAngle;
            canvas.drawArc(new RectF(left, top, right, bottom), (float) (-90 + mAngle), (float) ((float) mEndOutSizeCurAngle - (mAngle)), false, mReachedPaintEnd);
        }


    }

    private void drawBitmap(Canvas canvas) {
        if (mStartBitmap == null) {
            //画锚点
        } else {

            canvas.drawCircle(mBitmapStartOutSizeWheelCurX, mBitmapStartOutSizeWheelCurY, 0, mReachedPaintStart);
            canvas.drawCircle(mBitmapEndOutSizeWheelCurX, mBitmapEndOutSizeWheelCurY, 0, mReachedPaintEnd);
            if (mStartBitmap != null) {
                canvas.drawBitmap(mStartBitmap, mBitmapStartOutSizeWheelCurX - mStartBitmap.getWidth() / 2, mBitmapStartOutSizeWheelCurY - mStartBitmap.getHeight() / 2, mPointerPaint);
            }
            if (mEndBitmap != null) {
                canvas.drawBitmap(mEndBitmap, mBitmapEndOutSizeWheelCurX - mEndBitmap.getWidth() / 2, mBitmapEndOutSizeWheelCurY - mEndBitmap.getHeight() / 2, mPointerPaint);
            }
        }
        if (mStartBackgroundBitmap != null) {
            //画三角形
            mBitmapDegrees = setDegrees(mStartBackgroundBitmap, (float) (-90 + mCenterStartCurAngle));
//        canvas.drawBitmap(mBitmapDegrees, left + (right - left) / 2 - mBitmapDegrees.getWidth() / 2, calcYLocationInWheel2(-1) - mBitmapDegrees.getHeight() / 2, mPointerPaint);
            canvas.drawBitmap(mBitmapDegrees, mCenterStartWheelCurX - mBitmapDegrees.getWidth() / 2, mCenterStartWheelCurY - mBitmapDegrees.getHeight() / 2, mPointerPaint);
        }
        if (mEndBackgroundBitmap != null) {
            mBitmapDegrees2 = setDegrees(mEndBackgroundBitmap, (float) (-90 + mCenterEndCurAngle));
            canvas.drawBitmap(mBitmapDegrees2, mCenterEndWheelCurX - mBitmapDegrees2.getWidth() / 2, mCenterEndWheelCurY - mBitmapDegrees2.getHeight() / 2, mPointerPaint);
        }
    }

    private void drawClock(Canvas canvas, float centerX, float centerY, float wheelRadius) {
        if (mDurationBitmap != null) {
            //画时钟
            Bitmap bitmap = setImgSize(mDurationBitmap, (wheelRadius + mUnreachedWidth * 2), (wheelRadius + mUnreachedWidth * 2));
            canvas.drawBitmap(bitmap, centerX - bitmap.getWidth() / 2, centerY - bitmap.getHeight() / 2, mPointerPaint);
        }
    }

    private void drawLine(Canvas canvas) {
        //画连接线
        canvas.save();
        Paint mp = mReachedPaintEnd;
        mp.setStrokeWidth(5);
        canvas.rotate((float) (mCenterEndCurAngle), mCenterX, mCenterY);
        if (mEndBackgroundBitmap != null) {
            float v = mUnreachedRadius - mWheelPaint.getStrokeWidth() / 2;
            float v2 = mUnreachedRadius - mWheelPaint.getStrokeWidth() / 2 - mWheelPaint2.getStrokeWidth() - mWheelPaint3.getStrokeWidth() / 2 + mEndBackgroundBitmap.getHeight() / 3;
            canvas.drawLine(mCenterX, mCenterY - v, mCenterX, mCenterY - v2, mp);
        }

        canvas.restore();
        canvas.save();
        mp.setStrokeWidth(mReachedWidth);
        mp = mReachedPaintStart;
        mp.setStrokeWidth(5);
        canvas.rotate((float) (mCenterStartCurAngle), mCenterX, mCenterY);
        if (mStartBackgroundBitmap != null) {
            float v = mUnreachedRadius - mWheelPaint.getStrokeWidth() / 2;
            float v2 = mUnreachedRadius - mWheelPaint.getStrokeWidth() / 2 - mWheelPaint2.getStrokeWidth() - mWheelPaint3.getStrokeWidth() / 2 + mStartBackgroundBitmap.getHeight() / 3;
            canvas.drawLine(mCenterX, mCenterY - v, mCenterX, mCenterY - v2, mp);
        }
        mp.setStrokeWidth(mReachedWidth);
        canvas.restore();

    }

    private void buildCache(float centerX, float centerY, float wheelRadius) {
        mCacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheBitmap);

        //画环
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius - 35, mWheelPaint);
    }

    private double mStartSubAngle;
    private double mEndSubAngle;
    private double mStartOutSizeSubAngle;
    private double mEndOutSizeSubAngle;
    private double mEndDegree;
    private double mSubAngle;
    private double mDegreeCycle = 720;
    int isStartBitMapTouch = 1;
    int isEndBitMapTouch = 2;
    int isStartOutSizeBitmapTouch = 3;
    int isEndOutSizeBitmapTouch = 4;
    int isCircleBitmapTouch = 5;
    int isBitMapTouch = 0;
    float mStartY = 0;
    float mStartX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (isCanTouch) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isBitMapTouch = 0;
                mStartX = 0;
                mStartY = 0;
                mSubAngle = 0;
                if (isEndProcessSelect && mBitmapDegrees2 != null && isBitMapTouch(mBitmapDegrees2, x, y, mCenterEndWheelCurX, mCenterEndWheelCurY)) {
                    isBitMapTouch = isEndBitMapTouch;
                    mStartX = mCenterEndWheelCurX;
                    mStartY = mCenterEndWheelCurY;
                    mSubAngle = mEndSubAngle;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (isStartProcessSelect && mBitmapDegrees != null && isBitMapTouch(mBitmapDegrees, x, y, mCenterStartWheelCurX, mCenterStartWheelCurY)) {
                    isBitMapTouch = isStartBitMapTouch;
                    mStartX = mCenterStartWheelCurX;
                    mStartY = mCenterStartWheelCurY;
                    mSubAngle = mStartSubAngle;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (isEndSelect && mEndBitmap != null && isBitMapTouch(mEndBitmap, x, y, mBitmapEndOutSizeWheelCurX, mBitmapEndOutSizeWheelCurY)) {
                    isBitMapTouch = isEndOutSizeBitmapTouch;
                    mStartX = mBitmapEndOutSizeWheelCurX;
                    mStartY = mBitmapEndOutSizeWheelCurY;
                    mSubAngle = mEndOutSizeSubAngle;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (isStartSelect && mStartBitmap != null && isBitMapTouch(mStartBitmap, x, y, mBitmapStartOutSizeWheelCurX, mBitmapStartOutSizeWheelCurY)) {
                    isBitMapTouch = isStartOutSizeBitmapTouch;
                    mStartX = mBitmapStartOutSizeWheelCurX;
                    mStartY = mBitmapStartOutSizeWheelCurY;
                    mSubAngle = mStartOutSizeSubAngle;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (isStartSelect && isEndSelect && isMoveSelectedArea(x, y)) {
                    isBitMapTouch = isCircleBitmapTouch;
                    mStartX = x;
                    mStartY = y;
                    mSubAngle = mStartOutSizeSubAngle;
                    mEndDegree = mEndOutSizeSubAngle;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    isBitMapTouch = 0;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
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
                double moveDegree = SeekUtil.getMoveDegree(x, y, mCenterX, mCenterY, mStartX, mStartY);
                mSubAngle = (mSubAngle + moveDegree);
                mSubAngle = getDegreeCycleSubAngle(mSubAngle, mDegreeCycle);
                mSubAngle = (mSubAngle < 0) ? mSubAngle + mDegreeCycle : mSubAngle % mDegreeCycle;
                if (isBitMapTouch == isCircleBitmapTouch) {
                    mEndDegree = (float) (mEndDegree + moveDegree);
                    mEndDegree = getDegreeCycleSubAngle(mEndDegree, mDegreeCycle);
                    mEndDegree = (mEndDegree < 0) ? mEndDegree + mDegreeCycle : mEndDegree % mDegreeCycle;
                }
                mCurProcess = getSelectedValue();
                refershWheelCurPosition(cos);
                if (isBitMapTouch == isStartBitMapTouch) {
                    double sCenterStartCurAngle = getCenterAngle(mCurAngle, mStartOutSizeCurAngle, mMaxCenterStartCurAngle, mCenterStartCurAngle);
                    int sCuStartInProcess = getSelectedValue(mMaxStartProcess, Math.abs(mStartOutSizeCurAngle - sCenterStartCurAngle));
                    if (sCuStartInProcess % mGapProcess == 0) {
                        mCurStartInProcess = sCuStartInProcess;
                        mStartSubAngle = mSubAngle;
                        mCenterStartCurAngle = sCenterStartCurAngle;
                        Log.e(TAG, "onTouchEvent mCenterStartCurAngle: " + mCenterStartCurAngle);
                        double mCurAngle = getAbsCurAngle(sCenterStartCurAngle);
                        double mCenterStartCos = getCos(mCurAngle);
                        refershWheelCurPosition2(mCenterStartCos, mCurAngle);

                    }

                } else if (isBitMapTouch == isEndBitMapTouch) {
                    double sCenterEndCurAngle = getCenterAngle(mCurAngle, mEndOutSizeCurAngle, mMaxCenterEndCurAngle, mCenterEndCurAngle);
                    int sCurEndInProcess = getSelectedValue(mMaxEndProcess, Math.abs(mEndOutSizeCurAngle - sCenterEndCurAngle));
                    if (sCurEndInProcess % mGapProcess == 0) {
                        mEndSubAngle = mSubAngle;
                        mCenterEndCurAngle = sCenterEndCurAngle;
                        mCurEndInProcess = sCurEndInProcess;
                        double mCurAngle = getAbsCurAngle(sCenterEndCurAngle);
                        double mCenterStartCos = getCos(mCurAngle);
                        refershWheelCurPosition3(mCenterStartCos, mCurAngle);
                    }

                } else if (isBitMapTouch == isStartOutSizeBitmapTouch) {
                    int sCurStartProcess = getSelectedValue(mMaxStartProcess, mSubAngle);
                    if (sCurStartProcess % mGapProcess == 0) {
                        mCurStartProcess = sCurStartProcess;
                        refershStartOutPosition();
                        mCurStartInProcess = getSelectedValue(mMaxStartProcess, Math.abs(mStartOutSizeCurAngle - mCenterStartCurAngle));
                    }

                } else if (isBitMapTouch == isEndOutSizeBitmapTouch) {
                    int sCurEndProcess = getSelectedValue(mMaxEndProcess, mSubAngle);
                    if (sCurEndProcess % mGapProcess == 0) {
                        mCurEndProcess = sCurEndProcess;
                        refershEndOutPosition();
                        mCurEndInProcess = getSelectedValue(mMaxEndProcess, Math.abs(mEndOutSizeCurAngle - mCenterEndCurAngle));

                    }
                } else if (isBitMapTouch == isCircleBitmapTouch) {

                    int sCurStartProcess = getSelectedValue(mMaxStartProcess, mSubAngle);
                    int sCurEndProcess = getSelectedValue(mMaxEndProcess, mEndDegree);
                    if (sCurStartProcess % mGapProcess == 0) {
                        mCurStartProcess = sCurStartProcess;
                        refershStartOutPosition();
                        mCurStartInProcess = getSelectedValue(mMaxStartProcess, Math.abs(mStartOutSizeCurAngle - mCenterStartCurAngle));
//                    }
//                    if (sCurEndProcess % mGapProcess == 0) {
                        mCurEndProcess = sCurEndProcess;
                        refershEndOutPosition();
                        mCurEndInProcess = getSelectedValue(mMaxEndProcess, Math.abs(mEndOutSizeCurAngle - mCenterEndCurAngle));
                    }
                }
                onChangeListener();
                if (isBitMapTouch == isCircleBitmapTouch) {
                    mStartY = y;
                    mStartX = x;
                } else if (isBitMapTouch == isStartOutSizeBitmapTouch || isBitMapTouch == isEndOutSizeBitmapTouch) {
                    refershWheelCurXYPosition(cos, mCurAngle);
                } else {
                    refershWheelCurInXYPosition(cos, mCurAngle);

                }
                invalidate();
//                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                isCenterStartOutTouch = false;
                invalidate();
                getParent().requestDisallowInterceptTouchEvent(false);
                isBitMapTouch = 0;
//                return true;
            }
            return true;
        }
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.onTouchEvent(event);
    }

    public static double getDegreeCycleSubAngle(double subAngle, double degree) {
        return (subAngle < 0) ? subAngle + degree : subAngle % degree;
    }

    private double getCos(double mCurAngle) {
        return -Math.cos(Math.toRadians(mCurAngle < 0 ? (360 - mCurAngle) : mCurAngle));
    }

    private void onChangeListener() {
        if (mChangListener != null) {
            mChangListener.onChanged(this, Math.abs(mCurStartProcess), Math.abs(mCurEndProcess), Math.abs(mCurStartInProcess), Math.abs(mCurEndInProcess));
        }
    }

    private double getOutSizeCenterCurAngle(double curAngle, double centerStartCurAngle, double startOutSizeCurAngle) {
        double subAngle = getSubAngle(centerStartCurAngle, startOutSizeCurAngle);
        if (subAngle == 0) {
            centerStartCurAngle = curAngle;
        } else {
            centerStartCurAngle += (curAngle - startOutSizeCurAngle);
        }
//        double mCurAngle = getAbsCurAngle(centerStartCurAngle);
        return centerStartCurAngle;
    }

    private double mMaxCenterStartCurAngle = 30;
    private double mMaxCenterEndCurAngle = 30;

    private double getCenterAngle(double mCurAngle, double mStartOutSizeCurAngle, double mMaxCenterCurAngle, double mCenterStartCurAngle) {
        double subAngle = getSubAngle(mCurAngle, mStartOutSizeCurAngle);
        if ((subAngle > (360 - mMaxCenterCurAngle * 2)) && isCenterStartOutTouch) {
            mCenterStartCurAngle = mStartOutSizeCurAngle;
        }
        if ((subAngle - mMaxCenterCurAngle) < 0) {
            isCenterStartOutTouch = true;
            mCenterStartCurAngle = mCurAngle;

        } else if ((subAngle - mMaxCenterCurAngle * 2) < 0 && isCenterStartOutTouch) {
            mCenterStartCurAngle = mStartOutSizeCurAngle - mMaxCenterCurAngle;
        }
        return mCenterStartCurAngle;
    }

    private double getSubAngle(double curAngle, double startOutSizeCurAngle) {
        double subCurAngle = getAbsCurAngle(curAngle);
        double subStart = getAbsCurAngle(startOutSizeCurAngle);
        return subStart >= subCurAngle ? (subStart - subCurAngle) : (subStart - (subCurAngle - 360));
    }

    private double getAbsCurAngle(double curAngle) {
        return curAngle % 360 > 0 ? curAngle % 360 : 360 + curAngle % 360;
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

    private boolean isBitMapTouch(Bitmap mDurationBitmap, float x, float y, float lastx, float lasty) {
        int w = mDurationBitmap.getWidth() / 2;
        int h = mDurationBitmap.getHeight() / 2;
        return lastx - w <= x && x <= lastx + w && lasty - h <= y && y <= lasty + h;
    }

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

    private void refershWheelCurXYPosition(double cos, double mCurAngle) {
        mStartX = calcXLocationInWheel(mCurAngle, cos);
        mStartY = calcYLocationInWheel(cos);
    }

    private void refershWheelCurInXYPosition(double cos, double mCurAngle) {
        mStartX = calcXLocationInWheel2(mCurAngle, cos);
        mStartY = calcYLocationInWheel2(cos);
    }

    private void refershWheelCurPosition2(double cos, double mCurAngle) {
        mCenterStartWheelCurX = calcXLocationInWheel2(mCurAngle, cos);
        mCenterStartWheelCurY = calcYLocationInWheel2(cos);
        mCenterStartOutSizeWheelCurX = calcXLocationInWheel(mCurAngle, cos);
        mCenterStartOutSizeWheelCurY = calcYLocationInWheel(cos);
    }

    private void refershWheelCurPosition3(double cos, double mCurAngle) {
        mCenterEndWheelCurX = calcXLocationInWheel2(mCurAngle, cos);
        mCenterEndWheelCurY = calcYLocationInWheel2(cos);
        mCenterEndOutSizeWheelCurX = calcXLocationInWheel(mCurAngle, cos);
        mCenterEndOutSizeWheelCurY = calcYLocationInWheel(cos);
    }

    private void refershWheelCurPositionOutsizeBitmapStart(double cos, double mCurAngle) {
        mBitmapStartOutSizeWheelCurX = calcXLocationInWheel(mCurAngle, cos);
        mBitmapStartOutSizeWheelCurY = calcYLocationInWheel(cos);
    }

    private void refershWheelCurPositionOutsizeBitmapEnd(double cos, double mCurAngle) {
        mBitmapEndOutSizeWheelCurX = calcXLocationInWheel(mCurAngle, cos);
        mBitmapEndOutSizeWheelCurY = calcYLocationInWheel(cos);
    }

    private void refershPosition() {
        mCurAngle = (double) mCurProcess % 720 / mMaxProcess * 360.0;
        double cos = -Math.cos(Math.toRadians(mCurAngle));
        refershWheelCurPosition(cos);
    }

    private void refershStartOutPosition() {
        mStartOutSizeSubAngle = (double) mCurStartProcess % (mMaxStartProcess * 2) / mMaxStartProcess * 360.0;
        double mCurAngle = mStartOutSizeSubAngle % 360.0;
        mCenterStartCurAngle = getOutSizeCenterCurAngle(mCurAngle, mCenterStartCurAngle, mStartOutSizeCurAngle);
        mStartOutSizeCurAngle = mCurAngle;
        double cos = -Math.cos(Math.toRadians(mStartOutSizeCurAngle));
        refershWheelCurPositionOutsizeBitmapStart(cos, mStartOutSizeCurAngle);
        double outSizeCenterCurAngle = getAbsCurAngle(mCenterStartCurAngle);
        double mCenterStartCos = getCos(outSizeCenterCurAngle);
        refershWheelCurPosition2(mCenterStartCos, outSizeCenterCurAngle);
    }

    private void refershStartInPosition() {
        double mCenterCurAngle = (double) mCurStartInProcess % 720 / mMaxStartProcess * 360.0;
        mCenterStartCurAngle = (mStartOutSizeCurAngle - mCenterCurAngle) > 0 ? (mStartOutSizeCurAngle - mCenterCurAngle) : (360 + mStartOutSizeCurAngle - mCenterCurAngle);
        double cos = -Math.cos(Math.toRadians(mCenterStartCurAngle));
        refershWheelCurPosition2(cos, mCenterStartCurAngle);
    }

    private void refershEndInPosition() {
        double mCenterCurAngle = (double) mCurEndInProcess % 720 / mMaxEndProcess * 360.0;
        mCenterEndCurAngle = (mEndOutSizeCurAngle - mCenterCurAngle) > 0 ? (mEndOutSizeCurAngle - mCenterCurAngle) : (360 + mEndOutSizeCurAngle - mCenterCurAngle);
        double cos = -Math.cos(Math.toRadians(mCenterEndCurAngle));
        refershWheelCurPosition3(cos, mCenterEndCurAngle);
    }

    private void refershEndOutPosition() {
        mEndOutSizeSubAngle = (double) mCurEndProcess % (mMaxEndProcess * 2) / mMaxEndProcess * 360.0;
        double mCurAngle = mEndOutSizeSubAngle % 360.0;
        mCenterEndCurAngle = getOutSizeCenterCurAngle(mCurAngle, mCenterEndCurAngle, mEndOutSizeCurAngle);
        mEndOutSizeCurAngle = mCurAngle;
        double cos = -Math.cos(Math.toRadians(mEndOutSizeCurAngle));
        refershWheelCurPositionOutsizeBitmapEnd(cos, mEndOutSizeCurAngle);
        double outSizeCenterCurAngle = getAbsCurAngle(mCenterEndCurAngle);
        double mCenterStartCos = getCos(outSizeCenterCurAngle);
        refershWheelCurPosition3(mCenterStartCos, outSizeCenterCurAngle);
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

        onChangeListener();
    }

    private int getSelectedValue() {
        return Math.round(mMaxProcess * ((float) mCurAngle / 360));
    }

    private int getSelectedValue(int mMaxProcess, double mCurAngle) {
        return Math.round(mMaxProcess * ((float) mCurAngle / 360));
    }

    public int getCurProcess() {
        return mCurProcess;
    }

    public void setCurProcess(int curProcess) {
        this.mCurProcess = curProcess > mMaxProcess ? mMaxProcess : curProcess;
        onChangeListener();
        refershPosition();
        invalidate();
    }

    public void setCurEndInProcess(int curProcess) {
        this.mCurEndInProcess = curProcess > mMaxEndInProcess ? mMaxEndInProcess : curProcess;
        onChangeListener();
        refershEndInPosition();
        invalidate();
    }

    public void setCurStartInProcess(int curProcess) {
        this.mCurStartInProcess = curProcess > mMaxStartInProcess ? mMaxStartInProcess : curProcess;
        onChangeListener();
        refershStartInPosition();
        invalidate();
    }

    public void setCurEndProcess(int curProcess) {
        this.mCurEndProcess = curProcess > mMaxEndProcess * 2 ? mMaxEndProcess * 2 : curProcess;
        onChangeListener();
        refershEndOutPosition();
        invalidate();
    }

    public void setCurStartProcess(int curProcess) {
        this.mCurStartProcess = curProcess > mMaxStartProcess * 2 ? mMaxStartProcess * 2 : curProcess;
        onChangeListener();
        refershStartOutPosition();
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
        void onChanged(CircleOutSizeClockSeekBar seekbar, int curStartProcess, int curEndProcess, int curStartInProcess, int curEndInProcess);
    }

    public int getTimeColor() {
        return mTimeColor;
    }

    public void setTimeColor(int timeColor) {
        mTimeColor = timeColor;
    }

    public int getDurationBitmapid() {
        return mDurationBitmapid;
    }

    public void setDurationBitmapid(int durationBitmapid) {
        mDurationBitmapid = durationBitmapid;
    }

    public int getStartBackgroundBitmapid() {
        return mStartBackgroundBitmapid;
    }

    public void setStartBackgroundBitmapid(int startBackgroundBitmapid) {
        mStartBackgroundBitmapid = startBackgroundBitmapid;
    }

    public int getEndBackgroundBitmapid() {
        return mEndBackgroundBitmapid;
    }

    public void setEndBackgroundBitmapid(int endBackgroundBitmapid) {
        mEndBackgroundBitmapid = endBackgroundBitmapid;
    }

    public Bitmap getStartBackgroundBitmap() {
        return mStartBackgroundBitmap;
    }

    public void setStartBackgroundBitmap(Bitmap startBackgroundBitmap) {
        mStartBackgroundBitmap = startBackgroundBitmap;
    }

    public Bitmap getEndBackgroundBitmap() {
        return mEndBackgroundBitmap;
    }

    public void setEndBackgroundBitmap(Bitmap endBackgroundBitmap) {
        mEndBackgroundBitmap = endBackgroundBitmap;
    }

    public double getEndOutSizeCurAngle() {
        return mEndOutSizeCurAngle;
    }

    public void setEndOutSizeCurAngle(double endOutSizeCurAngle) {
        mEndOutSizeCurAngle = endOutSizeCurAngle;
    }

    public double getStartOutSizeCurAngle() {
        return mStartOutSizeCurAngle;
    }

    public void setStartOutSizeCurAngle(double startOutSizeCurAngle) {
        mStartOutSizeCurAngle = startOutSizeCurAngle;
    }

    public Paint getReachedPaintStart() {
        return mReachedPaintStart;
    }

    public void setReachedPaintStart(Paint reachedPaintStart) {
        mReachedPaintStart = reachedPaintStart;
    }

    public Paint getReachedPaintEnd() {
        return mReachedPaintEnd;
    }

    public void setReachedPaintEnd(Paint reachedPaintEnd) {
        mReachedPaintEnd = reachedPaintEnd;
    }

    public int getReachedColorSelect() {
        return mReachedColorSelect;
    }

    public void setReachedColorSelect(int reachedColorSelect) {
        mReachedColorSelect = reachedColorSelect;
    }

    public int getReachedColorStartNormal() {
        return mReachedColorStartNormal;
    }

    public void setReachedColorStartNormal(int reachedColorStartNormal) {
        mReachedColorStartNormal = reachedColorStartNormal;
    }

    public int getReachedColorStartSelect() {
        return mReachedColorStartSelect;
    }

    public void setReachedColorStartSelect(int reachedColorStartSelect) {
        mReachedColorStartSelect = reachedColorStartSelect;
    }

    public int getReachedColorEndNormal() {
        return mReachedColorEndNormal;
    }

    public void setReachedColorEndNormal(int reachedColorEndNormal) {
        mReachedColorEndNormal = reachedColorEndNormal;
    }

    public int getReachedColorEndSelect() {
        return mReachedColorEndSelect;
    }

    public void setReachedColorEndSelect(int reachedColorEndSelect) {
        mReachedColorEndSelect = reachedColorEndSelect;
    }

    public int getMaxEndInProcess() {
        return mMaxEndInProcess;
    }

    public void setMaxEndInProcess(int maxEndInProcess) {
        mMaxEndInProcess = maxEndInProcess;
    }

    public int getCurStartInProcess() {
        return mCurStartInProcess;
    }

    public int getMaxStartInProcess() {
        return mMaxStartInProcess;
    }

    public void setMaxStartInProcess(int maxStartInProcess) {
        mMaxStartInProcess = maxStartInProcess;
    }

    public int getMaxEndProcess() {
        return mMaxEndProcess;
    }

    public void setMaxEndProcess(int maxEndProcess) {
        mMaxEndProcess = maxEndProcess;
    }

    public int getCurEndProcess() {
        return mCurEndProcess;
    }

    public int getCurStartProcess() {
        return mCurStartProcess;
    }

    public int getMaxStartProcess() {
        return mMaxStartProcess;
    }

    public void setMaxStartProcess(int maxStartProcess) {
        mMaxStartProcess = maxStartProcess;
    }

    public int getCurEndInProcess() {
        return mCurEndInProcess;
    }

    /**
     * 是否实在选中区域
     *
     * @param eventX
     * @param eventY
     * @return
     */
    private boolean isMoveSelectedArea(float eventX, float eventY) {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        float dx = Math.abs(mCenterX - eventX);
        float dy = Math.abs(mCenterY - eventY);
        float dr = (float) Math.abs( radius-mUnreachedWidth/4);
        float dr2 = (float) Math.abs(radius-mUnreachedWidth*3/2);

        if ((dx * dx + dy * dy) >  ((dr ) * (dr ))) {
            return false;
        }
        if ((dx * dx + dy * dy) < ((dr2) * (dr2))) {
            return false;
        }
        double radian = Math.atan2(mCenterX - eventX, eventY - mCenterY);
        double degrees = Math.toDegrees(radian);
        double downDegree = degrees + 180;

        if (mEndOutSizeCurAngle > mStartOutSizeCurAngle && downDegree > mStartOutSizeCurAngle && downDegree < mEndOutSizeCurAngle) {
            Log.d("isMoveSelectedArea", "isMoveSelectedArea");
            return true;
        } else if (mEndOutSizeCurAngle < mStartOutSizeCurAngle && !(downDegree > mEndOutSizeCurAngle && downDegree < mStartOutSizeCurAngle)) {
            Log.d("isMoveSelectedArea", "isMoveSelectedArea");
            return true;
        }

        return false;
    }

}

package io.feeeei.circleseekbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.view.ViewParent;

import io.feeeei.circleseekbar.util.SeekUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by gaopengfei on 15/11/15.
 */
public class CircleSeekBar extends View {

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
    private float mFisrstWheelCurX;
    private float mFisrstWheelCurY;
    private double mEndCurAngle;
    private double mStartCurAngle;
    private int mCurEndProcess;
    private int mMaxStartProcess;
    private int mCurStartProcess;
    private int mMaxEndProcess;
    private double mGapProcess = 5;
    private double mLastCurAngle;
    private double mSubAngle;
    private double mDegreeCycle = 720;
    private float mCenterX;
    private float mCenterY;
    private double mStartSubAngle;
    private double mEndSubAngle;
    private double mEndDegree;
    private int mStartBitmapSelectid;
    private int mEndBitmapSelectid;
    private Context mContext;
    private int mReachedColorSelect;

    public CircleSeekBar(Context context) {
        this(context, null);
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs, defStyleAttr);
        initPadding();
        initPaints();
        if (mStartBitmapid != 0) {
            mStartBitmap = BitmapFactory.decodeResource(context.getResources(),
                    mStartBitmapid);
        }
        if (mEndBitmapid != 0) {
            mEndBitmap = BitmapFactory.decodeResource(context.getResources(),
                    mEndBitmapid);
        }

//        int width = getWidth();
//        int height = getHeight();
//        if (width <= 0) {
////            Log.e(TAG, "targetView width <= 0! please invoke the updatePivotX(int) method to update the PivotX!", new RuntimeException());
//        }
//        if (height <= 0) {
////            Log.e(TAG, "targetView height <= 0! please invoke the updatePivotY(int) method to update the PivotY!", new RuntimeException());
//        }
//        width /= 2;
//        height /= 2;
//        int x = (int) getAbsoluteX(this);
//        int y = (int) getAbsoluteY(this);
//        mCenterY=y+height;
//        mCenterX=x+width;
    }

    private void initPaints() {
        mDefShadowOffset = getDimen(R.dimen.def_shadow_offset);
        /**
         * 圆环画笔
         */
        mWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStyle(Paint.Style.STROKE);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        if (isHasWheelShadow) {
            mWheelPaint.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
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
        mUnreachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_unreached_color,
                getColor(R.color.def_wheel_color));
        mUnreachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_unreached_width,
                getDimen(R.dimen.def_wheel_width));
        isHasReachedCornerRound = a.getBoolean(R.styleable.CircleSeekBar_wheel_reached_has_corner_round, true);
        mReachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_reached_width, mUnreachedWidth);
        mPointerColor = a.getColor(R.styleable.CircleSeekBar_wheel_pointer_color, getColor(R.color.def_pointer_color));
        mPointerRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_radius, mReachedWidth / 2);
        isHasWheelShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_wheel_shadow, false);
        mStartBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_start_drawable, 0);
        mEndBitmapid = a.getResourceId(R.styleable.CircleSeekBar_wheel_end_drawable, 0);
        mStartBitmapSelectid = a.getResourceId(R.styleable.CircleSeekBar_wheel_start_drawable_select, 0);
        mEndBitmapSelectid = a.getResourceId(R.styleable.CircleSeekBar_wheel_end_drawable_select, 0);
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

    private void refershPosition() {
        refershStartPosition();
        refershEndPosition();
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

        if (isHasCache) {
            if (mCacheCanvas == null) {
                buildCache(mCenterX, mCenterY, wheelRadius);
            }
            canvas.drawBitmap(mCacheBitmap, 0, 0, null);
        } else {
            canvas.drawCircle(mCenterX, mCenterY, wheelRadius, mWheelPaint);
        }
        double mAngle = mStartCurAngle > mEndCurAngle ? (-360 + mStartCurAngle) : mStartCurAngle;
        //画选中区域
//        canvas.drawArc(new RectF(left, top, right, bottom), -90, (float) mCurAngle, false, mReachedPaint);
        canvas.drawArc(new RectF(left, top, right, bottom), (float) (-90 + mAngle), (float) ((float) mEndCurAngle - (mAngle)), false, mReachedPaint);
        //画选中区域
//        canvas.drawArc(new RectF(left, top, right, bottom), -90, (float) mCurAngle, false, mReachedPaint);
//        canvas.drawBitmap(mReachedPaint, );
        if (mStartBitmap == null) {
            //画锚点
//            canvas.drawCircle(mWheelCurX, mWheelCurY, mPointerRadius, mPointerPaint);
        } else {
//            canvas.drawBitmap(mStartBitmap, left + (right - left) / 2 - mStartBitmap.getWidth() / 2, top - mStartBitmap.getHeight() / 2, mPointerPaint);
            canvas.drawBitmap(mStartBitmap, mFisrstWheelCurX - mStartBitmap.getWidth() / 2, mFisrstWheelCurY - mStartBitmap.getHeight() / 2, mPointerPaint);
            canvas.drawBitmap(mEndBitmap, mWheelCurX - mEndBitmap.getWidth() / 2, mWheelCurY - mEndBitmap.getHeight() / 2, mPointerPaint);
        }
    }

    private void buildCache(float centerX, float centerY, float wheelRadius) {
        mCacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheBitmap);

        //画环
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
    }

    private boolean isBitMapTouch(Bitmap mDurationBitmap, float x, float y, float lastx, float lasty) {
        int w = mDurationBitmap.getWidth() / 2;
        int h = mDurationBitmap.getHeight() / 2;
        return lastx - w <= x && x <= lastx + w && lasty - h <= y && y <= lasty + h;
    }

    int isStartBitMapTouch = 1;
    int isEndBitMapTouch = 2;
    int isStartOutSizeBitmapTouch = 3;
    int isEndOutSizeBitmapTouch = 4;
    int isBitMapTouch = 0;
    float mStartY = 0;
    float mStartX = 0;
    boolean isStart = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float eventX = event.getX();
        float eventY = event.getY();
        if (isCanTouch) {
            if ((event.getAction() == MotionEvent.ACTION_DOWN)) {
                isStart = false;
                mLastCurAngle = 360;
                mSubAngle = 0;
                mStartY = y;
                mStartX = x;
//                Log.e(TAG, "mEndBitmap: " + mEndBitmap);
//                Log.e(TAG, "isBitMapTouch: " + isBitMapTouch(mEndBitmap, x, y, mWheelCurX, mWheelCurY));
                if (mEndBitmap != null && isBitMapTouch(mEndBitmap, x, y, mWheelCurX, mWheelCurY)) {
                    mSubAngle = mEndSubAngle;
                    isBitMapTouch = isEndBitMapTouch;
                    mStartY = mWheelCurY;
                    mStartX = mWheelCurX;
                    mLastCurAngle = mEndCurAngle;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (mStartBitmap != null && isBitMapTouch(mStartBitmap, x, y, mFisrstWheelCurX, mFisrstWheelCurY)) {
                    mSubAngle = mStartSubAngle;
                    isBitMapTouch = isStartBitMapTouch;
                    mLastCurAngle = mStartCurAngle;
                    mStartY = mFisrstWheelCurY;
                    mStartX = mFisrstWheelCurX;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (isMoveSelectedArea(x, y)) {
                    mSubAngle = mStartSubAngle;
                    mEndDegree = mEndSubAngle;
                    isBitMapTouch = isStartOutSizeBitmapTouch;
                    mLastCurAngle = mStartCurAngle;
                    mStartY = y;
                    mStartX = x;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    isBitMapTouch = 0;
                    mSubAngle = 0;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
                if (mLastCurAngle == 0) {
                    mLastCurAngle = 360;
                }
//                if (!isTouchOutSize(x, y)) {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                    return false;
//                } else {
//
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
            } else if ((event.getAction() == MotionEvent.ACTION_MOVE)) {
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
                mSubAngle = (mSubAngle < 0) ? mSubAngle + mDegreeCycle : mSubAngle % mDegreeCycle;
                if (isBitMapTouch == isStartOutSizeBitmapTouch) {
                    mEndDegree = (float) (mEndDegree + moveDegree);
                    mEndDegree = (mEndDegree < 0) ? mEndDegree + mDegreeCycle : mEndDegree % mDegreeCycle;
                }
                if (isBitMapTouch == isStartBitMapTouch) {
                    int sCurStartProcess = getSelectedValue(mMaxStartProcess, mSubAngle);
                    if (sCurStartProcess % mGapProcess == 0) {
                        mCurStartProcess = sCurStartProcess;
                        refershStartPosition();
                        onChangeListener();
                    }

                } else if (isBitMapTouch == isEndBitMapTouch) {
                    int sCurEndProcess = getSelectedValue(mMaxEndProcess, mSubAngle);
                    if (sCurEndProcess % mGapProcess == 0) {
                        mCurEndProcess = sCurEndProcess;
                        refershEndPosition();
                        onChangeListener();
                    }
                } else if (isBitMapTouch == isStartOutSizeBitmapTouch) {
                    int sCurStartProcess = getSelectedValue(mMaxStartProcess, mSubAngle);
                    int sCurEndProcess = getSelectedValue(mMaxEndProcess, mEndDegree);
                    if (sCurStartProcess % mGapProcess == 0) {
                        mCurStartProcess = sCurStartProcess;
                        mCurEndProcess = sCurEndProcess;
                        refershPosition();
                        onChangeListener();
                    }
                }
                if (isBitMapTouch == isStartOutSizeBitmapTouch) {
                    mStartY = y;
                    mStartX = x;
                } else {
                    refershPosition(cos, mCurAngle);
                }
                mLastCurAngle = mCurAngle;
                invalidate();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            isCenterStartOutTouch = false;
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

    /**
     * 计算滑动的角度
     */
    private float handleActionMove(float x, float y) {
        float l, t, r, b;
        if (mStartX > x) {
            r = mStartX;
            l = x;
        } else {
            r = x;
            l = mStartX;
        }
        if (mStartY > y) {
            b = mStartY;
            t = y;
        } else {
            b = y;
            t = mStartY;
        }
        float pA1 = Math.abs(mStartX - mCenterX);
        float pA2 = Math.abs(mStartY - mCenterY);
        float pB1 = Math.abs(x - mCenterX);
        float pB2 = Math.abs(y - mCenterY);
        float hypotenuse = (float) Math.sqrt(Math.pow(r - l, 2) + Math.pow(b - t, 2));
        float lineA = (float) Math.sqrt(Math.pow(pA1, 2) + Math.pow(pA2, 2));
        float lineB = (float) Math.sqrt(Math.pow(pB1, 2) + Math.pow(pB2, 2));
        if (hypotenuse > 0 && lineA > 0 && lineB > 0) {
            float angle = fixAngle((float) Math.toDegrees(Math.acos((Math.pow(lineA, 2) + Math.pow(lineB, 2) - Math.pow(hypotenuse, 2)) / (2 * lineA * lineB))));
            if (!Float.isNaN(angle)) {
//                mSlidingListener.onSliding((isClockwiseScrolling = isClockwise(x, y)) ? angle : -angle);
                return angle;
            }
        }
        return 0;
    }

    /**
     * 调整角度，使其在360之间
     *
     * @param rotation 当前角度
     * @return 调整后的角度
     */
    private float fixAngle(float rotation) {
        float angle = 360F;
        if (rotation < 0) {
            rotation += angle;
        }
        if (rotation > angle) {
            rotation = rotation % angle;
        }
        return rotation;
    }

    /**
     * 检测手指是否顺时针滑动
     *
     * @param x 当前手指的x坐标
     * @param y 当前手指的y坐标
     * @return 是否顺时针
     */
    private boolean isClockwise(float x, float y) {
        return (Math.abs(y - mStartY) > Math.abs(x - mStartX)) ?
                x < mCenterX != y > mStartY : y < mCenterY == x > mStartX;
    }

    private void onChangeListener() {
        if (mChangListener != null) {
            int abs = Math.abs(mCurStartProcess);
            int abs2 = Math.abs(mCurEndProcess);
            mChangListener.onChanged(this, abs, abs2);
        }
    }

    private double getSubAngle(double curAngle, double startOutSizeCurAngle) {
        double subCurAngle = getAbsCurAngle(curAngle);
        double subStart = getAbsCurAngle(startOutSizeCurAngle);
        return subStart >= subCurAngle ? (subStart - subCurAngle) : (subStart - (subCurAngle - 360));
    }

    private double getAbsCurAngle(double curAngle) {
        return curAngle % 360 > 0 ? curAngle % 360 : 360 - curAngle % 360;
    }


    private boolean isTouch(float x, float y) {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        return Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) < radius * radius;
    }

    private float getCircleWidth() {
        return Math.max(mUnreachedWidth, Math.max(mReachedWidth, mPointerRadius));
    }

    private void refershUnreachedWidth() {
        mUnreachedRadius = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
    }

    private void refershWheelCurPosition(double cos, double mCurAngle) {
        mWheelCurX = calcXLocationInWheel(mCurAngle, cos);
        mWheelCurY = calcYLocationInWheel(cos);
    }

    private void refershPosition(double cos, double mCurAngle) {
        mStartX = calcXLocationInWheel(mCurAngle, cos);
        mStartY = calcYLocationInWheel(cos);
    }

    private void refershWheelCurPositionFirst(double cos, double mCurAngle) {
        mFisrstWheelCurX = calcXLocationInWheel(mCurAngle, cos);
        mFisrstWheelCurY = calcYLocationInWheel(cos);
    }

    //    private void refershPosition() {
//        mCurAngle = (double) mCurProcess / mMaxProcess * 360.0;
//        double cos = -Math.cos(Math.toRadians(mCurAngle));
//        mStartCurAngle=mCurAngle;
//        mEndCurAngle=mCurAngle;
//        refershWheelCurPosition(cos, mCurAngle);
//        refershWheelCurPositionFirst(cos, mCurAngle);
//    }
    private void refershStartPosition() {
        mStartSubAngle = (double) mCurStartProcess % (mMaxStartProcess * 2) / (mMaxStartProcess) * 360.0;
        mStartCurAngle = mStartSubAngle % 360.0;
        double cos = -Math.cos(Math.toRadians(mStartCurAngle));
        refershWheelCurPositionFirst(cos, mStartCurAngle);
    }

    private void refershEndPosition() {
        mEndSubAngle = (double) (mCurEndProcess % (mMaxEndProcess * 2)) / mMaxEndProcess * 360.0;
        mEndCurAngle = mEndSubAngle % 360.0;
        double cos = -Math.cos(Math.toRadians(mEndCurAngle));
        refershWheelCurPosition(cos, mEndCurAngle);
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
            mChangListener.onChanged(this, mCurStartProcess, mCurEndProcess);
        }
    }

    private int getSelectedValue(int mMaxProcess, double mCurAngle) {
        return Math.round(mMaxProcess * ((float) mCurAngle / 360));
    }

    private int getSelectedValueDouble(int mMaxProcess, double mCurAngle) {
        return Math.round(mMaxProcess * ((float) mCurAngle / 720));
    }

    public int getCurProcess() {
        return mCurProcess;
    }

//    public void setCurProcess(int curProcess) {
//        this.mCurProcess = curProcess > mMaxProcess ? mMaxProcess : curProcess;
//        if (mChangListener != null) {
//            mChangListener.onChanged(this, curProcess, mCurEndProcess);
//        }
//        refershPosition();
//        invalidate();
//    }

    public int getMaxProcess() {
        return mMaxProcess;
    }

//    public void setMaxProcess(int maxProcess) {
//        mMaxProcess = maxProcess;
//        refershPosition();
//        invalidate();
//    }

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
        void onChanged(CircleSeekBar seekbar, int curStartProcess, int curEndProcess);
    }

    public int getCurEndProcess() {
        return mCurEndProcess;
    }

    public void setCurEndProcess(int curEndProcess) {
        this.mCurEndProcess = curEndProcess > mMaxEndProcess * 2 ? mMaxEndProcess * 2 : curEndProcess;
        onChangeListener();
        refershEndPosition();
        invalidate();
    }

    public int getMaxStartProcess() {
        return mMaxStartProcess;
    }

    public void setMaxStartProcess(int maxStartProcess) {
        mMaxStartProcess = maxStartProcess;
    }

    public int getCurStartProcess() {
        return mCurStartProcess;
    }

    public void setCurStartProcess(int curStartProcess) {
        this.mCurStartProcess = curStartProcess > mMaxStartProcess * 2 ? mMaxStartProcess * 2 : curStartProcess;
        onChangeListener();
        refershStartPosition();
        invalidate();
    }

    public int getMaxEndProcess() {
        return mMaxEndProcess;
    }

    public void setMaxEndProcess(int maxEndProcess) {
        mMaxEndProcess = maxEndProcess;
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
        if ((dx * dx + dy * dy) > (radius * radius)) {
            return false;
        }
        if ((dx * dx + dy * dy) < ((radius - mUnreachedWidth) * (radius - mUnreachedWidth))) {
            return false;
        }

        double radian = Math.atan2(mCenterX - eventX, eventY - mCenterY);
        double degrees = Math.toDegrees(radian);
        double downDegree = degrees + 180;

        if (mEndCurAngle > mStartCurAngle && downDegree > mStartCurAngle && downDegree < mEndCurAngle) {
            Log.d("isMoveSelectedArea", "isMoveSelectedArea");
            return true;
        } else if (mEndCurAngle < mStartCurAngle && !(downDegree > mEndCurAngle && downDegree < mStartCurAngle)) {
            Log.d("isMoveSelectedArea", "isMoveSelectedArea");
            return true;
        }

        return false;
    }

    private boolean isTouchOutSize(float x, float y) {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        return Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) < radius * radius && Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) > (radius - mUnreachedWidth) * (radius - mUnreachedWidth);
    }

    public void setSleepSelect(boolean isSelect) {
        isCanTouch = isSelect;
        mStartBitmap = SeekUtil.setBitmap(mContext, !isSelect ? mStartBitmapid : mStartBitmapSelectid);
        mEndBitmap = SeekUtil.setBitmap(mContext, !isSelect ? mEndBitmapid : mEndBitmapSelectid);
        if (mReachedPaint != null) {
            mReachedPaint.setColor(isSelect ? mReachedColorSelect : mReachedColor);
        }
        invalidate();
    }


}

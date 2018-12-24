package com.cchip.eq;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class CustomRecyclerView extends RecyclerView {
    private int mTouchSlop;
    private float lastX;

    public CustomRecyclerView(Context context) {
        super(context);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }


    int move_x, move_y;

//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                move_x = (int) e.getX();
//                move_y = (int) e.getY();
////                if (move_y <= 50) {
////                    getParent().requestDisallowInterceptTouchEvent(false);
////                } else {
//                    getParent().requestDisallowInterceptTouchEvent(true);
////                }
//                Log.e("motion_event", "down   x==y  " + move_x + " ==== " + move_y);
//                break;
//            case MotionEvent.ACTION_MOVE:
////                Log.e("motion_event", "move   x==y  " + move_x + " ==== " + move_y);
//                int y = (int) e.getY();
//                int x = (int) e.getX();
//                Log.e("motion_event", "move   x1==y1  " + x + " ==== " + y);
////                if (/*Math.abs(y - move_y) > mTouchSlop && Math.abs(x - move_x) < mTouchSlop * 2&&*/x < 50) {
////                    getParent().requestDisallowInterceptTouchEvent(false);
////                } else {
//                    getParent().requestDisallowInterceptTouchEvent(true);
////                }
//                break;
//            case MotionEvent.ACTION_UP:
//                getParent().requestDisallowInterceptTouchEvent(false);
//
//                Log.e("motion_event", "up   x==y  " + move_x + " ==== " + move_y);
//                break;
//        }
//        return super.onTouchEvent(e);
//    }

    private void isTouch(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            move_y = (int) ev.getY();
            getParent().getParent().requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
           /* if (lastX > x) {
                // 如果是水平向左滑动，且不能滑动了，则返回给上一层view处理
                if (!canScrollHorizontally(1)) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            } else*/
            if (x > lastX) {
                // 如果是水平向右滑动，且不能滑动了，则返回给上一层view处理
                if (!canScrollHorizontally(-1)) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
//                    return false;
                }
            }
        }
        lastX = ev.getX();
    }

    //    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        // screenWidth = getResources().getDisplayMetrics().widthPixels;
//        System.out.println("屏幕宽度" + screenWidth);
//  /*判断屏幕是否满足一定条件，满足则中断时间
//  即，两边各留出一定宽度使靠边滑动时可以相应父pagerview 的事件，例如左边有侧滑菜单，右边靠边可以滑到另一个父viewpager的下一个*/
//        if (ev.getRawX() > screenWidth / 8 && ev.getRawX() < screenWidth * 7 / 8) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }else{
//            getParent().requestDisallowInterceptTouchEvent(false);
//        }
//        return super.dispatchTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            move_y = (int) ev.getY();
            move_x = (int) ev.getX();
            getParent().getParent().requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();

            /* &&&&*//*x < 50) {
////                    getParent().requestDisallowInterceptTouchEvent(false);
////                } else {*/
           /* if (lastX > x) {
                // 如果是水平向左滑动，且不能滑动了，则返回给上一层view处理
                if (!canScrollHorizontally(1)) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            } else*/
            if (Math.abs(y - move_y) > mTouchSlop) {
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
            } else {
                if (x > lastX /*&& Math.abs(x - move_x) < mTouchSlop*2*/) {
//                    int dx=(x - move_x);
//                    Log.e("cxj", "dispatchTouchEvent: "+dx );

                    // 如果是水平向右滑动，且不能滑动了，则返回给上一层view处理
                    if (!canScrollHorizontally(-1)) {
                        getParent().getParent().requestDisallowInterceptTouchEvent(false);
//                    return false;
                    }
                }
            }

        }
        lastX = ev.getX();
        return super.dispatchTouchEvent(ev);
    }
}
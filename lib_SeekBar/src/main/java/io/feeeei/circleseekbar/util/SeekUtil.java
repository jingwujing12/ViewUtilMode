package io.feeeei.circleseekbar.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewParent;

public class SeekUtil {
    public static double getMoveDegree(float eventX, float eventY, float centerX, float centerY, float startX, float startY) {
        //                    坐标系的直线表达式
//                  直线l1的表达式子:过钟表中心点和结束控件中心点
        float a1 = centerY - startY;
        float b1 = startX - centerX;
        float c1 = startY * centerX - centerY * startX;
        double d1 = (a1 * eventX + b1 * eventY + c1) / (Math.sqrt(a1 * a1 + b1 * b1));
//                    Log.d("TAG", "d1==" + d1);

//                  直线l2的表达式:过钟表中心点且垂直直线l1
        float a2 = b1;
        float b2 = -a1;
        float c2 = -a2 * centerX - b2 * centerY;
        double d2 = (a2 * eventX + b2 * eventY + c2) / (Math.sqrt(a2 * a2 + b2 * b2));
//                    Log.d("TAG", "d2==" + d2);
//                    以l1为基准线,顺势针半圆为0-180度,逆时针半圆为0-负180度
        double moveDegree = Math.toDegrees(Math.atan2(d1, d2));
        return moveDegree;

    }

    /**
     * 获取view在屏幕中的绝对x坐标
     */
    private static float getAbsoluteX(View view) {
        float x = view.getX();
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof View) {
            x += getAbsoluteX((View) parent);
        }
        return x;
    }

    /**
     * 获取view在屏幕中的绝对y坐标
     */
    private static float getAbsoluteY(View view) {
        float y = view.getY();
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof View) {
            y += getAbsoluteY((View) parent);
        }
        return y;
    }

    public static Bitmap setBitmap(Context context, int bitmapid) {
        Bitmap bitmap = null;
        if (bitmapid != 0) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    bitmapid);
        }
        return bitmap;
    }

    public static boolean isBitMapTouch(Bitmap mDurationBitmap, float x, float y, float lastx, float lasty) {
        int w = mDurationBitmap.getWidth() / 2;
        int h = mDurationBitmap.getHeight() / 2;
        return lastx - w <= x && x <= lastx + w && lasty - h <= y && y <= lasty + h;
    }

}

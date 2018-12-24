package com.cchip.eq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;


public class TextUtil {

    public static byte stringToByte(String str) {
        int value = Integer.valueOf(str, 16);
        byte b = (byte) value;
        return b;
    }


    @SuppressLint("DefaultLocale")
    public static String byteToString(byte b) {
        String str = "";
        str = String.format("%02d", b);
        return str;
    }

    public static float getFontHeight(Paint var0) {
        Paint.FontMetrics var1;
        return (var1 = var0.getFontMetrics()).descent - var1.ascent;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static String HToB(String a) {
        String b = Integer.toBinaryString(Integer.valueOf(toD(a, 16)));
        return b;

    }

    // 二进制转十六进制
    public static String BToH(String a) {
// 将二进制转为十进制再从十进制转为十六进制
        String b = Integer.toHexString(Integer.valueOf(toD(a, 2)));
        return b;
    }

    public static String CToH(int a) {
        String b = Integer.toHexString(a);
        return b;
    }
    /**
   * 将int类型的数据转换为byte数组
   * @param n int数据
   * @return 生成的byte数组
   */
    public static byte[] intToBytes(int n){
    String s = String.valueOf(n);
    return s.getBytes();
    }

    public static String color(String str, float progress) {
        float f = progress / 100;
        int hex = Integer.parseInt(str, 16);
        hex = (int) (hex * f);
        return Integer.toHexString(hex);
    }

    // 任意进制数转为十进制数
    public static String toD(String a, int b) {//---------------------------a为16进制，b=16；三
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))
                    * Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }

    // 将十六进制中的字母转为对应的数字
    public static int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("a")) {
            i = 10;
        }
        if (a.equals("b")) {
            i = 11;
        }
        if (a.equals("c")) {
            i = 12;
        }
        if (a.equals("d")) {
            i = 13;
        }
        if (a.equals("e")) {
            i = 14;
        }
        if (a.equals("f")) {
            i = 15;
        }
        return i;
    }


    // 将十进制中的数字转为十六进制对应的字母
    public static String formattingH(int a) {
        String i = String.valueOf(a);

        switch (a) {

            case 10:
                i = "a";

                break;

            case 11:
                i = "b";

                break;

            case 12:
                i = "c";

                break;

            case 13:
                i = "d";

                break;

            case 14:
                i = "e";

                break;

            case 15:
                i = "f";

                break;

        }
        return i;
    }

    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }

    /**
     * 注释：字节数组到short的转换！
     *
     * @param b
     * @return
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    public static short byteToShort(byte a, byte b) {
        short s = 0;
        short s0 = (short) (a & 0xff);// 最低位
        short s1 = (short) (b & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * 注释：int到字节数组的转换！
     *
     * @param number
     * @return
     */
    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();//将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * 注释：字节数组到int的转换！
     *
     * @param b
     * @return
     */
    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }

    public static byte[] ShortToByte(int num) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (num >> 8 & 0xff);// 次低位   ;
        bytes[1] = (byte) (num & 0xff);//最低位;
        return bytes;
    }

    public static byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }

    public static double bytes2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    public static String byteArrayToString(byte[] bytes, int starindex) {
        String b = "";
        for (int i = starindex; i < bytes.length; i++) {
            b += Integer.toHexString(bytes[i] & 0xff) + "  ";
            if (b.length() == 1) {
                b = "0" + b;
            }
        }
        return b;
    }

    private String byteArrayToString(byte[] bytes) {
        String b = "";
        for (int i = 0; i < bytes.length; i++) {
            b += Integer.toHexString(bytes[i] & 0xff) + "  ";
        }
        return b;
    }
}

package com.wujing.view.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.wujing.view.App;

import org.json.JSONException;
import org.json.JSONObject;

public class SharePreferecnceUtil {
    public static final String SP_EQSS = "eqss";
    public static final String SP_EQ_EDIT = "sp_eq_edit";
    private static Context getApplicationContext() {
        return App.getInstant().getApplicationContext();
    }
    public static void setSpEq(int[] eqEdit) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(SP_EQSS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < eqEdit.length; i++) {
            try {
                jsonObject.put(i + SP_EQ_EDIT, eqEdit[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Log.e("cxj", " (float) eqEdit[i]==" + (float) eqEdit[i]);
        }
        edit.putString(SP_EQ_EDIT, jsonObject.toString());
        edit.commit();
    }

    public static int[] getSpEq() {
        int[] doubles = new int[10];
        SharedPreferences sp = getApplicationContext().getSharedPreferences(SP_EQSS, Context.MODE_PRIVATE);
        String string = sp.getString(SP_EQ_EDIT, "");
        if (!TextUtils.isEmpty(string)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(string);
                for (int i = 0; i < doubles.length; i++) {

                    doubles[i] = jsonObject.getInt(i + SP_EQ_EDIT);
//                    Log.e("cxj", "doubles==" + doubles[i]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            for (int i = 0; i < doubles.length; i++) {
                doubles[i] = 0;
//                Log.e("cxj", "null==" + doubles[i]);
            }
        }

        return doubles;
    }

}

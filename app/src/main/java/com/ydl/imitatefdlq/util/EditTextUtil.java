package com.ydl.imitatefdlq.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by qweenhool on 2017/8/10.
 */

public class EditTextUtil {
    public static void clearButtonListener(final EditText et, final View view) {
        // 取得et中的文字
        String etInputString = et.getText().toString();
        // 根据et中是否有文字进行X可见或不可见的判断
        if (TextUtils.isEmpty(etInputString)) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        //点击X时使et中的内容为空
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                et.requestFocusFromTouch();
            }
        });
        //对et的输入状态进行监听
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    view.setVisibility(View.INVISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //是否隐藏软键盘
    public static boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                //点击的事EditText区域，保留点击事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}

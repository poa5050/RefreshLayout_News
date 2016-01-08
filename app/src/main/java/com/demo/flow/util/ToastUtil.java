package com.demo.flow.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.flow.news.App;
import com.demo.flow.news.R;


/**
 *
 */
public class ToastUtil {

    private ToastUtil() {
    }

    private static Toast toast;
    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    public static void show(final String text) {
        doCancel();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            doShow(text);
        } else {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    doShow(text);
                }
            });
        }
    }

    private static void doShow(String text){
        toast = new Toast(App.getInstance());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, -300);
        View view = View.inflate(App.getInstance(), R.layout.toast,null);
        toast.setView(view);
        TextView textView = (TextView)view.findViewById(R.id.textMessage);
        textView.setText(text);
        toast.show();
    }

    public static Toast toast(String text){
        return Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT);
    }

    public static  void cancel(){
        if (Looper.myLooper() == Looper.getMainLooper()) {
            doCancel();
        } else {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    doCancel();
                }
            });
        }
    }

    public static void doCancel(){
        if(toast != null){
            toast.cancel();
        }
    }

    public static void show(@StringRes int resId) {
        show(App.getInstance().getString(resId));
    }

}
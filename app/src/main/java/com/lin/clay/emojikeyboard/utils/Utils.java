package com.lin.clay.emojikeyboard.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.FloatRange;

public class Utils {

    public static float sDensity;
    public static float sDensityDpi;
    public static int sScreenW;
    public static int sScreenH;

    public static void init(Activity activiy)
    {
        Display dis = activiy.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        dis.getMetrics(dm);
        int h = dis.getHeight();
        int w = dis.getWidth();
        sScreenW = w < h ? w : h;
        sScreenH = w < h ? h : w;
        sDensity = dm.density;
        sDensityDpi = dm.densityDpi;
    }

    public static int getScreenW()
    {
        return sScreenW;
    }

    public static int getScreenH()
    {
        return sScreenH;
    }

    public static int getRealPixel(int pxSrc)
    {
        int pix = (int)(pxSrc * sDensity / 2.0);
        if(pxSrc == 1 && pix == 0)
        {
            pix = 1;
        }
        return pix;
    }

    /**
     * 获取虚拟按键高度
     *
     * @param context
     * @return
     */
    public static int getVirtualBarHeigh(Context context)
    {
        int vh = 0;
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try
        {
            @SuppressWarnings("rawtypes") Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked") Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return vh;
    }

    public static int Dp2Px(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f);
    }

    public static View.OnTouchListener getTouchBackListener(@FloatRange(from = 0f, to = 1.0f) final float rate)
    {

        View.OnTouchListener mTouchListener = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(final View v, MotionEvent event)
            {

                switch(event.getActionMasked())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        AnimatorSet set = new AnimatorSet();
                        Animator animatorX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, rate);
                        Animator animatorY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, rate);
                        Animator animatorAlpha = ObjectAnimator.ofFloat(v, "alpha", 1.0f, rate * 2 / 3f);
                        set.play(animatorX).with(animatorY).with(animatorAlpha);
                        set.setDuration(100);
                        set.start();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_UP:
                    {

                        AnimatorSet set = new AnimatorSet();
                        Animator animatorX = ObjectAnimator.ofFloat(v, "scaleX", v.getScaleX(), 1.0f);
                        Animator animatorY = ObjectAnimator.ofFloat(v, "scaleY", v.getScaleY(), 1.0f);
                        Animator animatorAlpha = ObjectAnimator.ofFloat(v, "alpha", v.getAlpha(), 1.0f);
                        set.play(animatorX).with(animatorY).with(animatorAlpha);
                        set.setDuration(200);
                        set.start();

                        break;
                    }
                    default:
                        break;
                }
                return false;
            }
        };
        return mTouchListener;
    }

    public static boolean isWifi(Context context)
    {
        if(context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info != null)
        {
            if(info.getType() == ConnectivityManager.TYPE_WIFI) return true;
        }
        return false;
    }

    //获取手机状态栏高度
    public static int getStatusBarHeight(Context context)
    {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try
        {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideInput(Activity activity)
    {
        if(activity != null)
        {
            View view = activity.getWindow().peekDecorView();
            if(view != null)
            {
                InputMethodManager inputmanger = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}

package com.lin.clay.emojikeyboard.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.Toast;

import com.lin.clay.emojikeyboard.utils.widget.TipsView;

import java.lang.reflect.Field;


public class ToastUtils
{

	/**
	 * 成功
	 */
	public final static int SUCCES = 1;
	/**
	 * 失败
	 */
	public final static int DEFEAT = 0;


	private static Toast mToast;
	private static TipsView mTipsView;

	private static Handler mHandler = new Handler();

	private static Runnable mToastRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			if(mToast != null)
			{
				mToast.cancel();
				mToast = null;
			}
			if(mTipsView != null)
			{
				mTipsView = null;
			}
		}
	};


	/**
	 * 显示自定义吐司
	 *
	 * @param context  上下文
	 * @param text     显示文本
	 * @param duration 显示的时间
	 * @param type     吐司类型，ToastUtils.SUCCESS =1,ToastUtils.DEFEAT=0
	 */
	public static void showToast(Context context, String text, int duration, int type)
	{
		//makeCustomToast(context,text,duration,type).show();
		showShortToast(context, text, duration, type);
	}

	public static Toast makeCustomToast(Context context, String text, int duration, int type)
	{
		Toast t = new Toast(context);
		TipsView tis = new TipsView(context);
		tis.setTipsText(text);
		t.setDuration(duration);
		t.setView(tis);
		t.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
		return t;
	}

	public static void showShortToast(Context context, String text, int duration, int type)
	{
		if(mTipsView == null)
		{
			mTipsView = new TipsView(context);
		}
		mTipsView.setTipsText(text);
		mHandler.removeCallbacks(mToastRunnable);
		if(mToast == null)
		{//只有mToast==null时才重新创建，否则只需更改提示文字
			mToast = new Toast(context);
			mToast.setDuration(duration);
			mToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
			mToast.setView(mTipsView);
		}
		if(duration == Toast.LENGTH_SHORT)
		{
			mHandler.postDelayed(mToastRunnable, 2000);
		}
		else if(duration == Toast.LENGTH_LONG)
		{
			mHandler.postDelayed(mToastRunnable, 3500);
		}
		mToast.show();
	}


	public static void showToast(Context context, String text, int duration)
	{
		showShortToast(context, text, duration, 0);
	}


	/**
	 * 生成符合系统主题的AlertDialog.Builder
	 *
	 * @param context
	 * @return
	 */
	public static AlertDialog.Builder getAlertDialog(Context context, boolean isDark)
	{
		return new AlertDialog.Builder(new ContextThemeWrapper(context, getDialogTheme(isDark)));
	}

	public static int getDialogTheme(boolean isDark)
	{
		if(Build.VERSION.SDK_INT >= 11)
		{
			String Theme_Holo_Light = "Theme_Holo_Light_Dialog";
			String Theme_Holo = "Theme_Holo_Dialog";
			int Theme_Holo_Light_Id = 0;
			int Theme_Holo_Id = 0;
			try
			{
				Field field1 = android.R.style.class.getField(Theme_Holo_Light);
				Field field2 = android.R.style.class.getField(Theme_Holo);
				if(field1 != null && field2 != null)
				{
					Theme_Holo_Light_Id = field1.getInt(null);
					Theme_Holo_Id = field2.getInt(null);
				}
			}
			catch(Exception e)
			{

			}


			if(Theme_Holo_Light_Id > 0 && Theme_Holo_Id > 0)
			{
				return isDark ? Theme_Holo_Id : Theme_Holo_Light_Id;
			}
			else
			{
				return android.R.style.Theme_Dialog;
			}
		}
		else
		{
			return android.R.style.Theme_Dialog;
		}
	}


}
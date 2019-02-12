package com.lin.clay.statusbarutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import androidx.annotation.ColorInt;

public class StatusBarUtil
{

	/**
	 * 修改状态栏为全透明
	 *
	 * @param activity
	 */
	public static void transparencyBar(Activity activity)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);

		}
		else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			Window window = activity.getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	/**
	 * 透明状态栏和导航栏
	 *
	 * @param activity
	 */
	public static void transparencyStatusBarAndNavigationBar(Activity activity)
	{

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			//透明状态栏
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	/**
	 * 生成虚拟状态栏
	 *
	 * @param activity
	 * @param color    背景颜色
	 * @param alpha    透明度
	 */
	public static TextView createView(Activity activity, @ColorInt int color, float alpha)
	{
		TextView textView = new TextView(activity);
		textView.setBackgroundColor(color);
		textView.setAlpha(alpha);
		textView.setId(getId());
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
		textView.setLayoutParams(layoutParams);
		return textView;
	}

	public static int getId()
	{
		return R.id.statusbarutil_fake_status_bar_view;
	}

	/**
	 * 添加状态栏
	 *
	 * @param activity
	 * @param color    背景颜色
	 * @param alpha    透明度
	 */
	public static void addStatusBarViewAlpha(Activity activity, @ColorInt int color, float alpha)
	{
		transparencyBar(activity);
		TextView textView = new TextView(activity);
		textView.setBackgroundColor(color);
		textView.setAlpha(alpha);
		textView.setId(getId());
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
		textView.setLayoutParams(layoutParams);
		ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
		decorView.addView(textView);
	}

	/**
	 * 添加状态栏
	 *
	 * @param activity
	 * @param color    背景颜色
	 * @param alpha    透明度
	 */
	public static void addStatusBarView(Activity activity, @ColorInt int color, int alpha)
	{
		transparencyBar(activity);
		TextView textView = new TextView(activity);
		textView.setBackgroundColor(calculateStatusColor(color, alpha));
		textView.setId(getId());
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
		textView.setLayoutParams(layoutParams);
		ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
		decorView.addView(textView);
	}

	/**
	 * 显示或者隐藏状态栏
	 *
	 * @param activity
	 * @param show
	 */
	public static void hideStatusBar(Activity activity, boolean show)
	{
		ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
		View view = decorView.findViewById(getId());
		if(view == null)
		{
			return;
		}
		if(show)
		{
			view.setVisibility(View.VISIBLE);
		}
		else
		{
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 状态栏设置为沉浸式
	 *
	 * @param activity
	 */
	public static void immersionStatusBar(Activity activity)
	{
//        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
		}
	}

	/**
	 * 修改状态栏颜色，支持4.4以上版本
	 *
	 * @param activity
	 * @param color
	 */
	public static void setStatusBarColor(Activity activity, int color)
	{
		transparencyBar(activity);
		if(color == 0)
		{
			return;
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			Window window = activity.getWindow();
			//      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(color);
		}
		else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			//使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
			transparencyBar(activity);
			SystemBarTintManager tintManager = new SystemBarTintManager(activity);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(color);
		}
	}


	/**
	 * 状态栏亮色模式，设置状态栏黑色文字、图标，
	 * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
	 *
	 * @param activity
	 * @return 1:MIUUI 2:Flyme 3:android6.0
	 */
	public static int StatusBarLightMode(Activity activity)
	{
		if(!confirmFourUp())
		{
			return -1;
		}
		else
		{
			int result = 0;
			if(MIUISetStatusBarLightMode(activity, true))
			{
				result = 1;
			}
			else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
				result = 3;
			}
			else
			{
				setStatusBarColor(activity, R.color.black);
				result = 4;
			}
			return result;
		}
	}

	/**
	 * 已知系统类型时，设置状态栏黑色文字、图标。
	 * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
	 *
	 * @param activity
	 * @param type     1:MIUUI 2:Flyme 3:android6.0
	 */
	public static void StatusBarLightMode(Activity activity, int type)
	{
		if(type == 1)
		{
			MIUISetStatusBarLightMode(activity, true);
		}
		else if(type == 2)
		{
			FlymeSetStatusBarLightMode(activity.getWindow(), true);
		}
		else if(type == 3)
		{
			activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		}
	}

	/**
	 * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标tt
	 */
	public static void StatusBarDarkMode(Activity activity, int type)
	{
		if(type == 1)
		{
			MIUISetStatusBarLightMode(activity, false);
		}
		else if(type == 2)
		{
			FlymeSetStatusBarLightMode(activity.getWindow(), false);
		}
		else if(type == 3)
		{
			activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		}
	}


	/**
	 * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标tt
	 */
	public static int StatusBarDarkMode(Activity activity)
	{
		if(!confirmFourUp())
		{
			return -1;
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			if(isMIUI())
			{
				MIUISetStatusBarLightMode(activity, false);
				return 1;
			}
			else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
				return 3;
			}
		}
		setSocialityStatusBar(activity);
		return 0;
	}

	/**
	 * 设置状态栏图标为深色和魅族特定的文字风格
	 * 可以用来判断是否为Flyme用户
	 *
	 * @param window 需要设置的窗口
	 * @param dark   是否把状态栏文字及图标颜色设置为深色
	 * @return boolean 成功执行返回true
	 */
	public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark)
	{
		boolean result = false;
		if(window != null)
		{
			try
			{
				WindowManager.LayoutParams lp = window.getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if(dark)
				{
					value |= bit;
				}
				else
				{
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				window.setAttributes(lp);
				result = true;
			}
			catch(Exception e)
			{

			}
		}
		return result;
	}

	/**
	 * 需要MIUIV6以上
	 *
	 * @param activity
	 * @param dark     是否把状态栏文字及图标颜色设置为深色
	 * @return boolean 成功执行返回true
	 */
	public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark)
	{
		boolean result = false;
		Window window = activity.getWindow();
		if(window != null)
		{
			Class clazz = window.getClass();
			try
			{
				int darkModeFlag = 0;
				Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
				Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
				darkModeFlag = field.getInt(layoutParams);
				Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
				if(dark)
				{
					extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
				}
				else
				{
					extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
				}
				result = true;

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					//开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
					if(dark)
					{
						activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
					}
					else
					{
						activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
					}
				}
			}
			catch(Exception e)
			{

			}
		}
		return result;
	}

	/**
	 * 获取状态栏高度
	 *
	 * @param context context
	 * @return 状态栏高度
	 */
	public static int getStatusBarHeight(Context context)
	{
		if(!confirmFourUp())
		{
			return 0;
		}
		// 获得状态栏高度
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		return context.getResources().getDimensionPixelSize(resourceId);
	}

	/**
	 * 计算状态栏颜色
	 * <p>
	 * eg :View.setBackgroundColor(calculateStatusColor(color, alpha));
	 *
	 * @param color color值
	 * @param alpha alpha值
	 * @return 最终的状态栏颜色
	 */
	private static int calculateStatusColor(@ColorInt int color, int alpha)
	{
		if(alpha == 0)
		{
			return color;
		}
		float a = 1 - alpha / 255f;
		int red = color >> 16 & 0xff;
		int green = color >> 8 & 0xff;
		int blue = color & 0xff;
		red = (int)(red * a + 0.5);
		green = (int)(green * a + 0.5);
		blue = (int)(blue * a + 0.5);
		return 0xff << 24 | red << 16 | green << 8 | blue;
	}

	/**
	 * 退出全屏
	 *
	 * @param activity
	 */
	public static void quitFullScreen(Activity activity)
	{
		activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 设置全屏
	 *
	 * @param activity
	 */
	public static void setFullScreen(Activity activity)
	{
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 判断手机版本是否是4.4以上
	 *
	 * @return
	 */
	public static boolean confirmFourUp()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}

	/**
	 * 状态栏--白底黑字  适配6.0及以上
	 * 6.0一下状态栏设置为黑色
	 *
	 * @param activity
	 */
	public static void setSocialityStatusBar(Activity activity)
	{

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			transparencyBar(activity);
			StatusBarLightMode(activity);
//			setStatusBarColor(activity, activity.getResources().getColor(R.color.white));
			View rootView = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
			rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);
		}
		else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			setStatusBarColor(activity, R.color.black);
		}
	}

	// 检测是否是小米手机MIUI
	private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
	private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
	private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

	public static boolean isMIUI()
	{
		Properties prop = new Properties();
		boolean isMIUI;
		try
		{
			prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
		return isMIUI;
	}

	public static void setMainStatusBar(Activity activity)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			StatusBarUtil.transparencyBar(activity);
			View rootView = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
			rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);
		}
	}

	public static void setSocialStatusBar(Activity activity)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			StatusBarUtil.transparencyBar(activity);
			View rootView = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
			rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);
		}
		StatusBarLightMode(activity);
	}
}
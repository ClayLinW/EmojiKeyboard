/**
 *
 */
package com.lin.clay.emojikeyboard.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpanStringUtils
{
	/**
	 * 判断文本中是否含有表情
	 *
	 * @param text
	 * @return
	 */

	public static boolean isTextContainerEmoji(int emotion_map_type, String text)
	{
		if(TextUtils.isEmpty(text))
		{
			return false;
		}
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		Pattern mPattern2 = EmotionManager.getInstance().mPatternEmoji;
		Matcher matcher = mPattern2.matcher(text);
		while(matcher.find())
		{
			return true;
		}
		return false;
	}



	/**
	 * 根据文本替换成emoji表情图片
	 *
	 * @param text
	 * @return
	 */

	public static CharSequence getEmotionContent(int emotion_map_type, final Context mContext, int size, String text)
	{
		if(TextUtils.isEmpty(text))
		{
			return "";
		}
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		Pattern mPattern2 = EmotionManager.getInstance().mPatternEmoji;
		Matcher matcher = mPattern2.matcher(text);
		while(matcher.find())
		{
			// 利用表情名字获取到对应的图片
			int resId = EmotionManager.getInstance().getImgByName(emotion_map_type, matcher.group());
			Drawable drawable = mContext.getResources().getDrawable(resId);
			drawable.setBounds(0, 0, size, size);//这里设置图片的大小
			MyImageSpan imageSpan = new MyImageSpan(drawable);
			builder.setSpan(imageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
}

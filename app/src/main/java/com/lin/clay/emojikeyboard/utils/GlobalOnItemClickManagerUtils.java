package com.lin.clay.emojikeyboard.utils;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.lin.clay.emojikeyboard.adapter.EmotionFactoryAdapter;
import com.lin.clay.emojikeyboard.utils.listener.OnEmotionClickItemListener;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class GlobalOnItemClickManagerUtils {

	private static GlobalOnItemClickManagerUtils instance;
	private List<EditText> mEditTextList;

	public static GlobalOnItemClickManagerUtils getInstance(Context context)
	{
		if(instance == null)
		{
			synchronized(GlobalOnItemClickManagerUtils.class)
			{
				if(instance == null)
				{
					instance = new GlobalOnItemClickManagerUtils();
					instance.mEditTextList = new ArrayList<>();
				}
			}
		}
		return instance;
	}

	public void attachToEditText(EditText editText)
	{
		mEditTextList.add(editText);
	}

	/**
	 * 经典表情点击事件
	 *
	 * @param emotion_map_type
	 * @return
	 */
	public OnEmotionClickItemListener getOnClassicEmotionClickItemListener(final int emotion_map_type)
	{
		return new OnEmotionClickItemListener()
		{
			@Override
			public void onItemClick(RecyclerView.Adapter adapter, View view, int position)
			{

				if(adapter instanceof EmotionFactoryAdapter)
				{
					if(mEditTextList == null || mEditTextList.size() == 0)
					{
						return;
					}
					EditText mEditText = mEditTextList.get(mEditTextList.size() - 1);
					if(mEditText == null)
					{
						return;
					}
					Context mContext = mEditText.getContext();
					if(mContext == null)
					{
						return;
					}
					EmotionFactoryAdapter emotionFactoryAdapter = (EmotionFactoryAdapter)adapter;
					if(position == emotionFactoryAdapter.getItemCount() - 1)
					{
						// 如果点击了最后一个回退按钮,则调用删除键事件
						mEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
					}
					else
					{
						// 如果点击了表情,则添加到输入框中
						String emotionName = emotionFactoryAdapter.getItem(position);

						// 获取当前光标位置,在指定位置上添加表情图片文本
						int curPosition = mEditText.getSelectionStart();
						StringBuilder sb = new StringBuilder(mEditText.getText().toString());
						sb.insert(curPosition, emotionName);

						// 特殊文字处理,将表情等转换一下
						mEditText.setText(SpanStringUtils.getEmotionContent(emotion_map_type, mContext, (int)(mEditText.getTextSize() * 1.5), sb.toString()));

//                        mEditText.setText(sb.toString());
//                        ClickSpanBuilder.getInstance().setIsEmoji(true).build(mEditText);

						// 将光标设置到新增完表情的右侧
						mEditText.setSelection(curPosition + emotionName.length());
					}

				}
			}

			@Override
			public void onItemLongClick(RecyclerView.Adapter adapter, View view, int position)
			{

			}
		};
	}

	/**
	 * 其它表情点击事件
	 *
	 * @param emotion_map_type
	 * @return
	 */
	public OnEmotionClickItemListener getOnOtherEmotionClickItemListener(int emotion_map_type)
	{
		return new OnEmotionClickItemListener()
		{
			@Override
			public void onItemClick(RecyclerView.Adapter adapter, View view, int position)
			{

				if(adapter instanceof EmotionFactoryAdapter)
				{
					EmotionFactoryAdapter emotionFactoryAdapter = (EmotionFactoryAdapter)adapter;
				}
			}

			@Override
			public void onItemLongClick(RecyclerView.Adapter adapter, View view, int position)
			{

			}
		};
	}

	public void onDestrouGlobalOnItemClickManager()
	{
		if(mEditTextList == null || mEditTextList.size() == 0)
		{
			return;
		}
		EditText editText = mEditTextList.get(mEditTextList.size() - 1);
		mEditTextList.remove(mEditTextList.size() - 1);
		editText = null;
	}

}

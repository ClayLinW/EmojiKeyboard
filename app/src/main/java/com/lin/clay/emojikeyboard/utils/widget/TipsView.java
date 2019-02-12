package com.lin.clay.emojikeyboard.utils.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.utils.Utils;

public class TipsView extends RelativeLayout
{
	public static final int MP = LayoutParams.MATCH_PARENT;
	public static final int WC = LayoutParams.WRAP_CONTENT;
	private LinearLayout tislayout;
	private ImageView ivAlert;
	private TextView tvAlert;

	public TipsView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public TipsView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public TipsView(Context context)
	{
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if(msg.what == 1)
			{
				tislayout.setVisibility(View.VISIBLE);
			}
			else if(msg.what == 2)
			{
				tislayout.setVisibility(View.GONE);
			}
			else if(msg.what == 3)
			{
				tvAlert.setText((CharSequence)msg.obj);
			}
			else if(msg.what == 4)
			{
				tvAlert.setTextColor(0xffc0ac40);
			}
			else if(msg.what == 5)
			{
				tvAlert.setTextColor(0xFFe3675a);
			}
		}

		;
	};

	private void init(Context context)
	{
		tislayout = new LinearLayout(context);
		LayoutParams p = new LayoutParams(WC, Utils.getRealPixel(80));
		p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		p.topMargin = Utils.getRealPixel(172) - Utils.getStatusBarHeight(context);
		p.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		tislayout.setBackgroundColor(0xFFfe9d92);
		tislayout.setBackgroundResource(R.drawable.shape_toast_bgk);
		tislayout.setGravity(Gravity.CENTER);
		tislayout.setAlpha(0.95f);
		tislayout.setOrientation(LinearLayout.HORIZONTAL);
		addView(tislayout, p);
		{
			ivAlert = new ImageView(context);
			LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(WC, WC);
			p1.leftMargin = Utils.getRealPixel(45);
			p1.rightMargin = Utils.getRealPixel(25);
			ivAlert.setVisibility(GONE);
			tislayout.addView(ivAlert, p1);

			tvAlert = new TextView(context);
			p1 = new LinearLayout.LayoutParams(0, WC);
			p1.weight = 1;
			//etContent.setHint("输入评论内容");
			tvAlert.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
			tvAlert.setTextColor(0xffffffff);
			tvAlert.setGravity(Gravity.CENTER);
			tvAlert.setText("");
			tislayout.addView(tvAlert, p1);
		}
	}


	public void setTipsText(String text)
	{
		Message msg = new Message();
		msg.what = 3;
		msg.obj = text;
		handler.sendMessage(msg);
	}

	public void showTips()
	{
		handler.sendEmptyMessage(1);
		new Thread(new TisRunnable()).start();
	}

	class TisRunnable implements Runnable
	{

		@Override
		public void run()
		{
			try
			{
				Thread.sleep(2000);
				handler.sendEmptyMessage(2);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}

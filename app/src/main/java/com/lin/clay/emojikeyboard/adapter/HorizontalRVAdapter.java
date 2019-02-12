package com.lin.clay.emojikeyboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.model.EmotionGiftsCheckedModel;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalRVAdapter extends RecyclerView.Adapter<HorizontalRVAdapter.MyViewHolder>
{

	private List<EmotionGiftsCheckedModel> datas;

	private LayoutInflater mInflater;

	private Context context;

	private RecyclerView mRecyclerView;

	private OnClickItemListener onClickItemListener;

	public HorizontalRVAdapter(Context context, List<EmotionGiftsCheckedModel> datas)
	{
		this.datas = datas;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}


	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = mInflater.inflate(R.layout.keyboard_recyclerview_horizontal_item, parent, false);
		return new MyViewHolder(view);
	}

	public void setRecyclerView(RecyclerView mRecyclerView)
	{
		this.mRecyclerView = mRecyclerView;
	}


	public void updateLocalData(int unCheckedPosition, int checkedPosition)
	{
		MyViewHolder unCheckedPositionHolder = (MyViewHolder)mRecyclerView.findViewHolderForAdapterPosition(unCheckedPosition);
		if(unCheckedPositionHolder == null)
		{
			notifyItemChanged(unCheckedPosition);
		}
		else
		{
			unCheckedPositionHolder.llIconBgk.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}

		MyViewHolder checkedPositionHolder = (MyViewHolder)mRecyclerView.findViewHolderForAdapterPosition(checkedPosition);
		if(checkedPositionHolder == null)
		{
			notifyItemChanged(checkedPosition);
		}
		else
		{
			checkedPositionHolder.llIconBgk.setBackgroundColor(Color.parseColor("#f0f0f0"));
		}
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position)
	{
		EmotionGiftsCheckedModel model = datas.get(position);
		/**
		 * 点击事件和长按事件
		 */
		if(onClickItemListener != null)
		{
			holder.itemView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
//					//使用该方法获取position，防止点击事件时pos未刷新问题
//					int pos = holder.getLayoutPosition();
					onClickItemListener.onItemClick(v, position, datas);
				}
			});

			holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
//					//使用该方法获取position，防止点击事件时pos未刷新问题
//					int pos = holder.getLayoutPosition();
					onClickItemListener.onItemLongClick(v, position, datas);
					return false;
				}
			});

		}
		//设置图标
		if(model.isGift)
		{
			holder.imageBtn.setImageResource(model.imgId);
		}
		else
		{
			holder.imageBtn.setImageDrawable(model.icon);
		}
		if(model.isSelected)
		{
			holder.llIconBgk.setBackgroundColor(Color.parseColor("#f0f0f0"));
		}
		else
		{
			holder.llIconBgk.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}
	}

	@Override
	public int getItemCount()
	{
		return datas.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder
	{
		public LinearLayout llIconBgk;
		public ImageView imageBtn;

		public MyViewHolder(View itemView)
		{
			super(itemView);
			llIconBgk = (LinearLayout)itemView.findViewById(R.id.ll_iconBgk);
			imageBtn = (ImageView)itemView.findViewById(R.id.image_btn);
		}
	}


	/**
	 * 设置监听器
	 *
	 * @param listener
	 */
	public void setOnClickItemListener(OnClickItemListener listener)
	{
		this.onClickItemListener = listener;
	}

	public interface OnClickItemListener
	{

		void onItemClick(View view, int position, List<EmotionGiftsCheckedModel> datas);

		void onItemLongClick(View view, int position, List<EmotionGiftsCheckedModel> datas);

	}

}

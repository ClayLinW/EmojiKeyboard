package com.lin.clay.emojikeyboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lin.clay.emojikeyboard.utils.listener.OnEmotionClickItemListener;
import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.utils.EmotionManager;
import com.lin.clay.emojikeyboard.utils.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmotionFactoryAdapter extends RecyclerView.Adapter<EmotionFactoryAdapter.EmotionViewHolder> {


    private Context context;
    private List<String> emotionNames;
    private int emotion_map_type;
    private int item_width_height;
    private int emotion_columns;
    private int emotion_type;
    private LayoutInflater layoutInflater;

    public EmotionFactoryAdapter(Context context, List<String> emotionNames, int emotion_map_type, int item_width_height, int emotion_columns, int emotion_type) {
        this.context = context;
        this.emotionNames = emotionNames;
        this.emotion_map_type = emotion_map_type;
        this.item_width_height = item_width_height;
        this.emotion_columns = emotion_columns;
        this.emotion_type = emotion_type;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EmotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.factory_emotion, parent, false);
        EmotionViewHolder viewHolder = new EmotionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EmotionViewHolder holder, int position) {
        if (position < emotion_columns) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.iv_emotion.getLayoutParams();
            layoutParams.topMargin = Utils.Dp2Px(context, 18);
            layoutParams.width = item_width_height;
            layoutParams.height = item_width_height;
            holder.iv_emotion.setLayoutParams(layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.iv_emotion.getLayoutParams();
            layoutParams.topMargin = 0;
            layoutParams.width = item_width_height;
            layoutParams.height = item_width_height;
            holder.iv_emotion.setLayoutParams(layoutParams);
        }
        if (emotion_type == 0) {
            //判断是否为最后一个item
            if (position == getItemCount() - 1) {
                holder.iv_emotion.setImageResource(R.drawable.compose_emotion_delete);
            } else {
                String emotionName = emotionNames.get(position);
                holder.iv_emotion.setImageResource(EmotionManager.getInstance().getImgByName(emotion_map_type, emotionName));
            }
        } else {
            String emotionName = emotionNames.get(position);
            holder.iv_emotion.setImageResource(EmotionManager.getInstance().getImgByName(emotion_map_type, emotionName));
        }

        if (mOnEmotionClickItemListener != null) {
            holder.iv_emotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnEmotionClickItemListener.onItemClick(EmotionFactoryAdapter.this, view, layoutPosition);
                }
            });
        }

        if (mOnEmotionClickItemListener != null) {
            holder.iv_emotion.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnEmotionClickItemListener.onItemLongClick(EmotionFactoryAdapter.this, view, layoutPosition);
                    return false;
                }
            });
        }
    }

    private OnEmotionClickItemListener mOnEmotionClickItemListener;

    public void setOnEmotionClickItemListener(OnEmotionClickItemListener mOnEmotionClickItemListener) {
        this.mOnEmotionClickItemListener = mOnEmotionClickItemListener;
    }

    public String getItem(int position) {
        return emotionNames.get(position);
    }

    @Override
    public int getItemCount() {
        if (emotion_type == 0) {
            // +1 最后一个为删除按钮
            return emotionNames.size() + 1;
        } else {
            return emotionNames.size();
        }
    }

    static class EmotionViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_emotion;

        public EmotionViewHolder(View itemView) {
            super(itemView);
            iv_emotion = itemView.findViewById(R.id.img_emotion);
        }
    }
}

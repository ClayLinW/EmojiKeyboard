package com.lin.clay.emojikeyboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.model.ChatMsgModel;
import com.lin.clay.emojikeyboard.utils.EmotionManager;
import com.lin.clay.emojikeyboard.utils.SpanStringUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ChatMsgModel> mChatMsgList;
    private LayoutInflater mLayoutInflater;

    private int SEND_TYPE_TEXT = 1000;
    private int SEND_TYPE_GIFT = 1001;

    public ChatAdapter(Context context, ArrayList<ChatMsgModel> mChatMsgList) {
        this.mContext = context;
        this.mChatMsgList = mChatMsgList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMsgModel chatMsgModel = mChatMsgList.get(position);
        if (chatMsgModel.isSend && chatMsgModel.msgType.equals(ChatMsgModel.TEXT_MSG_TYPE)) {
            return SEND_TYPE_TEXT;
        } else {
            return SEND_TYPE_GIFT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SEND_TYPE_TEXT) {
            return new TextViewHolder(mLayoutInflater.inflate(R.layout.match_send_chat_item_text, parent, false));
        } else {
            return new GiftViewHolder(mLayoutInflater.inflate(R.layout.match_send_chat_item_gift, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMsgModel chatMsgModel = mChatMsgList.get(position);
        if (chatMsgModel.isSend && chatMsgModel.msgType.equals(ChatMsgModel.TEXT_MSG_TYPE)) {
            TextViewHolder textViewHolder = (TextViewHolder) holder;
//            textViewHolder.tvRightMsg.setText(chatMsgModel.text);
            textViewHolder.tvRightMsg.setText(SpanStringUtils.getEmotionContent(EmotionManager.EMOTION_CLASSIC_TYPE, mContext, (int)( textViewHolder.tvRightMsg.getTextSize() * 1.5), chatMsgModel.text));
        } else {
            GiftViewHolder giftViewHolder = (GiftViewHolder) holder;
            giftViewHolder.imgGiftIcon.setImageResource(chatMsgModel.giftIconId);
            giftViewHolder.tvGiftTitle.setText(chatMsgModel.giftTitle);
            giftViewHolder.rlGiftContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnGiftClickPlayListener != null) {
                        mOnGiftClickPlayListener.giftClick(chatMsgModel.strSvga);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mChatMsgList.size();
    }

    private OnGiftClickPlayListener mOnGiftClickPlayListener;

    public void setmOnGiftClickPlayListener(OnGiftClickPlayListener mOnGiftClickPlayListener) {
        this.mOnGiftClickPlayListener = mOnGiftClickPlayListener;
    }

    public interface OnGiftClickPlayListener {
        void giftClick(String strSvga);
    }

    class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_rightMsg)
        TextView tvRightMsg;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GiftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_giftIcon)
        ImageView imgGiftIcon;
        @BindView(R.id.rl_giftContent)
        RelativeLayout rlGiftContent;
        @BindView(R.id.tv_giftTitle)
        TextView tvGiftTitle;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

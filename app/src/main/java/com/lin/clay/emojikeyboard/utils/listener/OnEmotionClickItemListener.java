package com.lin.clay.emojikeyboard.utils.listener;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnEmotionClickItemListener {
    void onItemClick(RecyclerView.Adapter adapter, View view, int position);
    void onItemLongClick(RecyclerView.Adapter adapter, View view, int position);
}

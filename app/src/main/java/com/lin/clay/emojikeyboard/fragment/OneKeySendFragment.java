package com.lin.clay.emojikeyboard.fragment;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lin.clay.emojikeyboard.MainActivity;
import com.lin.clay.emojikeyboard.R;
import com.lin.clay.emojikeyboard.utils.ToastUtils;
import com.lin.clay.emojikeyboard.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OneKeySendFragment extends Fragment {
    private RelativeLayout rlOnekeyMsg;
    private TextView tvOnekeySendMsg;
    private TextView tvOnekeySend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_onekey_send_fragment, container, false);
        rlOnekeyMsg = view.findViewById(R.id.rl_onekeyMsg);
        tvOnekeySendMsg = view.findViewById(R.id.tv_onekeySendMsg);
        tvOnekeySend = view.findViewById(R.id.tv_onekeySend);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvOnekeySend.setOnTouchListener(Utils.getTouchBackListener(0.9f));
        tvOnekeySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).sendOneKeyMsg(tvOnekeySendMsg.getText().toString());
            }
        });

        tvOnekeySendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptAnimator();
            }
        });

        rlOnekeyMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptAnimator();
            }
        });
    }

    private void promptAnimator() {
        int offset = Utils.Dp2Px(getActivity(), 5);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(tvOnekeySend, "translationX", offset, -offset, offset, -offset, offset, -offset, 0);
        objectAnimator.setDuration(800);
        objectAnimator.start();
        ToastUtils.showToast(getActivity(), "请点击发送按钮", Toast.LENGTH_SHORT, ToastUtils.DEFEAT);
    }
}

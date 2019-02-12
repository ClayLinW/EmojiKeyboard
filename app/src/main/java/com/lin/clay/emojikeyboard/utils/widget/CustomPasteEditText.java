package com.lin.clay.emojikeyboard.utils.widget;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lin.clay.emojikeyboard.utils.SpanStringUtils;

public class CustomPasteEditText extends EditText {

    public CustomPasteEditText(Context context) {
        super(context);
    }

    public CustomPasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPasteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 作用：复制粘贴的是文本，所以需要将相应的文本转换为表情
     *
     * @param id
     * @return
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste) {
            ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            String content = clip.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                int curPosition = getSelectionStart();
                StringBuilder sb = new StringBuilder(getText().toString());
                sb.insert(curPosition, content);

                setText(SpanStringUtils.getEmotionContent(1, getContext(), (int) (getTextSize() * 1.5), sb.toString()));
                //                setText(sb.toString());
                //                ClickSpanBuilder.getInstance().setIsEmoji(true).build(this);
                setSelection(curPosition + content.length());
                return true;
            }
        }
        return super.onTextContextMenuItem(id);
    }
}

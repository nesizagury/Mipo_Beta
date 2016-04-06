package com.example.mipo.chat;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.mipo.R;

public class MessageInputToolBox extends RelativeLayout {

    private OnOperationListener onOperationListener;

    /**
     * input box
     **/
    private EditText messageEditText;
    private Button sendButton;
    private Button moreTypeButton;

    //...
    private Context context;

    public MessageInputToolBox(Context context) {
        super (context);
        this.context = context;
        LayoutInflater.from (context).inflate (R.layout.message_input_tool_box, this);
    }

    public MessageInputToolBox(Context context, AttributeSet attrs) {
        super (context, attrs);
        this.context = context;
        LayoutInflater.from (context).inflate (R.layout.message_input_tool_box, this);


    }

    public MessageInputToolBox(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
        this.context = context;
        LayoutInflater.from (context).inflate (R.layout.message_input_tool_box, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate ();
        this.initView ();
    }

    private void initView() {
        messageEditText = (EditText) findViewById (R.id.messageEditText);
        sendButton = (Button) findViewById (R.id.sendButton);

        moreTypeButton = (Button) findViewById (R.id.moreTypeButton);

        sendButton.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                if (onOperationListener != null && !"".equals (messageEditText.getText ().toString ().trim ())) {
                    String content = messageEditText.getText ().toString ();
                    onOperationListener.send (content);
                    messageEditText.setText ("");
                }
            }
        });

        messageEditText.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !"".equals (s.toString ().trim ())) {
                    moreTypeButton.setVisibility (View.GONE);
                    sendButton.setEnabled (true);
                    sendButton.setVisibility (View.VISIBLE);
                } else {
                    moreTypeButton.setVisibility (View.VISIBLE);
                    if (moreTypeButton.getVisibility () == View.VISIBLE) {
                        sendButton.setVisibility (View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        moreTypeButton.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                hideKeyboard (context);
                onOperationListener.selectedFuncation ();
            }
        });
    }

    public void hide() {
        hideKeyboard (context);
    }

    /**
     * ?????
     *
     * @param activity
     */
    public static void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService (Context.INPUT_METHOD_SERVICE);
            if (imm.isActive () && activity.getCurrentFocus () != null) {
                imm.hideSoftInputFromWindow (activity.getCurrentFocus ().getWindowToken (), 0);
            }
        }
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.onOperationListener = onOperationListener;
    }
}

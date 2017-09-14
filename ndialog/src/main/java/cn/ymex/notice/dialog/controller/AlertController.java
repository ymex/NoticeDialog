package cn.ymex.notice.dialog.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.ymex.notice.dialog.NoticeDialog;
import cn.ymex.notice.dialog.R;

/**
 * 默认 Alert Dialog
 */

public class AlertController implements DialogControlable {

    private String mMsg;
    private String mTitle;
    private String positiveName;
    private String negativeName;

    private View.OnClickListener positiveListener;
    private View.OnClickListener negativeListener;

    private int titleGravity = Gravity.LEFT;
    private int messageGravity = Gravity.LEFT;

    private boolean dismiss = true;

    private AlertController() {

    }

    public static AlertController build() {
        return new AlertController();
    }

    public AlertController message(String message) {
        this.mMsg = message;
        return this;
    }

    public AlertController title(String title) {
        this.mTitle = title;
        return this;
    }

    public AlertController clickDismiss(boolean dismiss) {
        this.dismiss = dismiss;
        return this;
    }

    public AlertController titleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
        return this;
    }

    public AlertController messageGravity(int messageGravity) {
        this.messageGravity = messageGravity;
        return this;
    }

    //positive
    public AlertController negativeButton(String text, View.OnClickListener listener) {
        this.negativeName = text;
        this.negativeListener = listener;
        return this;
    }

    public AlertController positiveButton(String text, View.OnClickListener listener) {
        this.positiveName = text;
        this.positiveListener = listener;
        return this;
    }

    @Override
    public View createView(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.notice_alert_dialog, parent, false);
    }

    @Override
    public NoticeDialog.OnBindViewListener bindView(final NoticeDialog dialog) {
        return new NoticeDialog.OnBindViewListener() {
            @Override
            public void onCreated(View layout) {
                dialog.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));

                TextView tvTitle = layout.findViewById(R.id.notice_dialog_title);
                if (!TextUtils.isEmpty(mTitle)) {
                    tvTitle.setGravity(titleGravity);
                    tvTitle.setText(mTitle);
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }
                TextView tvMessage = layout.findViewById(R.id.notice_dialog_message);
                tvMessage.setGravity(messageGravity);
                tvMessage.setText(TextUtils.isEmpty(mMsg) ? "" : mMsg);

                final Button btnNegative = layout.findViewById(R.id.notice_dialog_button_cancel);
                View line = layout.findViewById(R.id.notice_dialog_divier_line);
                if (!TextUtils.isEmpty(negativeName)) {
                    line.setVisibility(View.VISIBLE);
                    btnNegative.setVisibility(View.VISIBLE);
                    btnNegative.setText(negativeName);
                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (negativeListener != null) {
                                negativeListener.onClick(btnNegative);
                            }
                            if (dismiss) {
                                dialog.dismiss();
                            }
                        }
                    });

                } else {
                    btnNegative.setText("");
                    line.setVisibility(View.GONE);
                    btnNegative.setVisibility(View.GONE);
                }

                final Button btnPositive = layout.findViewById(R.id.notice_dialog_button_ok);
                if (!TextUtils.isEmpty(positiveName)) {
                    btnPositive.setText(positiveName);
                }
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (positiveListener != null) {
                            positiveListener.onClick(btnPositive);
                        }
                        if (dismiss) {
                            dialog.dismiss();
                        }
                    }
                });

            }
        };
    }
}
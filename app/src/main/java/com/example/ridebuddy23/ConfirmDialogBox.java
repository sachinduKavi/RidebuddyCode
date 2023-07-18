package com.example.ridebuddy23;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialogBox{
    Dialog confirm_dialog;
    Button cancel_btn, confirm;
    TextView head, content;
    Runnable runnableCode;
    ConfirmDialogBox(Context context, String heading, String content_s, Runnable runnable){
        confirm_dialog = new Dialog(context);
        confirm_dialog.setContentView(R.layout.activity_confirm_dialog_box);
        confirm_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        head = confirm_dialog.findViewById(R.id.heading);
        content = confirm_dialog.findViewById(R.id.content);
        cancel_btn = confirm_dialog.findViewById(R.id.cancel_button);
        confirm = confirm_dialog.findViewById(R.id.confirm_button);
        this.runnableCode = runnable;

        head.setText(heading);
        content.setText(content_s);
    }

    public void showDialogBox(){
        confirm_dialog.show();
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_dialog.dismiss();
                runnableCode.run();
            }
        });
    }
}
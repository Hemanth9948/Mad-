package com.vvit.hemanth2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class Days extends DialogFragment {
    public static String Days_ADD_DIALOG="addDays";
    public static String Number_ADD_DIALOG="addNumber";
    ProgressBar progressBar;
    private Days.OnClickListener listener;

    public interface OnClickListener{
        void onClick(String text1);
    }
    public void setListener(Days.OnClickListener listener) {
        this.listener = listener;
    }
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog=null;
        if (getTag().equals(Days_ADD_DIALOG))dialog=getAddDaysDialog();
        if (getTag().equals(Number_ADD_DIALOG))dialog=getAddNumberDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getAddNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout
                .days,null);
        builder.setView(view);
        TextView textView = view.findViewById(R.id._working_);
        EditText roll_edt=view.findViewById(R.id.days);
        Button add = view.findViewById(R.id.go);
        add.setText("Enter");
        textView.setText("Admin Number");
        roll_edt.setHint("Enter Number");
        add.setOnClickListener(v ->{
            String days= roll_edt.getText().toString();
                listener.onClick(days);
                setReturnTransition(days);
                dismiss();
        });
        return builder.create();
    }

    private Dialog getAddDaysDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout
                .days,null);
        builder.setView(view);
        EditText roll_edt=view.findViewById(R.id.days);
        Button add = view.findViewById(R.id.go);
        add.setOnClickListener(v ->{
            String days= roll_edt.getText().toString();
            listener.onClick(days);
            setReturnTransition(days);
            dismiss();
        });
        return builder.create();
    }
}

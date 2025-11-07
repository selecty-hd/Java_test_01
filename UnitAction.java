package com.example.simgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;



public class UnitAction extends DialogFragment {
    MUnit mUnit;    //アクティブユニット
    int mode= 0;    //ゲームモード

    Context context;
    /////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////
    UnitAction(Context context){
        mUnit = new MUnit();
        this.context = context;
    }
    ////////////////////////////////////////////////////

    ///  //////////////////////////////////////////////////////////////////////////////////////
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /// アラートダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TransparentAlertDialog);
        View view;
        view = new View(context);

        Bundle result = new Bundle();


        if (getActivity() != null) {
            view = getLayoutInflater().inflate(R.layout.unit_action_dialog, null);
        }

        // 背景を透明に設定


               TextView textView = view.findViewById(R.id.textView5);
               textView.setText(mUnit.name);

        TextView textView1 = view.findViewById(R.id.textView6);
       textView1.setText("Cansel");

        // alert_layout.xmlにあるボタンIDを使う
        ImageView combatButton = view.findViewById(R.id.imageButton);
       ImageView moveButton = view.findViewById(R.id.imageButton2);
       ImageView statusButton = view.findViewById(R.id.imageButton3);
        ImageView cancelButton = view.findViewById(R.id.imageButton4);

        ///ImageView  = view.findViewById(R.id.imageButton);
        ///ImageView  = view.findViewById(R.id.imageButton2);
        ///ImageView  = view.findViewById(R.id.imageButton3);

        combatButton.setOnClickListener(v -> {
            ///移動ユニットと、状態変数をActivityに送信
            result.putString("request_key", "10");





            getParentFragmentManager().setFragmentResult("request_key", result);// Stringの値を渡す
            this.dismiss();

            // Stringの値を渡す
            ///subIntent.putExtra("KEY_STRING", "文字列型だよ");
            ///subIntent.putStringArrayListExtra("KEY_LIST", (ArrayList)list);
            ///Toast ttt = new Toast(context);
            ///ttt.setText("combatButton");
            ///ttt.setDuration(Toast.LENGTH_SHORT);
            ///ttt.show();
        });

        moveButton.setOnClickListener(v -> {
            result.putString("request_key", "20");

            getParentFragmentManager().setFragmentResult("request_key", result);// Stringの値を渡す
            ///Toast ttt = new Toast(context);
            ///ttt.setText("moveButton");
            ///ttt.setDuration(Toast.LENGTH_SHORT);
            ///ttt.show();
            this.dismiss();
        });
        statusButton.setOnClickListener(v -> {
            result.putString("request_key", "30");
            getParentFragmentManager().setFragmentResult("request_key", result);// Stringの値を渡す
            ///Toast ttt = new Toast(context);
            ///ttt.setText("statusButton");
            ///ttt.setDuration(Toast.LENGTH_SHORT);
            ///ttt.show();
            this.dismiss();
        });
        ////////////////////////////////////////////////////////////////////////////////
        /// キャンセルボタンクリックで戻る
        cancelButton.setOnClickListener(v -> {
            this.dismiss();
        });




        builder.setView(view);
        return builder.create();
    }


        ///////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        /// メニュー選択中の、ユニット取得
    void setDialogUnitData(MUnit unitinfo){
        mUnit = unitinfo;
    }


        /////////////////////////////////////////////////////////////////////
        ///
  }


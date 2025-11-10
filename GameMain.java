package com.example.simgame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
///////////////////////////////////////////////////////
/// ゲーム本体のアクティビティ
//////////////////////////////////////////////////////
public class GameMain extends AppCompatActivity {
    GameView01 gameView01;
    RMenu rMenu;
    MUnit act_munit;
    private FragmentManager flagmentManager;
    ////////////////////////////////////////////////////////////////
    ///マップファイルからの読み出しデータ
    ArrayList<String> mapdata ;
    /////////////////////////////////////////////////////////////////
    ///ストレージデバイス
    File stragedirs;
    UnitList unitListA;
    UnitList unitListB;
    UnitList unitListC;

    ////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ///レイアウト実装
        setContentView(R.layout.activity_game_main);
        ///getSupportActionBar().hide();
        ///ナビゲーションバー非表示（要Ver対応）
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ///View decor = getWindow().getDecorView();
        /// hide navigation bar, hide status bar
        ///int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        ///        | View.SYSTEM_UI_FLAG_FULLSCREEN;
        ///decor.setSystemUiVisibility(uiOptions);



        unitListA = new UnitList(this);
        unitListB = new UnitList(this);
        unitListC = new UnitList(this);
///        unitListA.dummyUnitSet();
///        unitListB.dummyUnitSet();
///        unitListC.dummyUnitSet();



        /// /////////////////////////////////////////////////////////////////
        /// コンストラクタ（引数あり）
        ///     マップファイル名

        try {
            gameView01 = new GameView01(this,"map001.dat","mapcp001.png");           //表示先生成
        ///    gameView01.game_Status(rMenu, act_munit);    //状態変数/行動中ユニット送信
            
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ///gameView01.mode_change(mode,act_munit);
        flagmentManager=getSupportFragmentManager();
        /// ///////////////////////////////////////////////////////////////

        /// 仮に準備したUnitList（A）のみを設定
        gameView01.setUnitlist(1,unitListA);

        ///////////////////////////////////////////////////////////////////////////
        ///ダイアログ用リスナー（ダイアログだ閉じると呼ばれる）
        getSupportFragmentManager().setFragmentResultListener("request_key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(
                    @NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported.
                ///ダイアログに設定した値をStringのresultとして取得
                String result = bundle.getString("request_key");
                ///（仮）Stringのresultをintに変換してゲームモードに直接設定
                gameView01.mode=Integer.parseInt(result);
                // Do something with the result.
            }
        });










        gameView01.setflagmentManager(flagmentManager);

        setContentView(gameView01);                            //表示先指定

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }



}
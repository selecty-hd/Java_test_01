package com.example.simgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;
///////////////////////////////////////////////////////////////
/// タイトル画面のアクティビティ
///     グラフィックはXML参照
//////////////////////////////////////////////////////////////
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    /// ///////////////////////////////////////////////////////////
    /// ダウンロード先URL表示
    TextView url_textview;
    /// 保存先ストレージPath表示
    TextView path_textview;
    /// DownLoadFile表示
    TextView file_list_textview;
    /// マルチタスク状態表示
    TextView asynctask_textview;
    /// ////////////////////////////////////////////////////////////
    /// ダウンロードバー
    ProgressBar downloadbar;
    /// ////////////////////////////////////////////////////////////
    /// Configデータdownloadボタン
    Button download_list;
    /// 本体データdownloadボタン
    Button getDownload_file;
    /// 遷移ボタン
    Button file_test;
    /// ///////////////////////////////////////////////////////////
    /// データダウンロード先URL
    String url_dir = new String("http://www.ambrose.pw/simgame/");
    /// ///////////////////////////////////////////////////////////
    ///　ストレージのPath
    File stragedirs;
    /// //////////////////////////////////////////////////////////////
    ArrayList<String> gamestragefilenames;  ///ストレージファイル
    ArrayList<String> setfiles;             ///ダウンロード・ファイル（URLなし）
    String xxxx;
    Setting setting;
    Setting filesetting;
    String configfile = "config.txt";
    /// /////////////////////////////////////////////////////////////////
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///////////////////////////////////////////////////////
        //画面をエッジ・ツウ・エッジで表示する。（タイトルバー・システムバー表示）
        EdgeToEdge.enable(this);
        /////////////////////////////////////////////////////////
        //レイアウトをXMLで指定
        setContentView(R.layout.activity_main);
        //////////////////////////////////////////////////////////////////
        ///　ボタン・モードで操作部の重複を回避する（以降、検討事項）
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /// ////////////////////////////////////////////////////////////////
        /// 画面を縦に固定
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ////////////////////////////////////////////////////////////////////////
        ///各表示部設定
        url_textview = (TextView) findViewById(R.id.textView);           ///DownLoadURL
        path_textview = (TextView) findViewById(R.id.textView2);         ///StragePath
        file_list_textview = (TextView) findViewById(R.id.textView3);
        asynctask_textview = (TextView) findViewById(R.id.textView4);
        //////////////////////////////////////////////////////////////////
        ///ダウンロード進捗用プログレスバー
        downloadbar = (ProgressBar) findViewById(R.id.progressbar);
        /// ////////////////////////////////////////////////////////////////////
        ///各ボタン設定
        download_list = (Button) findViewById(R.id.button);
        download_list.setOnClickListener(this);

        getDownload_file = (Button) findViewById(R.id.button2);
        getDownload_file.setOnClickListener(this);

        file_test = (Button) findViewById(R.id.button3);
        file_test.setOnClickListener(this);
        /// /////////////////////////////////////////////////////////////////////
        /// ダウンロードURL表示
        url_textview.setText(url_dir);
        /// /////////////////////////////////////////////////////////////////////
        ///　ストレージのPath取得＆表示
        stragedirs = getExternalFilesDir(null);
        if (stragedirs != null) {
            path_textview.setText(stragedirs.getAbsolutePath());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            //////////////////////////////////////////////////////////////////
            /// Configファイル名称設定・表示・ダウンロード準備
            file_list_textview.setText(configfile);
            /// //////////////////////////////////////////////////////////////////
            /// 別タスクの生成（Configファイル読み込み処理）
            NetFileLoad sfa;
            /// 別タスクのコンストラクタにプログレスバー・ダミーTextViewを渡す
            sfa = new NetFileLoad(downloadbar,file_list_textview);
            ///////////////////////////////////////////////////////////////////////
            /// 別タスクにダウンロードファイルの設定
            ArrayList<String> configFiles = new ArrayList<String>();
            configFiles.add(configfile);
            sfa.setFilenames(configFiles);
            /// //////////////////////////////////////////////////////////////
            /// 別タスクへストレージ名称取得して送る
            String strage = stragedirs.getAbsolutePath()+"/";
            /// //////////////////////////////////////////////////////////////
            /// 取得したストレージ名を別タスクに設定
            sfa.setStrageFilePath(strage);
            ///file_list_textview.setText(configfile.get(0));
            ///file_list_textview.setText(strage);
            /// ///////////////////////////////////////////////////////////////
            /// 別タスクにダウンロードするURLを送る
            sfa.setdownloadURLFiles(url_dir);
            ///file_list_textview.setText(url_dir);
            /// ///////////////////////////////////////////////////////////////
            /// 別タスク起動
            sfa.start_task();

            /// ////////////////////////////////////////////////////////////////
            /// ファイルリスト取得
            String gamestragename = strage+"/"+configfile;
            /// ストレージ・オブジェ作成
            StrageFileAccess gamefilelist = new StrageFileAccess(gamestragename);
            /// ストレージからファイル読み込み（複数行）
            gamestragefilenames = gamefilelist.stragefile_text_read();
            //////////////////////////////////////////////////////////
            /// Configファイルの設定取り出し
            filesetting = new Setting();

            setfiles = filesetting.fileSetting(gamestragefilenames);

            ///////////////////////////////////////////////////////////////////////
            /// ファイルリスト確認のため、表示処理
            StringBuilder xxx;
            xxx= new StringBuilder();
            ///テスト確認用文字列生成
            for(int k=0;k<setfiles.size();k++) {
                xxx.append(gamestragefilenames.size());
                xxx.append(setfiles.size());
///                xxx.append(setfiles.get(k));
                xxx.append("/");
            }
            ///asynctask_textview.setText(xxx);
            ///xxxx=gamestragefilenames.get(1);
            ///asynctask_textview.setText(""+gamestragefilenames.size());

        } else if (view.getId() == R.id.button2) {
            /// /////////////////////////////////////////////////////////////
            /// 別タスクの生成（各種画像、パラメータ、マップ等のファイル読み込み処理）
            NetFileLoad gamesfl;
            /// //////////////////////////////////////////////////////////////
            /// 別タスクのコンストラクタにプログレスバー・ダミーTextViewを渡す
            gamesfl = new NetFileLoad(downloadbar,file_list_textview);
            /// //////////////////////////////////////////////////////////////
            /// 別タスクへストレージ名称取得して送る
            String gamestrag =stragedirs.getAbsolutePath()+"/";
            gamesfl.setStrageFilePath(gamestrag);
            /////////////////////////////////////////////////////////////////////
            /// 別タスクにダウンロードするURLを送る
            gamesfl.setdownloadURLFiles(url_dir);
            /// /////////////////////////////////////////////////////////////////
            ///ファイル・リスト作成            //総ファイル数
            //////////////////////////////////////////////////////////////////////
            /// 別タスクにダウンロードするファイル名を渡す
            gamesfl.setFilenames(setfiles);
            /// ///////////////////////////////////////////////////////////////////
            /// 別タスク起動
            gamesfl.start_task();


        } else if (view.getId() == R.id.button3) {
                Intent intent=new Intent(this,GameMain.class);
                startActivity(intent);


        }
    }
 }
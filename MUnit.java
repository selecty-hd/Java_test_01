package com.example.simgame;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

    public class MUnit implements Serializable {
    ///ストレージデバイス
    ///
    int unit_id;
    /// /////////////////////////////////////////////////////////////
    /// ユニット画像読み込み用Fileストレージ
    File stragedirs;
    ///////////////////////////////////////////////////////////////////
    /// 各種処理用Context
    Context context;
    ////////////////////////////////////////////////////////////////////
    /// ユニット画像
    Bitmap alias;
    ////////////////////////////////////////////////////////////////////
    /// ユニットパラメータ読み込み用Path
    String unitpath;
    /////////////////////////////////////////////////////////////////////
    /// ユニットパラメータ読み込み用ファイルストレージ
    File unitfilebuffer;
    //////////////////////////////////////////////////////////////////////
    /// ユニットパラメータ読み込み用ストリーム
    InputStream inputStream=null;
    //////////////////////////////////////////////////////////////////////
    /// ユニットX座標
    public int unitX;
    //////////////////////////////////////////////////////////////////////
    /// ユニットY座標
    public int unitY;
    //////////////////////////////////////////////////////////////////////
    /// ユニットファイル名バッファ領域
    StringBuffer stringBuffer;
    //////////////////////////////////////////////////////////////////////
    /// ユニット名
    String name;
    /// ///////////////////////////////////////////////////////////////////
    /// ステータス
    int status;
    ////////////////////////////////////////////////////////////////////
    /// 向き？
    int direction;
    /////////////////////////////////////////////////////////////////////
    /// パラメータ列
    ArrayList<String> unitParam;
    //////////////////////////////////////////////////////////////////////
    /// コンストラクタ
    MUnit(){
        ///処理なし
    }
    /////////////////////////////////////////////////////////////////////
    /// Context/ファイル名/ユニットパラメータ宣言
    void setContext(Context c){
        context = c;
        stringBuffer = new StringBuffer();
        unitParam = new ArrayList<String>();
    }
    //////////////////////////////////////////////////////////////////////
    /// ユニットNoで、画像
    void dummyUnitImageSet(int no){
        /// //////////////////////////////////////////////////////////////
        /// 指定画像の読み出し
        stragedirs = context.getExternalFilesDir(null);                         //ストレージPathセット
        unitpath = stragedirs.getAbsolutePath()+"/"+"unit"+setNumber(no)+".png";    //ファイル名生成
        unitfilebuffer = new File(unitpath);                                        //ファイルアクセス
        ///以下、ユニット画像読み出し
        try {
            inputStream = new FileInputStream(unitfilebuffer);
            alias = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    ////////////////////////////////////////////////////////////////
    /// ユニット名設定
    void dummyUnitNameSet(int no){
        /// //////////////////////////////////////////////////////////////
        stragedirs = context.getExternalFilesDir(null);
        unitpath = stragedirs.getAbsolutePath()+"/"+"param"+setNumber(no)+".dat";
        StrageFileAccess unitfilebuffer = new StrageFileAccess(unitpath);            ///ストレージOpen
        unitParam = unitfilebuffer.stragefile_text_read();                           ///ストレージデータ取得


///  //////////////////////////////////////////////////////////

        ///stringBuffer.append(unitParam.get(0));
        name = unitParam.get(0).toString();
///        name = stringBuffer.toString();
    }

    /// //////////////////////////////////////////////////////////////////
    ///ユニット座標設定
    void dummyUnitPosisionSet(int x,int y){
        unitX = x;
        unitY = y;
    }
    /// /////////////////////////////////////////////////////////////
    void dummyUnitID(int wid){
        unit_id = wid;
    }




    /// ///////////////////////////////////////////////////////////////
    /// 「000～999」番号生成
    String setNumber(int x){
        String retbuffer;
        if(x<10){
            retbuffer="00"+Integer.toString(x);
        }else if(x<100){
            retbuffer="0"+Integer.toString(x);
        }else{
            retbuffer=Integer.toString(x);
        }
        return (retbuffer);
    }

}

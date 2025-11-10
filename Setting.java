package com.example.simgame;

import java.util.ArrayList;
/*
 * #ここはコメント/以下はファイル数

#ここはコメント/以下はファイル数
#以下、マップパラメータ
mapcs001.dat
mapcs002.dat
#以下、ユニットデータ
mos001.png
mos002.png
param001.dat
param002.dat

 map=3
chip=2
unit=2



002
23456789A
3456789AB
456789ABC
56789ABCD
6789ABCDE
789ABCDEF
89ABCDEFG
9ABCDEFGH
ABCDEFGHI



001
10
20
30


ドラゴン
1
2
3
4

 
 * 
 * 
 */
///////////////////////////////////////////////////////////////////////
/// configファイル処理
/// ///////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/// ダウンロード・ファイル名を、configファイルから生成
 public class Setting {
    ArrayList<String> mapfile;
    /// Mapファイル名
    ArrayList<String> mapchipfile;
    /// Mapchipファイル名
    ArrayList<String> unitfile;
    /// Unitファイル名
    ArrayList<String> paramfile;
    ///UnitParamファイル名
    ArrayList<String> allFailelist;
    /// ダウンロードファイル一覧
    int mapcount;
    /// Map数
    int chipcount;
    /// Mapchip数
    int unitcount;

    /// Unit数
    /// /////////////////////////////////////////////////////
    /// コンストラクタ
    Setting() {
    }

    ;

    /// //////////////////////////////////////////////////////
    /// Configファイルの行単位の情報から各データファイル名を生成する
    ArrayList<String> fileSetting(ArrayList<String> stregefiles) {
        mapfile = new ArrayList<String>();
        /// Mapファイル名
        mapchipfile = new ArrayList<String>();
        /// Mapchipファイル名
        unitfile = new ArrayList<String>();
        /// Unitファイル名
        paramfile = new ArrayList<String>();
        allFailelist = new ArrayList<String>();

        StringBuffer mapbuffer;
        mapbuffer=new StringBuffer();
        StringBuffer chipbuffer;
        chipbuffer = new StringBuffer();
        StringBuffer unitbuffer;
        unitbuffer = new StringBuffer();
        StringBuffer parambuffer;
        parambuffer = new StringBuffer();

        for (int i = 0; i < stregefiles.size(); i++) {
            /// 設定ファイルから1行読み込み
            String buffer = new String(stregefiles.get(i));
            /// ファイルリストからスペース等、不要文字列を削除して戻す
            stregefiles.set(i, buffer.trim());
            /// 先頭が＃ならコメント行
            if (((stregefiles.get(i)).indexOf('#') != -1)) {
                /// コメントであれば、ファイルデータから抜く
                stregefiles.remove(i);
                i--;
            }

            ////////////////////////////////////////////////////////////
            /// Map枚数の抽出
            if ((buffer.indexOf("map=")) != -1) {
                ///＃以下、マップデータ
                ///map=3
                /// マップ件数を検出
                mapcount = Integer.valueOf(buffer.substring(4));
                /// Map設定行の削除
                stregefiles.remove(i);
                i--;
            } else if ((buffer.indexOf("chip=")) != -1) {
                ///#以下、マップ画像
                ///chip=2
                /// マップチップ件数を検出
                chipcount = Integer.valueOf(buffer.substring(5));
                /// Chip設定行の削除
                stregefiles.remove(i);
                i--;
            } else if ((buffer.indexOf("unit=")) != -1) {
                /// ユニット件数を検出
                ///unit=2
                unitcount = Integer.valueOf(buffer.substring(5));
                /// Unit設定行の削除
                stregefiles.remove(i);
                i--;
            }

        }

        for (int i=0;i<mapcount;i++){
            ///ArrayList<String> mapfile;
            /// ＃以下、マップデータ
            ///map001.dat
            ///map002.dat
            ///map003.dat
            mapbuffer.setLength(0);
            mapbuffer.append("map");
            mapbuffer.append(setNumber(i+1));
            mapbuffer.append(".dat");
            mapfile.add(mapbuffer.toString());
        }
        for (int i=0;i<chipcount;i++){
            ///#以下、マップ画像
            ///mapcp001.png
            ///mapcp002.png
            ///ArrayList<String> mapchipfile;
            chipbuffer.setLength(0);
            chipbuffer.append("mapcp");
            chipbuffer.append(setNumber(i+1));
            chipbuffer.append(".png");
            mapchipfile.add(i,chipbuffer.toString());
        }
        for (int i=0;i<unitcount;i++){
            ///#以下、ユニット画像
            ///unit001.png
            ///unit002.png
            ///ArrayList<String> unitfile;
            unitbuffer.setLength(0);
            unitbuffer.append("unit");
            unitbuffer.append(setNumber(i+1));
            unitbuffer.append(".png");
            unitfile.add(i,unitbuffer.toString());
        }

        for (int i=0;i<unitcount;i++){
            ///#以下、ユニットパラメータ
            ///param001.png
            ///param002.png
            ///ArrayList<String> paramfile;
            parambuffer.setLength(0);
            parambuffer.append("param");
            parambuffer.append(setNumber(i+1));
            parambuffer.append(".dat");
            paramfile.add(i,parambuffer.toString());
        }

        ///#以下、マップパラメータ
        ///mapcs001.dat
        ///mapcs002.dat
        ///#以下、ユニット・パラメータデータ
        ///param001.dat
        ///param002.dat
        for(int i=0;i<mapfile.size();i++){
            allFailelist.add(mapfile.get(i)); ///ダウンロードファイル一覧///Mapファイル名
        }
        for(int i=0;i<mapchipfile.size();i++){
            allFailelist.add(mapchipfile.get(i)); ///ダウンロードファイル一覧///Mapchipファイル名
        }
        for(int i=0;i<unitfile.size();i++){
            allFailelist.add(unitfile.get(i)); ///ダウンロードファイル一覧///Unitファイル名
        }
        for(int i=0;i<unitfile.size();i++){
            allFailelist.add(paramfile.get(i)); ///ダウンロードファイル一覧///Unitファイル名
        }
        /// //////////////////////////////////////////////////////
       return (allFailelist);
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
    /// ///////////////////////////////////////////////////////////////////////////

    int getMapcount(){
        return mapcount;
    }
    int getChipcount(){
        return chipcount;
    }
    int getUnitcount(){
        return unitcount;
    }
    /////////////////////////////////////////////////////////////////////////////








    /// ///////////////////////////////////////////////////////////////
    /// 「000～999」番号生成
    String setNumberArfa(int x){
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
    ////////////////////////////////////////////////////////////////
}

package com.example.simgame;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by AmbroseWord on 2016/04/22.
 */
public class StrageFileAccess {
    String stragefile_path=null;    //ストレージpath名
    int read_buff_size = 0;         //ストレージファイル行数
    /////////////////////////////////////////////////////////////////////////////////////////////
    StrageFileAccess(String path){
        this.stragefile_path = path;     //アクセス先をコンストラクタで指定
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    //内部＆外部ストレージファイルの存在確認
    //  ファイルあり：true
    //  ファイルなし：false
    public Boolean chk_stragefile(){
        if((stragefile_path==null)&&(read_buff_size<0)){    //Path名無し＆ファイルサイズNGチェック
            return false;                                     //ストレージファイル無し
        }
        try{
            File infile = new File(stragefile_path);        //ストレージファイル取得
            FileReader filereader = new FileReader(infile);     //読み出し用にOpen
            filereader.close();                                 //ストレージファイルをClose
        }catch(Exception e1){
            return false;             //読み出し用にOpen失敗の場合、ストレージファイルの存在なし
        }
        return true;                    //正常にOpen＞Close出来れば存在あり
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    //内部＆外部ストレージファイルへの書き込み
    //  引数：書き込み文字列の配列
    void stragefile_text_write(ArrayList<String> write_data){
        if((stragefile_path==null)&&(read_buff_size<0)){    //Path名無し＆ファイルサイズNGチェック
            return ;                                             //ストレージファイル無し
        }
        File file = new File(stragefile_path);          //ストレージファイル取得
        try {
            PrintWriter writer = new PrintWriter(file);     //書き込み用PrintWriter生成
            for(int i=0;i<write_data.size();i++){                                //書き込み用ループ
                writer.println(write_data.get(i));    //ストレージに行単位で書き込み
            }
            writer.flush();                             //書き込み終了処理
            writer.close();                             //ストレージファイルClose
            read_buff_size=write_data.size();       //書き込みデータ行数格納
        } catch (Exception e) {
            read_buff_size=0;                       //書き込み失敗時は、書き込み行数を0にセット
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    //内部＆外部ストレージファイルへからの読み出し
    //  戻り値：読み出された文字列の配列（可変長配列）
    ArrayList<String> stragefile_text_read(){
        if((stragefile_path==null)&&(read_buff_size<0)){    //Path名無し＆ファイルサイズNGチェック
            return (null) ;                                     //ストレージファイル無し
        }
        ArrayList <String> read_data = new ArrayList <String>();    //戻り値（可変長配列）
        try{
            File infile = new File(stragefile_path);        //ストレージファイル取得
            FileReader filereader = new FileReader(infile);     //読み出し用FileReader生成
            String text = new String();                         //読み出した文字列格納用
            int i = 0;                                          //行数カウント
            int ch;                                             //1文字のみ格納
            int ch0=0;
            int flag =0;
            while (true){                                       //読み出し用ループ
                StringBuffer buf = new StringBuffer();          //読み出し用バッファ
                if(ch0!=0){
                    buf.append((char)ch0);
                }
                flag = 0;
                while(true){                                    //行単位ループ
                    ch0=0;
                    ch = filereader.read();                     //1文字読み出し
                    if(ch==-1) break;                           //ファイル終端の検出

                    if(ch=='\r'){
                        ch0 = filereader.read();
                        if(ch0=='\n'){
                            ch0=0;
                            flag = 1;
                        }else{
                            flag = 1;
                        }
                    }else if(ch=='\n'){
                        flag = 1;
                    }
                    //if(ch=='\r')continue;                          //行端の検出
                    if(flag==1){
                        break;
                    }
                    buf.append((char)ch);                       //文字を接続し1行にまとめる

                }
                if(ch==-1) break;                               //ファイル終端の検出
                //if(ch=='\r')continue;                          //行端の検出
                read_data.add(buf.toString());                  //1行分を文字列に変換
                i++;                                            //行数カウント＋１
                //if(i>=read_data.size()) break;
            }
            filereader.close();                                 //読み出し用FileReaderをClose
            read_buff_size=i;                               //行数を格納
        } catch (java.io.IOException e) {
            return (null);                                  //エラー時はNull
        };
        return read_data;                                   //読み出し結果を返す
    }

}

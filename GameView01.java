package com.example.simgame;

import static android.graphics.Paint.*;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.fonts.Font;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GameView01 extends View implements Runnable{
    Context context;
    Paint paint;
    float ax,ay;
    /////////////////////////////////////////////////////////////////
    ///ストレージデバイス
    File stragedirs;
    /////////////////////////////////////////////////////////////////
    /// マップファイル名
    String mapFileName;
    //////////////////////////////////////////////////////////////////
    ///マップデータの1セルあたりのデータ
    char mapp[][];
    //////////////////////////////////////////////////////////////////
    ///マップの横方向サイズ
    int xmax;
    /////////////////////////////////////////////////////////////////
    ///マップの縦方向サイズ
    int ymax;
    ////////////////////////////////////////////////////////////////
    ///マップファイルからの読み出しデータ
    ArrayList<String> mapdata ;
    ///////////////////////////////////////////////////////////////////
    ///マップ表示基準点
    int basex , basey;
    /////////////////////////////////////////////////////////
    ///マップ展開用（１行分のマップデータを配列として読み込み）
    char buffer01[];
    /// /////////////////////////////////////////////////////
    /// マップWorkエリア
    int mapwork[][];
    String mapbuffer;
    //////////////////////////////////////////////////////////
    ///ドラッグ判定用
    float drag;
    float ndrag;
    float dd;
    ////////////////////////////////////////////////////////////
    //ユニット画像・ストレージ上のユニットファイル・その他
    Bitmap unitimage[];
    String unitpath;
    File unitfilebuffer;
    InputStream inputStream=null;
    MUnit mUnit[];
    /// ///////////////////////////////////////////////////////
    Bitmap mapcpimage;
    String mapcppath;
    File mapcpfilebuffer;
    int xw,yh;
    Rect retctU02;
    int tx,ty;
    /////////////////////////////////////////////////////////////////////////
    ///タッチフラグ
    int uflag;
    private FragmentManager flagmentManager;
    //////////////////////////////////////////////////////////////////
    ///状態変数
    int mode = 0;
    /// //////////////////////////////////////////////////////////////
    /// スレッド
    Thread thread;
    /// //////////////////////////////////////////////////////////////
    ///予備を含めてユニットリスト
    UnitList unitListA;
    UnitList unitListB;
    UnitList unitListC;
    ///アクティブなユニット
    MUnit act_Munit;
    /// アクティブなユニットのID
    int act_Munit_id=0;
    /// ////////////////////////////////////////////////////////////////
    public GameView01(Context context,String mapFileNameLocal,String mapChipLocal) throws FileNotFoundException {
        super(context);
        this.context=context;
        Resources resources = context.getResources();                     ///リソース取得
        thread = new Thread(this);                                  ///スレッド開始
        thread.start();
         paint = new Paint();                                         ///表示用Paint
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);

        initProcess(mapFileNameLocal,mapChipLocal);


    }
    /// //////////////////////////////////////////////////////////////////
    void initProcess(String mapFileNameLocal,String mapChipLocal){
        /// ////////////////////////////////////////////////////////
        ///ストレージのPath取得＆表示
        stragedirs = context.getExternalFilesDir(null);
        ///////////////////////////////////////////////////////////////////
        ///ストレージからマップファイル読み込み
        String strage = stragedirs.getAbsolutePath()+"/"+mapFileNameLocal;  ///ストレージへのPath生成
        StrageFileAccess map = new StrageFileAccess(strage);            ///ストレージOpen
        mapdata = map.stragefile_text_read();                           ///ストレージデータ取得
        ///マップサイズ取得
        ymax = mapdata.size();                                          ///マップのY軸方向のサイズ
        xmax = mapdata.get(0).length();                                 ///マップのX軸方向のサイズ
        /////////////////////////////////////////////////////////////////////
        ///取得したマップサイズで配列を生成
        mapp = new char[ymax][xmax];
        /// //////////////////////////////////////////////////////////////////
        /// マップワークエリア生成
        mapwork = new int[ymax][xmax];
        ////////////////////////////////////////////////////////////////////
        ///マップデータ展開
        for(int i=0;i<ymax;i++){
            ///一行分の取り出し
            String buffer = mapdata.get(i);
            for(int j=0;j<xmax;j++){
                ///２文字部分を16進数に変換
                /// Integer.parseInt(16進数文字列, 16)
                buffer01 = buffer.toCharArray();
                mapp[i][j]=buffer01[j];
                mapwork[i][j] = 0;
            }
        }
        /// ////////////////////////////////////////////////////////////////////////
        /// ユニット暫定版設定

        unitListA = new UnitList(context);
        ///unitListA.dummyUnitSet();
        ///unitListB
        ///unitListC


       /// ////////////////////////////////////////////////////////////////////////
        /// ////////////////////////////////////////////////////////////////////////
        ///マップ画像読み込み
        ///テスト用マップデータ設定
        /// マップチップ集は一応２枚読み込んでいる
        ///mapcpimage = new Bitmap();
        stragedirs = context.getExternalFilesDir(null);
        mapcppath = stragedirs.getAbsolutePath()+"/"+mapChipLocal;
        mapcpfilebuffer = new File(mapcppath);
        try {
            inputStream = new FileInputStream(mapcpfilebuffer);
            mapcpimage = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        /// ///////////////////////////////////////////////////////////////
        ///マップ表示起点初期化
        basex = 0;
        basey = 0;
        /// //////////////////////////////////////////////////////////////

    }


    /// /////////////////////////////////
   void setUnitlist(int num,UnitList unitListw){
        switch (num){
            case 1:
                unitListw.dummyUnitSet(ymax,xmax);
   ///             unitListw.dummyUnitSet(xmax,ymax);
                unitListA=unitListw;
                break;
            case 2:
///                unitListw.dummyUnitSet(xmax,ymax);
///                unitListB=unitListw;
                break;
            case 3:
///                unitListw.dummyUnitSet(xmax,ymax);
///                unitListC=unitListw;
                break;
            default:
                unitListA=unitListB=unitListC=null;
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////
    public void onDraw(@NonNull Canvas canvas) {
        ///以降描画処理、Canvasに対して表示
        super.onDraw(canvas);
        ////////////////////////////////////////////////////
        ////////////////////////////////////////////////
        /// Paint05
        /// 用途不明
        // ペイントストロークの太さを設定
        Paint paint05 = new Paint(ANTI_ALIAS_FLAG);
        paint05.setColor(Color.BLACK);
        paint05.setStyle(Style.STROKE);
        paint05.setStrokeWidth(1);
        /////////////////////////////////////////////////////
        ///Paint06
        /// 画面グレー表示用
        Paint paint06 = new Paint();
        paint06.setColor(Color.argb(128, 0, 0, 0));
        //paint06.setStyle(Paint.FILL_AND_STROKE);
        /// /////////////////////////////////////////////////////////////////////
        /// ゲームモード状態ごとの処理
        ///移動処理
        if(mode==10){
             int x,y,mv;
             if(act_Munit_id!=0) {
                 y = unitListA.selectMUnit(act_Munit_id).unitY;    ///起点位置X
                 x = unitListA.selectMUnit(act_Munit_id).unitX;    ///起点位置Y
                 mv = 4;                 ///仮の移動距離
                 mapwork = act_Munit_move(y, x, mv, mapwork);
                 mode = 11;
             }
        }else if(mode==0){
            for(int i=0;i<ymax;i++){
                for(int j=0;j<xmax;j++){
                    mapwork[i][j]=0;
                }
            }
        }


        /////////////////////////////////////////////////////////////////////////
        ///以下、マップ描画処理
        ///////////////////////////////////////////////////////////////////////
        ///X軸方向ループ
        for(int j=0;(j*2)<xmax;j++){
        ///    for(int j=basey;j<9;j++){
            ///X軸方向が奇数偶数で分岐している？ 
            if(j%2==0){
                /////////////////////////////////////////////////////////
                /// Y軸方向のループ
                for(int i=0;i<ymax;i++) {
                    ///    for(int i=basex;i<9;i++) {
                    //////////////////////////////////////////////////////////
                    /// Ｙ座標の奇数偶数で分岐させている？
                        if((((2*i)+basex)<xmax)&&(((i*2)+basex)>=0)&&((j+basey)<ymax)&&((j+basey)>=0)){
                            xw = Integer.parseInt(String.valueOf(mapp[j+basey][2*i+basex]), 16)*32;
                            yh = Integer.parseInt(String.valueOf(mapp[j+basey][2*i+1+basex]), 16)*32;
                            Rect retct010 = new Rect(xw, yh,xw+32, yh+32);
                            Rect retct020 = new Rect(256*i, 256*j,256*i+256, 256*j+256);
                            canvas.drawBitmap(mapcpimage,retct010,retct020,null);
                            if(mode==11){
                                ///if((i+basex>=0)&&(i+basex<=xmax)&&(j+basey>=0)&&(j+basey<=ymax)){
                                ///  if(((i+basey)<ymax)&&((j+basex)<xmax)){
                                if(mapwork[j+basey][i+basex]==0){
                                           Rect retct021=new Rect(256*((i+0)), 256*(j+0),256*((i+0))+256, 256*(j+0)+256);
                                           canvas.drawRect(retct021, paint06);
                                                ///canvas.drawRect(256 * (i-basex), 256 * (j-basey), 256 * ((i-basex) + 1), 256 * ((j-basey) + 1), paint06);
                                 ///       }
                                 ///   }
                                }
                            }

///                            if(mode==1){
///                                if((Math.abs(act_Munit.unitX-(i+basex))<=2)&&(Math.abs(act_Munit.unitY-(j+basey))<=2)){
///
///                                }else{
///                                    canvas.drawRect(256 * i, 256 * j, 256 * (i + 1), 256 * (j + 1), paint06);
///                                }
///                            }
                            canvas.drawRect(256 * i, 256 * j, 256 * (i + 1), 256 * (j + 1), paint05);
                        }
                   }
                }


                for(int i=0;(2*i)<ymax;i++) {
                    if (j % 2 != 0) {
                        if((((i*2)+basex)<xmax)&&(((i*2)+basex)>=0)&&((j+basey)<ymax)&&((j+basey)>=0)){

                            xw = Integer.parseInt(String.valueOf(mapp[j + basey][2*i+basex]), 16)*32;
                            yh = Integer.parseInt(String.valueOf(mapp[j + basey][2*i+1+basex]), 16)*32;
                            Rect retct010 = new Rect(xw, yh,xw+32, yh+32);
                            Rect retct020 = new Rect(256 * i + 126, 256 * j,256 * i + 126+256, 256 * j +256);
                            canvas.drawBitmap(mapcpimage,retct010,retct020,null);
                            if(mode==11){
                                ///if((i+basex>=0)&&(i+basex<=xmax)&&(j+basey>=0)&&(j+basey<=ymax)){
                                ///    if(((i+basey)<ymax)&&((j+basex)<xmax)){
                                        if(mapwork[j+basey][i+basex]==0){
                                            Rect retct021=new Rect(256*(i+0)+128, 256*(j+0),256*(i+0)+256+128, 256*(j+0)+256);
                                            canvas.drawRect(retct021, paint06);
                                ///            canvas.drawRect(256 * (i-basex), 256 * (j-basey), 256 * ((i-basex) + 1), 256 * ((j-basey) + 1), paint06);
                                            ///canvas.drawRect(256 * i + 126, 256 * j, 256 * (i + 1)+126, 256 * (j + 1), paint06);
                                ///        }
                                ///    }
                                }
                            }

///                            if(mode==1){
///
///                                if((Math.abs(act_Munit.unitX-(i+basex))<=2)&&(Math.abs(act_Munit.unitY-(j+basey))<=2)){
///
///                                }else{
///                                    canvas.drawRect(256 * i + 126, 256 * j, 256 * (i + 1) + 126, 256 * (j + 1), paint06);
///                                }
///                            }
                            canvas.drawRect(256 * i + 126, 256 * j, 256 * (i + 1) + 126, 256 * (j + 1), paint05);
                        }
                    }
                }
            for(int l=0;l<xmax;l++){
                for(int k=0;k<ymax;k++){
                    mapbuffer=String.valueOf(mapwork[k][l]);
                    ax=(float)(50.0f+50.0f*l);
                    ay=(float)(100.0f+50.0f*k);
                    canvas.drawText(mapbuffer,ax,ay,paint);
                }
            }





        }
        ///ここまでマップ表示処理
        ////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////
        /// ユニット処理（27体分）
        for(int k=1;k<27;k++){
            ///mUnit[k] = unitList.mUnitList[k];
            ///Rect retctU01 = new Rect(0,0,100,100);///90 68
            ///////////////////////////////////////////////////////////
            /// ユニットの縦横の描画サイズ取得
            Rect rectU01 = new Rect(0,0,
                unitListA.mUnit[k].alias.getWidth(),unitListA.mUnit[k].alias.getHeight());
            /////////////////////////////////////////////////////////////
            ///ユニットの表示位置のRect設定
            Rect rectU02 = new Rect();
            if(unitListA.mUnit[k].unitY%2==0){
                ////////////////////////////////////////////////////////////
                /// ユニットのY軸が偶数の場合
                ///Rect retct020 = new Rect(256*i, 256*j,256*i+256, 256*j+256);
                rectU02.set((unitListA.mUnit[k].unitX-basex)*256,
                        (unitListA.mUnit[k].unitY-basey)*256,
                        (unitListA.mUnit[k].unitX+1-basex)*256,
                        (unitListA.mUnit[k].unitY+1-basey)*256);
            }else{
                //////////////////////////////////////////////////////
                /// ユニットのY軸が奇数の場合（X軸方向に半コマずらす）
                rectU02.set(((unitListA.mUnit[k].unitX-basex)*256+126),
                        (unitListA.mUnit[k].unitY-basey)*256,
                        ((unitListA.mUnit[k].unitX+1-basex)*256+126),
                        (unitListA.mUnit[k].unitY+1-basey)*256);
            }
            /////////////////////////////////////////////////////////////
            /// Y軸が奇数/偶数でも座標が変更されるが、表示は共通
            canvas.drawBitmap(unitListA.mUnit[k].alias,rectU01,rectU02,null);
        }
    }
    ///ここまで、表示部
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    /// ここから、画面タッチ関連の処理
    public boolean onTouchEvent(MotionEvent event){
            ///drag=event.getX();
            ///int flag;
            ///Toast toast = Toast.makeText(this.getContext(), "aaaaaaaaaaa", Toast.LENGTH_LONG);
            ////toast.show();
            ///////////////////////////////////////////////////////
            //画面タッチの検出
            switch (event.getActionMasked()) {
                //////////////////////////////////////////////////
                /// 画面をタッチ
                case (MotionEvent.ACTION_DOWN):
                    dd = event.getX();      ///タッチ位置を記録し、スワイプ対策
                    uflag=1;                ///タッチフラグOn
                    break;
                //////////////////////////////////////////////////////////
                /// 画面からのリリース
                case (MotionEvent.ACTION_UP):
                    //////////////////////////////////////////////////////
                    /// タッチ位置修正
                    if((event.getY()/256)%2==0){
                        ty= (int) ((event.getY())/256)+basey;
                        tx= (int) ((event.getX())+256/256)+basex-1;
                    }else{
                        ty= (int) ((event.getY())/256)+basey;
                        tx= (int) ((event.getX()+128)/256)+basex-1;
                    }

                    /// リリース位置にユニットがいた場合
                    for(int i=1;i<28;i++) {
                        ///////////////////////////////////////////////////////////////////
                        ///タッチ位置にユニットがいる場合
                        if ((tx == unitListA.mUnit[i].unitX) && (ty == unitListA.mUnit[i].unitY)) {
                            ///現状のモードが10以外、もしくはモードが10で操作中のキャラクタの場合
                            if ( mode/10 != 1) {
                                /// ダイアログ生成
                                UnitAction dialog = new UnitAction(context);
                                ///ダイアログに選択されたユニット情報を送る
                                dialog.setDialogUnitData(unitListA.mUnit[i]);
                                ///選択中のユニットを「アクティブなユニットとして登録」
                                ///act_Munit = unitListA.mUnit[i];
                                act_Munit_id = unitListA.mUnit[i].unit_id;
                                /// /////////////////////////////////////////////////////////////////
                                /// ユニットメニュー開始
                                dialog.show(flagmentManager, "GAME_DIALOG");
                                /// ユニットメニュー終了
                                //////////////////////////////////////////////////////////////////
                                break;
                            }
                        }
                    }
                    ///////////////////////////////////////////////////////////////////////////////
                    ///ユニット28体分のループ終了
                    if(((mode==11)&&((tx==unitListA.selectMUnit(act_Munit_id).unitX)&&(ty==unitListA.selectMUnit(act_Munit_id).unitY)))){

                        /// ダイアログ生成
                        UnitAction dialog = new UnitAction(context);
                        ///ダイアログに選択されたユニット情報を送る
                        dialog.setDialogUnitData(unitListA.selectMUnit(act_Munit_id));
                        /// /////////////////////////////////////////////////////////////////
                        /// ユニットメニュー開始
                        dialog.show(flagmentManager, "GAME_DIALOG");
                        /// ユニットメニュー終了
                        //////////////////////////////////////////////////////////////////
                        break;
                    }
                    /// //////////////////////////////////////////////////////////////////
                    /// 移動範囲表示中に移動位置がクリックされたら
                    if((mode==11)&&(mapwork[ty][tx])!=0){


                        Point endP= new Point(ty,tx);
                        Point startP=new Point(unitListA.selectMUnit(act_Munit_id).unitY,
                                unitListA.selectMUnit(act_Munit_id).unitX);
                        ///ArrayList<Point> line = moveLine(mapwork,endP,startP);

                        mode=0;
                        unitListA.selectMUnit(act_Munit_id).unitX=tx;
                        unitListA.selectMUnit(act_Munit_id).unitY=ty;

                    }
                    /////////////////////////////////////////////////////////////////////////////
                    /// リリース位置にユニットがない場合、スワイプ扱いで画面スクロール
                    if(uflag==1){
                        ndrag=(dd-drag);
                        uflag = 0;
                        if (Math.abs(ndrag)>20.0f) {
                            if(ndrag>0.0f){
                                if ((basex+2) < xmax) {
                                    basex+=2;
                                }
                            }else if(ndrag<0.0f) {
                                if ((basex-2) >= 0) {
                                    basex-=2;
                                }
                            }
                        }
                    }
                    break;
                /////////////////////////////////////////////////////////////////
                /// タッチのまま、移動の場合
                case (MotionEvent.ACTION_MOVE):
                    ///////////////////////////////////////////////////////
                    /// ドラッグ距離カウント
                    if(uflag==1){
                        drag = event.getX();
                    }
                    ///drag=10.0f;
                    break;
            }
        ///ndrag=drag;
        return true;
    }
    ///////////////////////////////////////////////////////////////

    void setflagmentManager( FragmentManager flagmentManager){
        this.flagmentManager=flagmentManager;
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
/// /////////////////////////////////////////////////////////////////
/// 移動可能先の検索（再帰）
///int[][] act_Munit_move(MUnit act_unit,int[][] workmap){
int[][] act_Munit_move(int y,int x,int mv,int[][] workmap){
    ///int move = act_unit.
    if((x<0)||(x>=xmax)||(y<0)||(y>=ymax)){
        return workmap;
    }
    if(mv==0){
        return workmap;
    }

    if(y%2==0){
     if(workmap[y][x]<mv){
         workmap[y][x] = mv;
     }
        ;
        //mv--;
        if((x-1>=0)&&(x-1<xmax)&&(y>=0)&&(y<ymax)){
            workmap=act_Munit_move(y,x-1,mv-1,workmap);
        }
        if((x+1>=0)&&(x+1<xmax)&&(y>=0)&&(y<ymax)){
            workmap=act_Munit_move(y,x+1,mv-1,workmap);
        }
        if((x>=0)&&(x<xmax)&&(y-1>=0)&&(y-1<ymax)){
            workmap=act_Munit_move(y-1,x,mv-1,workmap);
        }
        if((x+1>=0)&&(x+1<xmax)&&(y-1>=0)&&(y-1<ymax)){
            workmap=act_Munit_move(y-1,x+1,mv-1,workmap);
        }
        if((x>=0)&&(x<xmax)&&(y+1>=0)&&(y+1<ymax)){
            workmap=act_Munit_move(y+1,x,mv-1,workmap);
        }
        if((x+1>=0)&&(x+1<xmax)&&(y+1>=0)&&(y+1<ymax)){
            workmap=act_Munit_move(y+1,x+1,mv-1,workmap);
        }
    }else{
        if(workmap[y][x]<mv){
            workmap[y][x] = mv;
        }
        ///mv--;
        if((x-1>=0)&&(x-1<xmax)&&(y>=0)&&(y<ymax)){
            workmap=act_Munit_move(y,x-1,mv-1,workmap);
        }
        if((x+1>=0)&&(x+1<xmax)&&(y>=0)&&(y<ymax)){
            workmap=act_Munit_move(y,x+1,mv-1,workmap);
        }
        if((x-1>=0)&&(x-1<xmax)&&(y-1>=0)&&(y-1<ymax)){
            workmap=act_Munit_move(y-1,x-1,mv-1,workmap);
        }
        if((x>=0)&&(x<xmax)&&(y-1>=0)&&(y-1<ymax)){
            workmap=act_Munit_move(y-1,x,mv-1,workmap);
        }
        if((x-1>=0)&&(x-1<xmax)&&(y+1>=0)&&(y+1<ymax)){
            workmap=act_Munit_move(y+1,x-1,mv-1,workmap);
        }
        if((x>=0)&&(x<xmax)&&(y+1>=0)&&(y+1<ymax)){
            workmap=act_Munit_move(y+1,x,mv-1,workmap);
        }
    }
    return workmap;
}
/// ////////////////////////////////////////////////////////////////
/// ユニット移動軌跡を生成
ArrayList<Point> moveLine(int movework[][], Point startPoint, Point endPoint){
    ArrayList<Point> line = new ArrayList<Point>();     ///経路データ（逆順）
    line.add(startPoint);         ///目的地を経路にセット
    int x,y;                        ///基準位置
    int i = 0;                      ///
    Point maxPoint = new Point();   ///最大位置
    Point linework = line.get(0);   ///初期位置設定
    int pointMax=0;
///    x = line.get(i).x;
///    y = line.get(i).y;
    while(true){                    ///最大値を求めるループ
        x = line.get(i).x;
        y = line.get(i).y;
        pointMax = movework[y][x];
        if((linework.y)%2==0){
            if((x-1>=0)&&(x-1<xmax)&&(y>=0)&&(y<ymax)){
                if(pointMax<movework[linework.y][linework.x-1]){
                    pointMax=movework[linework.y][linework.x-1];
                    maxPoint.x = linework.x-1;
                    maxPoint.y = linework.y;
                }
            }
            if((x+1>=0)&&(x+1<xmax)&&(y>=0)&&(y<ymax)){
                if(pointMax<movework[linework.y][linework.x+1]){
                    pointMax=movework[linework.y][linework.x+1];
                    maxPoint.x = linework.x+1;
                    maxPoint.y = linework.y;
                }
            }
            if((x>=0)&&(x<xmax)&&(y-1>=0)&&(y-1<ymax)){
                if(pointMax<movework[linework.y-1][linework.x]){
                    pointMax=movework[linework.y-1][linework.x];
                    maxPoint.x = linework.x;
                    maxPoint.y = linework.y-1;
                }
            }
            if((x+1>=0)&&(x+1<xmax)&&(y-1>=0)&&(y-1<ymax)){
                if(pointMax<movework[linework.y-1][linework.x+1]){
                    pointMax=movework[linework.y-1][linework.x+1];
                    maxPoint.x = linework.x+1;
                    maxPoint.y = linework.y-1;
                }
            }
            if((x>=0)&&(x<xmax)&&(y+1>=0)&&(y+1<ymax)){
                if(pointMax<movework[linework.y+1][linework.x]){
                    pointMax=movework[linework.y+1][linework.x];
                    maxPoint.x = linework.x;
                    maxPoint.y = linework.y+1;
                }
            }
            if((x+1>=0)&&(x+1<xmax)&&(y+1>=0)&&(y+1<ymax)){
                if(pointMax<movework[linework.y+1][linework.x+1]){
                    pointMax=movework[linework.y+1][linework.x+1];
                    maxPoint.x = linework.x+1;
                    maxPoint.y = linework.y+1;
                }
            }
        }else{
            if((x-1>=0)&&(x-1<xmax)&&(y>=0)&&(y<ymax)){
                if(pointMax<movework[linework.y][linework.x-1]){
                    pointMax=movework[linework.y][linework.x-1];
                    maxPoint.x = linework.x-1;
                    maxPoint.y = linework.y;
                }
            }
            if((x+1>=0)&&(x+1<xmax)&&(y>=0)&&(y<ymax)){
                if(pointMax<movework[linework.y][linework.x+1]){
                    pointMax=movework[linework.y][linework.x+1];
                    maxPoint.x = linework.x+1;
                    maxPoint.y = linework.y;
                }
            }
            if((x-1>=0)&&(x-1<xmax)&&(y-1>=0)&&(y-1<ymax)){
                if(pointMax<movework[linework.y-1][linework.x-1]){
                    pointMax=movework[linework.y-1][linework.x-1];
                    maxPoint.x = linework.x-1;
                    maxPoint.y = linework.y-1;
                }
            }
            if((x>=0)&&(x<xmax)&&(y-1>=0)&&(y-1<ymax)){
                if(pointMax<movework[linework.y-1][linework.x]){
                    pointMax=movework[linework.y-1][linework.x];
                    maxPoint.x = linework.x;
                    maxPoint.y = linework.y-1;
                }
            }
            if((x-1>=0)&&(x-1<xmax)&&(y+1>=0)&&(y+1<ymax)){
                if(pointMax<movework[linework.y+1][linework.x-1]){
                    pointMax=movework[linework.y+1][linework.x-1];
                    maxPoint.x = linework.x-1;
                    maxPoint.y = linework.y+1;
                }
            }
            if((x>=0)&&(x<xmax)&&(y+1>=0)&&(y+1<ymax)){
                if(pointMax<movework[linework.y+1][linework.x]){
                    pointMax=movework[linework.y+1][linework.x];
                    maxPoint.x = linework.x;
                    maxPoint.y = linework.y+1;
                }
            }
        }
        i++;
        line.add(maxPoint);     ///最大値の位置をリストに追加

    ///初期位置に到達したら終了
    if((endPoint.x==maxPoint.x)&&(endPoint.y==maxPoint.y)){
        break;
    }
    /// そうでなければ、最大値の位置からコンティニュー
        linework.x = maxPoint.x;
        linework.y = maxPoint.y;
    }
    return line;
}




    ///////////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        while (true){

            try {
                thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if((mode==20)||(mode==30)){
                ///act_Munit=null;
                act_Munit_id=0;
                mode=0;
            }
            invalidate();
        }

    }
}

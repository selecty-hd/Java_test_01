package com.example.simgame;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
/////////////////////////////////////////////////////////////
/// ユニットのリスト
///     個々のユニットをまとめて扱う
/// /////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// ユニットリスト生成
public class UnitList {

/////////////////////////////////////////////////////////////////////////////////
///Context
    Context context;
    ////////////////////////////////////////////////////////////////////////////
    /// ユニット格納配列
    MUnit[] mUnit;
    //////////////////////////////////////////////////////////////////////////////
    ///
    MUnit dummyUnit;
    ArrayList<Integer> randtable;
    int buffer;
    Random random;
    /////////////////////////////////////////////////////////////////////////////
    /// ユニットリスト生成
    public UnitList(Context context)
    {
        random = new Random();
        randtable = new ArrayList<Integer>();       ///ユニット番号をArrayListに設定
        for(int i=0;i<10;i++){                      ///ユニット番号上位1桁
            for(int j=0;j<9;j++){                   ///ユニット番号下位1桁
               buffer=i*10+j;                       ///ユニット番号 00～99まで生成
                randtable.add(buffer);              ///ユニット番号 00～99をArrayListに登録
            }
        }
        Collections.shuffle(randtable);             ///ユニット番号をシャッフル

        dummyUnit= new MUnit();                     ///ユニットオブジェクト生成
        mUnit = new MUnit[28];                      ///ユニット配列を28ユニット生成
        for(int i=1;i<28;i++){                      ///ユニット配列を28ユニット初期化
            mUnit[i]=new MUnit();
            mUnit[i].setContext(context);
        }
        /// //////////////////////////////////////////////////
        /// ユニットIDを生成
        ArrayList<Integer> arwork;
        arwork = new ArrayList<Integer>();
        for(int i=1;i<28;i++){
            arwork.add(i);
        }
        Collections.shuffle(arwork);
        for(int i=1;i<28;i++){
            mUnit[i].dummyUnitID(arwork.get(i-1));
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////
    /// ダミーユニットデータを生成
    void dummyUnitSet(int xmax,int ymax){
        int x , y;
        for(int i=1;i<27;i++){                      ///1～27までユニットを生成
            mUnit[i].dummyUnitImageSet(i);          ///ユニット画像セット
            mUnit[i].dummyUnitNameSet(i);           ///ユニット名セット
            int table = randtable.get(i);           ///ユニット座標を仮に設定
///            int x = table/10;                       ///ユニットX座標（仮）
///            int y = table%10;                       ///ユニットY座標（仮）
            int flag=0;
            while(true){
                flag = 0;
                x = random.nextInt(xmax);                       ///ユニットX座標（仮）
                y = random.nextInt(ymax);                       ///ユニットY座標（仮）
                for(int j=0;j<i;j++) {
                    if((mUnit[i].unitX==x)&&(mUnit[i].unitY==y)){
                        flag=1;
                    }
                }
                if(flag==1){
                    continue;
                }else{
                    mUnit[i].dummyUnitPosisionSet(y,x);     ///ユニット座標セット
                    break;
                }
            }
        }
    }
    /// ///////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////
    ///ユニットIDを引数として、ユニットを返す
    MUnit selectMUnit(int id) {
        for(int i=1;i<27;i++){                      ///1～27までユニットを生成
            if (mUnit[i].unit_id == id) {
                return mUnit[i];
            }
        }
        return null;
    }

    /// ////////////////////////////////////////////////////////////////
}

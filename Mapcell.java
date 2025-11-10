package com.example.simgame;
///////////////////////////////////////////
/// 2025/11/10 未使用のマップ関連
/// ///////////////////////////////////////
class Mapcell{
    char up;
    char down;
    void setCell(char u,char d){
        up=u;
        down=d;
    }
    char getUp(){return up;}
    char getDown(){return down;}    
}
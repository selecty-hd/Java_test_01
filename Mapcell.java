package com.example.simgame;

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
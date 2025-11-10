package com.example.simgame;
/////////////////////////////////////////////////
/// マップ周りの設定
/// ////////////////////////////////////////////
public class Map {
    int map_Chip_file;
    int map_xsize;
    int map_ysize;
    int map_Data[][];
    void setMap_Chip_file(int num){
        map_Chip_file = num;
    }
    void set_mapsize(int xnum,int ynum){
        map_xsize = xnum;
        map_ysize = ynum;
        map_Data = new int[map_xsize][map_ysize];
    }

}

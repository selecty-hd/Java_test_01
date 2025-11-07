package com.example.simgame;

public class MapController {
    private int map[][];
    public void MapController(){
        map=new int[64][64];
        for (int i=0;i<64;i++){
            for(int j=0;j<64;j++){
                map[i][j]=0;
            }
        }
    }
}

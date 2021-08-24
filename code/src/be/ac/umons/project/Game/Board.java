package be.ac.umons.project.Game;

import be.ac.umons.project.Elements.Tile;

public class Board {

    private Tile[][] tileMatrix;
    private int tileSize;
    private int height;
    private int width;


    /***************************************************************
     *Constructor
     ***************************************************************/
    public Board(int _height, int _width, int _tileSize){
        height = _height;
        width = _width;
        tileSize = _tileSize;
        tileMatrix = new Tile[height][width];
    }

    ////////////////////////////////////////////////
    //GETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@return (int) tile size for this board
     ***************************************************************/
    public int getTileSize() {
        return tileSize;
    }

    /***************************************************************
     *@return (int) height of this board
     ***************************************************************/
    public int getHeight() {
        return height;
    }

    /***************************************************************
     *@return (int) width of this board
     ***************************************************************/
    public int getWidth() {
        return width;
    }

    /***************************************************************
     *@return (Tile[][]) tile matrix of this board
     ***************************************************************/
    public Tile[][] getTileMatrix(){
        return tileMatrix;
    }


    ////////////////////////////////////////////////
    //SETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@param tileSize (int) size for tiles of this board
     ***************************************************************/
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
}

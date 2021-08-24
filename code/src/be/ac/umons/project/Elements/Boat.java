package be.ac.umons.project.Elements;

import java.io.Serializable;
import java.util.ArrayList;


public class Boat implements Serializable {
    private int length;
    private Integer boatid  ;
    private String type;
    private boolean sunk;
    private int hitCounter;
    private ArrayList<int[]> positions;


    /***************************************************************
     *Constructor
     ***************************************************************/
    public Boat (int len,Integer id, ArrayList<int[]> pos) {
        boatid = id;
        length = len;
        type = determineType(len);
        sunk = false;
        hitCounter=0;
        positions = pos;
    }


    ////////////////////////////////////////////////
    //GETTERS
    ////////////////////////////////////////////////

    /***************************************************************
     *@return (int) number of hits of this boat
     ****************************************************************/
    public int getHitCounter() {
        return hitCounter;
    }

    /***************************************************************
     *@return (String) type of this boat
     ****************************************************************/
    public String getType() {
        return type;
    }

    /***************************************************************
     *@return (ArrayList<int[]>) containing positions the boat occupies
     ****************************************************************/
    public ArrayList<int[]> getPositions(){
        return positions;
    }

    /***************************************************************
     *@return (int) length of this boat
     ****************************************************************/
    public int getLength() {
        return length;
    }

    /***************************************************************
     *@return (Integer) id of this boat
     ****************************************************************/
    public Integer getBoatid() {
        return boatid;
    }

    /***************************************************************
     *@return (boolean) true if this boat has sunk
     ****************************************************************/
    public boolean isSunk() {
        return sunk;
    }


    ////////////////////////////////////////////////
    //SETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@param sunk (boolean) value to set to sunk variable of this boat
     ****************************************************************/
    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }

    /***************************************************************
     *@param positions (ArrayList<int[]>) value to set to positions variable of this boat
     ****************************************************************/
    public void setPositions(ArrayList<int[]> positions) {
        this.positions = positions;
    }

    /***************************************************************
     *@param hitCounter (int) number of hits to set to this boat
     ****************************************************************/
    public void setHitCounter(int hitCounter) {
        this.hitCounter = hitCounter;
    }



    ////////////////////////////////////////////////
    //METHODS
    ////////////////////////////////////////////////
    /***************************************************************
     *@param len (int) length of boat
     *@return (String) the type of Boat
     ***************************************************************/
    private String determineType ( int len){
        if (len == 1){
            //Mine
            return "M";
        }if (len == 2){
            //Destroyer
            return "D";
        }if (len == 3){
            //submarine
            return "S";
        }if (len == 4){
            //Cruiser
            return "C";
        }if (len == 5){
            //Battleship
            return "B";
        }else{
            //Carrier
            return "K";
        }
    }


    /***************************************************************
     *increments the hitCounter of this boat
     ***************************************************************/
    public void incrementHits(){
        hitCounter+=1;
    }


}






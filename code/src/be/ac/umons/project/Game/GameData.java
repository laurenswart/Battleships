package be.ac.umons.project.Game;

import be.ac.umons.project.Elements.Boat;
import be.ac.umons.project.Players.AiPlayer;
import java.util.ArrayList;


public class GameData {
    private Board board;
    private ArrayList<Boat> boatArray;
    private ArrayList<int[]> guessArray;
    private boolean aiPlaying;
    private Integer[][] matrix;
    private AiPlayer ai;
    private int currentX;
    private int currentY;
    private ArrayList<Object[]> feedbackList;

    /***************************************************************
     *Constructor
     ***************************************************************/
    public GameData(Board _board,ArrayList<Boat> _boats, boolean _ai){
        board = _board;
        boatArray = _boats;
        guessArray = new ArrayList<>();
        aiPlaying = _ai;
        matrix = new Integer[board.getHeight()][board.getWidth()];
        ai = null;
        feedbackList = new ArrayList<>();
    }


    ////////////////////////////////////////////////
    //GETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@return (ArrayList<Object[]>) the list of feedbacks
     ***************************************************************/
    public ArrayList<Object[]> getFeedbackList() {
        return feedbackList;
    }

    /***************************************************************
     *@return (Integer[][]) the GameData matrix
     ***************************************************************/
    public Integer[][] getMatrix() {
        return matrix;
    }

    /***************************************************************
     *@return (AiPlayer) aiPlayer of this gameData
     ***************************************************************/
    public AiPlayer getAi() {
        return ai;
    }

    /***************************************************************
     *@return (Board) board of this gameData
     ***************************************************************/
    public Board getBoard(){
        return board;
    }

    /***************************************************************
     *@return (ArrayList<Boat>) boatArray of this gameData
     ***************************************************************/
    public ArrayList<Boat> getBoatArray() {
        return boatArray;
    }

    /***************************************************************
     *@return (boolean) true if an ai is playing this game
     ***************************************************************/
    public boolean isAiPlaying() {
        return aiPlaying;
    }

    /***************************************************************
     *@return (int) x value of current guess
     ***************************************************************/
    public int getCurrentX() {
        return currentX;
    }

    /***************************************************************
     *@return (int) y value of current guess
     ***************************************************************/
    public int getCurrentY() {
        return currentY;
    }

    /***************************************************************
     *@return (ArrayList<int[]>) array of guesses of this GameData
     ***************************************************************/
    public ArrayList<int[]> getGuessArray() {
        return guessArray;
    }


    ////////////////////////////////////////////////
    //SETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@param ai (AiPlayer) to set to this GameData
     ***************************************************************/
    public void setAi(AiPlayer ai) {
        this.ai = ai;
    }

    /***************************************************************
     *@param aiPlaying (boolean) true if an ai is playing this game
     ***************************************************************/
    public void setAiPlaying(boolean aiPlaying) {
        this.aiPlaying = aiPlaying;
    }

    /***************************************************************
     *@param feedbackList (ArrayList<Object[]>) list of feedback to set to data
     ***************************************************************/
    public void setFeedbackList(ArrayList<Object[]> feedbackList) {

        this.feedbackList = feedbackList;
    }

    /***************************************************************
     *@param currentX (int) x value of current guess
     ***************************************************************/
    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    /***************************************************************
     *@param currentY (int) y value of current guess
     ***************************************************************/
    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    /***************************************************************
     *@param guessArray (ArrayList<int[]>) array to set as guessArray
     ***************************************************************/
    public void setGuessArray(ArrayList<int[]> guessArray) {
        this.guessArray = guessArray;
    }


    ////////////////////////////////////////////////
    //METHODS
    ////////////////////////////////////////////////

    /***************************************************************
     *@param x (int) x value of position to check for
     *@param y (int) y value of position to check for
     *@return (boolean) true if there is a boat around (x, y)
     ***************************************************************/
    public Boolean existsBoatAround(int x, int y){
        int h = board.getHeight();
        int w = board.getWidth();
        for (int i=y-1; i<y+2; i++){
            for (int j=x-1; j<x+2; j++){
                if ((0<=i && i<h)&&(0<=j && j<w)&&(this.getMatrix()[i][j]!=null)){
                    return true;
                }
            }
        }return false;

    }


    /***************************************************************
     *@param id (int) represents the iD of wanted boat
     *@return the boat object of that iD
     ***************************************************************/
    public Boat getBoat ( int id){
        for (int i = 0; i < boatArray.size(); i++) {
            if (boatArray.get(i).getBoatid() == id) {
                return boatArray.get(i);
            }
        }
        return null;
    }


    /***************************************************************
     * @param x(int) the x value of guess
     * @param y(int) the x value of guess
     * @return (boolean) whether this guess has previously been made
     ***************************************************************/
    public boolean alreadyGuessed (int x, int y) {
        for (int[] guess : guessArray) {
            if (guess[0] == x && guess[1] == y) {
                return true;
            }
        }
        return false;
    }



    /***************************************************************
     * @param x(int) the x value of guess
     * @param y(int) the x value of guess
     * @return (Object[]) feedback from the guess where
     *                  ans[0] = (int) x entered
     *                  ans[1] = (int) y entered
     *                  ans[2] = (boolean) true if a boat was hit
     *                  ans[3] = (boolean) true if a boat was sunk
     *                  ans[4] = (int) distance to the closest, then shortest boat
     *                  ans[5] = (int) the length of that boat
     ***************************************************************/
    public Object[] processGuess(int x, int y){
        //VALID GUESS
        GameData game = Game.getInstance().getGameData();
        int[] newGuess = {x, y};
        ArrayList<int[]> oldGuesses = game.getGuessArray();
        oldGuesses.add(newGuess);
        game.setGuessArray(oldGuesses);
        game.setCurrentX(x);
        game.setCurrentY(y);

        //GET CLOSEST BOAT POSITION AND DISTANCE TO IT
        int[] closest = getClosestOccupiedTile(x,y);

        //GET CLOSEST BOAT AND IT'S ID AND LENGTH
        int boatId = matrix[closest[1]][closest[0]];
        Boat b = getBoat(boatId);
        int length = b.getLength();
        boolean hit=false;
        boolean sunk = false;

        //HIT
        if (closest[2] == 0) {
            hit=true;
            b.incrementHits();
            //SUNK
            if(b.getHitCounter()==b.getLength()) {
                b.setSunk(true);
                sunk=true;
            }
        }
        Object[] ans = { x, y, hit, sunk, closest[2], length};
        feedbackList.add(ans);
        checkForEnd();
        return ans;
    }


    /***************************************************************
     *checks if all boats has sunk. If so, sets game.Ended to true;
     ***************************************************************/
    private void checkForEnd(){
        for(Boat b : boatArray){
            if (!b.isSunk()){
                return;
            }
        }
        Game.getInstance().setEnded(true);
    }


    /***************************************************************
     * @param x(int) the x value of guess
     * @param y(int) the x value of guess
     * @return (int[]) int[0] the x value of a closest occupied tile by boat not sunk,
     *                              the shortest boat if there where to be more than 1
     *                  int[1] the y value of a closest occupied tile by boat not sunk,
     *                              the shortest boat if there where to be more than 1
     *                  int[2] the distance between the guessed tile and closest occupied tile by boat not sunk
     ***************************************************************/
    private int[] getClosestOccupiedTile(int x, int y) {
        int[] ans = new int[3];
        for (int d = 0; d < (getBoard().getHeight()+ getBoard().getWidth()); d++) {
            Boat shortest = null;
            for (int row = y-d; row <= y+d; row++) {
                for (int col = x-d; col <= x+d; col++) {
                    if ((Math.abs(x - col) + Math.abs(y - row) == d)
                            &&(row>=0) && (row< getBoard().getHeight())
                            && (col>=0) && (col< getBoard().getWidth())
                            && (matrix[row][col] != null)) {
                        Integer id = getMatrix()[row][col];
                        if (((shortest==null  || (getBoat(id).getLength()<shortest.getLength())) && (!getBoat(id).isSunk()))){
                            shortest = getBoat(id);
                            ans[0] = col;
                            ans[1] = row;
                            ans[2] = d;
                        }
                    }
                }
            }
            if (shortest!=null){
                return ans;
            }
        }
        return null;
    }

}

    //METHOD THAT WAS IN PREVIOUS GAME TO RESET CONFIG
    /*
    public void reset() {
        if (this != null) {
            for (int y = 0; y < getBoard().getHeight(); y++) {
                for (int x = 0; x < getBoard().getWidth(); x++) {
                    //Set value held in the matrix location to null
                    matrix[y][x] = null;
                }
            }
            setCurrentX(-1);
            setCurrentY(-1);
            guessArray.clear();
            boatArray.clear();
            getBoard().setHeight(0);
            getBoard().setWidth(0);
            Game.getInstance().setEnded(false);
        }
    }

 */



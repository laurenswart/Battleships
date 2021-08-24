package be.ac.umons.project.Players;


import be.ac.umons.project.Elements.Boat;
import be.ac.umons.project.Game.Board;
import be.ac.umons.project.Game.Game;
import be.ac.umons.project.Game.GameData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class AiPlayer implements Serializable {

    private int[][] probabilities;

    /***************************************************************
     *@param game (Game) game the ai player is going to play
     ***************************************************************/
    public AiPlayer(GameData game){
        Board board = game.getBoard();
        probabilities = new int[board.getHeight()][board.getWidth()];
        for (int y = 0; y<probabilities.length; y++){
            for(int x = 0; x<probabilities[0].length; x++){
                probabilities[y][x]=0;
            }
        }
    }


    /***************************************************************
     *@return (int[]) the guess to make
     ***************************************************************/
    public int[] makeGuess() {
        ArrayList<int[]> guesses = bestGuesses();
        int range = guesses.size();
        Random rand = new Random();
        int index = rand.nextInt(range);
        int[] bestGuess = guesses.get(index);

        return bestGuess;
    }


    /***************************************************************
     *@return ( ArrayList<int[]>) list of guesses with highest probability
     ***************************************************************/
    private ArrayList<int[]> bestGuesses() {
        int max = -1;
        Board board = Game.getInstance().getGameData().getBoard();
        ArrayList<int[]> list = new ArrayList<>();
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if(probabilities[y][x] == max){
                    int[] guess ={x, y};
                    list.add(guess);
                }
                if (probabilities[y][x] > max) {
                    list.clear();
                    max = probabilities[y][x];
                    int[] guess ={x, y};
                    list.add(guess);
                }
            }
        }
        return list;
    }


    /***************************************************************
     * updates the configuration matrix with the feedback from guess
     *@param feed (int[]) int[0] int[1]
     ***************************************************************/
    public void processFeedback(Object[] feed) {
        int dist = (int) feed[4];
        int lg = (int) feed[5];
        GameData game = Game.getInstance().getGameData();
        int myX = game.getCurrentX();
        int myY = game.getCurrentY();

        //AVOID REDUNDANT GUESS
        probabilities[myY][myX] = -1;

        //IF HIT
        if(dist==0) {
            int id = game.getMatrix()[myY][myX];
            Boat b = game.getBoat(id);
            boolean sunk = b.isSunk();
            if ((sunk)) {
                updateSunkenBoat(b);
            }else {
                updateHit();
                updateCorners();
            }
        }

        //IN EVERY CASE, INCREMENT BY 1 SQUARES AT A DISTANCE D, WHICH DONT HAVE VALUE -1
        for (int j = 0; j< game.getBoard().getHeight(); j++) {
            for (int i = 0; i < game.getBoard().getWidth(); i++) {
                int norm = Math.abs(j-myY) + Math.abs(i-myX);
                if ((norm==dist) && probabilities[j][i]>=0){
                    probabilities[j][i]+=3;
                } else if (norm<dist){
                    probabilities[j][i]=-1;
                } else if((norm<dist+lg)&&(probabilities[j][i]>=0)){
                    probabilities[j][i]+=1;
                }
            }
        }
    }



    /***************************************************************
     * updates the configuration matrix by setting corners of a hit tile to -1
     ***************************************************************/
    private void updateCorners() {
        GameData game = Game.getInstance().getGameData();
        int x = game.getCurrentX();
        int y = game.getCurrentY();
        for (int j = y - 1; j <= y + 1; j++) {
            for (int i = x - 1; i <= x + 1; i++) {
                if ((j >= 0) && (i >= 0) && (j < game.getBoard().getHeight()) && (i < game.getBoard().getWidth())
                        && ((Math.abs(j - y) + Math.abs(i - x)) == 2)) {
                    probabilities[j][i] = -1;
                }
            }
        }
    }


    /***************************************************************
     * @param x (int) coordinate indicating column
     * @param y (int) coordinate indicating row
     * @param positions (ArrayList<int[]>) array of occupied positions by a certain boat
     * @return (boolean) true if (x, y) is touching a position in the arraylist
     ***************************************************************/
    private boolean areTouching(int x, int y, ArrayList<int[]> positions){
        for(int[] pos : positions){
            int norm = Math.abs(pos[0]-x)+Math.abs(pos[1]-y);
            if(norm==1){
                return true;
            }else if( (norm==2) && (pos[0]!=x) && (pos[1]!=y)){
                return true;
            }
        }
        return false;
    }


    /***************************************************************
     * updates the configuration matrix by setting all tiles touching boat b to -1
     * @param b (Boat) the boat that as sunk
     ***************************************************************/
    private void updateSunkenBoat(Boat b) {
        GameData game = Game.getInstance().getGameData();
        ArrayList<int[]> positions = b.getPositions();
        for (int j = 0; j < game.getBoard().getHeight(); j++) {
            for (int i = 0; i < game.getBoard().getWidth(); i++) {
                if (areTouching(i, j, positions)) {
                    probabilities[j][i] = -1;
                }
            }
        }
    }


    /***************************************************************
     * updates the configuration matrix by setting tiles above, beneath, left and right of hit to 3
     ***************************************************************/
    private void updateHit() {
        GameData game = Game.getInstance().getInstance().getGameData();
        for (int j = 0; j < game.getBoard().getHeight(); j++) {
            for (int i = 0; i < game.getBoard().getWidth(); i++) {
                int norm = Math.abs(j - game.getCurrentY()) + Math.abs(i - game.getCurrentX());
                if ((norm == 1) && (probabilities[j][i] >= 0)) {
                    probabilities[j][i] += 4;
                }
            }
        }
    }



}

package be.ac.umons.project.Control;

import be.ac.umons.project.Elements.Boat;
import be.ac.umons.project.Game.GameData;
import be.ac.umons.project.Players.AiPlayer;
import be.ac.umons.project.Scene.MyScene;
import java.io.Serializable;
import java.util.ArrayList;



public class SavedGame implements Serializable {
    //BOARD
    private int heightSaved;
    private int widthSaved;

    //GAME
    private ArrayList<int[]> guessesSaved;
    private ArrayList<Boat> boatArraySaved;
    private boolean playerIsAiSaved;
    private ArrayList<Object[]> feedbackListSaved;

    //PLAYER
    private AiPlayer aiPlayerSaved;

    //DISPLAY
    private String feedbackSaved;

    /***************************************************************
     *Constructor
     * @param data (GameData) gameData to save
     ***************************************************************/
    public SavedGame(GameData data){
        //BOARD
        heightSaved = data.getBoard().getHeight();
        widthSaved = data.getBoard().getWidth();

        //GAME
        guessesSaved = new ArrayList<>();
        for (int[] guess : data.getGuessArray()){
            guessesSaved.add(guess);
        }
        boatArraySaved = new ArrayList<>();
        for (Boat b : data.getBoatArray()){
            boatArraySaved.add(b);
        }
        feedbackListSaved = data.getFeedbackList();

        //PLAYER
        playerIsAiSaved = data.isAiPlaying();
        if(playerIsAiSaved) {
            aiPlayerSaved = data.getAi();
        }
        //DISPLAY
        feedbackSaved = MyScene.getInstance().feedbackDisplay.getValue();
    }

    ////////////////////////////////////////////////
    //GETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     * @return (ArrayList<Object[]>) feedback list that is saved
     ***************************************************************/
    public ArrayList<Object[]> getFeedbackListSaved() {
        return feedbackListSaved;
    }

    /***************************************************************
     * @return (int) height of savedGame board
     ***************************************************************/
    public int getHeightSaved() {
        return heightSaved;
    }

    /***************************************************************
     * @return (int) width of savedGame board
     ***************************************************************/
    public int getWidthSaved() {
        return widthSaved;
    }

    /***************************************************************
     * @return (ArrayList<int[]>) guess array of savedGame
     ***************************************************************/
    public ArrayList<int[]> getGuessesSaved() {
        return guessesSaved;
    }

    /***************************************************************
     * @return (ArrayList<Boat>) boat array of savedGame
     ***************************************************************/
    public ArrayList<Boat> getBoatArraySaved() {
        return boatArraySaved;
    }

    /***************************************************************
     * @return (boolean) true if savedGame had an ai playing
     ***************************************************************/
    public boolean isPlayerIsAi() {
        return playerIsAiSaved;
    }

    /***************************************************************
     * @return (AiPlayer) ai player of the savedGame
     ***************************************************************/
    public AiPlayer getAiPlayerSaved() {
        return aiPlayerSaved;
    }


}

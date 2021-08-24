package be.ac.umons.project.Control;

import be.ac.umons.project.Elements.Boat;
import be.ac.umons.project.Elements.Tile;
import be.ac.umons.project.Game.Game;
import be.ac.umons.project.Game.GameData;
import be.ac.umons.project.Players.AiPlayer;
import be.ac.umons.project.Scene.MyScene;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.ArrayList;


public class Controller {

    private static Controller ourInstance;
    private MyScene scene;
    private Game game;

    /***************************************************************
     *Constructor
     ***************************************************************/
    private Controller(){
        scene = MyScene.getInstance();
        game = Game.getInstance();

    }

    ////////////////////////////////////////////////
    //GETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     * @return (Controler) our single instance of controler
     ***************************************************************/
    public static Controller getInstance(){
        if (ourInstance==null){
            ourInstance = new Controller();
        }
        return ourInstance;
    }

    /***************************************************************
     * @return (MyScene) scene of this controller
     ***************************************************************/
    public  MyScene getScene(){
        return scene;
    }


    /***************************************************************
     * @return (Game) the controler's game
     ***************************************************************/
    public Game getGame() {
        return game;
    }



    ////////////////////////////////////////////////
    //METHODS
    ////////////////////////////////////////////////

    /***************************************************************
     * checks if settings entered by user for custom gam are correct and do-able
     * @param lengthInput (TextField) length of board desired by user
     * @param heightInput (TextField) width of board desired by user
     * @param l1Input (TextField) number of boats of length 1 desired by user
     * @param l2Input (TextField) number of boats of length 2 desired by user
     * @param l3Input (TextField) number of boats of length 3 desired by user
     * @param l4Input (TextField) number of boats of length 4 desired by user
     * @param l5Input (TextField) number of boats of length 5 desired by user
     * @param l6Input (TextField) number of boats of length 6 desired by user
     * @return (boolean) true if entered settings are do-able
     ***************************************************************/
    public boolean customGameButtonPressed(TextField lengthInput, TextField heightInput,
                                           TextField l1Input, TextField l2Input, TextField l3Input,
                                           TextField l4Input, TextField l5Input, TextField l6Input) {

        if (lengthInput.getText().isEmpty() || heightInput.getText().isEmpty()
                || l1Input.getText().isEmpty() || l2Input.getText().isEmpty()
                || l3Input.getText().isEmpty() || l4Input.getText().isEmpty()
                || l5Input.getText().isEmpty() || l6Input.getText().isEmpty()) {
            MyScene.getInstance().feedbackDisplay.setValue
                    ("You must enter a number higher or equal to 0 in each box");
            return false;
        }

        //GET INPUTS
        int length = Integer.parseInt(lengthInput.getText());
        int height = Integer.parseInt(heightInput.getText());
        int l1 = Integer.parseInt(l1Input.getText());
        int l2 = Integer.parseInt(l2Input.getText());
        int l3 = Integer.parseInt(l3Input.getText());
        int l4 = Integer.parseInt(l4Input.getText());
        int l5 = Integer.parseInt(l5Input.getText());
        int l6 = Integer.parseInt(l6Input.getText());

        //GRID SIZE CHECK
        if ((length > 26) || (height > 26) || (length == 0) || (height == 0)) {
            MyScene.getInstance().feedbackDisplay.setValue
                    ("The height and width must each be lower or equal to 26, and greater than 0");
            return false;
        }

        //VERY POOR EFFORT TO GET LONGEST BOAT
        int biggestBoatLength = 0;
        if (l6 != 0) {
            biggestBoatLength = 6;
        } else if (l5 != 0) {
            biggestBoatLength = 5;
        } else if (l4 != 0) {
            biggestBoatLength = 4;
        } else if (l3 != 0) {
            biggestBoatLength = 3;
        } else if (l2 != 0) {
            biggestBoatLength = 2;
        } else if (l1 != 0) {
            biggestBoatLength = 1;
        }

        //CHECK IF THERE IS AT LEAST ONE BOAT, AND IF THEY FIT IN THE GRID
        int spaceNeeded = l1 * 8 + l2 * 10 + l3 * 12 + l4 * 14 + l5 * 16 + l6 * 18;
        if ((spaceNeeded <= length * height) && (spaceNeeded > 0) &&
                (biggestBoatLength <= length) && (biggestBoatLength <= height)) {
            return true;
        } else {
            MyScene.getInstance().feedbackDisplay.setValue("The settings you entered are impossible");
            return false;
        }
    }



    /***************************************************************
     * sets the gameData aiplaying to false
     ***************************************************************/
    public void humanButtonPressed(){
        Game.getInstance().getGameData().setAiPlaying(false);
    }


    /***************************************************************
     * sets the gameData aiplaying to true, and creates an ai player for the game
     ***************************************************************/
    public void aiPlayerButtonPressed(){
        GameData game = Game.getInstance().getGameData();
        game.setAiPlaying(true);
        game.setAi(new AiPlayer(game));
    }


    /***************************************************************
     *resets game info for user to replay the same game
     ***************************************************************/
    public void replayButtonPressed(){
        GameData data = Game.getInstance().getGameData();
        data.setGuessArray(new ArrayList<>());
        data.setCurrentX(-1);
        data.setCurrentY(-1);

        for (Boat b: data.getBoatArray()){
            b.setHitCounter(0);
            b.setSunk(false);
        }
        Game.getInstance().setEnded(false);
        MyScene.getInstance().getSceneManager().setupBoard();
        MyScene.getInstance().getSceneManager().replaySameGame();
    }


    /***************************************************************
     *displays all boats on board
     ***************************************************************/
    public void cheatModeOn() {
        for (Tile[] row : Game.getInstance().getGameData().getBoard().getTileMatrix()) {
            for (Tile t : row) {
                t.boat.setVisible(true);
            }
        }
    }


    /***************************************************************
     *hides boat tiles that have not yet been guessed
     ***************************************************************/
    public void cheatModeOff() {
        for (Tile[] row : Game.getInstance().getGameData().getBoard().getTileMatrix()) {
            for (Tile t : row) {
                if (!t.isHit()) {
                    t.boat.setVisible(false);
                }
            }
        }
    }


    /***************************************************************
     * Retrieves x et y entries and calls for check
     * @param xEntry (TextField) text entered by user for x
     * @param yEntry (TextField) text entered by user for y
     * @return (boolean) if the guess is valid
     ***************************************************************/
    public boolean guessButtonPressed(TextField xEntry, TextField yEntry) {
        String x = xEntry.getText();
        String y = yEntry.getText();
        if ((x.length()==0)||(y.length()==0)){
            MyScene.getInstance().feedbackDisplay.setValue("Guess not processed : You must enter a value in each box");
            return false;
        }
        return true;
    }


    /***************************************************************
     *calls for scene display to be reset for a new game
     ***************************************************************/
    public void newGameButtonPressed(){
        MyScene.getInstance().getSceneManager().resetDisplay();
    }

    /***************************************************************
     * called when ai needs to make a guess
     *return Object[]) feedback from the guess where
     *              ans[0] = (int) x entered
     *              ans[1] = (int) y entered
     *              ans[2] = (boolean) true if a boat was hit
     *              ans[3] = (boolean) true if a boat was sunk
     *              ans[4] = (int) distance to the closest, then shortest boat
     *              ans[5] = (int) the length of that boat
     ***************************************************************/
    public Object[] aiGuessButtonPressed(){
        //GameData game = Game.getInstance().getGameData();
        int[] guess = game.getGameData().getAi().makeGuess();
        Object[] feedback = game.getGameData().processGuess(guess[0], guess[1]);
        MyScene.getInstance().getSceneManager().validGuessUpdate(feedback);
        game.getGameData().getAi().processFeedback(feedback);
        return feedback;
    }


    /***************************************************************
     *called when ai needs to make guesses till end of game.
     *when end of game is reached, call for sceneManager to end the game
     ***************************************************************/
    public void aiEndGameButtonPressed(){
        while(!Game.getInstance().isEnded()){
            aiGuessButtonPressed();
        }
        MyScene.getInstance().getSceneManager().endTheGame();
    }


    /***************************************************************
     *allows user to select map to import, and sets the game up to play it
     * @throws Exception
     ***************************************************************/
    public void loadMapButtonPressed()throws Exception{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File file = fileChooser.showOpenDialog(null);

            //CREATE A LOADED MAP
            LoadedMap map = new LoadedMap(file);

            //CALL INITIALISER
            Game.getInstance().getInit().loadMapGame(map);

    }


    /***************************************************************
     *allows user to select file to save current game to, and saves game to it
     * @throws Exception
     ***************************************************************/
    public void saveGameButtonPressed() throws Exception {
        SavedGame game = new SavedGame(Game.getInstance().getGameData());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName("SavedBattle");

        File file = fileChooser.showSaveDialog(null);
        String path = file.getAbsolutePath();
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(game);
        objectOut.close();
    }


    /***************************************************************
     *allows user to select file containing game to load, and loads it
     * @throws Exception
     ***************************************************************/
    public void loadSavedGameButtonPressed()throws Exception{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(null);

        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream input = new ObjectInputStream(fileInputStream);
        SavedGame game = (SavedGame) input.readObject();
        input.close();

        Game.getInstance().getInit().loadSavedGame(game);

    }



}

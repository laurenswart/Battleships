package be.ac.umons.project.Scene;

import be.ac.umons.project.Control.Controller;
import be.ac.umons.project.Game.Board;
import be.ac.umons.project.Elements.Boat;
import be.ac.umons.project.Elements.Tile;
import be.ac.umons.project.Game.Game;
import be.ac.umons.project.Game.GameData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

import static javafx.scene.paint.Color.*;

public class SceneManager {

    /***************************************************************
     *Constructor
     ***************************************************************/
    public SceneManager (){
    }

    /***************************************************************
     *sets board tile size, setups tiles, background, and updates GUI
     ***************************************************************/
    public void setupBoard(){
        Board board = Game.getInstance().getGameData().getBoard();
        int h = 500/board.getHeight()+1;
        int w = 500/board.getWidth()+1;
        int value = Math.min(h, w);
        board.setTileSize(value);

        setupTiles();
        setupBackground();
        MyScene.getInstance().heightDisplay.setValue(Game.getInstance().getGameData().getBoard().getHeight());
        MyScene.getInstance().lengthDisplay.setValue(Game.getInstance().getGameData().getBoard().getWidth());
        MyScene.getInstance().nBoatsDisplay.setValue(Game.getInstance().getGameData().getBoatArray().size());
        MyScene.getInstance().mainBorderpane.setCenter(MyScene.getInstance().createCenterBox());
    }


    /***************************************************************
     * creates tiles for board tileMatrix and Scene
     ***************************************************************/
    private void setupTiles() {
        Pane pane = new Pane();
        Color tileColor =Color.DARKCYAN;
        GameData game = Game.getInstance().getGameData();
        int tileSize = game.getBoard().getTileSize();

        for (int y = 0; y < game.getBoard().getHeight(); y++) {
            for (int x = 0; x < game.getBoard().getWidth(); x++) {
                String s = boatLabel(x, y);
                Tile tile = new Tile( x, y,tileSize,tileColor,s );
                game.getBoard().getTileMatrix()[y][x]=tile;
            }
        }
        for (int y = 0; y < game.getBoard().getHeight(); y++) {
            for (int x = 0; x < game.getBoard().getWidth(); x++) {
                Tile tile = game.getBoard().getTileMatrix()[y][x];
                pane.getChildren().add(tile);
            }
        }
        //Reposition tile pane to show background pane
        pane.relocate(tileSize,tileSize);

        MyScene.getInstance().tilesPane = pane;
    }


    /***************************************************************
     * @param x (int) column index of tile
     * @param y (int) row index of tile
     * @return (String) to place on tile to display boatType
     ***************************************************************/
    private String boatLabel(int x, int y) {
        GameData game = Game.getInstance().getGameData();
        Integer id = game.getMatrix()[y][x];
        String ourValue = "";
        if (id != null) {
            Boat b = game.getBoat(id);
            ourValue = b.getType();
        }
        return ourValue;
    }


    /***************************************************************
     * creates tiles for Scene background
     ***************************************************************/
    private void setupBackground() {
        Board board = Game.getInstance().getGameData().getBoard();
        int tileSize = board.getTileSize();
        Pane pane = new Pane();
        Color tileColor = Color.LIGHTGRAY;;

        int col = 0;
        for (int y = 0; y < board.getHeight()+1; y++) {
            String c = backgroundLabel(col,y);
            Tile tile = new Tile(col, y, tileSize,tileColor, c);
            tile.boat.setVisible(true);
            pane.getChildren().add(tile);
        }
        int row = 0;
        for (int x = 1 ; x < board.getWidth()+1; x++) {
            String c = backgroundLabel(x,row);
            Tile tile = new Tile(x, row, tileSize,tileColor, c);
            tile.boat.setVisible(true);
            pane.getChildren().add(tile);
        }
        MyScene.getInstance().backgroundPane = pane;
    }


    /***************************************************************
     * @param x (int) column index of a background tile
     * @param y (int) row index of a background tile
     * @return (String) to place on the background tile
     ***************************************************************/
    private String backgroundLabel(int x, int y) {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String value= "";
        if ((y==0)&&(x!=0)){
            value = Integer.toString(x-1);
        }else if((x==0)&&(y!=0)){
            Character b = alpha.charAt(y-1);
            value = Character.toString(b);
        }
        return value;
    }


    /***************************************************************
     *updates visibilities after user has submitted correct custom game info
     ***************************************************************/
    public void customGameSubmitted(){
        MyScene.getInstance().customGrid.setDisable(true);
        MyScene.getInstance().playerBox.setDisable(false);
    }


    /***************************************************************
     *updates visibilities after user has chosen to play the game
     ***************************************************************/
    public void humanPlayerChosen(){
        MyScene.getInstance().playerBox.setDisable(true);
        MyScene.getInstance().guessGrid.setDisable(false);
        MyScene.getInstance().endGameBox.setDisable(false);
        MyScene.getInstance().gameBox.setVisible(true);
        MyScene.getInstance().cheatPane.setDisable(false);
    }


    /***************************************************************
     *updates visibilities after user has chosen ai player
     ***************************************************************/
    public void aiPlayerChosen(){
        MyScene.getInstance().playerBox.setDisable(true);
        MyScene.getInstance().aiBox.setDisable(false);
        MyScene.getInstance().endGameBox.setDisable(false);
        MyScene.getInstance().cheatPane.setDisable(false);
    }


    /***************************************************************
     *updates visibilities and displays to the opening scene
     ***************************************************************/
    public void resetDisplay() {
        MyScene.getInstance().heightDisplay.set(0);
        MyScene.getInstance().lengthDisplay.set(0);
        MyScene.getInstance().nBoatsDisplay.set(0);
        MyScene.getInstance().feedbackDisplay.set(null);
        MyScene.getInstance().xDisplay.setValue(null);
        MyScene.getInstance().yDisplay.setValue(null);
        MyScene.getInstance().tilesPane.setVisible(false);
        MyScene.getInstance().backgroundPane.setVisible(false);
        MyScene.getInstance().gameBox.getChildren().removeAll();
        MyScene.getInstance().nGuessesDisplay.set(0);
        MyScene.getInstance().nHitsDisplay.set(0);
        MyScene.getInstance().nSinksDisplay.set(0);
        MyScene.getInstance().cheatButton.setSelected(false);

        MyScene.getInstance().l1Input.setText("0");
        MyScene.getInstance().l2Input.setText("0");
        MyScene.getInstance().l3Input.setText("0");
        MyScene.getInstance().l4Input.setText("0");
        MyScene.getInstance().l5Input.setText("0");
        MyScene.getInstance().l6Input.setText("0");
        MyScene.getInstance().lengthInput.setText("0");
        MyScene.getInstance().heightInput.setText("0");

        MyScene.getInstance().gameTypeGrid.setDisable(false);
        MyScene.getInstance().customGrid.setDisable(true);
        MyScene.getInstance().playerBox.setDisable(true);
        MyScene.getInstance().guessGrid.setDisable(true);
        MyScene.getInstance().aiBox.setDisable(true);
        MyScene.getInstance().endGameBox.setDisable(true);
        MyScene.getInstance().gameBox.setVisible(false);
        MyScene.getInstance().cheatPane.setDisable(true);
        MyScene.getInstance().butLoadGame.setDisable(true);
        MyScene.getInstance().butLoadMap.setDisable(true);
        enableToggles();
        deselectToggles();

        MyScene.getInstance().gameBox.getChildren().clear();
    }


    /***************************************************************
     *deselects  all toggles in scene
     ***************************************************************/
    public void disableToggles(){
        for(Toggle t : MyScene.getInstance().tg.getToggles()){
            Node n = (Node) t;
            n.setDisable(true);
        }
    }


    /***************************************************************
     *enables  all toggles in scene
     ***************************************************************/
    private void enableToggles(){
        for(Toggle t : MyScene.getInstance().tg.getToggles()){
            Node n = (Node) t;
            n.setDisable(false);
        }
    }


    /***************************************************************
     *deselects  all toggles in scene
     ***************************************************************/
    private void deselectToggles(){
        for(Toggle t : MyScene.getInstance().tg.getToggles()){
            t.setSelected(false);
        }
    }


    /***************************************************************
     *updates the scene after a valid guess has been processed
     *@param feedback (Object[]) feedback received from the game checkGuess
     ***************************************************************/
    public void validGuessUpdate(Object[] feedback){
        int x = (int) feedback[0];
        int y = (int) feedback[1];
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        MyScene.getInstance().xDisplay.set(x);
        MyScene.getInstance().yDisplay.set(Character.toString(alpha.charAt(y)));
        MyScene.getInstance().xEntry.clear();
        MyScene.getInstance().yEntry.clear();
        MyScene.getInstance().nGuessesDisplay.set(Game.getInstance().getGameData().getGuessArray().size());

        boolean hit = (boolean)feedback[2];
        boolean sunk = (boolean)feedback[3];
        int distance = (int)feedback[4];
        int length = (int)feedback[5];
        Tile tile = Game.getInstance().getGameData().getBoard().getTileMatrix()[y][x];
        tile.setHit(true);
        String rsp1 = "";
        //HIT
        if (hit){

            tile.getBoat().setVisible(true);
            int nHits = MyScene.getInstance().nHitsDisplay.getValue();
            int updatedHits = nHits+1;
            MyScene.getInstance().nHitsDisplay.set(updatedHits);
            if(sunk){
                int nSinks = MyScene.getInstance().nSinksDisplay.getValue();
                int updatedSinks = nSinks+1;
                MyScene.getInstance().nSinksDisplay.set(updatedSinks);
                displaySunkenBoat(x, y);
                rsp1 = "HIT AND SUNK!!!";
            }else{
                tile.background.setFill(RED);
                rsp1 = "HIT!!!";
            }
            String msg = rsp1 + " The boat you hit is of length " + length;
            MyScene.getInstance().feedbackDisplay.setValue(msg);

        //MISS
        }else{
            tile.background.setFill(BLACK);
            String msg = "MISS..  The closest boat is "+ distance + " step(s) away and of length " + length;
            MyScene.getInstance().feedbackDisplay.setValue(msg);
        }
    }


    /***************************************************************
     *updates the scene feedback after an invalid guess
     *@param msg (String) message of the error
     ***************************************************************/
    public void invalidGuessUpdate(String msg) {
        String yAlpha = MyScene.getInstance().yEntry.getText().toUpperCase();
        MyScene.getInstance().xDisplay.set(Integer.parseInt(MyScene.getInstance().xEntry.getText()));
        MyScene.getInstance().yDisplay.set(yAlpha);
        MyScene.getInstance().feedbackDisplay.setValue(msg);
    }


    /***************************************************************
     *updates the scene after a boat has sunk
     *@param x (int) x position of the boat that has sunk
     *@param y (int) y position of the boat that has sunk
     ***************************************************************/
    private void displaySunkenBoat(int x, int y){
        GameData game = Game.getInstance().getGameData();
        int id = game.getMatrix()[y][x];
        Boat b = Game.getInstance().getGameData().getBoat(id);
        for (int[] pos : b.getPositions()){
            int j = pos[0];
            int i = pos[1];
            game.getBoard().getTileMatrix()[i][j].background.setFill(GREEN);
        }
    }


    /***************************************************************
     *updates the scene visibilities and call for the popup window to be created
     ***************************************************************/
    public void endTheGame(){
        MyScene.getInstance().gameTypeGrid.setDisable(true);
        MyScene.getInstance().customGrid.setDisable(true);
        MyScene.getInstance().playerBox.setDisable(true);
        MyScene.getInstance().gameSettingsGrid.setDisable(false);
        MyScene.getInstance().guessGrid.setDisable(true);
        MyScene.getInstance().aiBox.setDisable(true);
        MyScene.getInstance().endGameBox.setDisable(true);
        MyScene.getInstance().gameBox.setVisible(true);
        MyScene.getInstance().cheatPane.setDisable(true);
        MyScene.getInstance().scoreGrid.setDisable(false);
        displayEndWindow();
    }


    /***************************************************************
     *updates the scene once saved game has properly loaded
     ***************************************************************/
    public void gameLoaded(){
        GameData data = Game.getInstance().getGameData();
        //BOARD
        int nHits = 0;
        int nSinks = 0;
        for(Boat b : data.getBoatArray()){
            if(b.isSunk()){
                nSinks+=1;
            }
        }
        for(int[] guess : data.getGuessArray()){
            Tile t = data.getBoard().getTileMatrix()[guess[1]][guess[0]];
            Integer id = data.getMatrix()[guess[1]][guess[0]];
            if (id!=null){
                nHits+=1;
                t.getBoat().setVisible(true);
                Boat b = data.getBoat(id);
                if(b.isSunk()){
                    t.background.setFill(GREEN);
                }else{
                    t.background.setFill(RED);
                }
            }else{
                t.background.setFill(BLACK);
            }
        }

        //DISPLAYS
        MyScene.getInstance().nGuessesDisplay.setValue(data.getGuessArray().size());
        MyScene.getInstance().nHitsDisplay.setValue(nHits);
        MyScene.getInstance().nSinksDisplay.setValue(nSinks);
        if(data.getGuessArray().size()>0) {
            int[] guess = data.getGuessArray().get(data.getGuessArray().size() - 1);
            MyScene.getInstance().xDisplay.setValue(guess[0]);
            String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            MyScene.getInstance().yDisplay.setValue(Character.toString(alpha.charAt(guess[1])));
            ArrayList<Object[]> array = Game.getInstance().getGameData().getFeedbackList();
            Object[] feedback= array.get(array.size()-1);

            //HIT
            String rsp1 = "";
            if((boolean)feedback[2]) {
                //SUNK
                if ((boolean) feedback[3]) {
                    rsp1 = "HIT AND SUNK!!!";
                }else{
                    rsp1 = "HIT!!!";
                }
                String msg = rsp1 + " The boat you hit is of length " + feedback[5];
                MyScene.getInstance().feedbackDisplay.setValue(msg);
            }
            //MISS
            String msg = "MISS..  The closest boat is "+ feedback[4] + " step(s) away and of length " + feedback[5];
            MyScene.getInstance().feedbackDisplay.setValue(msg);
        }

        //VISIBILITIES
        MyScene.getInstance().endGameBox.setDisable(false);
        MyScene.getInstance().cheatPane.setDisable(false);
        if (Game.getInstance().getGameData().isAiPlaying()) {
        MyScene.getInstance().aiBox.setDisable(false);
        }else {
            MyScene.getInstance().guessGrid.setDisable(false);
        }
    }


    /***************************************************************
     *creates the end of game pop up window
     ***************************************************************/
    public void displayEndWindow() {

        Stage window = new Stage();
        window.setTitle("End of game");

        Label gameWon = new Label("GAME WON");
        gameWon.setFont(Font.font("Verdana", 40));
        Label score = new Label("Your score : " + Game.getInstance().getGameData().getGuessArray().size());
        //Label performance = new Label ("Your performance : ");

        //BUTTONS
        Button replaybtn = new Button("Replay Game");
        replaybtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) replaybtn.getScene().getWindow();
                stage.close();
                Controller.getInstance().replayButtonPressed();
            }
        });
        Button newGamebtn = new Button("New Game");
        newGamebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) newGamebtn.getScene().getWindow();
                stage.close();
                Controller.getInstance().newGameButtonPressed();
            }
        });
        Button exitbtn = new Button("Exit Game");
        exitbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage miniStage = (Stage) newGamebtn.getScene().getWindow();
                miniStage.close();
                Stage maxiStage = (Stage) MyScene.getInstance().mainBorderpane.getScene().getWindow();
                maxiStage.close();
            }
        });

        //FILL UP BOXES
        HBox btnBox = new HBox();
        btnBox.setSpacing(30);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(replaybtn, newGamebtn, exitbtn);

        VBox layout = new VBox();
        layout.setSpacing(30);
        layout.getChildren().addAll(gameWon, score, btnBox);
        layout.setAlignment(Pos.CENTER);

        Scene scn = new Scene(layout, 350, 250);
        window.setScene(scn);
        window.showAndWait();
    }


    /***************************************************************
     *updates scene visibilities once user has decided to replay game
     ***************************************************************/
    public void replaySameGame(){
        MyScene.getInstance().cheatButton.setSelected(false);
        MyScene.getInstance().gameTypeGrid.setDisable(true);
        MyScene.getInstance().playerBox.setDisable(false);
        MyScene.getInstance().xDisplay.setValue(null);
        MyScene.getInstance().yDisplay.setValue(null);
        MyScene.getInstance().feedbackDisplay.setValue(null);
        MyScene.getInstance().nGuessesDisplay.setValue(0);
        MyScene.getInstance().nHitsDisplay.setValue(0);
        MyScene.getInstance().nSinksDisplay.setValue(0);
    }


}


package be.ac.umons.project.Scene;

import be.ac.umons.project.Control.BadMapException;
import be.ac.umons.project.Control.Controller;
import be.ac.umons.project.Control.ReguessException;
import be.ac.umons.project.Game.Game;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;


public class MyScene {
    private static MyScene ourInstance;

    private SceneManager sceneManager;
    public BorderPane mainBorderpane;

    //LEFT BOX NODES
    public GridPane gameTypeGrid;
    public ToggleGroup tg;
    public Button butLoadGame;
    public Button butLoadMap;

    public GridPane customGrid;
    public TextField l1Input;
    public TextField l2Input;
    public TextField l3Input;
    public TextField l4Input;
    public TextField l5Input;
    public TextField l6Input;
    public TextField lengthInput;
    public TextField heightInput;

    public HBox playerBox;

    public GridPane cheatPane;
    public CheckBox cheatButton;

    //RIGHT BOX NODES
    public GridPane gameSettingsGrid;
    public ObjectProperty<Integer> nBoatsDisplay = new SimpleObjectProperty<>();
    public ObjectProperty<Integer> lengthDisplay = new SimpleObjectProperty<>();
    public ObjectProperty<Integer> heightDisplay = new SimpleObjectProperty<>();

    public GridPane guessGrid;
    public TextField xEntry;
    public TextField yEntry;

    public HBox aiBox;

    public HBox endGameBox;

    public GridPane scoreGrid = new GridPane();
    public ObjectProperty<Integer> nGuessesDisplay = new SimpleObjectProperty<>();
    public ObjectProperty<Integer> nHitsDisplay = new SimpleObjectProperty<>();
    public ObjectProperty<Integer> nSinksDisplay = new SimpleObjectProperty<>();

    public GridPane boatsBox = new GridPane();

    //BOTTOM BOX NODES
    public ObjectProperty<Integer> xDisplay = new SimpleObjectProperty<>();
    public ObjectProperty<String> yDisplay = new SimpleObjectProperty<>();
    public ObjectProperty<String> feedbackDisplay = new SimpleObjectProperty<>();

    //CENTER BOX
    public Pane gameBox;
    public Pane backgroundPane;
    public Pane tilesPane;

    /***************************************************************
     *Constructor
     ***************************************************************/
    private MyScene() {
        sceneManager = new SceneManager();
        mainBorderpane = new BorderPane();
        mainBorderpane.setLeft(createLeftBox());
        mainBorderpane.setRight(createRightBox());
        mainBorderpane.setBottom(createBottomBox());
    }

    /***************************************************************
     *@return (SceneManager) the scene's manager
     ***************************************************************/
    public SceneManager getSceneManager() {
        return sceneManager;
    }


    /***************************************************************
     *@return (MyScene) the single instance of MyScene
     ***************************************************************/
    public static MyScene getInstance() {
        if (ourInstance==null){
            ourInstance=new MyScene();
        }
        return ourInstance;
    }


    /***************************************************************
     *@return (VBox) the left box to position in Borderpane
     ***************************************************************/
    private VBox createLeftBox(){
        VBox leftBox = new VBox();
        leftBox.setPadding(new Insets(5, 5, 5, 5));
        leftBox.setSpacing(5);

        //GAMETYPEBOX***************************************
        gameTypeGrid = new GridPane();
        gameTypeGrid.setPadding(new Insets(5, 5, 5, 5));
        gameTypeGrid.setMinSize(200, 140);
        gameTypeGrid.setVgap(5);
        gameTypeGrid.setStyle("-fx-background-color: #99CCFF;");

        //TOGGLE GROUP
        tg = new ToggleGroup();

        RadioButton defaultGame = new RadioButton("Default Game");
        defaultGame.setToggleGroup(tg);
        defaultGame.setSelected(false);

        RadioButton customGame = new RadioButton("New Custom Game");
        customGame.setToggleGroup(tg);

        RadioButton importMapGame = new RadioButton("Import Map");
        importMapGame.setToggleGroup(tg);
        importMapGame.setSelected(false);

        RadioButton loadSavedGame = new RadioButton("Load Saved game");
        loadSavedGame.setToggleGroup(tg);
        loadSavedGame.setSelected(false);



        //EVENTHANDLERS FOR TOGGLE
        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (tg.getSelectedToggle() != null) {
                    if (tg.getSelectedToggle() == defaultGame) {
                        gameTypeGrid.setDisable(true);
                        customGrid.setDisable(true);
                        butLoadMap.setDisable(true);
                        butLoadGame.setDisable(true);
                        playerBox.setDisable(false);
                        sceneManager.disableToggles();
                        Game.getInstance().getInit().defaultGame();
                        sceneManager.setupBoard();

                    } else if (tg.getSelectedToggle() == customGame) {
                        customGrid.setDisable(false);
                        butLoadMap.setDisable(true);
                        butLoadGame.setDisable(true);
                        sceneManager.disableToggles();
                        gameTypeGrid.setDisable(true);

                    } else if (tg.getSelectedToggle() == importMapGame) {
                        customGrid.setDisable(true);
                        butLoadMap.setDisable(false);
                        butLoadGame.setDisable(true);

                    } else if (tg.getSelectedToggle() == loadSavedGame) {
                        customGrid.setDisable(true);
                        butLoadMap.setDisable(true);
                        butLoadGame.setDisable(false);
                    }
                }
            }
        });

        //LOAD SAVED GAME BUTTON
        butLoadGame = new Button("Choose File");
        butLoadGame.setDisable(true);
        butLoadGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Controller.getInstance().loadSavedGameButtonPressed();
                    MyScene.getInstance().getSceneManager().setupBoard();
                    MyScene.getInstance().gameTypeGrid.setDisable(true);
                    MyScene.getInstance().playerBox.setDisable(true);
                    sceneManager.gameLoaded();
                }catch (Exception e){
                    MyScene.getInstance().feedbackDisplay.setValue("DID NOT LOAD A GAME");
                }
            }
        });

        //LOAD MAP BUTTON
        butLoadMap = new Button("Choose File");
        butLoadMap.setDisable(true);
        butLoadMap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Controller.getInstance().loadMapButtonPressed();
                    MyScene.getInstance().getSceneManager().setupBoard();
                    MyScene.getInstance().gameTypeGrid.setDisable(true);
                    MyScene.getInstance().playerBox.setDisable(false);
                }catch (BadMapException e){
                    if (Game.getInstance().getGameData() != null) {
                    }
                        MyScene.getInstance().getSceneManager().resetDisplay();

                        MyScene.getInstance().feedbackDisplay.setValue
                                ("Unable to load your map. Cause : " + e.getMessage());

                }catch (Exception e){
                if (Game.getInstance().getGameData() != null) {
                }
                    MyScene.getInstance().getSceneManager().resetDisplay();

                    MyScene.getInstance().feedbackDisplay.setValue
                            ("Unable to load your map");

                }
            }
        });

        //FILL THE GAMETYPEGRID UP AND ADD TO LEFTBOX
        Label RightText = new Label("SELECT GAME TYPE");
        gameTypeGrid.add(RightText, 0, 0);
        gameTypeGrid.add(defaultGame, 0, 1);
        gameTypeGrid.add(customGame, 0, 2);
        gameTypeGrid.add(importMapGame, 0, 3);
        gameTypeGrid.add(butLoadMap, 1, 3);
        gameTypeGrid.add(loadSavedGame, 0, 4);
        gameTypeGrid.add(butLoadGame, 1, 4);
        leftBox.getChildren().add(gameTypeGrid);

        //CUSTOMGAMEGRID***************************************

        //DESIGN OF THE GRID
        customGrid = new GridPane();
        customGrid.setPadding(new Insets(5, 5, 5, 5));
        customGrid.setMinSize(200, 350);
        customGrid.setVgap(10);
        customGrid.setStyle("-fx-background-color: #99CCFF;");
        customGrid.setHgap(20);

        //LABELS
        Label l1 = new Label("Boats of length 1");
        Label l2 = new Label("Boats of length 2");
        Label l3 = new Label("Boats of length 3");
        Label l4 = new Label("Boats of length 4");
        Label l5 = new Label("Boats of length 5");
        Label l6 = new Label("Boats of length 6");
        Label length = new Label("Grid Length:");
        Label height = new Label("Grid Height:");

        //INPUTS FORMATTED
        l1Input = new TextField();
        l1Input.setPrefColumnCount(1);
        l1Input.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^[0-9]*$)")) ? change : null));
        l1Input.setMaxWidth(50);
        l1Input.setText("0");

        l2Input = new TextField();
        l2Input.setPrefColumnCount(1);
        l2Input.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^[0-9]*$)")) ? change : null));
        l2Input.setMaxWidth(50);
        l2Input.setText("0");

        l3Input = new TextField();
        l3Input.setPrefColumnCount(1);
        l3Input.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^[0-9]*$)")) ? change : null));
        l3Input.setMaxWidth(50);
        l3Input.setText("0");

        l4Input = new TextField();
        l4Input.setPrefColumnCount(1);
        l4Input.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^[0-9]*$)")) ? change : null));
        l4Input.setMaxWidth(50);
        l4Input.setText("0");

        l5Input = new TextField();
        l5Input.setPrefColumnCount(1);
        l5Input.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^[0-9]*$)")) ? change : null));
        l5Input.setMaxWidth(50);
        l5Input.setText("0");

        l6Input = new TextField();
        l6Input.setPrefColumnCount(1);
        l6Input.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^[0-9]*$)")) ? change : null));
        l6Input.setMaxWidth(50);
        l6Input.setText("0");

        lengthInput = new TextField();
        lengthInput.setPrefColumnCount(2);
        lengthInput.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^([0-9]*)$)")) ? change : null));
        lengthInput.setMaxWidth(50);
        lengthInput.setText("0");

        heightInput = new TextField();
        heightInput.setPrefColumnCount(2);
        heightInput.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^([0-9]*)$)")) ? change : null));
        heightInput.setMaxWidth(50);
        heightInput.setText("0");

        //BUTTON SUBMIT CUSTOM SETTINGS
        Button butSubmitCustom = new Button("Submit");
        butSubmitCustom.setAlignment(Pos.BASELINE_RIGHT);
        butSubmitCustom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean submit = Controller.getInstance().customGameButtonPressed(lengthInput, heightInput,
                          l1Input, l2Input, l3Input, l4Input, l5Input, l6Input);
                if (submit){
                    Game.getInstance().getInit().customGame(
                            Integer.parseInt(lengthInput.getText()),
                            Integer.parseInt(heightInput.getText()),
                            Integer.parseInt(l1Input.getText()),
                            Integer.parseInt(l2Input.getText()),
                            Integer.parseInt(l3Input.getText()),
                            Integer.parseInt(l4Input.getText()),
                            Integer.parseInt(l5Input.getText()),
                            Integer.parseInt(l6Input.getText()));
                    sceneManager.setupBoard();
                    sceneManager.customGameSubmitted();
                }
            }
        });

        Label customisationLbl = new Label("CUSTOMISATION");
        //FILL THE CUSTOMGAMEGRID UP AND ADD TO LEFTBOX
        customGrid.add(customisationLbl, 0, 0);
        customGrid.add(l1, 0, 1);
        customGrid.add(l1Input, 1, 1);
        customGrid.add(l2, 0, 2);
        customGrid.add(l2Input, 1, 2);
        customGrid.add(l3, 0, 3);
        customGrid.add(l3Input, 1, 3);
        customGrid.add(l4, 0, 4);
        customGrid.add(l4Input, 1, 4);
        customGrid.add(l5, 0, 5);
        customGrid.add(l5Input, 1, 5);
        customGrid.add(l6, 0, 6);
        customGrid.add(l6Input, 1, 6);

        customGrid.add(length, 0, 7);
        customGrid.add(lengthInput, 1, 7);
        customGrid.add(height, 0, 8);
        customGrid.add(heightInput, 1, 8);
        customGrid.add(butSubmitCustom, 2, 9);
        customGrid.setDisable(true);
        leftBox.getChildren().add(customGrid);

        //PLAYERGRID**********************************************
        playerBox = new HBox();
        playerBox.setPadding(new Insets(5, 5, 5, 5));
        playerBox.setStyle("-fx-background-color: #99CCFF;");
        playerBox.setSpacing(40);
        playerBox.setMinHeight(40);
        playerBox.setAlignment(Pos.CENTER);

        //BUTTONS
        Button playButton = new Button("Play");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.getInstance().humanButtonPressed();
                sceneManager.humanPlayerChosen();
            }
        });

        Button aiPlayerButton = new Button("AI Game");
        aiPlayerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.getInstance().aiPlayerButtonPressed();
                sceneManager.aiPlayerChosen();
            }

        });

        //FILL THE PLAYERBOX UP AND ADD TO LEFTBOX
        playerBox.getChildren().addAll(playButton,aiPlayerButton);
        playerBox.setDisable(true);
        leftBox.getChildren().add(playerBox);

        //CHEATMODE************************************************
        cheatPane = new GridPane();
        cheatPane.setPadding(new Insets(7, 5, 10, 7));
        cheatPane.setMinHeight(30);
        cheatPane.setStyle("-fx-background-color: #99CCFF;");

        //BUTTON
        cheatButton = new CheckBox();
        cheatButton.setText("Cheat Mode");
        cheatButton.setSelected(false);
        cheatButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cheatButton.isSelected()) {
                    Controller.getInstance().cheatModeOn();
                } else {
                    Controller.getInstance().cheatModeOff();
                }
            }
        });

        cheatPane.setDisable(true);

        //ADD BUTTON TO LEFTBOX
        cheatPane.getChildren().add(cheatButton);
        leftBox.getChildren().add(cheatPane);

        return leftBox;
    }


    /***************************************************************
     *@return (VBox) the right box to position in Borderpane
     ***************************************************************/
    private VBox createRightBox() {
        VBox rightBox = new VBox();
        rightBox.setSpacing(5);
        rightBox.setPadding(new Insets(5, 5, 5, 5));

        //GAMESETTINGSGRID***************************************
        gameSettingsGrid = new GridPane();
        gameSettingsGrid.setPadding(new Insets(5, 5, 5, 5));
        gameSettingsGrid.setMinSize(250, 100);
        gameSettingsGrid.setVgap(5);
        gameSettingsGrid.setStyle("-fx-background-color: #99CCFF;");

        //CONFIGURATION BOAT VALUES TO SHOW ON SCREEN
        nBoatsDisplay.setValue(0);
        Text NoBoats = new Text();
        NoBoats.textProperty().bind(Bindings.concat(nBoatsDisplay.asString()));

        //CONFIGURATION GRID LENGTH TO SHOW ON SCREEN
        lengthDisplay.setValue(0);
        Text GridLength = new Text();
        GridLength.textProperty().bind(Bindings.concat(lengthDisplay.asString()));

        //CONFIGURATION GRID HEIGHT TO SHOW ON SCREEN
        heightDisplay.setValue(0);
        Text GridHeight = new Text();
        GridHeight.textProperty().bind(Bindings.concat(heightDisplay.asString()));

        //LABELS
        Label gameSettingLbl = new Label("GAME SETTINGS");
        Label nBoatLbl = new Label("No of Boats:");
        Label lengthLbl = new Label("Grid Length:");
        Label heightLbl = new Label("Grid Height:");


        //FILL THE GAMESETTINGSGRID UP AND ADD TO RIGHTBOX
        gameSettingsGrid.add(gameSettingLbl, 0, 0);
        gameSettingsGrid.add(nBoatLbl, 0, 1);
        gameSettingsGrid.add(NoBoats, 1, 1);
        gameSettingsGrid.add(lengthLbl, 0, 2);
        gameSettingsGrid.add(GridLength, 1, 2);
        gameSettingsGrid.add(heightLbl, 0, 3);
        gameSettingsGrid.add(GridHeight, 1, 3);
        rightBox.getChildren().add(gameSettingsGrid);

        //GUESSGRID***************************************
        guessGrid = new GridPane();
        guessGrid.setPadding(new Insets(5, 5, 5, 5));
        guessGrid.setVgap(5);
        guessGrid.setStyle("-fx-background-color: #99CCFF;");

        //LABELS
        Label xLbl = new Label("Guess X:");
        Label yLbl = new Label("Guess Y:");

        //ENTRIES FORMATTED
        xEntry = new TextField();
        xEntry.setPrefColumnCount(1);
        xEntry.setMaxWidth(30);
        xEntry.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^[0-9]*$)")) ? change : null));

        yEntry=new TextField();
        yEntry.setPrefColumnCount(1);
        yEntry.setMaxWidth(30);
        yEntry.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("(^[A-Z]|[a-z]*$)")) ? change : null));

        //SUBMITGUESS BUTTON
        Button submitGuessButton = new Button("Guess");
        submitGuessButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean valid = Controller.getInstance().guessButtonPressed(xEntry,yEntry);
                if(valid){
                    try {
                        Object[] feedback = Game.getInstance().checkGuess(xEntry.getText(), yEntry.getText());
                        sceneManager.validGuessUpdate(feedback);
                        if(Game.getInstance().isEnded()){
                            sceneManager.endTheGame();
                        }

                    }catch(ReguessException e){
                        sceneManager.invalidGuessUpdate(e.getMessage());
                    }catch (IndexOutOfBoundsException e){
                        sceneManager.invalidGuessUpdate(e.getMessage());
                    }
                }
            }
        });

        //FILL GUESS GRID UP AND ADD TO RIGHTBOX
        guessGrid.add(xLbl, 0, 0);
        guessGrid.add(xEntry, 1, 0);
        guessGrid.add(yLbl, 0, 1);
        guessGrid.add(yEntry, 1, 1);
        guessGrid.add(submitGuessButton, 1, 2);
        guessGrid.setDisable(true);
        rightBox.getChildren().add(guessGrid);


        //AIOPTIONSBOX***************************************
        aiBox = new HBox();
        aiBox.setPadding(new Insets(5, 5, 5, 5));
        aiBox.setStyle("-fx-background-color: #99CCFF;");
        aiBox.setSpacing(40);
        aiBox.setMinHeight(40);
        aiBox.setAlignment(Pos.CENTER);

        //BUTTONS
        Button aiNextGuessButton = new Button("Next Guess");
        aiNextGuessButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.getInstance().aiGuessButtonPressed();
                if(Game.getInstance().isEnded()){
                    MyScene.getInstance().getSceneManager().endTheGame();
                }
            }
        });

        Button aiEndGameButton = new Button("Fast-forward");
        aiEndGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.getInstance().aiEndGameButtonPressed();
            }
        });

        //ADD BUTTONS TO HBOX AND HBOX TO RIGHTBOX
        aiBox.getChildren().addAll(aiNextGuessButton,aiEndGameButton);
        aiBox.setDisable(true);
        rightBox.getChildren().add(aiBox);


        //ENDGAMEBOX***************************************
        endGameBox = new HBox();
        endGameBox.setPadding(new Insets(5, 5, 5, 5));
        endGameBox.setStyle("-fx-background-color: #99CCFF;");
        endGameBox.setSpacing(40);
        endGameBox.setMinHeight(40);
        endGameBox.setAlignment(Pos.CENTER);

        //BUTTONS
        Button saveGameButton = new Button("Save Game");
        saveGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Controller.getInstance().saveGameButtonPressed();
                } catch (Exception e) {
                    feedbackDisplay.setValue("DID NOT SAVE YOUR GAME");
                }
            }

        });
        saveGameButton.setVisible(true);

        Button endGameButton = new Button("New Game");
        endGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.getInstance().newGameButtonPressed();
            }
        });
        endGameButton.setVisible(true);

        //ADD BUTTONS TO ENDGAMEBOX AND ADD BOX TO RIGHTBOX
        endGameBox.getChildren().addAll(saveGameButton,endGameButton);
        endGameBox.setDisable(true);
        rightBox.getChildren().add(endGameBox);

        //SCORESGRID***************************************
        scoreGrid = new GridPane();
        scoreGrid.setPadding(new Insets(5, 5, 5, 5));
        scoreGrid.setMinSize(250, 100);
        scoreGrid.setVgap(5);
        scoreGrid.setStyle("-fx-background-color: #99CCFF;");

        //CONFIGURATION BOAT VALUES TO SHOW ON SCREEN
        nGuessesDisplay.setValue(0);
        Text nGuesses = new Text();
        nGuesses.textProperty().bind(Bindings.concat(nGuessesDisplay.asString()));

        //CONFIGURATION BOAT VALUES TO SHOW ON SCREEN
        nHitsDisplay.setValue(0);
        Text nHits = new Text();
        nHits.textProperty().bind(Bindings.concat(nHitsDisplay.asString()));

        //CONFIGURATION BOAT VALUES TO SHOW ON SCREEN
        nSinksDisplay.setValue(0);
        Text nSinks = new Text();
        nSinks.textProperty().bind(Bindings.concat(nSinksDisplay.asString()));

        //LABELS
        Label guessesLbl = new Label("Number of guesses :  ");
        Label hitsLbl = new Label("Number of hits :  ");
        Label sinksLbl = new Label("Number of sunken boats :  ");
        Label score = new Label("SCORE");


        scoreGrid.add(score, 0, 0);
        scoreGrid.add(guessesLbl, 0, 1);
        scoreGrid.add(nGuesses, 1, 1);
        scoreGrid.add(hitsLbl, 0, 2);
        scoreGrid.add(nHits, 1, 2);
        scoreGrid.add(sinksLbl, 0, 3);
        scoreGrid.add(nSinks, 1, 3);
        rightBox.getChildren().add(scoreGrid);

        //BOATSBOX*******************************************

        //BOX
        boatsBox.setPadding(new Insets(5, 5, 5, 5));
        boatsBox.setMinSize(250, 180);
        boatsBox.setStyle("-fx-background-color: #99CCFF;");
        boatsBox.setHgap(40);
        boatsBox.setVgap(8);

        //COLUMN HEADERS
        Text nameTxt = new Text("BOAT");
        Text idTxt = new Text ("ID");
        Text lgthTxt = new Text("LENGTH");

        //NAMES
        Text mineTxt = new Text("Mine");
        Text destroyerTxt = new Text("Destroyer");
        Text submarineTxt = new Text("Submarine");
        Text cruiserTxt = new Text("Cruiser");
        Text batTxt = new Text("Battleship");
        Text carrierTxt = new Text("Carrier");

        //IDS
        Text mineId = new Text("M");
        Text destroyerId = new Text("D");
        Text submarineId = new Text("S");
        Text cruiserId = new Text("C");
        Text batId = new Text("B");
        Text carrierId = new Text("K");

        //LENGTHS
        Text mineLgth = new Text("1");
        Text destroyerLgth = new Text("2");
        Text submarineLgth = new Text("3");
        Text cruiserLgth = new Text("4");
        Text batLgth = new Text("5");
        Text carrierLgth = new Text("6");

        //ADD
        boatsBox.add(nameTxt, 0, 0);
        boatsBox.add(idTxt, 1, 0);
        boatsBox.add(lgthTxt, 2, 0);

        boatsBox.add(mineTxt, 0, 1);
        boatsBox.add(mineId, 1, 1);
        boatsBox.add(mineLgth, 2, 1);

        boatsBox.add(destroyerTxt, 0, 2);
        boatsBox.add(destroyerId, 1, 2);
        boatsBox.add(destroyerLgth, 2, 2);

        boatsBox.add(submarineTxt, 0, 3);
        boatsBox.add(submarineId, 1, 3);
        boatsBox.add(submarineLgth, 2, 3);

        boatsBox.add(cruiserTxt, 0, 4);
        boatsBox.add(cruiserId, 1, 4);
        boatsBox.add(cruiserLgth, 2, 4);

        boatsBox.add(batTxt, 0, 5);
        boatsBox.add(batId, 1, 5);
        boatsBox.add(batLgth, 2, 5);

        boatsBox.add(carrierTxt, 0, 6);
        boatsBox.add(carrierId, 1, 6);
        boatsBox.add(carrierLgth, 2, 6);


        rightBox.getChildren().add(boatsBox);

        return rightBox;
    }


    /***************************************************************
     *@return (VBox) the bottom box to position in Borderpane
    /***************************************************************/
    private GridPane createBottomBox(){
        GridPane bottomBox = new GridPane();
        bottomBox.setPadding(new Insets(5, 5, 5, 5));
        bottomBox.setStyle("-fx-background-color: #99CCFF;");
        bottomBox.setVgap(10);
        bottomBox.setHgap(10);

        Text xValue = new Text();
        xValue.textProperty().bind(xDisplay.asString());

        Text yValue = new Text();
        yValue.textProperty().bind(yDisplay.asString());

        Label guessLbl = new Label("Your Guess");
        Label guess = new Label();
        guess.textProperty().bind(Bindings.concat(" ( ", xValue.textProperty(), " : ", yValue.textProperty(), " ) "));


        Label feedbackLbl = new Label("Feedback");
        Label feedbackValue = new Label();
        feedbackValue.textProperty().bind(feedbackDisplay.asString());

        bottomBox.add(guessLbl,0,0);
        bottomBox.add(guess,1,0);
        bottomBox.add(feedbackLbl,0,1);
        bottomBox.add(feedbackValue,1,1);


        return bottomBox;
    }


    /***************************************************************
     *@return (Pane) the center box to position in Borderpane
    /***************************************************************/
    public Pane createCenterBox(){
        gameBox = new Pane();
        gameBox.setPadding(new Insets(20,20,20,20));
        gameBox.getChildren().addAll(backgroundPane, tilesPane);
        gameBox.setVisible(true);
        return gameBox;
    }

}



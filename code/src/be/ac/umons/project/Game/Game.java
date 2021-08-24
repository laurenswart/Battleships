package be.ac.umons.project.Game;

import be.ac.umons.project.Control.Initialiser;
import be.ac.umons.project.Control.ReguessException;


public class Game{
    private static Game ourInstance;

    private GameData gameData;
    private Initialiser init;
    private boolean ended;


    public Game() {
        init = new Initialiser();
        gameData = null;
    }

    ////////////////////////////////////////////////
    //GETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@return (Game) our single instance of Game
     ***************************************************************/
    public static Game getInstance() {
        if (ourInstance==null){
            ourInstance=new Game();
        }
        return ourInstance;
    }

    /***************************************************************
     *@return (GameData) the gameData of this game
     ***************************************************************/
    public GameData getGameData() {
        return gameData;
    }

    /***************************************************************
     *@return (Initialiser) the Initialiser of this game
     ***************************************************************/
    public Initialiser getInit() {
        return init;
    }

    /***************************************************************
     *@return (boolean) true if this game is ended
     ***************************************************************/
    public boolean isEnded() {
        return ended;
    }


    ////////////////////////////////////////////////
    //SETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@param gameData (GameData) to set to this Game's data
     ***************************************************************/
    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    /***************************************************************
     *@param ended (boolean) to set to this Game's ended state
     ***************************************************************/
    public void setEnded(boolean ended) {
        this.ended = ended;
    }


    ////////////////////////////////////////////////
    //METHODS
    ////////////////////////////////////////////////
    /***************************************************************
     *@param x (String) x value of the guess to check
     *@param y (String) y value of the guess to check
     *@return (Object[]) feedback from the guess where
     *              ans[0] = (int) x entered
     *              ans[1] = (int) y entered
     *              ans[2] = (boolean) true if a boat was hit
     *              ans[3] = (boolean) true if a boat was sunk
     *              ans[4] = (int) distance to the closest, then shortest boat
     *              ans[5] = (int) the length of that boat
     * @throws ReguessException,IndexOutOfBoundsException
     ***************************************************************/
    public Object[] checkGuess(String x, String y)throws ReguessException, IndexOutOfBoundsException{
        if (y.length() > 1) {
            throw new IndexOutOfBoundsException("Tile out of range");
        }
        int xGuess = Integer.parseInt(x);
        int yGuess = (y.charAt(0) & 31) - 1;
        GameData gameData = Game.getInstance().getGameData();

        if (gameData.alreadyGuessed(xGuess, yGuess)) {
            throw new ReguessException("You have already tried this tile");
        }
        if (xGuess >= gameData.getBoard().getWidth() || yGuess >= gameData.getBoard().getHeight()) {
            throw new IndexOutOfBoundsException("Tile out of range");
        }


        Object[] feedback = gameData.processGuess(xGuess, yGuess);

        return feedback;
    }


}
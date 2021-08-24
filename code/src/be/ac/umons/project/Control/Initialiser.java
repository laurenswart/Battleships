package be.ac.umons.project.Control;

import be.ac.umons.project.Elements.Boat;
import be.ac.umons.project.Game.Board;
import be.ac.umons.project.Game.Game;
import be.ac.umons.project.Game.GameData;
import java.util.ArrayList;
import java.util.Random;

public class Initialiser {

    /***************************************************************
     *Constructor
     ***************************************************************/
    public Initialiser() {
    }


    /***************************************************************
     *sets gameData of game in default mode
     ***************************************************************/
    public void defaultGame() {
        //CREATE A BOARD
        Board board = new Board(10, 10, 0);
        ArrayList<Boat> boats = createDefaultBoatArray();
        //CREATE A GAME
        GameData game = new GameData(board, boats, false);
        //POSITION BOATS
        randomPosBoats(game);

        //!!tile matrix is empty
        Game.getInstance().setGameData(game);
        Game.getInstance().setEnded(false);
    }


    /***************************************************************
     *sets gameData of game in custom mode
     * @param length (int) length of desired board
     * @param height (int) width of desired board
     * @param l1 (int) number of boats of length 1
     * @param l2 (int) number of boats of length 1
     * @param l3 (int) number of boats of length 1
     * @param l4 (int) number of boats of length 1
     * @param l5 (int) number of boats of length 1
     * @param l6 (int) number of boats of length 1
     ***************************************************************/
    public void customGame( int length, int height, int l1, int l2, int l3, int l4, int l5, int l6) {
        Board board = new Board(height, length,0);
        //BOATS
        ArrayList<Boat> boats = new ArrayList<>();
        int id = 1;
        boats.addAll(createMultipleBoats(l1, 1, id));
        id+=l1;
        boats.addAll(createMultipleBoats(l2, 2, id));
        id+=l2;
        boats.addAll(createMultipleBoats(l3, 3, id));
        id+=l3;
        boats.addAll(createMultipleBoats(l4, 4, id));
        id+=l4;
        boats.addAll(createMultipleBoats(l5, 5, id));
        id+=l5;
        boats.addAll(createMultipleBoats(l6, 6, id));

        //CREATE A GAME
        GameData gameData = new GameData(board, boats, false);

        //POSITION BOATS
        randomPosBoats(gameData);
        Game.getInstance().setGameData(gameData);
        Game.getInstance().setEnded(false);
    }


    /***************************************************************
     *sets gameData of game having read file chosen by user
     * @param map (LoadedMap) containing matrix of map config
     * @throws BadMapException
     ***************************************************************/
    public void loadMapGame(LoadedMap map)throws BadMapException{
        ArrayList<Boat> boats = createLoadedBoats(map);

        Board board = new Board(map.getInterMatrix().length, map.getInterMatrix()[0].length, 0);

        //CREATE A GAME
        GameData gameData = new GameData(board, boats, false);

        //POSITION BOATS INTO MATRIX
        loadPosBoats(gameData);
        Game.getInstance().setGameData(gameData);
        Game.getInstance().setEnded(false);
    }


    /***************************************************************
     *sets gameData of game having read file chosen by user
     * @param saved (SavedGame) containing matrix of map config
     ***************************************************************/
    public void loadSavedGame(SavedGame saved){
        ArrayList<Boat> boats = saved.getBoatArraySaved();
        int height = saved.getHeightSaved();
        int width = saved.getWidthSaved();

        Board board = new Board(height, width, 0);
        GameData gameData = new GameData(board, boats, saved.isPlayerIsAi());

        //POSITION BOATS INTO MATRIX
        loadPosBoats(gameData);

        if(saved.isPlayerIsAi()){
            gameData.setAi(saved.getAiPlayerSaved());
        }
        gameData.setFeedbackList(saved.getFeedbackListSaved());
        gameData.setGuessArray(saved.getGuessesSaved());
        Game.getInstance().setGameData(gameData);
        Game.getInstance().setEnded(false);

    }


    /***************************************************************
     *creates boats from the interMatrix
     * @param map (LoadedMap) map from which we want to load
     * @return (ArrayList<Boat>) boats for the game
     * @throws BadMapException if boats from map are touching
     ***************************************************************/
    private ArrayList<Boat> createLoadedBoats(LoadedMap map)
            throws BadMapException{
        ArrayList<Boat> boats = new ArrayList<>();
        boolean parsingBoat = false;
        int length = 0;
        int boatId = 1;
        ArrayList<int[]> coordinates = new ArrayList<>();

        for (int line = 0; line < map.getInterMatrix().length; line++) {
            for (int column = 0; column < map.getInterMatrix()[line].length; column++) {
                if (wrongPositioning(column, line, map)){
                    throw new BadMapException("Bad boat positioning");
                } if ((!parsingBoat)&& map.getInterMatrix()[line][column].equals("X")) {
                    parsingBoat = true;
                    coordinates = new ArrayList<>();
                    length = 1;
                    int[] pos = {column, line};
                    coordinates.add(pos);

                } else if (parsingBoat && (!map.getInterMatrix()[line][column].equals("X"))) {
                    //END OF HORIZONTAL PARSING OF X (BOAT)
                    if (length == 1) {
                        //EITHER A vertical boat, or a 1x1 boat
                        boatId = checkForVerticalBoat(coordinates, boatId, map, boats);

                    } else {
                        Boat b = new Boat(length, boatId, coordinates);
                        boats.add(b);
                        boatId += 1;
                    }
                    parsingBoat = false;
                } else if (parsingBoat && map.getInterMatrix()[line][column].equals("X")) {
                    //next horizontal in row is an X
                    length += 1;
                    int[] tile = {column, line};
                    coordinates.add(tile);
                }
            }
        }
        return boats;
    }



    /***************************************************************
     *creates a vertical boat if it is in fact one
     *@param coordinates (ArrayList<int[]>) of current temporary boat list
     *@param boatId (int) for the next boat to create
     *@return (int) id for next boat to be created
     ***************************************************************/
    private int checkForVerticalBoat(ArrayList<int[]> coordinates, int boatId,
                                     LoadedMap map, ArrayList<Boat> boats){
        // if we are on row 0, then boat hasn't yet been created
        // if not and if nothing above,then boat hasn't yet been created
        // check downwards for rest of boat and create it
        int length = 1;
        ArrayList<int[]> coord = coordinates;
        int y = coord.get(0)[1];
        int x = coord.get(0)[0];
        if ((y == 0) || (map.getInterMatrix()[y - 1][x].equals("."))) {
            y+=1;
            while (y < map.getInterMatrix().length && map.getInterMatrix()[y][x].equals("X")) {
                length += 1;
                int[] tile = {x, y};
                coord.add(tile);
                y += 1;
            }
            Boat b = new Boat(length, boatId, coord);
            boatId += 1;
            boats.add(b);
            return  boatId;
        }
        return  boatId;
    }


    /***************************************************************
     *@return (ArrayList<Boat>) boat list for a default game
     ***************************************************************/
    private ArrayList<Boat> createDefaultBoatArray(){
        ArrayList<Boat> array = new ArrayList();
        for (int id = 1; id<6; id++){
            if (id<3){
                Boat b = new Boat(id+1,id, new ArrayList<>());
                array.add(b);
            }else{
                Boat b = new Boat(id,id, new ArrayList<>());
                array.add(b);
            }
        }
        return array;
    }


    /***************************************************************
     * TODO
     *randomly positions the boats that in the Game boatArray
     * @param gameData (GameData) data contain the matrix in which to position boats
     ***************************************************************/
    private void randomPosBoats(GameData gameData) {
        int x0 = 0;
        int y0 = 0;
        for ( int n = gameData.getBoatArray().size()-1; n>=0; n--){
            Boat b = gameData.getBoatArray().get(n);
            Random rand = new Random();
            int l = b.getLength();

            boolean retry = true;
            while(retry) {
                int direction = rand.nextInt(2);

                //HORIZONTAL
                if (direction == 1) {
                    x0 = rand.nextInt(gameData.getBoard().getWidth() - l);
                    y0 = rand.nextInt(gameData.getBoard().getHeight());

                    // we suppose it's a good starting point
                    retry = false;
                    for (int i = 0; i < l; i++) {
                        //if one of the tiles already has a boat
                        if (gameData.existsBoatAround(x0 + i, y0)) {
                            retry = true;
                        }
                    }
                    //POSITION BOAT
                    if (!retry) {
                        ArrayList pos = new ArrayList();
                        for (int i = 0; i < l; i++) {
                            int x = x0 + i;
                            gameData.getMatrix()[y0][x] = b.getBoatid();
                            pos.add(new int[]{x, y0});
                        }
                        b.setPositions(pos);
                    }

                //VERTICAL
                } else {
                    // determine the tile on which to start
                    x0 = rand.nextInt(gameData.getBoard().getWidth());
                    y0 = rand.nextInt(gameData.getBoard().getHeight() - l);

                    // we suppose it's a good starting point
                    retry = false;
                    for (int i = 0; i < l; i++) {
                        if (gameData.existsBoatAround(x0, y0 + i)) {
                            retry = true;
                        }
                    }
                    if(!retry){
                        ArrayList pos = new ArrayList();
                        for (int i = 0; i < l; i++) {
                            int y = y0 + i;
                            gameData.getMatrix()[y][x0] = b.getBoatid();
                            pos.add(new int[]{x0, y});
                        }
                        b.setPositions(pos);
                    }
                }
            }
        }
    }


    /***************************************************************
     * @param n(int) the amount of boats wanted
     * @param length(int) the length of all the boats to create
     * @param id(int) index of the next boat to be created
     * @return (ArrayList<Boat>) n boats of length 'length
     ***************************************************************/
    private ArrayList<Boat> createMultipleBoats(int n, int length, int id){
        ArrayList<Boat> array = new ArrayList<>();
        for( int i = 0; i<n; i++){
            Boat b = new Boat(length, id, new ArrayList<>());
            id+=1;
            array.add(b);
        }
        return array;
    }


    /***************************************************************
     * checks if a map wanting to be loaded has boats touching
     * this methods checks for position (x, y)
     * @param x(int) x index of position to check
     * @param y(int) y index of position to check
     * @param map(LoadedMap) the map in which to check the positioning
     ***************************************************************/
    private boolean wrongPositioning(int x, int y, LoadedMap map) {
        boolean ans = false;
        if(hasAbove(x, y, map)&&(hasLeft(x, y, map)||hasRight(x, y, map))){
            ans = true;
        }else if (hasBelow(x, y, map)&&(hasLeft(x, y, map)||hasRight(x, y, map))) {
            ans = true;
        }
        return ans;
    }


    /***************************************************************
     * @param x(int) x index of position to check
     * @param y(int) y index of position to check
     * @return (boolean) true if position (x,y)  has a boat right above it
     ***************************************************************/
    private boolean hasAbove(int x, int y, LoadedMap map) {
        if (y > 0) {
            return map.getInterMatrix()[y - 1][x].equals("X");
        } else {
            return false;
        }
    }


    /***************************************************************
     * @param x(int) x index of position to check
     * @param y(int) y index of position to check
     * @return (boolean) true if position (x,y)  has a boat right below it
     ***************************************************************/
    private boolean hasBelow(int x, int y, LoadedMap map){
        if(y<map.getInterMatrix().length-1){
            return map.getInterMatrix()[y+1][x].equals("X");
        }else{
            return false;
        }
    }


    /***************************************************************
     * @param x(int) x index of position to check
     * @param y(int) y index of position to check
     * @return (boolean) true if position (x,y)  has a boat on its right side
     ***************************************************************/
    private boolean hasRight(int x, int y, LoadedMap map){
        if(x<map.getInterMatrix()[0].length-1){
            return map.getInterMatrix()[y][x+1].equals("X");
        }else{
            return false;
        }
    }


    /***************************************************************
     * @param x(int) x index of position to check
     * @param y(int) y index of position to check
     * @return (boolean) true if position (x,y)  has a boat on its left side
     ***************************************************************/
    private boolean hasLeft(int x, int y, LoadedMap map){
        if(x>0){
            return map.getInterMatrix()[y][x-1].equals("X");
        }else{
            return false;
        }
    }


    /***************************************************************
     * updates the gameData matrix according to a the gameData boat array
     * @param gameData(GameData) gameData for which boats need to be placed into matrix
     ***************************************************************/
    private void loadPosBoats(GameData gameData){
        for(Boat b : gameData.getBoatArray()) {
            for(int[] pos : b.getPositions()){
                gameData.getMatrix()[pos[1]][pos[0]] = b.getBoatid();
            }
        }
    }



}

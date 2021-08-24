package be.ac.umons.project.Tests;


import be.ac.umons.project.Control.BadMapException;
import be.ac.umons.project.Control.LoadedMap;
import be.ac.umons.project.Game.Game;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ImportTest {


    @Test(expected = BadMapException.class)
    public void badMapBoatsTouching() throws  Exception {
        File file = new File("Maps\\mapBoatsTouching.txt");
        LoadedMap map = new LoadedMap(file);
        Game.getInstance().getInit().loadMapGame(map);
    }


    @Test(expected = BadMapException.class)
    public void badMapDifferentRowLengths() throws  Exception {
        File file = new File("Maps\\map-different lengths.txt");
        LoadedMap map = new LoadedMap(file);
        Game.getInstance().getInit().loadMapGame(map);
    }


    @Test(expected = Exception.class)
    public void badMapEmpty() throws  Exception {
        File file = new File("Maps\\map-empty.txt");
        LoadedMap map = new LoadedMap(file);
        Game.getInstance().getInit().loadMapGame(map);
    }


    @Test(expected = BadMapException.class)
    public void badMapCharacter() throws  Exception {
        File file = new File("Maps\\map-other caracters.txt");
        LoadedMap map = new LoadedMap(file);
        Game.getInstance().getInit().loadMapGame(map);
    }


    @Test(expected = BadMapException.class)
    public void badMapTooBig() throws  Exception {
        File file = new File("Maps\\map-dim greater 26.txt");
        LoadedMap map = new LoadedMap(file);
        Game.getInstance().getInit().loadMapGame(map);
    }
    @Test
    public void interMatrixCheck() throws  Exception {
        File file = new File("Maps\\map-1.txt");
        LoadedMap map = new LoadedMap(file);
        String[][] actual = map.getInterMatrix();
        String[][] expected = {
                {"X","X",".",".",".",".","X",".",".","."},
                {".",".",".","X",".",".","X",".",".","X"},
                {".",".",".",".",".",".","X",".",".","X"},
                {".","X","X","X","X",".",".",".",".","X"}};
        assertEquals(actual, expected);
    }



}

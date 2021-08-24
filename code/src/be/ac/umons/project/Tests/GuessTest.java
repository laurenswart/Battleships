package be.ac.umons.project.Tests;

import be.ac.umons.project.Control.LoadedMap;
import be.ac.umons.project.Control.ReguessException;
import be.ac.umons.project.Game.Game;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GuessTest {


    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBoundsNumber() throws  Exception {
        Game.getInstance().getInit().defaultGame();
        Game.getInstance().checkGuess("0", "27");
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBoundsLetter() throws  Exception {
        Game.getInstance().getInit().defaultGame();
        Game.getInstance().checkGuess("1", "t");
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBoundsMultipleLetters() throws  Exception {
        Game.getInstance().getInit().defaultGame();
        Game.getInstance().checkGuess("1", "aa");
    }

    @Test
    public void hitSunkTest()throws Exception{
        File file = new File("Maps\\map-1.txt");
        LoadedMap map = new LoadedMap(file);
        Game.getInstance().getInit().loadMapGame(map);
        Object[] actual = Game.getInstance().checkGuess("3", "b");
        Object[] expected = {3, 1, true, true, 0, 1};
        assertEquals(actual,expected);
    }
    @Test
    public void shortestBoatTest()throws Exception{
        File file = new File("Maps\\map-1.txt");
        LoadedMap map = new LoadedMap(file);
        Game.getInstance().getInit().loadMapGame(map);
        Object[] actual = Game.getInstance().checkGuess("3", "c");
        Object[] expected = {3, 2, false, false, 1, 1};
        assertEquals(actual,expected);
    }

    @Test(expected = ReguessException.class)
    public void repeatGuess()throws ReguessException, IndexOutOfBoundsException{
        Game.getInstance().getInit().defaultGame();
        Game.getInstance().checkGuess("0", "a");
        Game.getInstance().checkGuess("0", "a");
    }

    @Test
    public void gameTest()throws Exception{
        File file = new File("Maps\\map-1.txt");
        LoadedMap map = new LoadedMap(file);
        Game.getInstance().getInit().loadMapGame(map);

        Object[] actual = Game.getInstance().checkGuess("0", "a");
        Object[] expected = {0, 0, true, false, 0, 2};
        assertEquals(actual,expected);

        Object[] actual2 = Game.getInstance().checkGuess("0", "b");
        Object[] expected2 = {0, 1, false, false, 1, 2};
        assertEquals(actual2,expected2);

        Object[] actual3 = Game.getInstance().checkGuess("1", "a");
        Object[] expected3 = {1, 0, true, true, 0, 2};
        assertEquals(actual3,expected3);

        Object[] actual4 = Game.getInstance().checkGuess("7", "d");
        Object[] expected4 = {7, 3, false, false, 2, 3};
        assertEquals(actual,expected);

        Object[] actual5 = Game.getInstance().checkGuess("2", "a");
        Object[] expected5 = {2, 0, false, false, 2,1};
        assertEquals(actual5,expected5);
    }



}

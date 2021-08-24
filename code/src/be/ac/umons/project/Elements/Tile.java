package be.ac.umons.project.Elements;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.io.Serializable;

public class Tile extends StackPane implements Serializable {
    private int size;
    private boolean hit;
    public Rectangle background;
    public Text boat;

    /***************************************************************
     *Constructor
     ***************************************************************/
    public Tile (int x, int y,int _size, Color c, String s){
        size = _size;
        hit=false;
        //BACKGROUND
        background = new Rectangle(size, size);
        background.setStroke(javafx.scene.paint.Color.BLACK);
        background.setFill(c);
        //TEXT
        boat = new Text(s);
        boat.setVisible(false);
        //STACKPANE
        this.getChildren().addAll(background, boat);
        relocate(x * size, y * size);
    }


    ////////////////////////////////////////////////
    //GETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@return (Text) the text for boat on this tile
     ***************************************************************/
    public Text getBoat() {
        return boat;
    }

    /***************************************************************
     *@return (boolean) true if this tile has been hit
     ***************************************************************/
    public boolean isHit() {
        return hit;
    }

    ////////////////////////////////////////////////
    //SETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@param hit (boolean) value to set to hit variable
     ***************************************************************/
    public void setHit(boolean hit) {
        this.hit = hit;
    }


}
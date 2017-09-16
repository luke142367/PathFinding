package Main;

import com.sun.org.apache.bcel.internal.generic.Select;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.invoke.SwitchPoint;

/**
 * Created by LUKE_2 on 03/10/2015.
 */
public class Tile {

    private TileTypes type;

    public Tile(TileTypes type){
        this.type = type;
    }

    public TileTypes getType(){
        return type;
    }

    public void setType(TileTypes type){
        this.type = type;
    }

    public BufferedImage getImage(){
        switch(type){
            case White:
                return null;
            case Green:
                return PathFinder.greenSquare;
            case Red:
                return PathFinder.redSquare;
            case Grey:
                return PathFinder.greySquare;
        }
        return null;
    }
}

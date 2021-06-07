package ch.bbbaden.io.game;

import java.util.Random;
import javafx.scene.paint.Color;

/**
 *
 * @author kappe
 */
public class ColourLib {

    // Define all the colours we want to use as background
    private Color[] colourArray = {
        Color.LAVENDERBLUSH,
        Color.LIGHTCYAN,
        Color.HONEYDEW,
        Color.LIGHTYELLOW,
        Color.ANTIQUEWHITE,
        Color.LAVENDER,
        Color.WHITESMOKE,
        Color.NAVAJOWHITE,
        Color.THISTLE,
        Color.PLUM,
        Color.LIGHTSTEELBLUE,
        Color.LIGHTSALMON
    };

    // Return a random colour to set to background
    public Color getColour() {
        Random r = new Random();
        int index = r.nextInt(colourArray.length - 1);
        return colourArray[index];
    }
}

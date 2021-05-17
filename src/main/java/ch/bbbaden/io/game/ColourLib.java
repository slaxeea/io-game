/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.io.game;

import java.util.Random;
import javafx.scene.paint.Color;

/**
 *
 * @author kappe
 */
public class ColourLib {

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

    public Color getColour() {
        Random r = new Random();
        int index = r.nextInt(colourArray.length - 1);
        return colourArray[index];
    }
}

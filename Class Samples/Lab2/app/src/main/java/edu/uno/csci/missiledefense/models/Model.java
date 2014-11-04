package edu.uno.csci.missiledefense.models;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * @author Daniel Ward <drward3@uno.edu>
 * @since 10/30/14
 */
public abstract class Model {
    public Point origin;

    public abstract void draw(Canvas canvas);
}

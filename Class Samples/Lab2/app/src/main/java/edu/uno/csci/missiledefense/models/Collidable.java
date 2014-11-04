package edu.uno.csci.missiledefense.models;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * @author Daniel Ward <drward3@uno.edu>
 * @since 11/3/14
 */
public interface Collidable {

    public Rect getBounds();
    public boolean collidesWith(Collidable collidable);
}

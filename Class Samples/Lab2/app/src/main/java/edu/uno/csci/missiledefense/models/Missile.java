package edu.uno.csci.missiledefense.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @author Daniel Ward <drward3@uno.edu>
 * @since 10/30/14
 */
public class Missile extends Model implements Collidable{

    public int velocity;
    public double angle;
    public Bitmap bitmap;

    public Missile(Bitmap bitmap, Point origin, double angle, float scale, int velocity) {
        this.angle = angle;
        this.origin = origin;
        this.velocity = velocity;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int)(scale * (float)bitmap.getWidth()), (int)(scale * (float)bitmap.getHeight()), false);
    }

    public void updatePosition(double elapsedTime) {
        double interval = elapsedTime;

        double radians = (angle/180.0) * Math.PI;
        // get the x component of the total velocity
        int velocityX = (int) (-velocity * Math.sin(radians));

        // get the y component of the total velocity
        int velocityY = (int) (velocity * Math.cos(radians));

        origin.x =  (int) (interval * velocityX);
        origin.y =  (int) (interval * velocityY);
    }

    @Override
    public void draw(Canvas canvas) {

        Matrix matrix = new Matrix();
        matrix.setRotate((int) angle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        canvas.drawBitmap(rotatedBitmap, origin.x, origin.y, null);
    }

    @Override
    public Rect getBounds() {
        Rect bounds = new Rect(origin.x, origin.y, 0, 0);
//        Matrix m = new Matrix();
// point is the point about which to rotate.
//        m.setRotate(degrees, point.x, point.y);
//        m.mapRect(r);

        return new Rect();
    }

    @Override
    public boolean collidesWith(Collidable collidable) {
        return false;
    }
}

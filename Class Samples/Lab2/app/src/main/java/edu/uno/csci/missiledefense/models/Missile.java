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

    public boolean isActive;

    public Missile(Bitmap bitmap, Point origin, double angle, float scale, int velocity) {
        this.angle = angle;
        this.origin = origin;
        this.velocity = velocity;
        this.isActive = true;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int)(scale * (float)bitmap.getWidth()), (int)(scale * (float)bitmap.getHeight()), false);
    }

    public void updatePosition(double elapsedTime) {
        double interval = elapsedTime;

        double radians = (angle/180.0) * Math.PI;

        origin.x = origin.x + (int) (interval * (-velocity * Math.sin(radians)));
        origin.y = origin.y + (int) (interval * (velocity * Math.cos(radians)));
    }

    @Override
    public void draw(Canvas canvas) {

        Matrix matrix = new Matrix();
        matrix.setRotate((int) angle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        canvas.drawBitmap(rotatedBitmap, origin.x, origin.y, null);
    }

    @Override
    public RectF getBounds() {
        RectF rect = new RectF(origin.x, origin.y, origin.x + bitmap.getWidth(), origin.y + bitmap.getHeight());

        final RectF rectF = new RectF(rect);
        final Matrix matrix = new Matrix();

        float centerX = origin.x + (bitmap.getWidth() / 2);
        float centerY = origin.y + (bitmap.getHeight() / 2);

        matrix.setRotate((int)angle, centerX, centerY);
        matrix.mapRect(rectF);

        rect.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);

       return rect;
    }

    @Override
    public boolean collidesWith(Collidable otherCollidable) {
        return RectF.intersects(this.getBounds(),otherCollidable.getBounds());
    }
}

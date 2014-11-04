package edu.uno.csci.missiledefense.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import edu.uno.csci.missiledefense.R;
import edu.uno.csci.missiledefense.models.Missile;
import edu.uno.csci.missiledefense.models.Projectile;

/**
 * @author Daniel Ward <drward3@uno.edu>
 * @since 11/3/14
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private Activity activity;

    // state variables
    private int screenWidth;
    private int screenHieght;
    private double totalElapsedTime; // elapsed seconds

    private List<Missile> missiles;
    private List<Projectile> projectiles;

    private Paint backgroundPaint;

    private GameLoop gameLoop;
    private boolean gameOver;
    private int baseMissileSpeed;
    private int screenHeight;
    private double missileScaleHeight;

    // public constructor
    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs); // call superclass constructor
        activity = (Activity) context; // store reference to MainActivity

        // register SurfaceHolder.Callback listener
        getHolder().addCallback(this);

        // set up the lists for the falling missiles and the targets that will be on screen
        missiles = new ArrayList<Missile>();
        projectiles = new ArrayList<Projectile>();

        // construct Paints for drawing text, cannonball, cannon,
        // blocker and target; these are configured in method onSizeChanged
        backgroundPaint = new Paint();
    } // end CannonView constructor


    // called by surfaceChanged when the size of the SurfaceView changes,
    // such as when it's first added to the View hierarchy
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w; // store CannonView's width
        screenHeight = h; // store CannonView's height

        missileScaleHeight = h * 0.1;
        baseMissileSpeed = (int) (h * 0.1); // cannonball speed multiplier

        backgroundPaint.setColor(Color.WHITE); // set background color

        newGame(); // set up and start a new game
    } // end method onSizeChanged

    // reset all the screen elements and start a new game
    public void newGame()
    {
        // set every element of hitStates to false--restores target pieces
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.rocket);
        float missilesScale = ((float)missileScaleHeight / (float)bitmap.getHeight());
        missiles.add(new Missile(bitmap, new Point(screenWidth/2, 0), -20, missilesScale, baseMissileSpeed));

        totalElapsedTime = 0.0; // set the time elapsed to zero

        if (gameOver) // starting a new game after the last game ended
        {
            gameOver = false;
            gameLoop = new GameLoop(getHolder()); // create thread
            gameLoop.start(); // start the game loop thread
        }
    } // end method newGame

    public void updatePositions(double elapsedTime) {
        for (Missile missile : missiles) {
            missile.updatePosition(elapsedTime);
        }
    }

    public void drawGameElements(Canvas canvas) {
        // clear the background

        if (canvas != null) {
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

            for (Missile missile : missiles) {
                missile.draw(canvas);
            }
        }
    }

    //==============================================================================================
    // SURFACE HOLDER CALLBACKS

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoop = new GameLoop(holder); // create thread
        gameLoop.setRunning(true); // start game running
        gameLoop.start(); // start the game loop thread
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
     public void surfaceDestroyed(SurfaceHolder holder) {
        // ensure that thread terminates properly
        boolean retry = true;
        gameLoop.setRunning(false); // terminate cannonThread

        while (retry)
        {
            try
            {
                gameLoop.join(); // wait for cannonThread to finish
                retry = false;
            }
            catch (InterruptedException e)
            {
                // left blank
            }
        }
    }

    //==============================================================================================
    // INNER CLASSES

    public class GameLoop extends Thread {
        private SurfaceHolder surfaceHolder; // for manipulating canvas
        private boolean threadIsRunning = true; // running by default

        // initializes the surface holder
        public GameLoop(SurfaceHolder holder)
        {
            surfaceHolder = holder;
            setName("GameLoop");
        }

        // changes running state
        public void setRunning(boolean running)
        {
            threadIsRunning = running;
        }

        // controls the game loop
        @Override
        public void run()
        {
            Canvas canvas = null; // used for drawing
            long previousFrameTime = System.currentTimeMillis();

            while (threadIsRunning)
            {
                try
                {
                    // get Canvas for exclusive drawing from this thread
                    canvas = surfaceHolder.lockCanvas(null);

                    // lock the surfaceHolder for drawing
                    synchronized(surfaceHolder)
                    {
                        long currentTime = System.currentTimeMillis();
                        double elapsedTimeMS = currentTime - previousFrameTime;
                        totalElapsedTime += elapsedTimeMS / 1000.0;

                        updatePositions(totalElapsedTime);
                        drawGameElements(canvas);

                        previousFrameTime = currentTime; // update previous time
                    }
                }
                finally
                {
                    // display canvas's contents on the CannonView
                    // and enable other threads to use the Canvas
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } // end while
        } // end method run
    }

}

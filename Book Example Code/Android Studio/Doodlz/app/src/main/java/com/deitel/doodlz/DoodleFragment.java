// DoodleFragment.java
// Fragment in which the DoodleView is displayed
package com.deitel.doodlz;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class DoodleFragment extends Fragment
{
   private DoodleView doodleView; // handles touch events and draws
   private float acceleration; 
   private float currentAcceleration; 
   private float lastAcceleration; 
   private boolean dialogOnScreen = false;
   
   // value used to determine whether user shook the device to erase
   private static final int ACCELERATION_THRESHOLD = 100000;

   // called when Fragment's view needs to be created
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
   {
      super.onCreateView(inflater, container, savedInstanceState);    
      View view = 
         inflater.inflate(R.layout.fragment_doodle, container, false);
               
      setHasOptionsMenu(true); // this fragment has menu items to display

      // get reference to the DoodleView
      doodleView = (DoodleView) view.findViewById(R.id.doodleView);
      
      // initialize acceleration values
      acceleration = 0.00f; 
      currentAcceleration = SensorManager.GRAVITY_EARTH;    
      lastAcceleration = SensorManager.GRAVITY_EARTH;    
      return view;
   }
      
   // start listening for sensor events 
   @Override
   public void onStart()
   {
      super.onStart();
      enableAccelerometerListening(); // listen for shake            
   }

   // enable listening for accelerometer events
   public void enableAccelerometerListening()
   {
      // get the SensorManager
      SensorManager sensorManager = 
         (SensorManager) getActivity().getSystemService(
            Context.SENSOR_SERVICE);

      // register to listen for accelerometer events
      sensorManager.registerListener(sensorEventListener, 
         sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
         SensorManager.SENSOR_DELAY_NORMAL);
   } 
   
   // stop listening for sensor events
   @Override
   public void onPause()
   {
      super.onPause();
      disableAccelerometerListening(); // stop listening for shake 
   }
 
   // disable listening for accelerometer events
   public void disableAccelerometerListening()
   {
      // get the SensorManager
      SensorManager sensorManager = 
         (SensorManager) getActivity().getSystemService(
            Context.SENSOR_SERVICE);

      // stop listening for accelerometer events
      sensorManager.unregisterListener(sensorEventListener, 
         sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));         
   } 

   // event handler for accelerometer events
   private SensorEventListener sensorEventListener = 
      new SensorEventListener()
      {
         // use accelerometer to determine whether user shook device 
         @Override
         public void onSensorChanged(SensorEvent event)
         {  
            // ensure that other dialogs are not displayed
            if (!dialogOnScreen)
            {
               // get x, y, and z values for the SensorEvent
               float x = event.values[0];
               float y = event.values[1];
               float z = event.values[2];
      
               // save previous acceleration value
               lastAcceleration = currentAcceleration;
      
               // calculate the current acceleration
               currentAcceleration = x * x + y * y + z * z;
      
               // calculate the change in acceleration
               acceleration = currentAcceleration * 
                  (currentAcceleration - lastAcceleration);
      
               // if the acceleration is above a certain threshold
               if (acceleration > ACCELERATION_THRESHOLD)
                  confirmErase();
            } 
         } // end method onSensorChanged
      
         // required method of interface SensorEventListener
         @Override
         public void onAccuracyChanged(Sensor sensor, int accuracy)
         {
         } 
      }; // end anonymous inner class 
   
   // confirm whether image should be erased
   private void confirmErase()
   {
      EraseImageDialogFragment fragment = new EraseImageDialogFragment();
      fragment.show(getFragmentManager(), "erase dialog");
   } // end method confirmErase

   // display this fragment's menu items
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
   {
      super.onCreateOptionsMenu(menu, inflater);
      inflater.inflate(R.menu.doodle_fragment_menu, menu);
   }

   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      // switch based on the MenuItem id
      switch (item.getItemId()) 
      {
         case R.id.color:
            ColorDialogFragment colorDialog = new ColorDialogFragment();      
            colorDialog.show(getFragmentManager(), "color dialog");
            return true; // consume the menu event
         case R.id.lineWidth:
            LineWidthDialogFragment widthdialog = 
               new LineWidthDialogFragment();      
            widthdialog.show(getFragmentManager(), "line width dialog");
            return true; // consume the menu event
         case R.id.eraser:
            doodleView.setDrawingColor(Color.WHITE); // line color white
            return true; // consume the menu event
         case R.id.clear:
            confirmErase(); // confirm before erasing image
            return true; // consume the menu event
         case R.id.save:     
            doodleView.saveImage(); // save the current image
            return true; // consume the menu event
         case R.id.print:     
            doodleView.printImage(); // print the current images
            return true; // consume the menu event
      } // end switch

      return super.onOptionsItemSelected(item); // call super's method
   } // end method onOptionsItemSelected
   
   // returns the DoodleView
   public DoodleView getDoodleView()
   {
      return doodleView;
   }

   // indicates whether a dialog is displayed
   public void setDialogOnScreen(boolean visible)
   {
      dialogOnScreen = visible;  
   }
}

/**************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               * 
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     * 
 * best efforts in preparing the book. These efforts include the          * 
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       * 
 * no warranty of any kind, expressed or implied, with regard to these    * 
 * programs or to the documentation contained in these books. The authors * 
 * and publisher shall not be liable in any event for incidental or       * 
 * consequential damages in connection with, or arising out of, the       * 
 * furnishing, performance, or use of these programs.                     *
 **************************************************************************/


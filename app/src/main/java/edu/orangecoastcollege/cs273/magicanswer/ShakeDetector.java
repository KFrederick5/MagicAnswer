package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by kfrederick5 on 10/27/2016.
 */

public class ShakeDetector implements SensorEventListener {

    // Constant to represent a shake threshold
    private static final float SHAKE_THRESHOLD = 25f;
    //Constant to rep how long between shakes (milliseconds)
    private static final int SHAKE_TIME_LAPSE = 5000;

    //What was last time event occurred
    private long timeOfLastShake;
    //Define a listener to register onShake events
    private OnShakeListener shakeListener;

    // Constructor to create a new ShakeDetector passing in an OnShakeListener as arg
    public ShakeDetector(OnShakeListener listener)
    {
        shakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //Determine if the event is an accelerometer
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            //Get the x, y, and z values when event occurs
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            //Compare all 3 values against gravity
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            // Compute sum of squares
            double vector = Math.pow(gForceX, 2.0) + Math.pow(gForceY, 2.0) + Math.pow(gForceZ,2.0);

            // Compute g
            float gForce = (float) Math.sqrt(vector);

            // Compare gForce against the threshold
            if(gForce > SHAKE_THRESHOLD)
            {
                //Get the current time
                long now = System.currentTimeMillis();

                // Compare to see if the current time is at least 2000 milliseconds greater
                // than time of last shake
                if(now - timeOfLastShake > SHAKE_TIME_LAPSE)
                {
                    timeOfLastShake = now;
                    // Register a shake event
                    shakeListener.onShake();
                }

            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //Define our own interface (method for other classes to implement)
    //called onShake()
    //It's the responsibility of Magic Answer activity to implement this method.
    public interface  OnShakeListener {
        void onShake();
    }
}

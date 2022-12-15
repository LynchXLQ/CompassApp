package com.example.compassapp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    private TextView tv_orientation_Azimuth_result;
    private TextView tv_orientation_Pitch_result;
    private TextView tv_orientation_Roll_result;
    private TextView tv_gyroscope_x_result;
    private TextView tv_gyroscope_y_result;
    private TextView tv_gyroscope_z_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tv_orientation_Azimuth_result = findViewById(R.id.tv_orientation_Azimuth_result);
        tv_orientation_Pitch_result = findViewById(R.id.tv_orientation_Pitch_result);
        tv_orientation_Roll_result = findViewById(R.id.tv_orientation_Roll_result);
        tv_gyroscope_x_result = findViewById(R.id.tv_gyroscope_x_result);
        tv_gyroscope_y_result = findViewById(R.id.tv_gyroscope_y_result);
        tv_gyroscope_z_result = findViewById(R.id.tv_gyroscope_z_result);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            // get the angle around the z-axis rotated
            float Azimuth_result = Math.round(event.values[0]);
            float Pitch_result = Math.round(event.values[1]);
            float Roll_result = Math.round(event.values[2]);

            tv_orientation_Azimuth_result.setText(Float.toString(Azimuth_result));
            tv_orientation_Pitch_result.setText(Float.toString(Pitch_result));
            tv_orientation_Roll_result.setText(Float.toString(Roll_result));


            // create a rotation animation (reverse turn degree degrees)
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -Azimuth_result,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            // how long the animation will take place
            ra.setDuration(210);

            // set the animation after the end of the reservation status
            ra.setFillAfter(true);

            // Start the animation
            image.startAnimation(ra);
            currentDegree = -Azimuth_result;
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            tv_gyroscope_x_result.setText(Float.toString(Math.round(event.values[0])));
            tv_gyroscope_y_result.setText(Float.toString(Math.round(event.values[1])));
            tv_gyroscope_z_result.setText(Float.toString(Math.round(event.values[2])));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
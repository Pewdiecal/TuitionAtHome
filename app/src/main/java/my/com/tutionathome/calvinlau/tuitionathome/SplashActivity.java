package my.com.tutionathome.calvinlau.tuitionathome;

/**
 * Created by calvinlau on 27/10/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, ParStuActivity.class);
        startActivity(intent);
        finish();

        Log.i("SPLASH ACTIVITY" ,"LAUNCHED");
    }
}

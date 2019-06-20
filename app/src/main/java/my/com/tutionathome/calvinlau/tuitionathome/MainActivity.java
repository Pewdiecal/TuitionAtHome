package my.com.tutionathome.calvinlau.tuitionathome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button tutor;
    Button students;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        students = (Button)findViewById(R.id.student);
        tutor = (Button)findViewById(R.id.tutor);

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ParStuActivity.class);
                startActivity(intent);

            }
        });

       tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TutorLogin.class);
                startActivity(intent);
            }
        });
    }
}

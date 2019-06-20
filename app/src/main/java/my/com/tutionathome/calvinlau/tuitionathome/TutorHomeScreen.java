package my.com.tutionathome.calvinlau.tuitionathome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TutorHomeScreen extends AppCompatActivity {

    ListView menu;

    String[] menuitem = {
            "About Me",
            "Location",
            "Subject",                                                                       //put data here
            "Education and experience",
            "Rate and availability",
            "My webpage"
    };

    Integer[] imgid2 = {
            R.drawable.ic_assignment_ind_black_24dp1,
            R.drawable.ic_my_location_black_24dp,
            R.drawable.ic_chrome_reader_mode_black_24dp1,
            R.drawable.ic_local_library_black_24dp,
            R.drawable.ic_attach_money_black_24dp,
            R.drawable.ic_public_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home_screen);

        MenuListAdapter adapter = new MenuListAdapter(this, menuitem, imgid2);
        menu = (ListView)findViewById(R.id.menulist);
        menu.setAdapter(adapter);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = menuitem[+position];
                if(Slecteditem.contains("About Me")){

                    Intent intent = new Intent(TutorHomeScreen.this, AboutTutor.class);
                    startActivity(intent);
                }
                if(Slecteditem.contains("Location")){

                    Intent intent = new Intent(TutorHomeScreen.this, TutorMapLocation.class);
                    startActivity(intent);
                }
                if(Slecteditem.contains("Subject")){

                    Intent intent = new Intent(TutorHomeScreen.this, AboutTutor.class);
                    startActivity(intent);
                }
                if(Slecteditem.contains("Education and experience")){

                    Intent intent = new Intent(TutorHomeScreen.this, EducationAndExperience.class);
                    startActivity(intent);
                }
                if(Slecteditem.contains("Rate and availability")){

                    Intent intent = new Intent(TutorHomeScreen.this, RateAndAvailability.class);
                    startActivity(intent);
                }
                if(Slecteditem.contains("My webpage")){

                    Intent intent = new Intent(TutorHomeScreen.this, MyWebpage.class);
                    startActivity(intent);
                }

            }
        });

    }
}

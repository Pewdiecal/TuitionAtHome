package my.com.tutionathome.calvinlau.tuitionathome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class EducationAndExperience extends AppCompatActivity {
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    EditText edtcurrentOcc;
    Spinner yearspin;
    EditText edtexp;
    Spinner highestqual;
    EditText institute;
    EditText fieldofStu;
    EditText academicinfo;
    String year;
    String qualification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_and_experience);

        edtcurrentOcc = (EditText)findViewById(R.id.occupation) ;
        edtexp = (EditText)findViewById(R.id.texp);
        institute = (EditText)findViewById(R.id.nameins);
        fieldofStu = (EditText)findViewById(R.id.fos);
        academicinfo = (EditText)findViewById(R.id.academicin);
        yearspin = (Spinner)findViewById(R.id.ttsince);
        highestqual = (Spinner)findViewById(R.id.qualispin);

        final ArrayAdapter<CharSequence> adapteryear = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        adapteryear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearspin.setAdapter(adapteryear);

        final ArrayAdapter<CharSequence> adapterhighestqual = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);                                      //change sting id
        adapterhighestqual.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        highestqual.setAdapter(adapterhighestqual);



        yearspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                yearspin.setPrompt("Choose One");
            }
        });

        highestqual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qualification = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                highestqual.setPrompt("Choose One");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabexp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {


                            httpclient = new DefaultHttpClient();
                            httppost = new HttpPost("http://192.168.0.4/androidtest/index.php?contactnumber=" + edtcurrentOcc.getText().toString() + "&experience=" + edtexp.getText().toString() + "&yearborn=" + year + "&institute=" + institute.getText().toString() + "&fieldOfStudy" + fieldofStu.getText().toString() + "&accademicInfo" + academicinfo.getText().toString() + "&qualification" + qualification); // make sure the url is correct.
                            response = httpclient.execute(httppost);

                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            final String response = httpclient.execute(httppost, responseHandler);
                            System.out.println("Response : " + response);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // txt.setText("Response from PHP : " + response);
                                    //do code here

                                }
                            });


                        } catch (Exception e) {

                            runOnUiThread(new Runnable() {
                                public void run() {
                                }
                            });
                            System.out.println("Exception : " + e.getMessage());
                        }
                    }
                };

                thread.start();
            }
        });
    }

}

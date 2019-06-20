package my.com.tutionathome.calvinlau.tuitionathome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class MyWebpage extends AppCompatActivity {
    EditText wpagename;
    EditText slogan;
    Switch fbswitch;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_webpage);

        wpagename = (EditText)findViewById(R.id.link);
        slogan = (EditText)findViewById(R.id.editText);
        fbswitch = (Switch)findViewById(R.id.fbbtn);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabweb);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /**  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();**/
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {

                            httpclient = new DefaultHttpClient();
                            httppost = new HttpPost("http://192.168.0.4/androidtest/index.php?wpagename=" + wpagename.getText().toString() + "&slogan=" + slogan.getText().toString() + "&fbComment="); // make sure the url is correct.
                            response = httpclient.execute(httppost);

                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            final String response = httpclient.execute(httppost, responseHandler);

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

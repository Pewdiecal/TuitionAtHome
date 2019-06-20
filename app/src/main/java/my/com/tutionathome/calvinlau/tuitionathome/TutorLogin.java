package my.com.tutionathome.calvinlau.tuitionathome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class TutorLogin extends AppCompatActivity {
    Button signup;
    Button signin;
    EditText email;
    EditText password;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    String url;
    String guid;
    InputStream is;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_login);
        getSupportActionBar().setTitle("Sign In");
        signup = (Button)findViewById(R.id.signupbtn);
        signin = (Button)findViewById(R.id.signinbtn);
        email = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorLogin.this, TutorSignUp.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               verifyUser();
            }
        });
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                verifyUser();

                return true;
            }
        });


    }

    public void verifyUser(){
        pDialog = new ProgressDialog(TutorLogin.this);
        pDialog.setMessage("Verifying");
        pDialog.show();
        pDialog.setCancelable(false);
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try{

                    String mail = email.getText().toString();
                    String pass = password.getText().toString();
                    url = "https://www.tuitionathome.my/inc/myMobileDLdev.php?Operation=chkUserLogin&eMail="+ mail +"&password="+pass;
                    httpclient=new DefaultHttpClient();
                    httppost= new HttpPost("https://www.tuitionathome.my/inc/myMobileDLdev.php?Operation=chkUserLogin&eMail="+ mail +"&password="+pass); // make sure the url is correct.
                    response=httpclient.execute(httppost);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            // txt.setText("Response from PHP : " + response);
                            //do code here
                            if(response.contains("true")){
                                pDialog.dismiss();
                                Toast.makeText(TutorLogin.this,"Login Success", Toast.LENGTH_SHORT).show();
                                email.setText("");
                                password.setText("");
                                Intent intent = new Intent(TutorLogin.this, TutorHomeScreen.class);
                                startActivity(intent);




                            }else{
                                Toast.makeText(TutorLogin.this,"Login failed", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }
                        }
                    });



                }catch(Exception e){

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {

        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }


    }

    public void startSession() {
        BasicHttpParams mHttpParams = new BasicHttpParams();

        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = 15000;
        HttpConnectionParams.setConnectionTimeout(mHttpParams, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 20000;
        HttpConnectionParams.setSoTimeout(mHttpParams, timeoutSocket);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        registry.register(new Scheme("https", sslSocketFactory, 443));

    /*ClientConnectionManager cm = new ThreadSafeClientConnManager(mHttpParams, registry);*/
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(/*cm,*/ mHttpParams);

        RequestQueue requestQueue = Volley.newRequestQueue(TutorLogin.this,new HttpClientStack(defaultHttpClient));
    }

}

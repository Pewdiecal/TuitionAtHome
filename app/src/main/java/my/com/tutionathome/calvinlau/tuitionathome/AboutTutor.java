package my.com.tutionathome.calvinlau.tuitionathome;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class AboutTutor extends AppCompatActivity {
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    EditText ctcnum;
    RadioButton male;
    RadioGroup rsex;
    String test = null;
    String ethnic = null;
    String year = null;
    String marital = null;
    Spinner ethspin;
    Spinner yrborn;
    Spinner Marital;
    ProgressDialog pDialog;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_tutor);
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(AboutTutor.this), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);
        getTutors();
        ctcnum = (EditText) findViewById(R.id.ctcnum);
        ethspin = (Spinner) findViewById(R.id.ethnicspin);
        yrborn = (Spinner) findViewById(R.id.yearspin);
        Marital = (Spinner) findViewById(R.id.maritalspin);

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ethnic_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        ethspin.setAdapter(adapter);

        final ArrayAdapter<CharSequence> adapteryear = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        adapteryear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yrborn.setAdapter(adapteryear);

        final ArrayAdapter<CharSequence> adaptermarital = ArrayAdapter.createFromResource(this,
                R.array.marital_array, android.R.layout.simple_spinner_item);
        adaptermarital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Marital.setAdapter(adaptermarital);

        ethspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ethnic = (String) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ethspin.setPrompt("Pick One");

            }
        });

        yrborn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                yrborn.setPrompt("");
            }
        });

        Marital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                marital = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Marital.setPrompt("");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fababout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Done", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });
    }
    public void getTutors(){
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(AboutTutor.this), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        url = "https://www.tuitionathome.my/inc/myMobileDLdev.php?Operation=getAboutMe";
// Creating volley request obj
        pDialog = new ProgressDialog(AboutTutor.this);
        pDialog.setMessage("Hold On....");
        pDialog.show();
        pDialog.setCancelable(false);
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Respond : ", response.toString());
                pDialog.dismiss();
                int totalRecords = 60;
                // Parsing json
                for (int i = -1; i < totalRecords; i++) {
                    try {

                        JSONArray obj = response.getJSONArray("Row");
                        JSONObject jsonObject = obj.getJSONObject(i);
                        final String contact = jsonObject.getString("ContactNo");
                        final String sex = jsonObject.getString("Sex");
                        final String year = jsonObject.getString("YearID");
                        final String maritalStatus = jsonObject.getString("MaritalStatusDesc");
                        final String ethnic = jsonObject.getString("EthnicDesc");
                        final String BornInYear = jsonObject.getString("BornInYear");

                        JSONObject RsYear = response.getJSONObject("RsYear");
                        JSONArray yearRow = RsYear.getJSONArray("Rows");
                        JSONObject JSONobject = yearRow.getJSONObject(i);
                        String arraytest = JSONobject.getString("RecId");

                        int convertedVal = Integer.parseInt(arraytest);
                        Log.i("RecID",arraytest+year+ethnic);


                        if(sex.contains("M")){

                        }

                       /** for(int i2 = 1; i2 < convertedVal; i++){
                            int age = (1943 + i2)-1;

                        }**/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();

                new AlertDialog.Builder(AboutTutor.this)
                        .setTitle("Error")
                        .setMessage("Internet connection error")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                            }
                        })
                        .show();


            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }



}
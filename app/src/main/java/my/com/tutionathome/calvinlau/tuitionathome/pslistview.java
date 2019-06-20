package my.com.tutionathome.calvinlau.tuitionathome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pslistview extends AppCompatActivity {

    EditText searchedt;
    String url = null;
    String lat;
    String lng;
    ListView list;
    Button btn;
    List<String> jsonarrydata = new ArrayList<>();
    List<String> tutorId = new ArrayList<>();
    List<String> tutorDistance = new ArrayList<>();
    boolean datachecker = true;
    private ProgressDialog pDialog;
    private List<tutorMData> tutorList = new ArrayList<tutorMData>();
    private TutorCustomListAdapter adapter;
    PlaceAutocompleteFragment autocompleteFragmentlist;

    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.i("longtest", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("longtest", str);
    }
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pslistview);

        datachecker = false;
        list = (ListView) findViewById(R.id.listView);
        searchedt = (EditText) findViewById(R.id.searchparlist);
        searchedt.setInputType(InputType.TYPE_CLASS_TEXT);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(searchedt, InputMethodManager.SHOW_FORCED);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_appbar);
        adapter = new TutorCustomListAdapter(pslistview.this, tutorList);
        list.setAdapter(adapter);

        final InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isSpaceChar(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        searchedt.setFilters(new InputFilter[]{filter});

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pslistview.this);
        SharedPreferences.Editor editor = preferences.edit();
        lng = preferences.getString("lng", "");
        lat = preferences.getString("lat","");
        editor.remove("tutordistance");
        searchedt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                getTutors();
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String testitem = jsonarrydata.get(+position);
                String tutorIds = tutorId.get(+position);
                String tutorDs = tutorDistance.get(+position);


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pslistview.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("itemname", testitem);
                editor.putString("TTID", tutorIds);
                editor.putString("tutordistance", tutorDs);
                editor.apply();

                Intent intent = new Intent(pslistview.this, TutorView.class);
                startActivity(intent);
            }
        });

        btn = (Button) findViewById(R.id.searchlistbtn);
        autocompleteFragmentlist = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragmentlist);

        autocompleteFragmentlist.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();
                System.out.print("Place: " + place.getName() + placeDetailsStr);

                url = "http://192.168.0.8/androidtest/inc/myMobileDL.php?Operation=getTutors&lat=" + String.valueOf(place.getLatLng().latitude) + "&lng=" + String.valueOf(place.getLatLng().longitude) + "&search=";

                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pslistview.this);
                final SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lat1", String.valueOf(place.getLatLng().latitude));
                editor.putString("lng2", String.valueOf(place.getLatLng().longitude));
                editor.putString("cityname", String.valueOf(place.getName()));
                editor.apply();

                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.print("An erroe occured " + status);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (lat.isEmpty()) {
                    Snackbar.make(view, "Please select your loaction", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getTutors&lat=" + lat + "&lng=" + lng + "&search=" + searchedt.getText().toString();
                    list.setAdapter(null);
                    tutorList.clear();
                    jsonarrydata.clear();
                    tutorId.clear();
// Creating volley request obj
                    pDialog = new ProgressDialog(pslistview.this);
                    pDialog.setMessage("Hold On....");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url,
                            null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("respond test length", response.toString());
                            longInfo(response.toString());
                            adapter.notifyDataSetChanged();
                            list.setAdapter(adapter);
                            pDialog.dismiss();
                            jsonarrydata.clear();
                            int totalrecords = 1;
                            String test = null;
                            // Parsing json
                            for (int i = 0; i < totalrecords; i++) {
                                try {


                                    JSONArray obj = response.getJSONArray("dataTable");
                                    JSONObject jsonObject = obj.getJSONObject(i);
                                    tutorMData movie = new tutorMData();
                                    jsonarrydata.add(jsonObject.getString("userName"));
                                    tutorId.add(jsonObject.getString("recId"));
                                    tutorDistance.add(jsonObject.getString("distance"));
                                    movie.setTitle(jsonObject.getString("userName"));
                                    movie.setSubject(jsonObject.getString("subject"));
                                    movie.setDistance(jsonObject.getString("distance") +"KM");
                                    movie.setThumbnailUrl(jsonObject.getString("photoPath"));
                                    totalrecords = Integer.valueOf(response.getString("recordsTotal"));
                                    Log.i("Obj", response.getString("recordsTotal"));

                                    //  movie.setRating(((Number) jsonObject.get("rati"))
                                    //        .doubleValue());
                                    //movie.setYear(jsonObject.getInt("releaseYear"));

                                    // Genre is json array
                                    //JSONArray genreArry = obj.getJSONArray("");
                                    // ArrayList<String> genre = new ArrayList<String>();
                                    //  for (int j = 0; j < genreArry.length(); j++) {
                                    //     genre.add((String) genreArry.get(j));
                                    //  }
                                    // movie.setGenre(genre);

                                    // adding movie to movies array
                                    tutorList.add(movie);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            pDialog.dismiss();

                            new AlertDialog.Builder(pslistview.this)
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
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void getTutors(){

        url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getTutors&lat=" + lat + "&lng=" + lng + "&search=" + searchedt.getText().toString();
        list.setAdapter(null);
        tutorList.clear();
        jsonarrydata.clear();
        tutorId.clear();
// Creating volley request obj
        pDialog = new ProgressDialog(pslistview.this);
        pDialog.setMessage("Hold On....");
        pDialog.show();
        pDialog.setCancelable(false);
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("respond test length", response.toString());
                longInfo(response.toString());
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);
                pDialog.dismiss();
                jsonarrydata.clear();
                int totalrecords = 1;
                String test = null;
                // Parsing json
                for (int i = 0; i < totalrecords; i++) {
                    try {


                        JSONArray obj = response.getJSONArray("dataTable");
                        JSONObject jsonObject = obj.getJSONObject(i);
                        tutorMData movie = new tutorMData();
                        jsonarrydata.add(jsonObject.getString("userName"));
                        tutorId.add(jsonObject.getString("recId"));
                        tutorDistance.add(jsonObject.getString("distance"));
                        movie.setTitle(jsonObject.getString("userName"));
                        movie.setSubject(jsonObject.getString("subject"));
                        movie.setDistance(jsonObject.getString("distance") +"KM");
                        movie.setThumbnailUrl(jsonObject.getString("photoPath"));
                        totalrecords = Integer.valueOf(response.getString("recordsTotal"));
                        Log.i("Obj", response.getString("recordsTotal"));

                        //  movie.setRating(((Number) jsonObject.get("rati"))
                        //        .doubleValue());
                        //movie.setYear(jsonObject.getInt("releaseYear"));

                        // Genre is json array
                        //JSONArray genreArry = obj.getJSONArray("");
                        // ArrayList<String> genre = new ArrayList<String>();
                        //  for (int j = 0; j < genreArry.length(); j++) {
                        //     genre.add((String) genreArry.get(j));
                        //  }
                        // movie.setGenre(genre);

                        // adding movie to movies array
                        tutorList.add(movie);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();

                new AlertDialog.Builder(pslistview.this)
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

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pslistview.this);
        SharedPreferences.Editor editor = preferences.edit();
        String cityname = preferences.getString("cityname","");

        autocompleteFragmentlist.setText(cityname);
        getTutors();
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
}

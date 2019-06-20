package my.com.tutionathome.calvinlau.tuitionathome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ParStuActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button searchbtn;
    EditText searchedt2;
    public int locupdater =0;
    String lat = null;
    String lng = null;
    String url23;
    int dcounter;
    double alongitude;
    double alatitude;
    private ProgressDialog pDialog;
    private GoogleMap maps;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private String url = null;
    Location mLastLocation;
    Location location3;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    PlaceAutocompleteFragment autocompleteFragment;
    Geocoder geocoder;
    List<android.location.Address> addresses;
    // URL to get contacts JSON

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_stu);

        dcounter = 0;
        searchbtn = (Button) findViewById(R.id.searchbtn);
        searchedt2 = (EditText) findViewById(R.id.searchpar);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        searchedt2.setInputType(InputType.TYPE_CLASS_TEXT);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(searchedt2, InputMethodManager.SHOW_FORCED);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_appbar2);
        buildGoogleApiClient();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(locupdater == 0){
            if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

            }else{
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1, 0, this);
            }
        }else{
            lm.removeUpdates(this);
        }

        if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        }else{
            Toast.makeText(this, "Searching for location ...", Toast.LENGTH_LONG).show();
            if(mLastLocation !=null){
                alatitude = mLastLocation.getLatitude();
                alongitude = mLastLocation.getLongitude();

                try {
                    addresses = geocoder.getFromLocation(alatitude, alongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


                if (addresses == null || addresses.size() == 0) {
                    Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    autocompleteFragment.setText(city);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ParStuActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cityname", city);
                    editor.putString("lat", lat);
                    editor.putString("lng", lng);
                    editor.apply();
                }

            }

        }


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        searchedt2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                getTutors();

                return true;
            }
        });

        InputFilter filter = new InputFilter() {
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

        searchedt2.setFilters(new InputFilter[]{filter});

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getTutors&lat=" + lat + "&lng=" + lng + "&search=" + searchedt2.getText().toString();
                MapFragment mapFragment = (MapFragment) getFragmentManager()
                        .findFragmentById(R.id.maps);
                mapFragment.getMapAsync(ParStuActivity.this);
// Creating volley request obj
                maps.clear();
                pDialog = new ProgressDialog(ParStuActivity.this);
                pDialog.setMessage("Hold On....");
                pDialog.show();
                pDialog.setCancelable(false);
                JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Respond : ", response.toString());
                        pDialog.dismiss();
                        int totalRecords = 1;
                        // Parsing json
                        for (int i = -1; i < totalRecords; i++) {
                            try {

                                final int i2 = 0;

                                JSONArray obj = response.getJSONArray("dataTable");
                                JSONObject jsonObject = obj.getJSONObject(i);
                                final String markerurl = jsonObject.getString("iconPath");
                                double lat3 = jsonObject.getDouble("lat");
                                double lng3 = jsonObject.getDouble("lng");
                                final String title = jsonObject.getString("userName");
                                final String subtitle = jsonObject.getString("address");
                                final LatLng sydney = new LatLng(lat3, lng3);


                                if (markerurl.contains("null")) {
                                    maps.addMarker(new MarkerOptions()
                                            .title(title)
                                            .snippet(subtitle)
                                            .position(sydney));
                                } else {
                                    final Bitmap[] bmp = new Bitmap[1];
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            URL url;
                                            try {
                                                url = new URL(markerurl);
                                                bmp[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Marker marker = maps.addMarker(new MarkerOptions()
                                                            .position(sydney)
                                                            .title(title)
                                                            .snippet(subtitle)
                                                            .icon(BitmapDescriptorFactory.fromBitmap(bmp[0].createScaledBitmap(bmp[0], 100, 100, false))));
                                                    mHashMap.put(marker, i2);

                                                }
                                            });
                                        }
                                    });
                                    thread.start();

                                }


                                totalRecords = Integer.valueOf(response.getString("recordsTotal"));
                                Log.i("Total records", response.getString("recordsTotal"));

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

                        new AlertDialog.Builder(ParStuActivity.this)
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
        });



        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();
                System.out.print("Place: " + place.getName() + placeDetailsStr);


                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


                if (addresses == null || addresses.size() == 0) {
                    Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    autocompleteFragment.setText(city);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ParStuActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cityname", city);
                    editor.putString("lat", lat);
                    editor.putString("lng", lng);
                    editor.apply();
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.maps);
                    mapFragment.getMapAsync(ParStuActivity.this);
                    getTutors();
                }


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.print("An error occured " + status);

            }
        });


    }


    @Override
    public void onMapReady(final GoogleMap map) {

        if (lat == null && lng == null) {
            System.out.print("location null");
        } else {
            double latd = Double.parseDouble(lat);
            double lngd = Double.parseDouble(lng);
            LatLng testloc = new LatLng(latd, lngd);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(testloc, 13));
        }


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                final String username = marker.getTitle();
                final double lat2 = marker.getPosition().latitude;
                final double lng2 = marker.getPosition().longitude;

                url23 = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getTutors&lat=" + lat2 + "&lng=" + lng2 + "&search=" + username.replace(" ", "%20");
                Log.i("URL23", url23);

                Log.i("Lat", String.valueOf(lat2));
                Log.i("lng", String.valueOf(lng2));
// Creating volley request obj
                pDialog = new ProgressDialog(ParStuActivity.this);
                pDialog.setMessage("Hold On....");
                pDialog.show();
                pDialog.setCancelable(false);
                JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url23,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("respond test length", response.toString());
                        pDialog.dismiss();
                        Intent intent = new Intent(ParStuActivity.this, TutorView.class);
                        startActivity(intent);

                        // Parsing json
                        for (int i = 0; i < 1; i++) {
                            try {

                                JSONArray obj = response.getJSONArray("dataTable");
                                JSONObject jsonObject = obj.getJSONObject(i);
                                String ttID = jsonObject.getString("recId");

                                Log.i("ttID test", ttID);
                                Log.i("lat", String.valueOf(lat2));
                                Log.i("lng", String.valueOf(lng2));
                                Log.i("USERNAME", username.toString());

                                LatLng distanceloc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                LatLng markerloc = new LatLng(lat2, lng2);
                                CalculationByDistance(distanceloc, markerloc);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ParStuActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("TTID", ttID);
                                editor.putString("itemname", username.toString());
                                editor.apply();


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

                        new AlertDialog.Builder(ParStuActivity.this)
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
        });
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
       if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?


                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(ParStuActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        2);


                // No explanation needed, we can request the permission.



                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        } else {
            map.setMyLocationEnabled(true);

        }


        maps = map;
    }

    /**
     * Async task class to get json by making HTTP call
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_list:
                Intent intent = new Intent(ParStuActivity.this, pslistview.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.maps);
                    mapFragment.getMapAsync(ParStuActivity.this);
                    if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                    }else {
                       maps.setMyLocationEnabled(true);
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1, 0, this);
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1, 0, this);
                        if (mLastLocation != null) {
                            alatitude = mLastLocation.getLatitude();
                            alongitude = mLastLocation.getLongitude();
                            LatLng aloc = new LatLng(alatitude, alongitude);
                            maps.animateCamera(CameraUpdateFactory.newLatLngZoom(aloc, 13));
                        }

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ParStuActivity.this, "Permission access to current locarion denied", Toast.LENGTH_LONG).show();
                }


                break;
            }
        }
        // other 'case' lines to check for other
        // permissions this app might request

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        LocationManager locManager = (LocationManager)getSystemService(
                Context.LOCATION_SERVICE);

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locManager.getBestProvider(crit, true);

        if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


        } else {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                alatitude = mLastLocation.getLatitude();
                alongitude = mLastLocation.getLongitude();

                lat = String.valueOf(mLastLocation.getLatitude());
                lng = String.valueOf(mLastLocation.getLongitude());


            }
        }

        if (dcounter == 0) {
            if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

            } else {
                dcounter++;

                LatLng aloc = new LatLng(alatitude, alongitude);
                maps.animateCamera(CameraUpdateFactory.newLatLngZoom(aloc, 13));
                try {
                    addresses = geocoder.getFromLocation(alatitude, alongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


                if (addresses == null || addresses.size() == 0) {
                    Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    autocompleteFragment.setText(city);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ParStuActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cityname", city);
                    editor.putString("lat", lat);
                    editor.putString("lng", lng);
                    editor.apply();
                    getTutors();
                }


            }
        } else {

        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        location3 = location;
        alatitude = location.getLatitude();
        alongitude = location.getLongitude();
        lng = String.valueOf(location.getLongitude());
        lat = String.valueOf(location.getLatitude());
        try {
            addresses = geocoder.getFromLocation(alatitude, alongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            autocompleteFragment.setText(city);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ParStuActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("cityname", city);
            editor.putString("lat", lat);
            editor.putString("lng", lng);
            editor.apply();
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


            }else{
                lm.removeUpdates(this);
                locupdater ++;
                Log.i("UPDATE STATUS", "REMOVED");
            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


                alatitude = location3.getLatitude();
                alongitude = location3.getLongitude();
                lng = String.valueOf(location3.getLongitude());
                lat = String.valueOf(location3.getLatitude());
                try {
                    addresses = geocoder.getFromLocation(alatitude, alongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


                if (addresses == null || addresses.size() == 0) {
                    Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    autocompleteFragment.setText(city);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ParStuActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("cityname", city);
                    editor.putString("lat", lat);
                    editor.putString("lng", lng);
                    editor.apply();
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {


                    }else{
                        lm.removeUpdates(ParStuActivity.this);
                        locupdater ++;
                        Log.i("UPDATE STATUS", "REMOVED");
                    }
                }


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    synchronized void buildGoogleApiClient() {



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }
    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mGoogleApiClient.disconnect();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


        }else{
            lm.removeUpdates(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


        }else{
            lm.removeUpdates(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locupdater==0){
            if (ContextCompat.checkSelfPermission(ParStuActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


            }else{
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1, 0, this);
            }
        }else{
            lm.removeUpdates(this);
        }

    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        String disttt = String.format("%.1f", valueResult);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ParStuActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tutordistance", disttt);
        editor.apply();


        return Radius * c;
    }

public void getTutors(){

    MapFragment mapFragment = (MapFragment) getFragmentManager()
            .findFragmentById(R.id.maps);
    mapFragment.getMapAsync(ParStuActivity.this);
    url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getTutors&lat=" + lat + "&lng=" + lng + "&search=" + searchedt2.getText().toString();
// Creating volley request obj
    maps.clear();
    pDialog = new ProgressDialog(ParStuActivity.this);
    pDialog.setMessage("Hold On....");
    pDialog.show();
    pDialog.setCancelable(false);
    JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url,
            null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.i("Respond : ", response.toString());
            pDialog.dismiss();
            int totalRecords = 1;
            // Parsing json
            for (int i = -1; i < totalRecords; i++) {
                try {

                    final int i2 = 0;

                    JSONArray obj = response.getJSONArray("dataTable");
                    JSONObject jsonObject = obj.getJSONObject(i);
                    final String markerurl = jsonObject.getString("iconPath");
                    double lat3 = jsonObject.getDouble("lat");
                    double lng3 = jsonObject.getDouble("lng");
                    final String title = jsonObject.getString("userName");
                    final String subtitle = jsonObject.getString("address");
                    final LatLng sydney = new LatLng(lat3, lng3);


                    if (markerurl.contains("null")) {
                        maps.addMarker(new MarkerOptions()
                                .title(title)
                                .snippet(subtitle)
                                .position(sydney));
                    } else {
                        final Bitmap[] bmp = new Bitmap[1];
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                URL url;
                                try {
                                    url = new URL(markerurl);
                                    bmp[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Marker marker = maps.addMarker(new MarkerOptions()
                                                .position(sydney)
                                                .title(title)
                                                .snippet(subtitle)
                                                .icon(BitmapDescriptorFactory.fromBitmap(bmp[0].createScaledBitmap(bmp[0], 100, 100, false))));
                                        mHashMap.put(marker, i2);

                                    }
                                });
                            }
                        });
                        thread.start();

                    }


                    totalRecords = Integer.valueOf(response.getString("recordsTotal"));
                    Log.i("Total records", response.getString("recordsTotal"));

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

            new AlertDialog.Builder(ParStuActivity.this)
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
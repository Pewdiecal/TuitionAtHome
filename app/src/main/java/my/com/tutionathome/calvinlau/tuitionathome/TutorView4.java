package my.com.tutionathome.calvinlau.tuitionathome;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class TutorView4 extends Fragment {
    private String url =null;
    private ProgressDialog pDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public TutorView4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_tutor_view4, container, false);


        final TextView txtPh = (TextView)rootView.findViewById(R.id.txtPHN);
        final TextView txtAd = (TextView)rootView.findViewById(R.id.txtADD);
        final TextView txtDs = (TextView)rootView.findViewById(R.id.txtD);
        final TextView txtdata4 = (TextView)rootView.findViewById(R.id.textViewdata4);
       // txtdata4.setMovementMethod(new ScrollingMovementMethod());
        final NetworkImageView thumbNail = (NetworkImageView) rootView
                .findViewById(R.id.thumbnailforTView3);

        imageLoader = AppController.getInstance().getImageLoader();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String tutorids = preferences.getString("tutorID", "");
        final String tutordistace = preferences.getString("tutordistance","");
        String ttID = preferences.getString("TTID","");


        url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getSingleTutor&tutorId="+ttID.toString();


        Log.i("RECid" ,ttID.toString());
// Creating volley request obj
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Hold On....");
        pDialog.show();
        pDialog.setCancelable(false);
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("respond test length" , response.toString());
                pDialog.dismiss();
                int totalrecords = 2 ;
                String test = null;
                // Parsing json
                for (int i = 0; i < 1; i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("Data");
                        String phnumber = jsonObject.getString("ContactNo");
                        String address = jsonObject.getString("Address");
                        String photopath = jsonObject.getString("PhotoPath");
                        String status = jsonObject.getString("StudentQuotaDesc");
                        String atmyhome = jsonObject.getString("AtMyHome");
                        String cansmallgp = jsonObject.getString("CanSmallGroup");
                        String minrate = jsonObject.getString("minRate");
                        String maxrate = jsonObject.getString("maxRate");
                        String tagline = jsonObject.getString("WebHeadline");
                        String RateRemark = jsonObject.getString("RateRemark");
                        String gotPromo = jsonObject.getString("gotPromo");
                        String remarkPromo = jsonObject.getString("remarkPromo");
                        String D10 = jsonObject.getString("D10");
                        String D11 = jsonObject.getString("D11");
                        String D12 = jsonObject.getString("D12");
                        String D20 = jsonObject.getString("D20");
                        String D21 = jsonObject.getString("D21");
                        String D22 = jsonObject.getString("D22");
                        String D30 = jsonObject.getString("D30");
                        String D31 = jsonObject.getString("D31");
                        String D32 = jsonObject.getString("D32");
                        String D40 = jsonObject.getString("D40");
                        String D41 = jsonObject.getString("D41");
                        String D42 = jsonObject.getString("D42");
                        String D50 = jsonObject.getString("D50");
                        String D51 = jsonObject.getString("D50");
                        String D52 = jsonObject.getString("D52");
                        String D60 = jsonObject.getString("D60");
                        String D61 = jsonObject.getString("D61");
                        String D62 = jsonObject.getString("D62");
                        String D70 = jsonObject.getString("D70");
                        String D71 = jsonObject.getString("D71");
                        String D72 = jsonObject.getString("D72");








                        thumbNail.setImageUrl(photopath,imageLoader);
                        txtAd.setText(address.replace("null",""));
                        txtPh.setText(phnumber.replace("null",""));
                        txtDs.setText("Distance : "+tutordistace+ " KM");
                        txtdata4.setText("Availability & Rate \n\n");
                        txtdata4.append("Status "+"\n" + status.replace("null",""));
                        if(tagline.equals("null")){

                        }else{
                            txtDs.append("\n"+tagline);
                        }

                        if(atmyhome.equals("1")){
                            txtdata4.append(" Tuition at my place also available. ");
                        }
                        if(cansmallgp.equals("1")){
                            txtdata4.append("Group tuition also can be arranged.");
                        }
                        if(minrate.contains("null")|| maxrate.contains("null")){

                        }else{
                            txtdata4.append("\n" + "\n"+ "1-to-1 tuition rate"+"\nRM" + minrate + " to RM" + maxrate);
                        }

                        txtdata4.append("\n"+ RateRemark.replace("null",""));

                        if(gotPromo.contains("1")){
                            txtdata4.append("\n\nSpecial promotion\n");
                            txtdata4.append(remarkPromo);
                        }

                        if (D10.contains("null")&&D11.contains("null")&&D12.contains("null")){

                        }else {
                            txtdata4.append("\n" + "\n" + "Monday");
                            if (D10.contains("1")) {

                                txtdata4.append("\n" + "8-12 Noon");
                            }
                            if (D11.contains("1")) {

                                txtdata4.append("\n" + "12-5 PM");
                            }
                            if (D12.contains("1")) {

                                txtdata4.append("\n" + "5-10 PM");
                            }
                        }

                        if(D20.contains("null")&&D21.contains("null")&&D22.contains("null")){

                        }else {
                            txtdata4.append("\n \n" + "Tuesday");
                            if (D20.contains("1")) {

                                txtdata4.append("\n" + "8-12 Noon");
                            }
                            if (D21.contains("1")) {

                                txtdata4.append("\n" + "12-5 PM");

                            }
                            if (D22.contains("1")) {

                                txtdata4.append("\n" + "5-10 PM");
                            }
                        }

                        if(D30.contains("null")&&D31.contains("null")&&D32.contains("null")){

                        }else {
                            txtdata4.append("\n \n" + "Wednesday");
                            if (D30.contains("1")) {

                                txtdata4.append("\n" + "8-12 Noon");
                            }
                            if (D31.contains("1")) {

                                txtdata4.append("\n" + "12-5 PM");
                            }
                            if (D32.contains("1")) {

                                txtdata4.append("\n" + "5-10 PM");
                            }
                        }

                        if(D40.contains("null")&&D41.contains("null")&&D42.contains("null")){

                        }else {
                            txtdata4.append("\n \n" + "Thursday");
                            if (D40.contains("1")) {

                                txtdata4.append("\n" + "8-12 Noon");
                            }
                            if (D41.contains("1")) {

                                txtdata4.append("\n" + "12-5 PM");
                            }
                            if (D42.contains("1")) {

                                txtdata4.append("\n" + "5-10 PM");
                            }
                        }

                        if(D50.contains("null")&&D51.contains("null")&&D52.contains("null")){

                        }else {
                            txtdata4.append("\n \n" + "Friday");
                            if (D50.contains("1")) {

                                txtdata4.append("\n" + "8-12 Noon");
                            }
                            if (D51.contains("1")) {

                                txtdata4.append("\n" + "12-5 PM");
                            }
                            if (D52.contains("1")) {

                                txtdata4.append("\n" + "5-10 PM");
                            }
                        }

                        if(D60.contains("null")&&D61.contains("null")&&D62.contains("null")){

                        }else {
                            txtdata4.append("\n \n" + "Saturday");
                            if (D60.contains("1")) {

                                txtdata4.append("\n" + "8-12 Noon");
                            }
                            if (D61.contains("1")) {

                                txtdata4.append("\n" + "12-5 PM");
                            }
                            if (D62.contains("1")) {

                                txtdata4.append("\n" + "5-10 PM");
                            }
                        }

                        if(D70.contains("null")&&D71.contains("null")&&D72.contains("null")){

                        }else {
                            txtdata4.append("\n \n" + "Sunday");
                            if (D70.contains("1")) {

                                txtdata4.append("\n" + "8-12 Noon");
                            }
                            if (D71.contains("1")) {

                                txtdata4.append("\n" + "12-5 PM");
                            }
                            if (D72.contains("1")) {

                                txtdata4.append("\n" + "5-10 PM");
                            }
                        }

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

                new AlertDialog.Builder(getActivity())
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



        return rootView;
    }
}

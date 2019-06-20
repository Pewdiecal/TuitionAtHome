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

public class TutorView5 extends Fragment {
    private String url =null;
    private ProgressDialog pDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public TutorView5() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_tutor_view5, container, false);

        final TextView txtPh = (TextView)rootView.findViewById(R.id.txtPHN);
        final TextView txtAd = (TextView)rootView.findViewById(R.id.txtADD);
        final TextView txtDs = (TextView)rootView.findViewById(R.id.txtD);
        final TextView txtdata5 = (TextView)rootView.findViewById(R.id.textViewdata5);
       // txtdata5.setMovementMethod(new ScrollingMovementMethod());
        final NetworkImageView thumbNail = (NetworkImageView) rootView
                .findViewById(R.id.thumbnailforTView3);

        imageLoader = AppController.getInstance().getImageLoader();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        final String tutordistace = preferences.getString("tutordistance","");

        String ttID = preferences.getString("TTID","");


        url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getSingleTutor&tutorId="+ttID.toString();

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
                // Parsing json
                for (int i = 0; i < 1; i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("Data");
                        String phnumber = jsonObject.getString("ContactNo");
                        String address = jsonObject.getString("Address");
                        String photopath = jsonObject.getString("PhotoPath");
                        String age = jsonObject.getString("Age");
                        String marital = jsonObject.getString("MaritalStatusDesc");
                        String sex = jsonObject.getString("Sex");
                        String ethnic = jsonObject.getString("EthnicDesc");
                        String webpage = jsonObject.getString("WebName");
                        String membersince = jsonObject.getString("ActivationDate");
                        String tagline = jsonObject.getString("WebHeadline");

                        thumbNail.setImageUrl(photopath,imageLoader);
                        txtAd.setText(address.replace("null",""));
                        txtPh.setText(phnumber.replace("null",""));
                        txtDs.setText("Distance : "+tutordistace+ " KM");
                        txtdata5.setText("Tutor Details" + "\n" + "\n"+ "Age "+ "\n" + age.replace("null","") + "\n \n"+ "Marital Status "+"\n" + marital.replace("null","") + "\n \n"+ "Sex "+"\n" + sex.replace("null","") + "\n \n" + "Ethnic "+"\n" + ethnic.replace("null","") + "\n \n" + "Member since "+"\n" + membersince.replace("null",""));

                        if(tagline.equals("null")){

                        }else{
                            txtDs.append("\n"+tagline);
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
        // Inflate the layout for this fragment
        return rootView;
    }
}

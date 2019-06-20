package my.com.tutionathome.calvinlau.tuitionathome;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

public class TutorView3 extends Fragment {
    private String url =null;
    private ProgressDialog pDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public TutorView3() {
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
        View rootView = inflater.inflate(R.layout.activity_tutor_view3, container, false);


        final TextView txtPh = (TextView)rootView.findViewById(R.id.txtPHN);
        final TextView txtAd = (TextView)rootView.findViewById(R.id.txtADD);
        final TextView txtDs = (TextView)rootView.findViewById(R.id.txtD);
        final TextView txtdata = (TextView)rootView.findViewById(R.id.textViewDATA);

       // txtdata.setMovementMethod(new ScrollingMovementMethod());
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
                        String Coccpation = jsonObject.getString("OccupationDesc");
                        String yeartutor = jsonObject.getString("YearsExperience");
                        String higestqualification = jsonObject.getString("HighestQualiDesc");
                        String institution = jsonObject.getString("NameOfUni");
                        String fos = jsonObject.getString("FieldOfStudy");
                        String remark = jsonObject.getString("RateRemark");
                        String tagline = jsonObject.getString("WebHeadline");
                        String workInfo = jsonObject.getString("WorkInfo");
                        String Qualifi = jsonObject.getString("QualiInfo");
                        thumbNail.setImageUrl(photopath,imageLoader);
                        txtAd.setText(address.replace("null",""));
                        txtPh.setText(phnumber.replace("null",""));
                        txtDs.setText("Distance : "+tutordistace+ " KM");

                        txtdata.setText("Experience & Academic Qualification \n \n");
                        if(tagline.equals("null")){

                        }else{
                            txtDs.append("\n"+tagline);
                        }

                        txtdata.append("Current Occupation \n" + Coccpation.replace("null","") +"\n" +"\n"+ "Tutor experience \n" + yeartutor.replace("null","") + " Years "+"\n\n"+ workInfo.replace("null","")+"\n" +"\n" +"Highest qualification \n"+ higestqualification.replace("null","") + "\n"+"\n"+ "Institution \n" +institution.replace("null","")+"\n"+"\n"+ "Field of study \n" + fos.replace("null","") + "\n" + Qualifi.replace("null",""));

                        if(remark.equals("null")||remark.contains("")){

                        }else {
                            txtdata.append("\n" + "\n" + "Remark " +"\n" +remark.replace("null",""));
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

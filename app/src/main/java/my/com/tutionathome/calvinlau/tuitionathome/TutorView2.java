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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TutorView2 extends Fragment {
    private String url =null;
    private ProgressDialog pDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public TutorView2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_tutor_view2, container, false);

        final TextView txtPh = (TextView)rootView.findViewById(R.id.txtPHN);
        final TextView txtAd = (TextView)rootView.findViewById(R.id.txtADD);
        final TextView txtDs = (TextView)rootView.findViewById(R.id.txtD);
        final TextView txtDta = (TextView)rootView.findViewById(R.id.textView37);

        final NetworkImageView thumbNail = (NetworkImageView) rootView
                .findViewById(R.id.thumbnailforTView3);

        imageLoader = AppController.getInstance().getImageLoader();
        //txtDta.setMovementMethod(new ScrollingMovementMethod());



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        final String tutordistace = preferences.getString("tutordistance","");
        String ttID = preferences.getString("TTID","");


     /**   if(tutorids.contains("null")){
            url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getSingleTutor&tutorId="+tutorids.toString();
        }
        if(ttID.contains("null")){
            url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getSingleTutor&tutorId="+ttID.toString();
        }**/

        url = "https://www.tuitionathome.my/inc/myMobileDL.php?Operation=getSingleTutor&tutorId="+ttID.toString();
        Log.i("TUTOR ID", ttID.toString());

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
                for (int i = 0; i < 20; i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("Data");
                        String phnumber = jsonObject.getString("ContactNo");
                        String address = jsonObject.getString("Address");
                        String photopath = jsonObject.getString("PhotoPath");
                       // String distance = jsonObject.getString("userName");
                        JSONArray subject = jsonObject.getJSONArray("Subjects");
                       JSONObject subobj = subject.getJSONObject(i);
                        String subjects = subobj.getString("Subject");
                        String level = subobj.getString("Level");
                        String Wheadline = jsonObject.getString("WebHeadline");

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("phonenumber", phnumber);
                        editor.apply();

                        thumbNail.setImageUrl(photopath,imageLoader);
                        txtPh.setText(phnumber.replace("null",""));
                        txtAd.setText(address.replace("null",""));
                        if(i==1){
                            txtDta.setText("Subjects Taught \n \n");
                        }
                        txtDta.append(subjects.replace("null" ,"") + "\n " + level.replace("null" ,"") +"\n" +"\n");
                        txtDs.setText("Distance : "+ tutordistace +" KM");
                        if(Wheadline.equals("null")){

                        }else{
                            txtDs.append("\n"+Wheadline);
                        }



                       Log.i("Subject TEST", subjects);
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

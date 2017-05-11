package com.example.user.e_rail;


import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.e_rail.DataClass.ApiKey;
import com.example.user.e_rail.Utils.PnrStatusFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PnrStatus extends Fragment {

    private Button checkStatusbtn;
    private Button clearAllbtn;
    private EditText pnr;
    private String pnrNo="";
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progressDialog;

    public PnrStatus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pnr_status, container, false);
        checkStatusbtn=(Button)view.findViewById(R.id.check_status);
        clearAllbtn=(Button)view.findViewById(R.id.clearAll);
        pnr=(EditText)view.findViewById(R.id.pnr_status);
        alertDialog=new AlertDialog.Builder(getActivity());
        progressDialog=new ProgressDialog(getActivity());


        clearAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnr.setText("");
            }
        });

        checkStatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnrNo=pnr.getText().toString().trim();
                if(checkString(pnrNo))
                {
                    if(isNetworkAvailable())
                    loadPnrStatus();
                    else
                        noNetwrokErrorMessage();
                }
                else{
                    displayErrorMessage(pnrNo);
                }

            }
        });

        return view;
    }
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void noNetwrokErrorMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("No internet connection. Please check your internet setting.");
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert=alertDialog.create();
        alert.show();

    }

    public void invalidInputMessage(int code){
        if(code==204) {
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Invalid Pnr No.");
            alertDialog.setCancelable(true);
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
        else if(code==403){
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Request quota for the api is exceeded.");
            alertDialog.setCancelable(true);
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();

        }

        else
        {
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Something went wrong with the server. Please refresh after some time");
            alertDialog.setCancelable(true);
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }

    }


    public boolean checkString(String s)
 {
     boolean isValid=true;
     if(s.isEmpty())
         isValid=false;
     return isValid;
 }

    public boolean checkResponseCode(int code)
    {
        boolean isValid=true;
        if(code!=200)
            isValid=false;
        return isValid;
    }


    public void displayErrorMessage(String s){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please enter a valid 10 digit pnr no and then click check status.");
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert=alertDialog.create();
        alert.show();
    }

    public void loadPnrStatus(){

        String url="http://api.railwayapi.com/pnr_status/pnr/"+pnrNo+"/apikey/"+ ApiKey.API_KEY+"/";
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response","hfhfhfgggg");
                        String trainNo="";
                        String trainName="";
                        String trainInfo="";
                        String source="";
                        String destination="";
                        String dateofjourney="";

                        int countPassenger=0;
                        String chartStatus="";
                        String classCode="";
                        ArrayList<String> status= new ArrayList<>();

                        progressDialog.cancel();
                        try {
                            JSONObject object= new JSONObject(response);
                            int responseCode=object.getInt("response_code");
                            if(checkResponseCode(responseCode)){
                                trainNo=object.getString("train_num");
                                trainName=object.getString("train_name");
                                trainInfo=trainName+"-"+trainNo;
                                dateofjourney=object.getString("doj");
                                JSONObject src=object.getJSONObject("from_station");
                                String name=src.getString("name");
                                String code=src.getString("code");
                                source=name+" "+code;
                                JSONObject dest=object.getJSONObject("to_station");
                                name=dest.getString("name");
                                code=dest.getString("code");
                                destination=name+" "+code;
                                countPassenger=object.getInt("total_passengers");
                                chartStatus=object.getString("chart_prepared");
                                classCode=object.getString("class");
                                JSONArray passengerArray=object.getJSONArray("passengers");
                                for(int i=0;i<passengerArray.length();i++){
                                    JSONObject passObject=passengerArray.getJSONObject(i);
                                    String bookingStatus=passObject.getString("booking_status");
                                    String currentStatus=passObject.getString("current_status");
                                    status.add(i,bookingStatus+"-"+currentStatus);


                                }


                                Log.e("details",trainName+"\n"+trainNo+"\n"+source+"\n"+destination+"\n"+dateofjourney+"\n"+responseCode+"\n"+countPassenger);
                             //   tring train, String src,String dest,String date,String c, int count,String chart,LinkedHashMap<String ,String> status
                                PnrStatusFragment pnrStatusFragment= PnrStatusFragment.newInstance(trainInfo,source,destination,dateofjourney,
                                classCode,countPassenger,chartStatus,status);
                                FragmentManager fragmentManager=getActivity().getFragmentManager();
                                pnrStatusFragment.show(fragmentManager,"pnr");


                            }
                            else
                            {
                                invalidInputMessage(responseCode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("Some problem has occurred with the server. Please try after some time.");
                            alertDialog.setCancelable(true);
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        AlertDialog msg=alertDialog.create();
                        msg.show();


                    }
                });

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }
}

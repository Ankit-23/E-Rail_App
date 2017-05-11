package com.example.user.e_rail;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.e_rail.DataClass.ApiKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeatAvailability2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeatAvailability2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private SeatAvailabilityDialogFragment  seat;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private TextView trainInfo;
    private TextView source;
    private TextView destination;
    private TextView date1;
    private TextView status1;
    private TextView date2;
    private TextView status2;
    private TextView date3;
    private TextView status3;
    private TextView date4;
    private TextView status4;
    private TextView date5;
    private TextView status5;
    private TextView date6;
    private TextView status6;


    // TODO: Rename and change types of parameters
    private String srcStaion;
    private String destStation;

    private String mtrainNo;
    private String mtrainName;
    private String msource;
    private String mdestination;
    private String mclass;
    private String mquota;
    private String mdate;


    public SeatAvailability2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeatAvailability2.
     */
    // TODO: Rename and change types and number of parameters
    public static SeatAvailability2 newInstance(String param1, String param2,String param3,String param4,
                                                String param5,String param6,String param7) {
        SeatAvailability2 fragment = new SeatAvailability2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        args.putString(ARG_PARAM7, param7);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mtrainNo = getArguments().getString(ARG_PARAM1);
            mtrainName = getArguments().getString(ARG_PARAM2);
            srcStaion = getArguments().getString(ARG_PARAM3);
            String [] code1=srcStaion.split("-");
            msource=code1[1];
            destStation= getArguments().getString(ARG_PARAM4);
            String [] code=destStation.split("-");
            mdestination=code[1];
            mclass = getArguments().getString(ARG_PARAM5);
            mquota = getArguments().getString(ARG_PARAM6);
            mdate = getArguments().getString(ARG_PARAM7);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_seat_availability2, container, false);
        progressDialog=new ProgressDialog(getActivity());
        alertDialog=new AlertDialog.Builder(getActivity());
        trainInfo=(TextView)view.findViewById(R.id.trainInfo);
        source=(TextView)view.findViewById(R.id.source);
        destination=(TextView)view.findViewById(R.id.destination);

        date1=(TextView)view.findViewById(R.id.date1);
        status1=(TextView)view.findViewById(R.id.status1);

        date2=(TextView)view.findViewById(R.id.date2);
        status2=(TextView)view.findViewById(R.id.status2);

        date3=(TextView)view.findViewById(R.id.date3);
        status3=(TextView)view.findViewById(R.id.status3);

        date4=(TextView)view.findViewById(R.id.date4);
        status4=(TextView)view.findViewById(R.id.status4);

        date5=(TextView)view.findViewById(R.id.date5);
        status5=(TextView)view.findViewById(R.id.status5);

        date6=(TextView)view.findViewById(R.id.date6);
        status6=(TextView)view.findViewById(R.id.status6);



        Log.e("seat availability",mtrainNo);
        Log.e("seat availability",mtrainName);
        Log.e("seat availability",msource);
        Log.e("seat availability",mdestination);Log.e("seat availability",mclass);
        Log.e("seat availability",mquota);
        Log.e("seat availability",mdate);

        if(isNetworkAvailable())
        new seatAsync().execute();
        else
        errorNetwork();
        return view;

    }

    public boolean isNetworkAvailable(){

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public void errorNetwork(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("No internet connectoion.Please check your internet settings.");
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert1=alertDialog.create();
        alert1.show();

    }

    public boolean checkCode(int code){
        boolean isValid=true;
        if(code!=200)
            isValid=false;
        return isValid;

    }

    public void errorCode(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Problem while fetching results");
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert1=alertDialog.create();
        alert1.show();
    }

    public LinkedHashMap<String ,String > extractData(String jsonResponse) {
        LinkedHashMap<String,String> result=new LinkedHashMap<>();

        try {
            JSONObject jsonObject=new JSONObject(jsonResponse);
            int responseCode=jsonObject.getInt("response_code");
            if(checkCode(responseCode))
            {
                JSONArray availability = jsonObject.getJSONArray("availability");
                for(int i=0;i<availability.length();i++){
                    JSONObject demo=availability.getJSONObject(i);
                    String date=demo.getString("date");
                    String status=demo.getString("status");
                    result.put(date,status);
                }
            }
            else{
                errorCode();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }


    public class seatAsync extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);


        }

        @Override
        protected String doInBackground(Void... params) {
            String response ="";
            URL url=null;
            try {
                url=new URL("http://api.railwayapi.com/check_seat/train/"+mtrainNo+"/source/"+msource+"/dest/"+mdestination+"/date/"+mdate+"/class/"+mclass+"/quota/"+mquota+"/apikey/"+ ApiKey.API_KEY+"/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(15000);

                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line ="";
                StringBuilder responseOutput=new StringBuilder();
                while((line=bufferedReader.readLine())!=null){
                    responseOutput.append(line);
                }

                response =String.valueOf(responseOutput);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("noooooooooooo",response);
            return response;

        }


        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            if(!s.isEmpty()){
                progressDialog.cancel();
                trainInfo.setText(mtrainName+"-"+mtrainNo);
                source.setText(srcStaion);
                destination.setText(destStation);
                LinkedHashMap<String,String> results=extractData(s);








                String demo="";

                for(Map.Entry m:results.entrySet()){
                    String key=(String)m.getKey();
                   String value=(String)m.getValue();
                    demo=demo+key+"z"+value+"\n";
                    Log.e("output",key);
                }

                String [] ankit=demo.split("\n");
                String s1[]=ankit[0].split("z");
                date1.setText(s1[0]);
                status1.setText(s1[1]);

                String s2[]=ankit[1].split("z");
                date2.setText(s2[0]);
                status2.setText(s2[1]);

                String s3[]=ankit[2].split("z");
                date3.setText(s3[0]);
                status3.setText(s3[1]);

                String s4[]=ankit[3].split("z");
                date4.setText(s4[0]);
                status4.setText(s4[1]);

                String s5[]=ankit[4].split("z");
                date5.setText(s5[0]);
                status5.setText(s5[1]);

                String s6[]=ankit[5].split("z");
                date6.setText(s6[0]);
                status6.setText(s6[1]);





            }
            else{
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
        }
    }
}

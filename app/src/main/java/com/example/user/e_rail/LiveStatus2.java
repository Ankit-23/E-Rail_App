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
import com.example.user.e_rail.DataClass.LiveStatusData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LiveStatus2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveStatus2 extends Fragment {

    private TextView source;
    private TextView destination;
    private TextView trainInfo;
    private TextView station;
    private TextView scharr;
    private TextView actarr;
    private TextView delayArr;

    private TextView schdep;
    private TextView actdep;
    private TextView delayDep;
    private TextView lastL;
    private static int flag=0;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4="param4";


    // TODO: Rename and change types of parameters
    private String mName;
    private String mTrainNo;
    private String mDate;
    private String mStation;
    private TextView resulttext;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    public LiveStatus2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveStatus2.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveStatus2 newInstance(String param1, String param2,String param3,String param4) {
        LiveStatus2 fragment = new LiveStatus2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3,param3);
        args.putString(ARG_PARAM4,param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrainNo = getArguments().getString(ARG_PARAM1);
            mDate = getArguments().getString(ARG_PARAM2);
            mStation=getArguments().getString(ARG_PARAM3);
            mName=getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_live_status2, container, false);

        progressDialog=new ProgressDialog(getActivity());
        alertDialog=new AlertDialog.Builder(getActivity());
        source=(TextView)view.findViewById(R.id.source);
        destination=(TextView)view.findViewById(R.id.destination);
        trainInfo=(TextView)view.findViewById(R.id.trainInfo);
        station=(TextView)view.findViewById(R.id.station);
        scharr=(TextView)view.findViewById(R.id.scharr);
        actarr=(TextView)view.findViewById(R.id.actarr);
        delayArr=(TextView)view.findViewById(R.id.delayArr);
        schdep=(TextView)view.findViewById(R.id.schdep);
        actdep=(TextView)view.findViewById(R.id.actdep);
        delayDep=(TextView)view.findViewById(R.id.delayDep);
        lastL=(TextView)view.findViewById(R.id.lastL);
        Log.e("data",mStation);
        Log.e("data",mTrainNo);
        Log.e("data",mDate);
        Log.e("data",mName);
        if(flag==0)
        formatdate();
        if(isNetworkAvailable())
            new LiveAsync().execute(mTrainNo,mDate);
        else
            errorNetwork();


        return view;
    }

    public void formatdate(){
        flag=1;
        Calendar calendar=Calendar.getInstance();
        int thisMonth = calendar.get(Calendar.MONTH);

        String arr[]=mDate.split("-");
        switch(thisMonth){
            case 0:arr[1]="01";
                break;
            case 1:arr[1]="02";
                break;
            case 2:arr[1]="03";
                break;
            case 3:arr[1]="04";
                break;
            case 4:arr[1]="05";
                break;
            case 5:arr[1]="06";
                break;
            case 6:arr[1]="07";
                break;
            case 7:arr[1]="08";
                break;
            case 8:arr[1]="09";
                break;
            case 9:arr[1]="10";
                break;
            case 10:arr[1]="11";
                break;
            case 11:arr[1]="12";
                break;


        }
        mDate=arr[2]+arr[1]+arr[0];
    }


    public boolean checkCode(int code){
        boolean isValid=true;
      //  Log.e("code ",String.valueOf(code));

        if(code!=200)
        {
            isValid=false;

        }

        return isValid;


    }

    public void invalidTrainNo(int responsecode){
        if(responsecode==403)
        {

            alertDialog.setTitle("Error");

            alertDialog.setMessage("Request quota limit exceeded . Change API_KEY in ApiKey class witht the provided api key or try next day."+responsecode);
            alertDialog.setCancelable(true);
            alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface d, int arg1) {
                    d.cancel();
                };
            });
            AlertDialog alert11 = alertDialog.create();

            alert11.show();
        }
        else{

            alertDialog.setTitle("Error");

            alertDialog.setMessage("Invalid train No"+" Response code"+responsecode);
            alertDialog.setCancelable(true);
            alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface d, int arg1) {
                    d.cancel();
                };
            });
            AlertDialog alert11 = alertDialog.create();

            alert11.show();

        }

    }
    public LiveStatusData extractData(String jsonResponse){

         int trainNumber=0;
         String trainName;
         String source="";
         String destination="";
         String queryStaton="";
         String queryStationSchArr="";
         String queryStationActArr="";
         String queryStationSchDep="";
         String queryStationActDep="";
         int delay=0;
         String lastLocation="";
        String actarr_date="";
        String scharr_date="";
        String code="";


        try {
            JSONObject jsonObject=new JSONObject(jsonResponse);
            int resonseCode=jsonObject.getInt("response_code");
            if(checkCode(resonseCode)) {
                lastLocation = jsonObject.getString("position");
                trainNumber = jsonObject.getInt("train_number");
                JSONArray route = jsonObject.getJSONArray("route");
                for (int i = 0; i < route.length(); i++) {
                    JSONObject demo = route.getJSONObject(i);
                    String src = demo.getString("scharr");
                    if (src.equals("Source")) {
                        JSONObject station = demo.getJSONObject("station_");
                        String name = station.getString("name");
                        source = name;
                        break;
                    }

                }
                for (int i = 0; i < route.length(); i++) {
                    JSONObject demo = route.getJSONObject(i);
                    String src = demo.getString("schdep");
                    if (src.equals("Destination")) {
                        JSONObject station = demo.getJSONObject("station_");
                        String name = station.getString("name");
                        destination = name;
                        break;
                    }

                }

                for (int i = 0; i < route.length(); i++) {
                    JSONObject demo = route.getJSONObject(i);
                    JSONObject station = demo.getJSONObject("station_");
                    String name = station.getString("name");

                    if (name.equals(mStation)) {
                        queryStationSchArr = demo.getString("scharr");
                        queryStationActArr = demo.getString("actarr");
                        queryStationSchDep = demo.getString("schdep");
                        queryStationActDep = demo.getString("actdep");
                        delay = demo.getInt("latemin");
                        scharr_date = demo.getString("scharr_date");
                        actarr_date = demo.getString("actarr_date");
                        code = station.getString("code");
                    }

                }
            }
            else{
                invalidTrainNo(resonseCode);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LiveStatusData liveStatusData=new LiveStatusData(trainNumber,mName,source,destination,mStation,queryStationSchArr,queryStationActArr,queryStationSchDep,queryStationActDep,delay,lastLocation,code,scharr_date,actarr_date);
        return liveStatusData;

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

    }


    public class LiveAsync extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading..");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String trainno=params[0];
            String date=params[1];
            String response="";
            URL url = null;
            try {
                url=new URL("http://api.railwayapi.com/live/train/"+trainno+"/doj/"+date+"/apikey/"+ ApiKey.API_KEY+"/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(10000);

                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line="";
                StringBuilder responseOut=new StringBuilder();
                while((line=bufferedReader.readLine())!=null)
                {
                    responseOut.append(line);
                }
                response=String.valueOf(responseOut);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return response;



        }


        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();


            if(s.isEmpty())
            {
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
            else {
                LiveStatusData liveStatusData = extractData(s);
                int trainNumber = liveStatusData.getTrainNumber();
               // Log.e("sad", String.valueOf(trainNumber));
                String trainName = liveStatusData.getTrainName();
                trainInfo.setText(trainNumber + "-" + trainName);
                source.setText(liveStatusData.getSource());
                destination.setText(liveStatusData.getDestination());
                station.setText(liveStatusData.getQueryStaton() + "-" + liveStatusData.getCode());
                scharr.setText(liveStatusData.getQueryStationSchArr() + "-" + liveStatusData.getScharr_date());
                actarr.setText(String.valueOf(liveStatusData.getQueryStationActArr()) + "-" + liveStatusData.getActarr_date());
                delayArr.setText("Delayed by " + String.valueOf(liveStatusData.getDelay()) + " mins");
                schdep.setText(liveStatusData.getQueryStationSchDep() + "-" + liveStatusData.getScharr_date());
                actdep.setText(liveStatusData.getqueryStationActDep() + "-" + liveStatusData.getActarr_date());
                delayDep.setText("Delayed by " + String.valueOf(liveStatusData.getDelay()) + " mins");
                lastL.setText(liveStatusData.getLastLocation());

            }



        }
    }

}

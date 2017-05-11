package com.example.user.e_rail;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveStatusFragment1 extends Fragment implements View.OnClickListener {
    private static  int i=0;
    private String trainName="";
    private String trainNo="";
    private EditText mTrainNo;
    private EditText mDate;
    private Button mCheckStatus;
    private Button mClearAll;
    private TextInputLayout layoutTrain;
    private Button searchbtn;
    private String station="";
    private Spinner selectStationSpinner;

    private String date;
    private Spinner dateSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private ProgressDialog progressDialog;

    private AlertDialog.Builder alertDialog;






    public LiveStatusFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_live_status_fragment1, container, false);


        mTrainNo=(EditText)view.findViewById(R.id.trainNotext);


        mCheckStatus=(Button)view.findViewById(R.id.checkStatus);
        layoutTrain=(TextInputLayout)view.findViewById(R.id.inputlayouttrainno);

        searchbtn=(Button)view.findViewById(R.id.searchbtn);
        mClearAll=(Button)view.findViewById(R.id.clearbtn);
        dateSpinner=(Spinner)view.findViewById(R.id.spinnderdate);
        adapter=ArrayAdapter.createFromResource(getActivity(),R.array.date,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);

        alertDialog=new AlertDialog.Builder(getActivity());

        progressDialog=new ProgressDialog(getActivity());
        selectStationSpinner=(Spinner)view.findViewById(R.id.select_station_spinner);






        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();




        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        final String todayAsString = dateFormat.format(today);
        date=todayAsString;
        final String yesterdayAsString = dateFormat.format(yesterday);
        final String tomorrowAsString = dateFormat.format(tomorrow);


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String trainNo=mTrainNo.getText().toString();
                if(checkEmpty())
                {
                    if(isNetworkAvailable())
                    {new SearchTrainAsync().execute(trainNo);
                    i++;}
                    else{
                        errorInternetMessage();
                    }
                }
                else
                {
                    errorMessage();
                }
            }
        });




        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String)parent.getItemAtPosition(position);

                if(selected.equals("Yesterday"))
                    date=yesterdayAsString;
                else  if(selected.equals("Today"))
                    date=todayAsString;



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectStationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String)parent.getItemAtPosition(position);
                station=selected;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrainNo.setText("");

            }
        });



        mCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainNo=mTrainNo.getText().toString().trim();
                if(checkEmpty())
                {
                    if(checkStation(station)) {
                        Log.e("what",station);
                        LiveStatus2 liveStatus2 = LiveStatus2.newInstance(trainNo, date,station,trainName);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, liveStatus2).addToBackStack("live2").commit();
                    }
                    else
                        emptyStationMessage();
                }
                else
                {
                    errorMessage();

                }
            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public boolean checkEmpty(){
        boolean isValid=true;

        if(mTrainNo.getText().toString().isEmpty())
            isValid=false;

       return isValid;

    }

    public boolean checkStation(String s){
        boolean isValid=true;
        if(s.length()==0) {
            isValid = false;
        }
        return isValid;
    }

    public void emptyStationMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please click search train and then select station before checking status");
        alertDialog.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int arg1) {
                d.cancel();
            };
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void errorMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please Enter 5 digit train number");
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

    public boolean checkResponse(int code){
        boolean isValid=true;
        Log.e("kya ho gya ",String.valueOf(code));

        if(code!=200)
        {
            isValid=false;

        }

        return isValid;


    }

    @Override
    public void onResume() {
        super.onResume();

        if(i!=0 && isNetworkAvailable() && !trainNo.isEmpty())
        {
            new SearchTrainAsync().execute(trainNo);
        }

    }

    public ArrayList<String> extractData(String jsonResponse){
        ArrayList<String> station=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(jsonResponse);
            int responseCode=jsonObject.getInt("response_code");
           if (checkResponse(responseCode))
           {    JSONObject trainNameObj=jsonObject.getJSONObject("train");
                trainName=trainNameObj.getString("name");

               JSONArray routeArray=jsonObject.getJSONArray("route");
               for(int i=0;i<routeArray.length();i++)
               {
                   JSONObject routeObj=routeArray.getJSONObject(i);
                   String stationName=routeObj.getString("fullname");
                   station.add(i,stationName);
               }
           }
            else
           {
               invalidTrainNo(responseCode);
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return station;

    }


    public void invalidTrainNo(int responsecode){
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
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void errorInternetMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("No internet connection. Please check your internet settings.");
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int arg1) {
                d.cancel();
            };
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        Log.e("id","yess");
        if(id==R.id.searchbtn || id==R.id.checkStatus)
        {
            Log.e("id","Noo");
            InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mTrainNo.getWindowToken(),0);
        }
    }


    public class SearchTrainAsync extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String response="";
            String trainNo=params[0];
            URL url = null;
            try {
                url= new URL("http://api.railwayapi.com/route/train/"+trainNo+"/apikey/"+ ApiKey.API_KEY+"/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("wrong1--------","bkjxcbjkdbcd");
            }

            try {
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(15000);
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line="";
                StringBuilder responseOutput=new StringBuilder();
                while((line=bufferedReader.readLine())!=null)
                {
                    responseOutput.append(line);
                }
                response=String.valueOf(responseOutput);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("wron--------","bkjxjkdbcd");
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
                ArrayList<String> stationName = extractData(s);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stationName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectStationSpinner.setAdapter(adapter);

            }
        }
    }






}

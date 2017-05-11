package com.example.user.e_rail;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;

import com.example.user.e_rail.DataClass.ApiKey;
import com.example.user.e_rail.Utils.DateDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeatAvailability1 extends Fragment{


    private View view;
    private int posSource=0;
    private Button btn_date;
    private int posDestination=0;
    private static int i=0;
    private Button checkbtn;
    private String classCode="";
    private String quotaCode="";
    private String date="";
    private TextView tname;
    private String trainNo="";
    private EditText trainEditText;
    private AlertDialog.Builder alertDialog;
    private String trainName="";
    private TextView mon;
    private TextView tue;
    private TextView wed;
    private TextView thu;
    private TextView fri;
    private TextView sat;
    private TextView sun;
    private Spinner sourceSpinner;
    private Spinner destinationSpinner;
    private Button searchbtn;
    private ArrayAdapter<String> sourceAdapter;
    private ArrayAdapter<String> destAdapter;
    private String sourceStation="";
    private String destStation="";
    private ProgressDialog progressDialog;
    private Spinner classSpinner;
    private Spinner quotaSpinner;
    private EditText dateText;

    private Toolbar toolbar;

    private int day,month,year;
    private static final int DIALOG_ID=999;

    public SeatAvailability1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_seat_availability1, container, false);
        trainEditText=(EditText) view.findViewById(R.id.trainNotext);

        tname=(TextView)view.findViewById(R.id.name);
        mon=(TextView)view.findViewById(R.id.mon);
        tue=(TextView)view.findViewById(R.id.tue);
        wed=(TextView)view.findViewById(R.id.wed);
        thu=(TextView)view.findViewById(R.id.thu);
        fri=(TextView)view.findViewById(R.id.fri);
        sat=(TextView)view.findViewById(R.id.sat);
        sun=(TextView)view.findViewById(R.id.sun);

        dateText=(EditText)view.findViewById(R.id.date_text);
        searchbtn=(Button)view.findViewById(R.id.search);
        checkbtn=(Button)view.findViewById(R.id.check_availability);
        sourceSpinner=(Spinner)view.findViewById(R.id.source_spinner);
        destinationSpinner=(Spinner)view.findViewById(R.id.destination_spinner);

        View.OnFocusChangeListener onFocusChangeListener=new MyFocusChangeListener();
        trainEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm =  (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
            }
        });

        classSpinner=(Spinner)view.findViewById(R.id.class_spinner);
        alertDialog=new AlertDialog.Builder(getActivity());
        progressDialog=new ProgressDialog(getActivity());
        quotaSpinner=(Spinner)view.findViewById(R.id.quota_spinner);
        final String[] quota={"General Quota","Ladies Quota","Tatkal Quota","Defence Quota","Foreign Tourist Quota",
        "Physically Handicapped Quota","Lower Berth","Yuva"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,quota);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        quotaSpinner.setAdapter(adapter);

        Calendar calendar=Calendar.getInstance();
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        year=calendar.get(Calendar.YEAR);






        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(trainEditText.getWindowToken(), 0);

        sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceStation = (String)parent.getItemAtPosition(position);






            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posDestination=position;
                destStation = (String)parent.getItemAtPosition(position);







            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classCode=(String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        quotaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String quota=(String)parent.getItemAtPosition(position);

                switch(quota){
                    case "General Quota":quotaCode="GN";
                        break;
                    case "Ladies Quota":quotaCode="LD";
                        break;
                    case "Tatkal Quota":quotaCode="Ck";
                        break;
                    case "Defence Quota":quotaCode="DF";
                        break;
                    case "Foreign Tourist Quota":quotaCode="FT";
                        break;
                    case "Physically Handicapped Quota":quotaCode="HP";
                        break;
                    case "Lower Berth":quotaCode="LB";
                        break;
                    case "Yuva":quotaCode="YU";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              trainNo=trainEditText.getText().toString();
                if(checkEmpty()){
                    if(isNetworkAvailable()) {
                        new searchAsync().execute(trainNo);
                        i++;
                    }

                    else
                        errorInternetMessage();
                }
                else{
                    errorMessage();
                }

            }
        });


        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date=dateText.getText().toString();
                Log.e("whaaatt",date);
                Log.e("whaaatt",quotaCode);

                if(!correctDate(date))
                    wrongDateSelected();
                else if(trainNo.isEmpty())
                {
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please enter train no and then click search before checking availabilty");
                    alertDialog.setCancelable(true);
                    alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert=alertDialog.create();
                    alert.show();
                }
                else if(!checkString(date))
                    emptyDateMessage();
                else if(!correctDate(date))
                    wrongDateSelected();
                else if(!checkString(sourceStation))
                    emptySourceMessage();
                else if(!checkString(destStation))
                    emptyDestMessage();
                else if(!checkString(quotaCode))
                    emptyquotaMessage();
                else if(!checkString(classCode))
                    emptyclassMessage();
                else if(!checkEmpty())
                    errorMessage();
                else  if(posSource>=posDestination){
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Wrong combination of source and destination");
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
                else{

                        SeatAvailability2 seatAvailability2=SeatAvailability2.newInstance(trainNo,trainName,sourceStation,
                                destStation,classCode,quotaCode,date);
                    FragmentManager fragmentManager=getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main,seatAvailability2).addToBackStack("seat2").commit();
                }

            }
        });






        return view;
    }



    public  boolean correctDate(String date){
        boolean isValid=true;

        Calendar calendar=Calendar.getInstance();

        Date date1 = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = date_format.parse(year+"-"+month+"-"+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date1);

        String goal="";
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date2 = inFormat.parse(date);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
             goal = outFormat.format(date2);
            Log.e("no idea",goal);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Calendar calendar = new GregorianCalendar(year, month, day); // Note that Month value is 0-based. e.g., 0 for January.

        switch (goal) {
            case "Monday":
                String monCheck=mon.getText().toString();
                Log.e("monday",monCheck+" "+String.valueOf(Calendar.MONDAY));
                if(!monCheck.equals("Y"))
                    isValid=false;
                break;
            case "Tuesday":
                String tueCheck=tue.getText().toString();
                Log.e("tuesday",tueCheck+" "+String.valueOf(Calendar.TUESDAY));
                if(!tueCheck.equals("Y"))
                    isValid=false;
                break;
            case "Wednesday":
                String wedCheck=wed.getText().toString();
                if(!wedCheck.equals("Y"))
                    isValid=false;
                break;
            case "Thursday":
                String thuCheck=thu.getText().toString();
                if(!thuCheck.equals("Y"))
                    isValid=false;
                break;
            case "Friday":
                String friCheck=fri.getText().toString();
                if(!friCheck.equals("Y"))
                    isValid=false;
                break;
            case "Saturday":
                String satCheck=sat.getText().toString();
                if(!satCheck.equals("Y"))
                    isValid=false;
                break;
            case "Sunday":
                String sunCheck=sun.getText().toString();
                if(!sunCheck.equals("Y"))
                    isValid=false;
                break;
        }



        return isValid;
    }
    public void wrongDateSelected(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Provided train does not run on the selected date. Please select the date as per the running days of the given train");
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


    @Override
    public void onResume() {
        super.onResume();
        if(i!=0 && isNetworkAvailable() && !trainNo.isEmpty())
        {
            new searchAsync().execute(trainNo);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("datepicker","captain America");
                DateDialog dateDialog=new DateDialog(v);
                android.app.FragmentManager fragment= getActivity().getFragmentManager();
                dateDialog.show(fragment,"Datepicker");
            }
        });

        dateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Log.e("datepikcer", "superman");
                    DateDialog dateDialog = new DateDialog(v);
                    android.app.FragmentManager fragment = getActivity().getFragmentManager();
                    dateDialog.show(fragment, "DatePicker");
                }
            }
        });
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

    public boolean checkResponse(int code){
        boolean isValid=true;
        Log.e("code ",String.valueOf(code));

        if(code!=200)
        {
            isValid=false;

        }

        return isValid;


    }

    public boolean checkEmpty(){
        boolean isValid=true;

        if(trainNo.isEmpty())
            isValid=false;

        return isValid;

    }

    public boolean checkString(String s){
        boolean isValid=true;
        if(s.length()==0) {
            isValid = false;
        }
        return isValid;
    }



    public void emptySourceMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please select source to check the availability");
        alertDialog.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int arg1) {
                d.cancel();
            };
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void emptyDestMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please select a destination to check availability");
        alertDialog.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int arg1) {
                d.cancel();
            };
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void emptyclassMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please select class to check availability");
        alertDialog.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int arg1) {
                d.cancel();
            };
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void emptyquotaMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please select a quota");
        alertDialog.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int arg1) {
                d.cancel();
            };
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void emptyDateMessage(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Please select date");
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





    public void invalidTrainNo(int responsecode){
        if(responsecode==403)
        {
            alertDialog.setTitle("Error");

            alertDialog.setMessage("Request quota limit exceeded"+responsecode);
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
        else {
            alertDialog.setTitle("Error");

            alertDialog.setMessage("Invalid train No" + " Response code" + responsecode);
            alertDialog.setCancelable(true);
            alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int arg1) {
                    d.cancel();
                }

                ;
            });
            AlertDialog alert11 = alertDialog.create();

            alert11.show();
        }
    }


    public ArrayList<String> extractSource(String jsonResponse){
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
                    String stationcode=routeObj.getString("code");
                    String data=stationName+"-"+stationcode;
                    station.add(i,data);
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

    public String[] extractDays(String jsonResponse){
        String[] days=new String[7];
        try {
            JSONObject jsonObject=new JSONObject(jsonResponse);
            int responseCode=jsonObject.getInt("response_code");
            if (checkResponse(responseCode))
            {    JSONObject trainNameObj=jsonObject.getJSONObject("train");


                JSONArray dayArray=trainNameObj.getJSONArray("days");
                for(int i=0;i<dayArray.length();i++)
                {
                    JSONObject daysObj=dayArray.getJSONObject(i);
                    days[i]=daysObj.getString("runs");

                }
            }
            else
            {
                invalidTrainNo(responseCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return days;


    }

    public ArrayList<String> extractClasses(String jsonResponse){
        ArrayList<String> code=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(jsonResponse);
            int responseCode=jsonObject.getInt("response_code");
            if (checkResponse(responseCode))
            {    JSONObject trainNameObj=jsonObject.getJSONObject("train");


                JSONArray classArray=trainNameObj.getJSONArray("classes");
                for(int i=0;i<classArray.length();i++)
                {
                    JSONObject classObj=classArray.getJSONObject(i);
                    String available=classObj.getString("available");
                    if(available.equals("Y")) {
                        Log.e("problem", classObj.getString("class-code"));
                        String demo=classObj.getString("class-code");
                        code.add(demo);

                    }


                }
            }
            else
            {
                invalidTrainNo(responseCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return code;


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(v.getId() == R.id.trainNotext && !hasFocus) {
                Log.e("noooooooooooooooo","hona chaiye tha ");
                InputMethodManager imm =  (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }


    public class searchAsync extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String response="";
            String trainNo=params[0];
            URL url = null;
            //api key awddq8z9
            //api key2 rwsmb4m3
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

                Log.e("wrong2--------","bkjxcbjkdbcd");
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            //Log.e("kuch samjh nh aaraha",s);
            if(!s.isEmpty()) {
                ArrayList<String> station = extractSource(s);
                String [] days=extractDays(s);
                Log.e("runs",days[0]);
                if(days[1].equals("Y"))
                    mon.setTextColor(Color.parseColor("#79C879"));
                else
                    mon.setTextColor(Color.parseColor("#ff0000"));

                if(days[2].equals("Y"))
                    tue.setTextColor(Color.parseColor("#79C879"));
                else
                    tue.setTextColor(Color.parseColor("#ff0000"));

                if(days[3].equals("Y"))
                    wed.setTextColor(Color.parseColor("#79C879"));
                else
                    wed.setTextColor(Color.parseColor("#ff0000"));

                if(days[4].equals("Y"))
                    thu.setTextColor(Color.parseColor("#79C879"));
                else
                    thu.setTextColor(Color.parseColor("#ff0000"));

                if(days[5].equals("Y"))
                    fri.setTextColor(Color.parseColor("#79C879"));
                else
                    fri.setTextColor(Color.parseColor("#ff0000"));

                if(days[6].equals("Y"))
                    sat.setTextColor(Color.parseColor("#79C879"));
                else
                    sat.setTextColor(Color.parseColor("#ff0000"));

                if(days[0].equals("Y"))
                    sun.setTextColor(Color.parseColor("#79C879"));
                else
                    sun.setTextColor(Color.parseColor("#ff0000"));

                mon.setText(days[1]);
                tue.setText(days[2]);
                wed.setText(days[3]);
                thu.setText(days[4]);
                fri.setText(days[5]);
                sat.setText(days[6]);
                sun.setText(days[0]);
                tname.setText(trainName);

                ArrayList<String> classes=extractClasses(s);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,classes);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                classSpinner.setAdapter(adapter);
                sourceAdapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,station);
                destAdapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,station);
                sourceAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                destAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                sourceSpinner.setAdapter(sourceAdapter);
                destinationSpinner.setAdapter(destAdapter);
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

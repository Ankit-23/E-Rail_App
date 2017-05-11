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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.e_rail.DataClass.ApiKey;
import com.example.user.e_rail.Utils.DateDialog;
import com.example.user.e_rail.Utils.FareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FareInfo extends Fragment {
    private EditText trainInfo;
    private Spinner src_spinner;
    private Spinner dest_spinner;
    private String date="";
    private EditText dateText;
    private Spinner age_spinner;
    private Spinner quota_spinner;
    private String age="";
    private String quotaCode="";
    private String source="";
    private String destination="";
    private String trainNo="";
    private String trainName="";
    private Button btnSearch;
    private Button btnCheck;
    private AlertDialog.Builder alertDialog;
    private ProgressDialog progressDialog;
    private TextView trainNametext;
    private int positionSrc;
    private int positonDest;
    private ArrayList<String > result;




    public FareInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fare_info, container, false);
        result=new ArrayList<>();
        trainInfo=(EditText)view.findViewById(R.id.trainNotext);
        src_spinner=(Spinner)view.findViewById(R.id.source_spinner);
        dest_spinner=(Spinner)view.findViewById(R.id.destination_spinner);
        age_spinner=(Spinner)view.findViewById(R.id.age_spinner);
        trainNametext=(TextView)view.findViewById(R.id.name);
        quota_spinner=(Spinner)view.findViewById(R.id.quota_spinner);
        btnSearch=(Button)view.findViewById(R.id.search);
        btnCheck=(Button)view.findViewById(R.id.check);
        dateText=(EditText)view.findViewById(R.id.date_text);
        alertDialog=new AlertDialog.Builder(getActivity());
        progressDialog=new ProgressDialog(getActivity());

        final String[] quotaArray={"General Quota","Ladies Quota","Tatkal Quota","Defence Quota","Foreign Tourist Quota",
                "Physically Handicapped Quota","Lower Berth","Yuva"};
        ArrayAdapter<String> quotaAdapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,quotaArray);
        quotaAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        quota_spinner.setAdapter(quotaAdapter);

        quota_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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



        String[] ageArray={
                "AGE 5-11",
                "AGE 12-60",
                "AGE 60+"

        };

        ArrayAdapter<String > ageAdapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,ageArray);
        ageAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        age_spinner.setAdapter(ageAdapter);

        age_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String demo=(String)parent.getItemAtPosition(position);
                if(demo.equals("AGE 5-11"))
                    age="10";
                else if(demo.equals("AGE 12-59"))
                    age="20";
                else if(demo.equals("AGE 60+"))
                    age="61";


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainNo=trainInfo.getText().toString();
                if(checkEmpty()){
                    if(isNetworkAvailable()) {
                        loadTrainInfo();
                    }

                    else
                        errorInternetMessage();
                }
                else{
                    errorMessage();
                }
            }
        });


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date=dateText.getText().toString();
                Log.e("whaaatt",date);
                Log.e("whaaatt",quotaCode);
                if(trainNo.isEmpty())
                {
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please enter train no and then click search before checking fare");
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
                else if(!checkString(source))
                    emptySourceMessage();
                else if(!checkString(destination))
                    emptyDestMessage();
                else if(!checkString(quotaCode))
                    emptyquotaMessage();
                else if(!checkString(age))
                    emptyclassMessage();
                else if(!checkEmpty())
                    errorMessage();
                else  if(positionSrc>=positonDest){
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

                    Log.e("success","everything is working fine");
                    if(isNetworkAvailable())
                        getFarevolley();
                    else
                        errorInternetMessage();


                       }


            }
        });






        return view;
    }


    public void getFarevolley(){
        String[] demo=source.split("-");
        String src=demo[1];
        demo=destination.split("-");
        String dest=demo[1];

        Log.e("spiderman",trainNo);
        Log.e("spiderman",src);
        Log.e("spiderman",dest);
        Log.e("spiderman",age);
        Log.e("spiderman",date);
        Log.e("spiderman",quotaCode);



        String url="http://api.railwayapi.com/fare/train/"+trainNo+"/source/"+src+"/dest/"+dest+"/age/"+age+"/quota/"+quotaCode+"/doj/"+date+"/apikey/"+ApiKey.API_KEY+"/";
       progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        StringRequest resquet=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();
                        JSONObject data= null;
                        LinkedHashMap<String,Integer> hm= new LinkedHashMap<>();
                        try {
                            data = new JSONObject(response);
                            int responseCode=data.getInt("response_code");
                            if(checkResponse(responseCode)) {

                                JSONArray fareArray = data.getJSONArray("fare");
                                for (int i = 0; i < fareArray.length(); i++) {
                                    JSONObject fareObj = fareArray.getJSONObject(i);
                                    String classType = fareObj.getString("name");
                                    Integer fare = fareObj.getInt("fare");
                                    hm.put(classType, fare);
                                }

                                String fare = "";


                                for (Map.Entry m : hm.entrySet()) {
                                    String key = (String) m.getKey();
                                    Integer value = (Integer) m.getValue();
                                    fare = fare + key + " - Rs" + String.valueOf(value) + "\n\n";
                                    Log.e("output", key);
                                }

                                String trainData=trainName+"-"+trainNo;
                                FareDialog fareDialog=FareDialog.newInstance(trainData,source,destination,date,fare);
                                FragmentManager fragmentManager =getActivity().getFragmentManager();
                                fareDialog.show(fragmentManager,"FareData");

                            }
                            else
                            {
                                invalidTrainNo(responseCode);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("Some problem occurred. Please try again");
                        alertDialog.setCancelable(true);
                        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog msg=alertDialog.create();
                        msg.show();

                    }
                });

        RequestQueue resquestQueue=Volley.newRequestQueue(getActivity());
        resquestQueue.add(resquet);
    }


    public void loadTrainInfo(){
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        Log.e("trainnnn",trainNo);
        String url="http://api.railwayapi.com/route/train/"+trainNo+"/apikey/"+ ApiKey.API_KEY+"/";
        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();
                        ArrayList<String> station=new ArrayList<>();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int responseCode=jsonObject.getInt("response_code");
                            if (checkResponse(responseCode))
                            {    JSONObject trainNameObj=jsonObject.getJSONObject("train");
                                trainName=trainNameObj.getString("name");
                                trainNametext.setText(trainName);

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
                        result=station;

                        ArrayAdapter<String> srcAdapter= new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,station);
                        srcAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                        src_spinner.setAdapter(srcAdapter);
                        dest_spinner.setAdapter(srcAdapter);


                        src_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                source=(String)parent.getItemAtPosition(position);
                                positionSrc=position;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        dest_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                destination=(String)parent.getItemAtPosition(position);
                                positonDest=position;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("Some problem occurred. Please try after some time");
                        alertDialog.setCancelable(true);
                        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog msg=alertDialog.create();
                        msg.show();


                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }


    @Override
    public void onStart() {
        super.onStart();

       dateText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DateDialog dateDialog=new DateDialog(v);
               FragmentManager fragment= getActivity().getFragmentManager();
               dateDialog.show(fragment,"Datepicker");
           }
       });

        dateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DateDialog dateDialog=new DateDialog(v);
                    FragmentManager fragment= getActivity().getFragmentManager();
                    dateDialog.show(fragment,"Datepicker");
                }

            }
        });

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

    public boolean checkResponse(int code){
        boolean isValid=true;
        Log.e("kya ho gya ",String.valueOf(code));

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

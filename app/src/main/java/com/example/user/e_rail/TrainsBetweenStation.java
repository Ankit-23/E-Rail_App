package com.example.user.e_rail;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.user.e_rail.DataClass.TrainBetween;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainsBetweenStation extends Fragment {

    private EditText source;

    private EditText destination;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<TrainBetween> lists;
    private Button search;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;


    public TrainsBetweenStation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_trains_between_station, container, false);
        source=(EditText)v.findViewById(R.id.src);
        destination=(EditText)v.findViewById(R.id.dest);
        recyclerView=(RecyclerView)v.findViewById(R.id.recyler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        search=(Button)v.findViewById(R.id.search);
        progressDialog=new ProgressDialog(getActivity());
        alertDialog=new AlertDialog.Builder(getActivity());
        lists=new ArrayList<>();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String src=source.getText().toString().trim();
                String dest=destination.getText().toString().trim();
                if(checkString(src,dest)){

                    if(isNetworkAvailable()){

                        loadRecyclerViewData(src,dest);

                    }
                    else{
                        noNetworkMsg();
                    }

                }else
                {
                    showErrorMsg();
                }
            }
        });

        return v;
    }

    public boolean checkString(String s,String d){
        boolean isValid=true;
        if(s.isEmpty() || d.isEmpty())
            isValid=false;
        return isValid;
    }

    public void showErrorMsg() {
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Fields cant be empty");
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        AlertDialog msg = alertDialog.create();
        msg.show();
    }

    public void noNetworkMsg(){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Problem with Internet connection.Please check your internet settings.");
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

    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public boolean checkResponse(int responseCode, int total){
        boolean isValid=true;
        if(responseCode!=200 || total==0)
            isValid=false;
        return isValid;
    }

    public void errorResponse(int responsecCode){
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Invalid codes for source/destination or server problem . Check inputs and try again "+responsecCode);
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

    public void loadRecyclerViewData(String s,String d){
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        String url="http://api.railwayapi.com/between/source/"+s+"/dest/"+d+"/apikey/"+ ApiKey.API_KEY+"/";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();
                        ArrayList<TrainBetween> demo=new ArrayList<>();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int responseCode=jsonObject.getInt("response_code");
                            int total=jsonObject.getInt("total");
                            Log.e("number",String.valueOf(responseCode));
                            if(checkResponse(responseCode,total)){
                                JSONArray train=jsonObject.getJSONArray("train");
                                for(int i=0;i<train.length();i++){

                                    JSONObject object=train.getJSONObject(i);
                                    String number=object.getString("number");
                                    String name=object.getString("name");
                                    String info=name+"-"+number;
                                    JSONObject srcObject=object.getJSONObject("from");
                                    String source=srcObject.getString("name");
                                    JSONObject destObject=object.getJSONObject("to");
                                    String destination=destObject.getString("name");
                                    String timeArr=object.getString("dest_arrival_time");
                                    String timeDept=object.getString("src_departure_time");
                                    String classCodes="";
                                    JSONArray classArray=object.getJSONArray("classes");
                                    for(int j=0;j<classArray.length();j++){
                                        JSONObject codes=classArray.getJSONObject(j);
                                        if(codes.getString("available").equals("Y"))
                                        {
                                            String a=codes.getString("class-code");
                                            classCodes=classCodes+a+"  ";
                                        }
                                    }

                                    String days="";
                                    JSONArray daysArray=object.getJSONArray("days");
                                    for(int j=0;j<daysArray.length();j++){
                                        JSONObject codes=daysArray.getJSONObject(j);
                                        if(codes.getString("runs").equals("Y"))
                                        {
                                            String a=codes.getString("day-code");
                                            days=days+a+" ";
                                        }
                                    }

                                    TrainBetween trainBetween=new TrainBetween(info,source,destination,classCodes,days,timeArr,timeDept);
                                    lists.add(trainBetween);
                                    Log.d("what is going",name);


                                }

                                Log.e("i have no idea",String.valueOf(lists.size()));

                                adapter= new Myadapter(lists,getActivity());
                                recyclerView.setAdapter(adapter);

                            }
                            else
                            {
                                errorResponse(responseCode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //setting adapter by creating an instance of myadapter and passing the parameters
                    //setting the adpater to the recyclerview
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }







}

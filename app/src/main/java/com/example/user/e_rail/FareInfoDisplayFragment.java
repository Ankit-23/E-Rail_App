package com.example.user.e_rail;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.e_rail.DataClass.FareDataClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FareInfoDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FareInfoDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private ProgressDialog pDialog;

    private TextView mResult;
    private String jsonResult="result";
    private FareDataClass fareDataClass;

    // TODO: Rename and change types of parameters
    private String mTrainNo;
    private String mDate;
    private String mSrc;
    private String mDest;
    private String mAge;
    private String mQuota;


    public FareInfoDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FareInfoDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FareInfoDisplayFragment newInstance(String param1, String param2,String param3, String param4,String param5, String param6) {
        FareInfoDisplayFragment fragment = new FareInfoDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrainNo = getArguments().getString(ARG_PARAM1);
            mDate = getArguments().getString(ARG_PARAM2);
            mSrc = getArguments().getString(ARG_PARAM3);
            mDest = getArguments().getString(ARG_PARAM4);
            mAge = getArguments().getString(ARG_PARAM5);
            mQuota = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fare_info_display, container, false);
        pDialog=new ProgressDialog(getActivity());
        mResult=(TextView)view.findViewById(R.id.result_text);
        fareDataClass=new FareDataClass();
       /* Log.e("trainNo",mTrainNo);
        Log.e("date",mDate);
        Log.e("src",mSrc);
        Log.e("dest",mDest);Log.e("age",mAge);
        Log.e("quota",mQuota);
       // new FareAsysncTask().execute(mTrainNo,mDate,mSrc,mDest,mAge,mQuota);
*/
        return view;
    }



    public FareDataClass extractData(){
        FareDataClass demo ;
        HashMap<String ,Integer> hm= new HashMap<>();
        Log.e("input",jsonResult);
        String srcOutput="xx";
        String destOutput="des";
        String trainNameOutput="vv";
        int trainNoOutput=00;
        if(jsonResult.length()!=0){
            try {
                JSONObject data= new JSONObject(jsonResult);
                JSONObject jsonSrc=data.getJSONObject("from");
                srcOutput=jsonSrc.getString("name");
                JSONObject jsonDest=data.getJSONObject("to");
                destOutput=jsonDest.getString("name");
                JSONObject jsonTrain=data.getJSONObject("train");
                trainNameOutput=jsonTrain.getString("name");
                trainNoOutput=jsonTrain.getInt("number");
                JSONArray fareArray= data.getJSONArray("fare");
                for(int i=0;i<fareArray.length();i++){
                    JSONObject fareObj=fareArray.getJSONObject(i);
                    String classType=fareObj.getString("name");
                    Integer fare=fareObj.getInt("fare");
                    hm.put(classType,fare);
                }









            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


       /* fareDataClass.setTrainName(srcOutput);
        fareDataClass.setTrainNo(trainNoOutput);
        fareDataClass.setDestStation(destOutput);
        fareDataClass.setSrcStation(srcOutput);*/
        demo=new FareDataClass(trainNameOutput,trainNoOutput,srcOutput,destOutput,hm);
        Log.e("OUTPUT",demo.getSrcStation());
        Log.e("OUTPUT",demo.getSrcStation());
        Log.e("OUTPUT",demo.getTrainName());
        return demo;



    }



    public class FareAsysncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String trainNo=params[0];
            String date=params[1];
            String source=params[2];
            String destination=params[3];
            String age=params[4];
            String quota=params[5];





            String response="";
            URL url=null;
            String urlString="http://api.railwayapi.com/fare/train/"+trainNo+"/source/"+source+"/dest/"+destination+"/age/"+age+"/quota/"+quota+"/doj/"+date+"/apikey/awddq8z9/";
            try {
                url= new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection httpURLConnection= (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(10000 /* milliseconds */);
                httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                String line="";
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder responseOutput= new StringBuilder();
                while((line=bufferedReader.readLine())!=null)
                {
                    responseOutput.append(line);
                }


                response=String.valueOf(responseOutput);


            } catch (IOException e) {
                e.printStackTrace();
            }


            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            pDialog.cancel();
            jsonResult=s;
            Log.e("FARE",jsonResult);
            fareDataClass=extractData();
            Log.e("fuck",fareDataClass.getTrainName());
            String fare="";

            HashMap<String,Integer> hm=fareDataClass.getFare();
            for(Map.Entry m:hm.entrySet()){
                String key=(String)m.getKey();
                Integer value=(Integer)m.getValue();
                fare=fare+key+" "+String.valueOf(value)+"\n\n";
                Log.e("output",key);
            }

            mResult.setText("Train Name:-"+fareDataClass.getTrainName()+"\n\n"+
                    "Train No:-"+fareDataClass.getTrainNo()+"\n\n"+
                    "Source Station:-"+fareDataClass.getSrcStation()+"\n\n"+
                    "Destination Station:-"+fareDataClass.getDestStation()+"\n\n"+fare

            );
        }
    }






}

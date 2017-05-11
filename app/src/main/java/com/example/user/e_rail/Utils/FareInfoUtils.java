package com.example.user.e_rail.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.e_rail.DataClass.FareDataClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 4/15/2017.
 */
public class FareInfoUtils {

    private String trainNo;
    private String date;
    private String source;
    private String destination;
    private String age;
    private String quota;
    private static String jsonResult="result of connection";
    private ProgressDialog pDialog;
    private FareDataClass fareDataClass;
    private AlertDialog.Builder builder1 ;

    public FareInfoUtils(String trainNo, String date, String src, String dest, String age, String quota, Context context){
        this.trainNo=trainNo;
        this.date=date;
        this.source=src;
        this.destination=dest;
        this.age=age;
        this.quota=quota;
        pDialog = new ProgressDialog(context);
        fareDataClass=new FareDataClass();
        builder1= new AlertDialog.Builder(context);

    }

    public void fareRequest(){

        new FareAsysncTask().execute();







    }

    public void copyObject(FareDataClass sample){
        fareDataClass=sample;
    }



    public FareDataClass extractData(){
        FareDataClass demo=new FareDataClass() ;
        Log.e("input",jsonResult);
        String srcOutput="xx";
        String destOutput="des";
        String trainNameOutput="vv";
       int trainNoOutput=00;
        if(jsonResult.length()!=0){
            try {
                JSONObject data= new JSONObject(jsonResult);
                JSONObject jsonSrc=data.getJSONObject("to");
                srcOutput=jsonSrc.getString("name");
                JSONObject jsonDest=data.getJSONObject("from");
                destOutput=jsonDest.getString("name");
                JSONObject jsonTrain=data.getJSONObject("train");
                trainNameOutput=jsonTrain.getString("name");
                trainNoOutput=jsonTrain.getInt("number");









            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


       /* fareDataClass.setTrainName(srcOutput);
        fareDataClass.setTrainNo(trainNoOutput);
        fareDataClass.setDestStation(destOutput);
        fareDataClass.setSrcStation(srcOutput);*/
    //demo=new FareDataClass(trainNameOutput,trainNoOutput,srcOutput,destOutput);
       /* Log.e("OUTPUT",demo.getSrcStation());
        Log.e("OUTPUT",demo.getSrcStation());
        Log.e("OUTPUT",demo.getTrainName());*/
        return demo;



    }

    public class FareAsysncTask extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
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
            builder1.setTitle("Message");
            builder1.setMessage("Device wifi is not enabled");

            builder1.setCancelable(true);

           // copyObject(fareDataClass);

        }
    }
}

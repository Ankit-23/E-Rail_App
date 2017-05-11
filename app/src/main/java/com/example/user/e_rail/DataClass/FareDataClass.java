package com.example.user.e_rail.DataClass;

import java.util.HashMap;

/**
 * Created by user on 4/16/2017.
 */
public class FareDataClass {
    private String trainName;
    private int trainNo;
    private String srcStation;
    private String destStation;
    private HashMap<String,Integer> fare=new HashMap<>();

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setTrainNo(int trainNo) {
        this.trainNo = trainNo;
    }

    public void setSrcStation(String srcStation) {
        this.srcStation = srcStation;
    }

    public void setDestStation(String destStation) {
        this.destStation = destStation;
    }

    public void setFare(HashMap<String, Integer> fare) {
        this.fare = fare;
    }

    public FareDataClass(){

    }

    public FareDataClass(String trainName,int trainNo,String src,String dest,HashMap<String , Integer> fare){
        this.trainName=trainName;
        this.trainNo=trainNo;
        this.srcStation=src;
        this.destStation=dest;
        this.fare=fare;
    }




    public int getTrainNo() {
        return trainNo;
    }

    public String getDestStation() {
        return destStation;
    }

    public String getSrcStation() {
        return srcStation;
    }

    public String getTrainName() {
        return trainName;
    }

    public HashMap<String, Integer> getFare() {
        return fare;
    }
}

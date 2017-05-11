package com.example.user.e_rail.DataClass;

/**
 * Created by user on 4/19/2017.
 */
public class LiveStatusData {

    private int trainNumber;
    private String trainName;
    private String source;
    private String destination;
    private String queryStaton;
    private String queryStationSchArr;
    private String queryStationActArr;
    private String queryStationSchDep;
    private String queryStationActDep;
    private int delay;
    private String lastLocation;
    private String code;
    private String scharr_date;
    private String actarr_date;

    public LiveStatusData(){

    }


    public String getCode() {
        return code;
    }

    public String getScharr_date() {
        return scharr_date;
    }

    public String getActarr_date() {
        return actarr_date;
    }

    public LiveStatusData(int trainNumber, String trainName, String source,
                          String destination, String queryStaton, String queryStationSchArr,
                          String queryStationActArr, String queryStationSchDep, String queryStationActDep, int delay,
                          String lastLocation, String code, String scharr_date, String actarr_date){

        this.trainNumber=trainNumber;
        this.trainName=trainName;
        this.source=source;
        this.destination=destination;
        this.queryStaton=queryStaton;
        this.queryStationSchArr=queryStationSchArr;
        this.queryStationActArr=queryStationActArr;
        this.queryStationSchDep=queryStationSchDep;
        this.queryStationActDep=queryStationActDep;
        this.delay=delay;
        this.lastLocation=lastLocation;
        this.code=code;
        this.scharr_date=scharr_date;
        this.actarr_date=actarr_date;



    }

    public int getTrainNumber(){
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getQueryStaton() {
        return queryStaton;
    }

    public String getQueryStationSchArr() {
        return queryStationSchArr;
    }

    public String getQueryStationActArr() {
        return queryStationActArr;
    }

    public String getQueryStationSchDep() {
        return queryStationSchDep;
    }

    public String getqueryStationActDep() {
        return queryStationActDep;
    }

    public int getDelay() {
        return delay;
    }

    public String getLastLocation() {
        return lastLocation;
    }


}

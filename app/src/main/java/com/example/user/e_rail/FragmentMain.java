package com.example.user.e_rail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMain extends Fragment {

    private ExpandableListView expandableListView;
    private List<String> group;
    private HashMap<String,List<String>> child;
    private ExpandableAdapter expandableAdapter;


    public FragmentMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.fragment_main,container,false);

        expandableListView=(ExpandableListView)v.findViewById(R.id.expandable_listview);

        prepareData();

        expandableAdapter= new ExpandableAdapter(getActivity(),group,child);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        return v;
    }

    public void prepareData(){
        group=new ArrayList<>();
        child=new HashMap<>();

        group.add("Introduction");
        group.add("Check Fare");
        group.add("Seat Availabilty");
        group.add("Train Status");
        group.add("Train between Station");
        group.add("Pnr Status");
        group.add("Share");

        List<String> introduction=new ArrayList<>();
        introduction.add("E-Rail: An android app to provide the functionalities of indian railways to users on their finger tips. It " +
                "facilitates features like checking of seat availability, checking of fare for various tickets, live status of train" +
                " etc. This app is incorporated with amazing technologies like cardview with recyclerview, expandable listview, dialogfragment, datepicker" +
                " etc, to provide a good user interface.");
        List<String> seatAvailability=new ArrayList<>();
        seatAvailability.add("This feature can be used to check availability of seats in the specific train on the specific date. User must provide " +
                "source and destination of their journey along with the class and quota corresponding to various tickets." +
                "Inter fragment communication is used to pass the relevant data for checking seat avilability.");

        List<String> checkFare=new ArrayList<>();
        checkFare.add("This feature can be used to check fare for tickets of the specific train on the specific date. User must provide " +
                "source and destination of their journey along with the age and quota corresponding to various tickets." +
                "The results are displayed using dialogfragment.");

        List<String> trainStatus=new ArrayList<>();
       trainStatus.add("This feature can be used to check live status of the specific train. User must provide " +
                "station to check the live status of the train corresponding to that specifed station." +
                "The results are displayed using cardview.");

        List<String> trainbetweenstation=new ArrayList<>();
        trainbetweenstation.add("This feature can be used to get the list of the trains running between user specified source and destination stations." +
                "The results are displayed using recycelerview along with the cardview for each train.");

        List<String> share=new ArrayList<>();
        share.add("This feature can be used to share the github/dropbox link for the apk file of this app. ");

        List<String> pnr=new ArrayList<>();
        pnr.add("This feature helps the user to check pnr status of any valid pnr number. It displays result in a dialog fragment which contains " +
                "dynamically generated view according to the number of passengers for the specified pnr no.");
        child.put(group.get(0),introduction);
        child.put(group.get(1),checkFare);
        child.put(group.get(2),seatAvailability);
        child.put(group.get(3),trainStatus);
        child.put(group.get(4),trainbetweenstation);
        child.put(group.get(5),pnr);
        child.put(group.get(6),share);




    }

}

package com.example.user.e_rail.Utils;


import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.e_rail.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PnrStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PnrStatusFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TRAIN = "train";
    private static final String SOURCE = "source";
    private static final String DESTINATION="destination";
    private static final String DOJ="date";
    private static final String CLASS="class";
    private static final String COUNT="count";
    private static final String STATUS="status";
    private static final String CHART="chart status";


    // TODO: Rename and change types of parameters
    private String mtrain="";
    private String msrc="";
    private String mdest="";
    private String mdate="";
    private String mclassC="";
    private int mcount=0;
    private String chart;
    private ArrayList<String> mstatus;


    private TextView train;
    private TextView dest;
    private TextView src;
    private TextView doj;
    private TextView classCode;
    private TextView chartStatus;
    private TextView close;
    private TextView countText;


    public PnrStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment PnrStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PnrStatusFragment newInstance(String train, String src,String dest,String date,String c, int count,String chart,ArrayList<String> status) {
        PnrStatusFragment fragment = new PnrStatusFragment();
        Bundle args = new Bundle();
        args.putString(TRAIN, train);
        args.putString(SOURCE, src);
        args.putString(DESTINATION, dest);
        args.putString(DOJ, date);
        args.putString(CLASS, c);

        args.putString(COUNT, String.valueOf(count));
        args.putString(CHART,chart);
        args.putStringArrayList(STATUS, status);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mtrain = getArguments().getString(TRAIN);
            msrc = getArguments().getString(SOURCE);
            mdest= getArguments().getString(DESTINATION);
            mclassC = getArguments().getString(CLASS);
            mdate = getArguments().getString(DOJ);
            mcount =Integer.parseInt( getArguments().getString(COUNT));
            chart=getArguments().getString(CHART);
           mstatus = getArguments().getStringArrayList(STATUS);




        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pnr_status2, container, false);
        train=(TextView)view.findViewById(R.id.trainInfo);
        dest=(TextView)view.findViewById(R.id.destination);
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.linear);

        src=(TextView)view.findViewById(R.id.source);
        doj=(TextView)view.findViewById(R.id.date);

        chartStatus=(TextView)view.findViewById(R.id.chart);
       classCode=(TextView)view.findViewById(R.id.classCode);
        countText=(TextView)view.findViewById(R.id.count);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setTitle("PNR STATUS");


        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                50,
                1.0f
        );


        for(int i=0;i<mstatus.size();i++){
            LinearLayout extra=new LinearLayout(getActivity());
            extra.setOrientation(LinearLayout.VERTICAL);



            //adding passenger titile
            TextView passenger = new TextView(getActivity());
            passenger.setText("Passenger "+(i+1));
            passenger.setBackgroundColor(Color.parseColor("#0077C3"));
            passenger.setTextColor(Color.parseColor("#ffffff"));
            passenger.setGravity(Gravity.CENTER);
            extra.addView(passenger);



            LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    50,
                    1.0f
            );

            //booking linear layout
            LinearLayout booking= new LinearLayout(getActivity());
            booking.setOrientation(LinearLayout.HORIZONTAL);
            TextView bookingStatus=new TextView(getActivity());
            bookingStatus.setLayoutParams(param3);
            bookingStatus.setText("Booking Status");
            bookingStatus.setPadding(4,0,0,0);

            TextView bookingValue=new TextView(getActivity());
            bookingValue.setLayoutParams(param3);
            String demo[]=mstatus.get(i).split("-");
            bookingValue.setText(demo[0]);
            bookingValue.setTextColor(Color.parseColor("#000000"));
            booking.addView(bookingStatus);
            booking.addView(bookingValue);

    //current linear layout
            LinearLayout current= new LinearLayout(getActivity());
            booking.setOrientation(LinearLayout.HORIZONTAL);
            TextView currentStatus=new TextView(getActivity());
            currentStatus.setLayoutParams(param3);
           currentStatus.setText("Current Status");
            currentStatus.setPadding(4,0,0,0);

            TextView currentValue=new TextView(getActivity());
            currentValue.setLayoutParams(param3);
            currentValue.setTextColor(Color.parseColor("#000000"));

            currentValue.setText(demo[1]);
            current.addView(currentStatus);
            current.addView(currentValue);


            //adding views to extra
            extra.addView(booking);
            extra.addView(current);
            //finally adding view to parent
            linearLayout.addView(extra);



        }




        Log.e("count",String.valueOf(mcount));
        train.setText(mtrain);
        src.setText(msrc);
        dest.setText(mdest);
        doj.setText(mdate);
        classCode.setText(mclassC);
        if(chart.equals("N"))
            chartStatus.setText("Not prepared");
        else if(chart.equals("Y"))
            chartStatus.setText("Prepared");
       countText.setText(String.valueOf(mcount));


        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT

        );

        param2.setMargins(0,10,0,0);

        close= new TextView(getActivity());
        close.setText("CLOSE[X]");

        close.setTextColor(Color.parseColor("#ff0000"));
        close.setLayoutParams(param2);
        close.setGravity(Gravity.CENTER);

        linearLayout.addView(close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });



        return view;
    }

}

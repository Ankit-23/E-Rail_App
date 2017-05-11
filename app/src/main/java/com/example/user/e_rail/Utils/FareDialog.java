package com.example.user.e_rail.Utils;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.e_rail.R;

/**
 * Created by user on 5/2/2017.
 */
public class FareDialog extends DialogFragment {
    private String trainData="";
    private String source="";
    private String destination="";
    private String dateText="";
    private String fareText="";

    private final static String PARAM1="trainInfo";
    private final static String PARAM2="source";
    private final static String PARAM3="destination";
    private final static String PARAM4="date";
    private final static String PARAM5="fare";


    private TextView trainInfo;
    private TextView close;
    private TextView src;
    private TextView dest;
    private TextView fare;
    private TextView date;

    public static FareDialog newInstance(String trainNo,String src,String dest,String date,String fare) {

        Bundle args = new Bundle();

        FareDialog fragment = new FareDialog();
        args.putString(PARAM1,trainNo);
        args.putString(PARAM2,src);
        args.putString(PARAM3,dest);
        args.putString(PARAM4,date);
        args.putString(PARAM5,fare);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            trainData = getArguments().getString(PARAM1);
            source = getArguments().getString(PARAM2);
            destination = getArguments().getString(PARAM3);
            dateText = getArguments().getString(PARAM4);
            fareText= getArguments().getString(PARAM5);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.faredialog,container,false);
        getDialog().setTitle("FARE DETAILS");
        getDialog().setCanceledOnTouchOutside(false);

        trainInfo=(TextView)v.findViewById(R.id.trainInfo);
        src=(TextView)v.findViewById(R.id.source);
        dest=(TextView)v.findViewById(R.id.destination);
        date=(TextView)v.findViewById(R.id.date);
        fare=(TextView)v.findViewById(R.id.fare);
        close=(TextView)v.findViewById(R.id.close);



        trainInfo.setText(trainData);
        src.setText(source);
        dest.setText(destination);
        date.setText(dateText);
        fare.setText(fareText);



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        return v;

    }
}

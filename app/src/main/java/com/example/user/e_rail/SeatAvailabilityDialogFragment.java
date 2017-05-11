package com.example.user.e_rail;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by user on 4/23/2017.
 */
public class SeatAvailabilityDialogFragment extends DialogFragment {

    private TextView train;
    private Button btn;
    private String demo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        {
            demo=getArguments().getString("trainNo");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.simple_layout,container,false);
        train=(TextView)view.findViewById(R.id.trainfragment);
        train.setText(demo);
        btn=(Button)view.findViewById(R.id.btndismiss);
        getDialog().setTitle("Simple Dialog");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public static SeatAvailabilityDialogFragment newInstance(String param) {

        Bundle args = new Bundle();

        SeatAvailabilityDialogFragment fragment = new SeatAvailabilityDialogFragment();
        args.putString("trainNo",param);
        fragment.setArguments(args);
        return fragment;
    }
}



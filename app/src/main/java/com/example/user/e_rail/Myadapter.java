package com.example.user.e_rail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.e_rail.DataClass.TrainBetween;

import java.util.ArrayList;

/**
 * Created by user on 4/26/2017.
 */
public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {
    private ArrayList<TrainBetween> lists;
    private Context context;

    public Myadapter(ArrayList<TrainBetween> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_item_train,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TrainBetween trainBetween=lists.get(position);
        holder.train.setText(trainBetween.getTainInfo());
        holder.src.setText(trainBetween.getSource());
        holder.dest.setText(trainBetween.getDestination());
        holder.timeArr.setText(trainBetween.getTimeArr());
        holder.timeDept.setText(trainBetween.getTimeDept());
        holder.classCode.setText(trainBetween.getClassCode());
        holder.days.setText(trainBetween.getDays());

    }


    @Override
    public int getItemCount() {
        return lists.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView train;
        private TextView src;
        private TextView dest;
        private TextView timeArr;
        private TextView timeDept;
        private TextView classCode;
        private TextView days;
        public MyViewHolder(View itemView) {
            super(itemView);

            train=(TextView)itemView.findViewById(R.id.train);
            src=(TextView)itemView.findViewById(R.id.source);

           dest=(TextView)itemView.findViewById(R.id.destination);

            timeArr=(TextView)itemView.findViewById(R.id.arr);

            timeDept=(TextView)itemView.findViewById(R.id.dept);

            classCode=(TextView)itemView.findViewById(R.id.classCode);
            days=(TextView)itemView.findViewById(R.id.dayCode);





        }
    }
}

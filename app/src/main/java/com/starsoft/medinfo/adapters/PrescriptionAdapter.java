package com.starsoft.medinfo.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.starsoft.medinfo.R;
import com.starsoft.medinfo.model.UserData;

import java.util.ArrayList;


/**
 * Created by Aashish on 10/6/2016.
 */

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {

    private Context context;

    private ArrayList<UserData> userDatas;

    private RecycleClickListener recycleClickListener;

    private DatabaseReference reference;

    public PrescriptionAdapter(Context con,ArrayList<UserData> datas,DatabaseReference ref,RecycleClickListener rc){
        this.context = con;
        this.userDatas = datas;
        this.reference = ref;
        this.recycleClickListener = rc;
    }

    @Override
    public PrescriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclelist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PrescriptionAdapter.ViewHolder holder, int position) {

        String[] months = new String[12];
        months[0] = "January";
        months[1] = "February";
        months[2] = "March";
        months[3] = "April";
        months[4] = "May";
        months[5] = "June";
        months[6] = "July";
        months[7] = "August";
        months[8] = "September";
        months[9] = "October";
        months[10] = "November";
        months[11] = "December";

        final UserData userData = userDatas.get(position);
        //String ad = holder.docName.getText().toString();
        holder.docName.setText("Prescribed By:"+' '+userData.getDoctorName());
        holder.diseaseName.setText(userData.getDiseaseName());

        String[] dates = userData.getPrescriptionDate().split("/");
        holder.date.setText(dates[0]);
        holder.month.setText(months[Integer.valueOf(dates[1])-1]);

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("userData").child(userData.getUserId()).child(userData.getPresId()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(userDatas!=null)
            return userDatas.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date,month,diseaseName,docName;
        private ImageButton delBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.textDate);
            month = (TextView)itemView.findViewById(R.id.textMonth);
            diseaseName = (TextView)itemView.findViewById(R.id.textDisease);
            docName = (TextView)itemView.findViewById(R.id.textDoc);
            delBtn = (ImageButton)itemView.findViewById(R.id.btnDel);
            //delBtn.setOnClickListener(recycleClickListener);

           itemView.setOnClickListener(recycleClickListener);

        }
    }


    public interface RecycleClickListener extends View.OnClickListener {
        @Override
        void onClick(View v);
    }
}

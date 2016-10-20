package com.starsoft.medinfo.fragments;

import android.app.DialogFragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.starsoft.medinfo.R;
import com.starsoft.medinfo.model.UserData;

import org.w3c.dom.Text;

/**
 * Created by Aashish on 10/12/2016.
 */

public class CustomFragment extends DialogFragment {

    public static CustomFragment getInstance(UserData udata){
        CustomFragment customFragment = new CustomFragment();
        System.out.println(udata.getDiseaseName());
        Bundle args = new Bundle();
        args.putString("presDate",udata.getPrescriptionDate());
        args.putString("desName",udata.getDiseaseName());
        args.putString("description",udata.getPrescriptionDescription());
        args.putString("docName",udata.getDoctorName());
        customFragment.setArguments(args);
        return customFragment;
    }

    // Empty constructor required for DialogFragment
    public CustomFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dailog_frag, container);

        TextView presDate = (TextView)view.findViewById(R.id.presDate);
        TextView disName = (TextView)view.findViewById(R.id.disName);
        TextView desc = (TextView)view.findViewById(R.id.presDesc);
        TextView docName = (TextView)view.findViewById(R.id.presDoc);


        //Log.d("CustomFragment",savedInstanceState.getString("presDate"));
        presDate.setText(getArguments().getString("presDate"));
        disName.setText(getArguments().getString("desName"));
        String[] nn = getArguments().getString("description").split("\n");
        desc.setText(getArguments().getString("description"));
        docName.setText("Prescribed By: "+getArguments().getString("docName"));

        getDialog().setTitle("Prescription");

        return view;
    }

}

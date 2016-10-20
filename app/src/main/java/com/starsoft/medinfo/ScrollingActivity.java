package com.starsoft.medinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.starsoft.medinfo.model.Drug;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private ImageView brandImage;
    private int con = 0;
    private TextView brandName,genericName,manufacturer,category,diseases,usage,type,contains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Drug drug = intent.getParcelableExtra("drugData");

        getSupportActionBar().setTitle(drug.getBrand_name());

        brandImage = (ImageView)findViewById(R.id.brandImage);
        brandName = (TextView)findViewById(R.id.brandName);
        manufacturer = (TextView)findViewById(R.id.brandCompany);
        genericName = (TextView)findViewById(R.id.genericName);
        contains = (TextView)findViewById(R.id.contains);
        category = (TextView)findViewById(R.id.medCategory);
        type = (TextView)findViewById(R.id.medType);
        diseases = (TextView)findViewById(R.id.diseases);
        usage = (TextView)findViewById(R.id.drugUsage);



        if(drug.getBrand_image()!=null || drug.getBrand_image()!=""){
            try {
                Picasso.with(this).load(drug.getBrand_image()).placeholder(R.mipmap.ic_launcher).into(brandImage);
            }catch (Exception e){e.printStackTrace();}
        }

        try{
            brandName.setText(drug.getBrand_name());
            manufacturer.setText(drug.getBrand_manufacturer_name());
            genericName.setText(drug.getGeneric_name());
            contains.setText(drug.getBrand_contains());
            category.setText(drug.getCategory());
            type.setText(drug.getBrand_type());
            diseases.setText(drug.getDiseases());
            usage.setText(drug.getUse_of_drug());
        }catch (Exception e){e.printStackTrace();}


        //Picasso.with(this).load(drug.)


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(con == 0) {
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    con = 1;
                }
                else if(con == 1){
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    con = 0;
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

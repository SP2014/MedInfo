package com.starsoft.medinfo;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Aashish on 10/11/2016.
 */

public class MedInfoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

package com.starsoft.medinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.starsoft.medinfo.adapters.PrescriptionAdapter;
import com.starsoft.medinfo.model.UserData;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;


public class PrescriptionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private DatabaseReference reference;
    private static final String TAG = PrescriptionActivity.class.getSimpleName();
    private ValueEventListener mUserListener;
    private EditText editText1,editText2,editText3,editText4;
    private Button btnSave;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private int presCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        //Get database reference
        reference = FirebaseDatabase.getInstance().getReference();

        //Get Firebase Auth module Instance
        firebaseAuth = FirebaseAuth.getInstance();
        //Get the current user from Firebase Auth
        firebaseUser = firebaseAuth.getCurrentUser();



        editText1 = (EditText)findViewById(R.id.prescriptionDate);
        editText2 = (EditText)findViewById(R.id.diseaseName);
        editText3 = (EditText)findViewById(R.id.prescriptionDescription);
        editText4 = (EditText)findViewById(R.id.doctorName);


        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show date picker dialog
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PrescriptionActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.setTitle("Choose Date");
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData userData = new UserData(editText1.getText().toString(),editText2.getText().toString(),
                                                editText3.getText().toString(),editText4.getText().toString());
                userData.setUserId(firebaseUser.getUid());
                userData.setPresId("pres"+presCount);
                //Add data to firebase reference
                /**
                 * Database structure
                 * userData
                 *        - "Userid"
                 *                  - "pres0"
                 *                          - "prescriptionDate"
                 *                          - "diseaseName"
                 *                          - "prescriptionDescription"
                 *                          - "doctorName"
                 *                          - "presId"
                 *
                 * For detail description refer this 'java->com.starsoft.medinfo->model->UserData'
                 */
                reference.child("userData").child(firebaseUser.getUid()).child("pres"+presCount).setValue(userData);

                Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"OnStart called");


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    System.out.println(snapshot.getChildrenCount());
                    int pp = (int) snapshot.getChildrenCount();

                    if(pp!=0){
                        // Set prescription count
                        presCount = pp;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        // Add an event listener to database reference
        /**
         * We first get the count of previously saved user's prescription
         */
        reference.child("userData").addValueEventListener(userListener);

        mUserListener = userListener;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mUserListener != null) {
            reference.removeEventListener(mUserListener);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
         String dd = dayOfMonth+"/"+monthOfYear+"/"+year;
         editText1.setText(dd);
    }
}

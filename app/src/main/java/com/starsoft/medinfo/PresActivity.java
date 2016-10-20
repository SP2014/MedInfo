package com.starsoft.medinfo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.starsoft.medinfo.adapters.PrescriptionAdapter;
import com.starsoft.medinfo.fragments.CustomFragment;
import com.starsoft.medinfo.model.UserData;

import java.util.ArrayList;

public class PresActivity extends AppCompatActivity implements PrescriptionAdapter.RecycleClickListener {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private static final String TAG = PresActivity.class.getSimpleName();
    private ValueEventListener mPostListener;
    private ValueEventListener mUserListener;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private Query query;
    private ArrayList<UserData> userDatas = new ArrayList<>();
    private PrescriptionAdapter prescriptionAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pres);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check from the shared preferences whether any user record is saved or not
        sharedPreferences = getApplicationContext().getSharedPreferences("pref_user",MODE_PRIVATE);

        progressBar = (ProgressBar)findViewById(R.id.loadProgress);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        relativeLayout = (RelativeLayout)findViewById(R.id.emptyState);

        // Get the FirebaseDatabase reference
        reference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        // Get the current user from the firebase auth module
        firebaseUser = firebaseAuth.getCurrentUser();


        // Make a database query on the basis of current user
        query = reference.child("userData").child(firebaseUser.getUid()).orderByKey();


        if(!sharedPreferences.getBoolean("isEmpty",false)){
            relativeLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),PrescriptionActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final int offset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = offset;
                }
            }
        });


        prescriptionAdapter = new PrescriptionAdapter(getApplicationContext(),userDatas,reference,this);
        recyclerView.setAdapter(prescriptionAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("isEmpty",false);
                    editor.commit();
                }
                else{
                    editor = sharedPreferences.edit();
                    editor.putBoolean("isEmpty",false);
                    editor.commit();
                    progressBar.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }

                userDatas.clear();

                prescriptionAdapter.notifyDataSetChanged();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    UserData usData = snapshot.getValue(UserData.class);
                    userDatas.add(usData);
                }

                prescriptionAdapter.notifyItemRangeInserted(0,userDatas.size());
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(userListener);
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
    protected void onDestroy() {
        super.onDestroy();
        if (mUserListener != null) {
            reference.removeEventListener(mUserListener);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("fragment_edit_name");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        CustomFragment editNameDialog = CustomFragment.getInstance(userDatas.get(recyclerView.getChildAdapterPosition(v)));
        editNameDialog.show(manager, "fragment_edit_name");
    }
}

package com.starsoft.medinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private EditText uname,upwd,ucontact;
    private Button registerBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = getApplicationContext().getSharedPreferences("pref_user",MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        uname = (EditText)findViewById(R.id.userName);
        upwd = (EditText)findViewById(R.id.userPassword);
        ucontact = (EditText)findViewById(R.id.userContact);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        progressBar = (ProgressBar)findViewById(R.id.register_progress);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uName = uname.getText().toString().trim();
                String uPwd = upwd.getText().toString().trim();
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(uName)){
                    Toast.makeText(getApplicationContext(),"User Name can't be empty",Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(uPwd)){
                    Toast.makeText(getApplicationContext(),"Password can't be empty",Toast.LENGTH_SHORT).show();
                }

                if(sharedPreferences.getString("userEmail","").equals(uName)){
                    Toast.makeText(getApplicationContext(),"User already Registered",Toast.LENGTH_SHORT).show();
                }
                else
                firebaseAuth.createUserWithEmailAndPassword(uName,uPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          progressBar.setVisibility(View.GONE);
                          Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_SHORT).show();
                          editor = sharedPreferences.edit();
                          editor.putString("userEmail",uName);
                          editor.putBoolean("showRegister",false);
                          editor.commit();
                          startActivity(new Intent(getApplicationContext(),MainActivity.class));
                          finish();
                      }
                      else{
                          Toast.makeText(getApplicationContext(),"Oops, an error occured. Try again !!!",Toast.LENGTH_SHORT).show();
                      }
                    }
                });

            }
        });


    }
}

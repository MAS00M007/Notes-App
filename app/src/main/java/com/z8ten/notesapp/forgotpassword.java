package com.z8ten.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
private EditText mforgotpassword;
private RelativeLayout mpasswordrecoverbtn;
private TextView mgobacktologin;

FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

       //getSupportActionBar().hide();

        mforgotpassword=findViewById(R.id.et_forgotpassword);
        mpasswordrecoverbtn=findViewById(R.id.tv_passwordrecoverbtn);
        mgobacktologin=findViewById(R.id.tv_gobacktologin);

firebaseAuth=FirebaseAuth.getInstance();

        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(forgotpassword.this, MainActivity.class);
                startActivity(intent);
            }
        });


        mpasswordrecoverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=mforgotpassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(forgotpassword.this, "Enter Your mail first", Toast.LENGTH_SHORT).show();
                }

                else {
firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
            Toast.makeText(forgotpassword.this, "Mail sent,you can recover your password using mail", Toast.LENGTH_SHORT).show();
       finish();
       startActivity(new Intent(forgotpassword.this, MainActivity.class));
        }
        else {
            Toast.makeText(forgotpassword.this, "Email is wrong or account not Exist", Toast.LENGTH_SHORT).show();
        }
    }
});


                }
            }
        });
    }
}
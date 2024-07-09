package com.z8ten.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private EditText mloginemail,mloginpassword;
private RelativeLayout mlogin,mgotosignup;
private TextView mgotoforgotpassword;

private FirebaseAuth firebaseAuth;
ProgressBar mprogressbarofmainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getActionBar().hide();
        mprogressbarofmainActivity=findViewById(R.id.progressbarofmainactivity);
        mloginemail=findViewById(R.id.et_loginemail);
        mloginpassword=findViewById(R.id.et_loginpassword);
        mlogin=findViewById(R.id.login);
        mgotosignup=findViewById(R.id.gotosignup);
        mgotoforgotpassword=findViewById(R.id.gotoforgotpassword);

firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            startActivity(new Intent(MainActivity.this,allnotes.class));
        }

        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, signup.class));
            }
        });

        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, forgotpassword.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=mloginemail.getText().toString().trim();
                String password=mloginpassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "All fields are required to fill", Toast.LENGTH_SHORT).show();
                }
                else {

                    mprogressbarofmainActivity.setVisibility(View.VISIBLE);
firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            checkMailVerification();
        }
        else {
            Toast.makeText(MainActivity.this, "Account doesn't Exist", Toast.LENGTH_SHORT).show();
        mprogressbarofmainActivity.setVisibility(View.INVISIBLE);
        }
    }
});
                }
            }
        });
    }
private void checkMailVerification(){
FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

if(firebaseUser.isEmailVerified()==true){
    Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
    finish();
    startActivity(new Intent(MainActivity.this,allnotes.class));
}

else {
    mprogressbarofmainActivity.setVisibility(View.INVISIBLE);
    Toast.makeText(this, "Verify your mail first", Toast.LENGTH_SHORT).show();
firebaseAuth.signOut();
}
}
}
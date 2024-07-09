package com.z8ten.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createnote extends AppCompatActivity {
EditText mcreatetitlenote,mcreatecontentnote;
ImageView savenote;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
FirebaseFirestore firestore;
ProgressBar mprogressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);

        mcreatetitlenote=findViewById(R.id.createtitleofnote);
        mcreatecontentnote=findViewById(R.id.createcontentofnote);
        savenote=findViewById(R.id.savenote);
        mprogressBar=findViewById(R.id.progressbarofcreatenote);

        Toolbar toolbar=findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();

        savenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=mcreatetitlenote.getText().toString();
                String content=mcreatecontentnote.getText().toString();

                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(createnote.this, "Both field are Required", Toast.LENGTH_SHORT).show();
                }

                else {
                    mprogressBar.setVisibility(View.VISIBLE);
                    DocumentReference documentReference=firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                Map<String,Object> note=new HashMap<>();
                note.put("title",title);
                note.put("content",content);

                documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mprogressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(createnote.this, "Note created successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(createnote.this,allnotes.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mprogressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(createnote.this, "Failed to create Note", Toast.LENGTH_SHORT).show();
                    }
                });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
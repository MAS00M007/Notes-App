package com.z8ten.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteActivity extends AppCompatActivity {
Intent data;
EditText medittitleofnote,meditcontentofnote;
ImageView msaveeditnote;

FirebaseAuth firebaseAuth;
FirebaseFirestore firestore;
FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        medittitleofnote=findViewById(R.id.edittitleofnote);
        meditcontentofnote=findViewById(R.id.editcontentofnote);
        msaveeditnote=findViewById(R.id.saveeditnote);
        data=getIntent();

        firestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newtitle=medittitleofnote.getText().toString();
                String newcontent=meditcontentofnote.getText().toString();

                if(newtitle.isEmpty() || newcontent.isEmpty()){
                    Toast.makeText(editnoteActivity.this, "Something is Empty", Toast.LENGTH_SHORT).show();

                }
                else {
                    DocumentReference documentReference=firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String ,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);


                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(editnoteActivity.this, "Note is Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(editnoteActivity.this,allnotes.class));
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editnoteActivity.this, "Failed to Update", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });



        String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");

        medittitleofnote.setText(notetitle);
        meditcontentofnote.setText(notecontent);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(editnoteActivity.this,allnotes.class));

        super.onBackPressed();
    }
}
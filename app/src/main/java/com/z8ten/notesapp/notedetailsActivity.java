package com.z8ten.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class notedetailsActivity extends AppCompatActivity {
private TextView mtitleofnotedetail,mcontentofnotedetail;
ImageView gotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        mtitleofnotedetail=findViewById(R.id.titleofnotedetail);
        mcontentofnotedetail=findViewById(R.id.contentofnotedetail);
        gotoeditnote=findViewById(R.id.gotoeditnote);

        Intent data=getIntent();

        gotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),editnoteActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
                view.getContext().startActivity(intent);
            }
        });

        mtitleofnotedetail.setText(data.getStringExtra("title"));
        mcontentofnotedetail.setText(data.getStringExtra("content"));


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), allnotes.class);
        startActivity(intent);
        super.onBackPressed();
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if(item.getItemId()==android.R.id.home){
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
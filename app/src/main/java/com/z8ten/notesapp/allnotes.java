package com.z8ten.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class allnotes extends AppCompatActivity {
    FloatingActionButton mcreatenotfbtn;
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    FirestoreRecyclerAdapter<firebaseModel, NoteViewHolder> NotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allnotes);


        mcreatenotfbtn = findViewById(R.id.createnotefbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();



        mcreatenotfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(allnotes.this, createnote.class));
            }
        });
        Query query = firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebaseModel> allusernotes = new FirestoreRecyclerOptions.Builder<firebaseModel>().setQuery(query, firebaseModel.class).build();
NotesAdapter= new FirestoreRecyclerAdapter<firebaseModel, NoteViewHolder>(allusernotes) {
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebaseModel model) {

        ImageView popupbtn=holder.itemView.findViewById(R.id.menupopupbtn);

        int colorcode=getRandomColor();
        holder.mnotes.setBackgroundColor(holder.itemView.getResources().getColor(colorcode,null));

holder.notetitle.setText(model.getTitle());
holder.notecontent.setText(model.getContent());

String docId=NotesAdapter.getSnapshots().getSnapshot(position).getId();

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(view.getContext(), notedetailsActivity.class);
        intent.putExtra("title",model.getTitle());
        intent.putExtra("content",model.getContent());
        intent.putExtra("noteId",docId);
        view.getContext().startActivity(intent);

        //Toast.makeText(allnotes.this, "clicked item!", Toast.LENGTH_SHORT).show();
    }
});

popupbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
        popupMenu.setGravity(Gravity.END);
        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Intent intent=new Intent(view.getContext(),editnoteActivity.class);
                intent.putExtra("title",model.getTitle());
                intent.putExtra("content",model.getContent());
                intent.putExtra("noteId",docId);
                view.getContext().startActivity(intent);

                return false;
            }
        });

        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {

                DocumentReference documentReference=firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
              documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      Toast.makeText(allnotes.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(allnotes.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                  }
              });
                //  Toast.makeText(allnotes.this, "Deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        popupMenu.show();
    }
});
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
        return new NoteViewHolder(view);
    }
};



recyclerView=findViewById(R.id.recycleview);
recyclerView.setHasFixedSize(true);
 staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,staggeredGridLayoutManager.VERTICAL);
 recyclerView.setLayoutManager(staggeredGridLayoutManager);
 recyclerView.setAdapter(NotesAdapter);

    }


public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnotes;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        notetitle=itemView.findViewById(R.id.notetitle);
        notecontent=itemView.findViewById(R.id.notecontent);
        mnotes=itemView.findViewById(R.id.notes);
    }
}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(allnotes.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        NotesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(NotesAdapter!=null){
            NotesAdapter.stopListening();
        }
    }

    private int getRandomColor(){
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.purple);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.yellow);
        colorcode.add(R.color.gray);
        colorcode.add(R.color.green);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }

    @Override
    public void onBackPressed() {

    finishAffinity();
        super.onBackPressed();
    }
}

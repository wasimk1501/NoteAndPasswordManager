package com.example.notepasswordmanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class passwordactivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton more_button, add_button, generate_button;
    private Boolean clicked = false;
    private TextView tvgenerate, tvadd;
    RecyclerView mpasswordrecyclerview;
    StaggeredGridLayoutManager pstaggeredGridLayoutManager;
    private FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<pfirebasemodel, passwordactivity.pNoteViewHolder> passwordAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordactivity);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        more_button = findViewById(R.id.more_button);
        add_button = findViewById(R.id.add_button);
        generate_button = findViewById(R.id.generate_button);
        tvgenerate = findViewById(R.id.tvgenerate);
        tvadd = findViewById(R.id.tvadd);
        mpasswordrecyclerview = findViewById(R.id.passwordrecyclerview);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarColor)));
        getSupportActionBar().setTitle("Saved Password");
        Animation rotateOpen = AnimationUtils.loadAnimation(passwordactivity.this, R.anim.rotate_open_anim);
        Animation rotateClose = AnimationUtils.loadAnimation(passwordactivity.this, R.anim.rotate_close_anim);
        Animation fromBottom = AnimationUtils.loadAnimation(passwordactivity.this, R.anim.from_bottom_anim);
        Animation toBottom = AnimationUtils.loadAnimation(passwordactivity.this, R.anim.to_bottom_anim);


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(passwordactivity.this, "Add button is clicked", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(passwordactivity.this, createpassword.class));

            }
        });
        generate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(passwordactivity.this, "generate button is clicked", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(passwordactivity.this, choosepasslength.class));

            }
        });

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onMoreButtonClicked();
                clicked = !clicked;
                if (clicked == true) {
                    tvadd.setVisibility(View.VISIBLE);
                    tvgenerate.setVisibility(View.VISIBLE);
                    tvadd.startAnimation(fromBottom);
                    tvgenerate.startAnimation(fromBottom);
                    more_button.startAnimation(rotateOpen);
                    add_button.startAnimation(fromBottom);
                    generate_button.startAnimation(fromBottom);

                } else {
                    tvadd.setVisibility(View.INVISIBLE);
                    tvgenerate.setVisibility(View.INVISIBLE);
                    tvadd.startAnimation(toBottom);
                    tvgenerate.startAnimation(toBottom);
                    more_button.setAnimation(rotateClose);
                    add_button.startAnimation(toBottom);
                    generate_button.startAnimation(toBottom);
                }
            }
        });

//        query for displaying data in password activity by fetching data from firebase firestore
        Query pquery = firebaseFirestore.collection("password").document(firebaseUser.getUid())
                .collection("mypassword").orderBy("ptitle", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<pfirebasemodel> alluserpassword = new FirestoreRecyclerOptions
                .Builder<pfirebasemodel>().setQuery(pquery, pfirebasemodel.class).build();

        passwordAdapter = new FirestoreRecyclerAdapter<pfirebasemodel, passwordactivity
                .pNoteViewHolder>(alluserpassword) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull passwordactivity.pNoteViewHolder holder,
                                            int position, @NonNull pfirebasemodel model) {
                ImageView ppopupMenu = holder.itemView.findViewById(R.id.pmenupopbutton);

//                int colourcode=getRandomColor();
//                holder.mpass.setBackgroundColor
//                        (holder.itemView.getResources().getColor(colourcode, null));
                holder.passwordtitle.setText(model.getPtitle());
                holder.passwordid.setText("Id : " + model.getPid());
                holder.actualpassword.setText("Password : " + model.getPpassword());

                String docId = passwordAdapter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //we have to open note detail activity
                        Intent intent = new Intent(view.getContext(), passworddetails.class);
                        intent.putExtra("ptitle", model.getPtitle());
                        intent.putExtra("pid", model.getPid());
                        intent.putExtra("ppassword", model.getPpassword());
                        intent.putExtra("passId", docId);
                        view.getContext().startActivity(intent);

//                        Toast.makeText(noteactivity.this, "This is clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                ppopupMenu.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)//
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit")
                                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        Intent intent = new Intent(view.getContext(), editpasswordactivity.class);
                                        intent.putExtra("ptitle", model.getPtitle());
                                        intent.putExtra("pid", model.getPid());
                                        intent.putExtra("ppassword", model.getPpassword());
                                        intent.putExtra("passId", docId);
                                        view.getContext().startActivity(intent);
                                        return false;
                                    }
                                });
                        popupMenu.getMenu().add("Delete")
                                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
//                                Toast.makeText(view.getContext(), "This note is deleted",
//                                        Toast.LENGTH_SHORT).show();
                                        DocumentReference documentReference = firebaseFirestore
                                                .collection("password").document(firebaseUser.getUid())
                                                .collection("mypassword").document(docId);
                                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(view.getContext(), "This password is deleted",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(), "Failed to Delete",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        return false;
                                    }
                                });
                        popupMenu.show();
                    }
                });
            }


            @NonNull
            @Override
            public passwordactivity.pNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.password_layout, parent, false);
                return new pNoteViewHolder(view);
            }
        };

        mpasswordrecyclerview = findViewById(R.id.passwordrecyclerview);
        mpasswordrecyclerview.setHasFixedSize(true);
        pstaggeredGridLayoutManager = new StaggeredGridLayoutManager
                (1, StaggeredGridLayoutManager.VERTICAL);
        mpasswordrecyclerview.setLayoutManager(pstaggeredGridLayoutManager);
        mpasswordrecyclerview.setAdapter(passwordAdapter);

    }


    public class pNoteViewHolder extends RecyclerView.ViewHolder {
        private TextView passwordtitle;
        private TextView passwordid;
        private TextView actualpassword;
        LinearLayout mpass;

        public pNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            passwordtitle = itemView.findViewById(R.id.passwordtitle);
            passwordid = itemView.findViewById(R.id.passwordid);
            actualpassword = itemView.findViewById(R.id.actualpassword);
            mpass = itemView.findViewById(R.id.pass);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        passwordAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (passwordAdapter != null) {
            passwordAdapter.stopListening();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                finish();
                firebaseAuth.signOut();
                startActivity(new Intent(passwordactivity.this, MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(passwordactivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
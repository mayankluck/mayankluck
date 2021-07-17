package com.example.adminpannel.ui.Users;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminpannel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class myAdapter extends FirebaseRecyclerAdapter<User, myAdapter.myviewholder> {
    public myAdapter(@NonNull @NotNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull myviewholder holder, int position, @NonNull @NotNull User model) {
        holder.name.setText(model.getUserName());
        holder.email.setText(model.getEmail());
        holder.mobile.setText(model.getMobileNo());
        holder.designation.setText(model.getDesignation());

        try {
            Picasso.get().load(model.getImage()).into(holder.userImg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.userImg.getContext())
                        .setContentHolder(new ViewHolder(R.layout.user_update_layout))
                        .setExpanded(true, 1100)
                        .create();

                View itemview = dialogPlus.getHolderView();
                TextView username = itemview.findViewById(R.id.username);
                EditText updatemobile = itemview.findViewById(R.id.updatemobileNo);
                EditText updateaddress = itemview.findViewById(R.id.updateaddress);
                EditText updatepin = itemview.findViewById(R.id.updatepin);
                Button updatesubmit = itemview.findViewById(R.id.updatesubmit);

                username.setText(model.getUserName());
                updatemobile.setText(model.getMobileNo());
                updateaddress.setText(model.getAddress());
                updatepin.setText(model.getPin());
                dialogPlus.show();

                updatesubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("mobileNo", updatemobile.getText().toString());
                        map.put("address", updateaddress.getText().toString());
                        map.put("pin", updatepin.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Users").child("Users")
                                .child(getRef(position).getKey().toString()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth;
                firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.signInWithEmailAndPassword(model.getEmail(), model.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                    dialog.setTitle("Delete User");
                                    dialog.setMessage("Are you Want to delete "+model.getEmail()+" Account");
                                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(v.getContext(), "Login with " + model.getEmail(), Toast.LENGTH_SHORT).show();
                                            FirebaseUser firebaseUser;
                                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                            firebaseUser.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                FirebaseDatabase.getInstance().getReference().child("Users").child("Users")
                                                                        .child(getRef(position).getKey()).removeValue();
                                                                Toast.makeText(v.getContext(), "Account Deleted" + model.getEmail(), Toast.LENGTH_SHORT).show();
                                                            }else
                                                                Toast.makeText(v.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });
                                    dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog alertDialog = dialog.create();
                                    alertDialog.show();
                                } else
                                    Toast.makeText(v.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(v.getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        private TextView name, email, mobile, designation;
        private Button updateInfo, deleteUser;
        private ImageView userImg;

        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.tx_userName);
            email = itemView.findViewById(R.id.tx_email);
            mobile = itemView.findViewById(R.id.tx_mobile);
            designation = itemView.findViewById(R.id.tx_area);
            updateInfo = itemView.findViewById(R.id.userUpdate);
            userImg = itemView.findViewById(R.id.userImg);
            deleteUser = itemView.findViewById(R.id.userDelete);
        }
    }
}

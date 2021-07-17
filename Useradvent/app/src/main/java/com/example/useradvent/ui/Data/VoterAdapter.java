package com.example.useradvent.ui.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useradvent.PublicData;
import com.example.useradvent.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoterAdapter extends RecyclerView.Adapter<VoterAdapter.VoterviewAdapter>{
    private final List<VoterData> list;
    private final Context context;

    public VoterAdapter(List<VoterData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public VoterviewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voter_item_layout, parent, false);
        return new VoterviewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VoterviewAdapter holder, int position) {
        VoterData item = list.get(position);
        holder.name.setText(item.getFullname());
        holder.mobile.setText(item.getMobileNo());
        holder.age.setText(item.getAge());
        holder.gender.setText(item.getGender());
        holder.cost.setText(item.getSubcaste());
        holder.city.setText(item.getBooth());
        try {
            Picasso.get().load(item.getImage()).into(holder.voterImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(item.getCaste().toLowerCase().equals("general"))
//            PublicData.count +=1;
        holder.voterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VoterFullDetails.class);
                intent.putExtra("image", item.getImage());
                intent.putExtra("epic", item.getEpic());
                intent.putExtra("name", item.getFullname());
                intent.putExtra("father", item.getFathername());
                intent.putExtra("age", item.getAge());
                intent.putExtra("gender", item.getGender());
                intent.putExtra("caste", item.getCaste());
                intent.putExtra("subCaste", item.getSubcaste());
                intent.putExtra("mobile", item.getMobileNo());
                intent.putExtra("adhar", item.getAadhaarNo());
                intent.putExtra("occupation", item.getOccupation());
                intent.putExtra("male18p", item.getMale18p());
                intent.putExtra("female18p", item.getFemale18p());
                intent.putExtra("male18m", item.getMale18m());
                intent.putExtra("female18m", item.getFemale18m());
                intent.putExtra("address1", item.getAdderess1());
                intent.putExtra("address2", item.getAddress2());
                intent.putExtra("city", item.getBooth());
                intent.putExtra("district", item.getDistrict());
                intent.putExtra("state", item.getState());
                v.getContext().startActivity(intent);
            }
        });
        holder.updateVoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.voterImg.getContext())
                        .setContentHolder(new ViewHolder(R.layout.voter_update_layout))
                        .setExpanded(true, 1100)
                        .setInAnimation(R.anim.abc_fade_in)
                        .setOutAnimation(R.anim.abc_fade_out)
                        .create();

                View itemview = dialogPlus.getHolderView();
                TextView votername = itemview.findViewById(R.id.votername);

                EditText votermobile = itemview.findViewById(R.id.votermobile);
                EditText voteraddress1 = itemview.findViewById(R.id.voteraddress1);
                EditText voteraddress2 = itemview.findViewById(R.id.voteraddress2);
                EditText voterfather = itemview.findViewById(R.id.voterfatherName);
                EditText voteroccupation = itemview.findViewById(R.id.voteroccupation);
                Button updatevoter = itemview.findViewById(R.id.updatevoter);

                votername.setText(item.getFullname());
                votermobile.setText(item.getMobileNo());
                voteraddress1.setText(item.getAdderess1());
                voteraddress2.setText(item.getAddress2());
                voterfather.setText(item.getFathername());
                voteroccupation.setText(item.getOccupation());
                dialogPlus.show();

                updatevoter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("mobile", votermobile.getText().toString());
                        map.put("adderess1", voteraddress1.getText().toString());
                        map.put("address2", voteraddress2.getText().toString());
                        map.put("fathername", voterfather.getText().toString());
                        map.put("occupation", voteroccupation.getText().toString());
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("VoterData");
                        reference.keepSynced(true);
                        reference.child(item.getFullname()+item.getAadhaarNo())
                                .updateChildren(map)
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
        holder.deleteVoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Delete Voter");
                dialog.setMessage("You have No Permission to Delete " + item.getFullname());
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.setNegativeButton("...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VoterviewAdapter extends RecyclerView.ViewHolder {
        private TextView name, mobile, age, gender, cost, city;
        private ImageView voterImg;
        private Button updateVoter, deleteVoter;


        public VoterviewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tx_userName);
            gender = itemView.findViewById(R.id.tx_gender);
            mobile = itemView.findViewById(R.id.tx_mobile);
            age = itemView.findViewById(R.id.tx_age);
            cost = itemView.findViewById(R.id.tx_caste);
            city = itemView.findViewById(R.id.tx_city);
            voterImg = itemView.findViewById(R.id.im_voterImg);
            updateVoter = itemView.findViewById(R.id.voterUpdate);
            deleteVoter = itemView.findViewById(R.id.voterDelete);
            CardView finalData = itemView.findViewById(R.id.finalData);


        }
    }
}

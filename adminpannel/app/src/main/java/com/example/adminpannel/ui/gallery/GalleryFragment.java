package com.example.adminpannel.ui.gallery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminpannel.MainadminActivity;
import com.example.adminpannel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText title, description;
    private TextView selectImgageTxt, manageCategory;
    private LinearLayout crtNewLayout;
    private static final int REQ = 1;
    private String category, delCatItem;
    private Bitmap bitmap;
    private ImageView galImgPrev;
    private Spinner imgCat;
    private ProgressDialog pd;
    private ArrayAdapter<String> categoryAdapter;
    ValueEventListener categoryListener;
    ArrayList<String> allcat;


    private RecyclerView party;
    GalleryAdapter adapterParty;
    DatabaseReference reference;
    Spinner categorySpinner;
    private ArrayList<String> allcategory;
    private ArrayList<GalleryData> partyImg;
    private ArrayAdapter<String> catAdapter;
    ValueEventListener catListener;
    private TextView showNewCat;

    private DatabaseReference db;
    private StorageReference storageReference;
    private String downloadUrl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        allcat = new ArrayList<>();
        showNewCat = root.findViewById(R.id.txCreateNewGallery);
        CardView selectImg = root.findViewById(R.id.cardImgGall);
        MaterialButton uploadImg = root.findViewById(R.id.btupoadImg);
        imgCat = root.findViewById(R.id.imgCategory);
        galImgPrev = root.findViewById(R.id.gallimgPrev);
        title = root.findViewById(R.id.titleNews);
        description = root.findViewById(R.id.detailImg);
        manageCategory = root.findViewById(R.id.managecat);
        selectImgageTxt = root.findViewById(R.id.selectImg);
        crtNewLayout = root.findViewById(R.id.createNewGallery);
        pd = new ProgressDialog(getActivity());
        db = FirebaseDatabase.getInstance().getReference().child("Gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("Gallery");

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                uploadImg.setEnabled(true);
            }
        });

        categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allcat);
        imgCat.setAdapter(categoryAdapter);

        categoryListener = db.child("CategoryModel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                allcat.clear();
                allcat.add("Select Category");
                for (DataSnapshot data : snapshot.getChildren())
                    allcat.add(data.getValue().toString());
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        imgCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = imgCat.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        manageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(requireContext())
                        .setContentHolder(new ViewHolder(R.layout.managecategory_layout))
                        .setExpanded(true, 1100)
                        .create();

                View itemview = dialogPlus.getHolderView();
                TextView crtNewCat = itemview.findViewById(R.id.crtCat);
                CardView crtNewCatCard = itemview.findViewById(R.id.cretCatCard);
                EditText newCatName = itemview.findViewById(R.id.catName);
                MaterialButton btCrtNewCat = itemview.findViewById(R.id.createCat);
                TextView deleteCat = itemview.findViewById(R.id.deleteCat);
                CardView deleteCatCard = itemview.findViewById(R.id.deleteCatCard);
                Spinner selectCat = itemview.findViewById(R.id.catspinner);
                MaterialButton btDelete = itemview.findViewById(R.id.delete);
                dialogPlus.show();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Gallery");
                ArrayList<String> delCatList = new ArrayList<>();

                ArrayAdapter<String> deleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, delCatList);
                selectCat.setAdapter(deleteAdapter);

                selectCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        delCatItem = parent.getItemAtPosition(position).toString();
                        Toast.makeText(getActivity(),delCatItem .toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                reference.child("CategoryModel").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        delCatList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            delCatList.add(data.getValue().toString());
                        }
                        deleteAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                crtNewCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (crtNewCatCard.getVisibility() == View.GONE) {
                            crtNewCatCard.setVisibility(View.VISIBLE);
                            deleteCatCard.setVisibility(View.GONE);
                        } else {
                            crtNewCatCard.setVisibility(View.GONE);
                            deleteCatCard.setVisibility(View.GONE);
                        }
                    }
                });

                deleteCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (deleteCatCard.getVisibility() == View.GONE) {
                            deleteCatCard.setVisibility(View.VISIBLE);
                            crtNewCatCard.setVisibility(View.GONE);
                        } else {
                            crtNewCatCard.setVisibility(View.GONE);
                            deleteCatCard.setVisibility(View.GONE);
                        }
                    }
                });

                btCrtNewCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newCatName.getText().toString().isEmpty()) {
                            newCatName.setError("");
                            newCatName.requestFocus();
                        } else {

                            reference.child("CategoryModel").child(newCatName.getText().toString()).setValue(newCatName.getText().toString().trim());
                            Toast.makeText(getActivity(), "Category Added Successfully", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        }
                    }
                });
                btDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reference.child(delCatItem).removeValue();
                        reference.child("CategoryModel").child(delCatItem).removeValue();
                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();

                    }
                });
            }
        });

        uploadImg.setEnabled(false);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    selectImgageTxt.setError("");
                    selectImgageTxt.requestFocus();
                } else if (category.equals("Select Category")) {
                    manageCategory.setError("");
                    manageCategory.requestFocus();
                } else if (title.getText().toString().isEmpty()) {
                    title.setError("");
                    title.requestFocus();
                } else if (description.getText().toString().isEmpty()) {
                    description.setError("");
                    description.requestFocus();
                } else {
                    pd.setMessage("Uploading...");
                    pd.show();
                    uploadImage();
                }
            }
        });


        allcategory = new ArrayList<>();
        partyImg = new ArrayList<>();

        party = root.findViewById(R.id.galView);
        categorySpinner = root.findViewById(R.id.imgViewcategory);


        reference = FirebaseDatabase.getInstance().getReference().child("Gallery");
        reference.keepSynced(true);
        adapterParty = new GalleryAdapter(getContext(), partyImg);
        party.setLayoutManager(new GridLayoutManager(getContext(), 2));
        party.setAdapter(adapterParty);

        categorySpinner.setOnItemSelectedListener(this);

        catAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allcategory);
        categorySpinner.setAdapter(catAdapter);

        catListener = reference.child("CategoryModel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allcategory.clear();
                for (DataSnapshot data : snapshot.getChildren())
                    allcategory.add(data.getValue().toString());
                catAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        showNewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectImg.getVisibility()==View.GONE){
                    selectImg.setVisibility(View.VISIBLE);
                    crtNewLayout.setVisibility(View.VISIBLE);
                }else {
                    selectImg.setVisibility(View.GONE);
                    crtNewLayout.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }

    private void uploadImage() {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, boas);
        byte[] finalImg = boas.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(finalImg + "jpeg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(requireActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();

                                }
                            });

                        }
                    });
                }
            }
        });
    }

    private void uploadData() {
        db = db.child(category);
        final String uniqueKey = db.push().getKey();

        Map<String, Object> map = new HashMap<>();
        map.put("title", title.getText().toString().trim());
        map.put("description", description.getText().toString().trim());
        map.put("image", downloadUrl);
        Calendar calendarDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currDate = new SimpleDateFormat("dd-MM-yy");
        String date = currDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currTime = new SimpleDateFormat("hh:mm a");
        String time = currTime.format(calendarTime.getTime());

        db.child(date+"_"+time).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainadminActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent picImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picImg, REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            galImgPrev.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String cate = parent.getItemAtPosition(position).toString();
        reference.child(cate).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                partyImg.clear();
                for (DataSnapshot partySnapshot : datasnapshot.getChildren())
                    partyImg.add(partySnapshot.getValue(GalleryData.class));
                adapterParty.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
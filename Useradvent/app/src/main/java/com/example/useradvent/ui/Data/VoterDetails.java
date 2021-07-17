package com.example.useradvent.ui.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.useradvent.R;
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

import android.app.ProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoterDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public String vFullName, vFather, vAge, vSex = "sex", vcaste = "caste", vsubcaste, vMobile, vadharNo, vEpic, vMale18p, vOccupation, vFemale18p, vMale18m, vFemale18m, vAddressLine1, vAddressLine2 = "", vbooth, vsector, vDistrict, vZone, vState;
    public int vTotal18p = -1, vTotal18m = -1;
    private final int REQ = 1;
    private Bitmap bitmap;
    String downloadUrl = "";
    private EditText fullName;
    private EditText father;
    private EditText mobile;
    private EditText adhar;
    private EditText epic;
    private EditText male18p;
    private EditText occupation;
    private EditText female18p;
    private EditText male18m;
    private EditText female18m;
    private EditText addressLine1;
    private EditText addressLine2;
    private TextView total18p, total18m;
    private RadioButton sex;
    private RadioButton caste;
    private ImageView voterImgPrev;

    private ArrayAdapter<String> stateAdapter, zoneAdapter, districtAdapter, sectorAdapter, boothAdapter, subcasteAdapter;
    List<String> allStates, allZone, allDistrict, allSector, allBooth, allsubcaste;
    private DatabaseReference databaseReference, reference, subCasteReference;


    private StorageReference storageReference;
    private ProgressDialog pd;


    private Calendar calendar;
    private TextView age, ageYr;
    private int year, month, day;


    @SuppressLint({"ResourceType", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("All Voters");
        setContentView(R.layout.activity_voter_details);

        allStates = new ArrayList<>();
        allDistrict = new ArrayList<>();
        allZone = new ArrayList<>();
        allSector = new ArrayList<>();
        allBooth = new ArrayList<>();
        allsubcaste = new ArrayList<>();

        TextView personal = findViewById(R.id.personal);
        fullName = findViewById(R.id.fullName);
        father = findViewById(R.id.fathName);
        sex = findViewById(R.id.sexOther);
        caste = findViewById(R.id.casteSt);
        mobile = findViewById(R.id.fmobileNo);
        adhar = findViewById(R.id.adharNo);
        epic = findViewById(R.id.epicNo);
        occupation = findViewById(R.id.occup);
        male18p = findViewById(R.id.male18p);
        female18p = findViewById(R.id.female18p);
        male18m = findViewById(R.id.male18m);
        female18m = findViewById(R.id.female18m);
        addressLine1 = findViewById(R.id.address1);
        addressLine2 = findViewById(R.id.address2);
        total18p = findViewById(R.id.total18p);
        total18m = findViewById(R.id.total18m);


        Spinner stateSpinner = findViewById(R.id.sp_state);
        Spinner zoneSpinner = findViewById(R.id.sp_zone);
        Spinner districtSpinner = findViewById(R.id.sp_district);
        Spinner sectorSpinner = findViewById(R.id.sp_sector);
        Spinner boothSpinner = findViewById(R.id.sp_booth);
        Spinner subCasteSpinner = findViewById(R.id.sp_subcaste);

        subCasteReference = FirebaseDatabase.getInstance().getReference().child("SubCaste");
        reference = FirebaseDatabase.getInstance().getReference().child("Place");
        reference.keepSynced(true);
        subCasteReference.keepSynced(true);

        stateSpinner.setOnItemSelectedListener(this);
        zoneSpinner.setOnItemSelectedListener(this);
        districtSpinner.setOnItemSelectedListener(this);
        sectorSpinner.setOnItemSelectedListener(this);
        boothSpinner.setOnItemSelectedListener(this);
        subCasteSpinner.setOnItemSelectedListener(this);


        databaseReference = FirebaseDatabase.getInstance().getReference("VoterData");
        databaseReference.keepSynced(true);
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);
        voterImgPrev = findViewById(R.id.voterImg);
        MaterialButton submit = findViewById(R.id.submitDetails);
        MaterialButton voterImg = findViewById(R.id.selectVoterImg);


        age = findViewById(R.id.age);
        ageYr = findViewById(R.id.ageNow);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        subcasteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allsubcaste);
        subCasteSpinner.setAdapter(subcasteAdapter);

        stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allStates);
        stateSpinner.setAdapter(stateAdapter);
        zoneAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allZone);
        zoneSpinner.setAdapter(zoneAdapter);

        districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allDistrict);
        districtSpinner.setAdapter(districtAdapter);

        sectorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allSector);
        sectorSpinner.setAdapter(sectorAdapter);
        boothAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allBooth);
        boothSpinner.setAdapter(boothAdapter);

        ValueEventListener stateListener = reference.child("State").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allStates.add("Select State");
                for (DataSnapshot data : snapshot.getChildren())
                    allStates.add(data.getValue().toString());
                stateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        voterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vFullName = fullName.getText().toString().trim();
                vFather = father.getText().toString().trim();
                vAge = age.getText().toString().trim();
                vadharNo = adhar.getText().toString().toLowerCase().trim();
                vMobile = mobile.getText().toString().trim();
                vEpic = epic.getText().toString().trim();
                vOccupation = occupation.getText().toString().trim();
                vMale18p = male18p.getText().toString().trim();
                vFemale18p = female18p.getText().toString().trim();
                vMale18m = male18m.getText().toString().trim();
                vFemale18m = female18m.getText().toString().trim();
                vAddressLine1 = addressLine1.getText().toString().trim();
                vAddressLine2 = addressLine2.getText().toString().trim();


                if (vFullName.isEmpty() || vFullName.length() > 25) {
                    fullName.setError("Enter Valid Voter Full Name");
                    fullName.requestFocus();
                }
                else if (vFather.isEmpty() || vFather.length() > 25) {
                    father.setError("Enter Voter Father's Name");
                    father.requestFocus();
                } else if (vAge.isEmpty()) {
                    age.setError("Enter valid Voter age");
                    age.requestFocus();
                } else if (vSex.equals("sex")) {
                    sex.setError("Select Voter Sex");
                    sex.requestFocus();
                } else if (vcaste.equals("caste")) {
                    caste.setError("Select Voter caste");
                    caste.requestFocus();
                } else if (vsubcaste.isEmpty()) {
                    caste.setError("Select Voter Subcaste");
                    caste.requestFocus();
                } else if (vMobile.length() != 10) {
                    mobile.setError("Enter 10 digit mobile Number");
                    mobile.requestFocus();
                } else if (vadharNo.length() != 12) {
                    adhar.setError("Enter 12 digit Aadhaar Number");
                    adhar.requestFocus();
                } else if (vEpic.isEmpty()) {
                    epic.setError("By Default Number is 0");
                    epic.requestFocus();
                } else if (vOccupation.isEmpty() || vOccupation.length() > 25) {
                    occupation.setError("Enter Voter Occupation");
                    occupation.requestFocus();
                } else if (vMale18p.isEmpty() || vFemale18p.isEmpty() || vMale18m.isEmpty() || vFemale18m.isEmpty()) {
                    total18m.setError("Enter Voter Occupation");
                    total18m.requestFocus();
                } else if (vAddressLine1.isEmpty()) {
                    addressLine1.setError("Enter Voter address");
                    addressLine1.requestFocus();
                } else if (bitmap == null) {
                    personal.setError("Please select an Image");
                    personal.requestFocus();

                }
                else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("VoterData");
                    reference.orderByChild("aadhaarNo").equalTo(adhar.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(VoterDetails.this);
                                builder.setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Already Exits")
                                        .setMessage("This Aadhaar Card is already exits please enter the new New Aadhaar Number")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                vadharNo = null;
                                                adhar.setText("");
                                                adhar.requestFocus();
                                            }
                                        })
                                        .setNegativeButton("Edit", null)
                                        .show();
                            }
                            else {
                                vTotal18p = Integer.parseInt(vMale18p) + Integer.parseInt(vFemale18p);
                                vTotal18m = Integer.parseInt(vMale18m) + Integer.parseInt(vFemale18m);
                                AlertDialog.Builder builder = new AlertDialog.Builder(VoterDetails.this);
                                builder.setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Please Conform the details")
                                        .setMessage("Name - " + vFullName + "\nFather's Name - " + vFather + "\nAge - " + vAge + "\nGender - " + vSex + "\ncaste - " + vcaste + "\t" + vsubcaste + "\nMobile No - " + vMobile + "\nAadhaar No - " + vadharNo + "\nEPIC No - " + vEpic + "\nOccupation - " + vOccupation + "\nAdult Member - " + vTotal18p + "\nKids -" + vTotal18m + "\nAddress - " + vAddressLine1 + " \n\t\t\t" + vAddressLine2 + "\n\t\t\t " + vZone + " \n\t\t\t" + vDistrict + " \n\t\t\t" + vState)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                pd.setMessage("Uploading Data");
                                                pd.show();
                                                uploadImage();
                                            }
                                        })
                                        .setNegativeButton("Edit", null)
                                        .show();
                                Log.e("TAG", "New User");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void uploadImage() {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, boas);
        byte[] finalImg = boas.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("VoterDataCollection").child(finalImg + "jpeg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadVoterDetails();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(VoterDetails.this, "Something went Wrong Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadVoterDetails() {
        final String uniqueKey = databaseReference.push().getKey();
        VoterData voterData = new VoterData(downloadUrl, vFullName, vFather, vAge, vSex, vcaste, vsubcaste,vMobile, vadharNo, vEpic, vOccupation,
                vMale18p, vFemale18p, vMale18m, vFemale18m, vAddressLine1, vAddressLine2,vbooth, vsector, vDistrict, vZone, vState, uniqueKey);
        assert uniqueKey != null;
        databaseReference.child(vFullName+vadharNo).setValue(voterData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(VoterDetails.this, "VOTER Data Upload Successfully", Toast.LENGTH_SHORT).show();
                nullsetall();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(VoterDetails.this, "Something went Wrong upload data", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void nullsetall() {
        voterImgPrev.setImageResource(0);
        bitmap.recycle();
        fullName.setText("");
        father.setText("");
        age.setText("0Y 0M 0D");
        adhar.setText("");
        mobile.setText("");
        epic.setText("");
        occupation.setText("");
        male18p.setText("");
        female18p.setText("");
        total18p.setText("");
        male18m.setText("");
        female18m.setText("");
        total18m.setText("");
        addressLine1.setText("");
        addressLine2.setText("");
        stateAdapter.notifyDataSetChanged();
        allZone.clear();
        zoneAdapter.notifyDataSetChanged();
        allDistrict.clear();
        districtAdapter.notifyDataSetChanged();
        allSector.clear();
        sectorAdapter.notifyDataSetChanged();
        allBooth.clear();
        boothAdapter.notifyDataSetChanged();
        allsubcaste.clear();
        subcasteAdapter.notifyDataSetChanged();
        RadioButton msex = findViewById(R.id.sexMale);
        RadioButton fsex = findViewById(R.id.sexFemale);
        RadioButton gcaste = findViewById(R.id.casteGeneral);
        RadioButton ocaste = findViewById(R.id.casteObc);
        RadioButton scaste = findViewById(R.id.casteSc);
        msex.setChecked(false);
        fsex.setChecked(false);
        sex.setChecked(false);
        gcaste.setChecked(false);
        ocaste.setChecked(false);
        scaste.setChecked(false);
        caste.setChecked(false);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sp_state) {
            vState = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Selected " + vState, Toast.LENGTH_SHORT).show();
            ValueEventListener zoneListener = reference.child(vState).child("Zone").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    allZone.clear();
                    for (DataSnapshot data : snapshot.getChildren())
                        allZone.add(data.getValue().toString());
                    zoneAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        if (parent.getId() == R.id.sp_zone) {
            vZone = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Selected " + vZone, Toast.LENGTH_SHORT).show();
            ValueEventListener districtListener = reference.child(vState).child(vZone).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                    allDistrict.clear();
                    for (DataSnapshot distData : snapshot2.getChildren())
                        allDistrict.add(distData.getValue().toString());
                    districtAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        if (parent.getId() == R.id.sp_district) {
            vDistrict = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Selected " + vDistrict, Toast.LENGTH_SHORT).show();

            ValueEventListener sectorListener = reference.child(vState).child(vDistrict).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    allSector.clear();
                    for (DataSnapshot data : snapshot.getChildren())
                        allSector.add(data.getValue().toString());
                    sectorAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        if (parent.getId() == R.id.sp_sector) {
            vsector = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Selected " + vsector, Toast.LENGTH_SHORT).show();

            ValueEventListener boothListener = reference.child(vState).child(vsector).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    allBooth.clear();
                    for (DataSnapshot data : snapshot.getChildren())
                        allBooth.add(data.getValue().toString());
                    boothAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (parent.getId() == R.id.sp_booth) {
            vbooth = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Selected " + vbooth, Toast.LENGTH_SHORT).show();
        }
        if(parent.getId()== R.id.sp_subcaste){
            vsubcaste = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "SubCaste "+vsubcaste, Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @SuppressLint("NonConstantResourceId")
    public void sexButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.sexMale:
                if (checked)
                    vSex = "Male";
                Toast.makeText(this, "Male", Toast.LENGTH_LONG).show();
                break;
            case R.id.sexFemale:
                if (checked)
                    vSex = "Female";
                Toast.makeText(this, "Female", Toast.LENGTH_LONG).show();

                break;
            case R.id.sexOther:
                if (checked)
                    vSex = "Other";
                Toast.makeText(this, "Other", Toast.LENGTH_LONG).show();
                break;

        }
    }

    @SuppressLint("NonConstantResourceId")
    public void casteButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        ValueEventListener subCasteListener;
        switch (view.getId()) {
            case R.id.casteGeneral:
                if (checked) {
                    subCasteListener = subCasteReference.child("General").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            allsubcaste.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allsubcaste.add(data.getValue().toString());
                            subcasteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                vcaste = "GENERAL";
                Toast.makeText(this, "General", Toast.LENGTH_LONG).show();
                break;
            case R.id.casteObc:
                if (checked) {
                    subCasteListener = subCasteReference.child("Obc").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            allsubcaste.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allsubcaste.add(data.getValue().toString());
                            subcasteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                vcaste = "OBC";
                Toast.makeText(this, "OBC", Toast.LENGTH_LONG).show();
                break;
            case R.id.casteSc:
                if (checked) {
                    subCasteListener = subCasteReference.child("Sc").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            allsubcaste.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allsubcaste.add(data.getValue().toString());
                            subcasteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                vcaste = "SC";
                Toast.makeText(this, "SC", Toast.LENGTH_LONG).show();
                break;
            case R.id.casteSt:
                if (checked) {
                    subCasteListener = subCasteReference.child("St").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            allsubcaste.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allsubcaste.add(data.getValue().toString());
                            subcasteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                vcaste = "ST";
                Toast.makeText(this, "ST", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public void total18p(View view) {
        vMale18p = male18p.getText().toString().trim();
        vFemale18p = female18p.getText().toString().trim();

        vTotal18p = Integer.parseInt(vMale18p) + Integer.parseInt(vFemale18p);
        String s = Integer.toString(vTotal18p);
        total18p.setText(s);
    }

    public void total18m(View view) {
        vMale18m = male18m.getText().toString().trim();
        vFemale18m = female18m.getText().toString().trim();
        vTotal18m = Integer.parseInt(vMale18m) + Integer.parseInt(vFemale18m);
        String s = Integer.toString(vTotal18m);
        total18m.setText(s);
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
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            voterImgPrev.setImageBitmap(bitmap);
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @SuppressLint("SetTextI18n")
    private void showDate(int year, int month, int day) {
        age.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
        String dob = age.getText().toString().trim();
        vAge = dob;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = formatter.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Converting obtained Date object to LocalDate object
        assert date != null;
        Instant instant = date.toInstant();
        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
        LocalDate givenDate = zone.toLocalDate();
        //Calculating the difference between given date to current date.
        Period period = Period.between(givenDate, LocalDate.now());
        ageYr.setText(period.getYears() + "Y " + period.getMonths() + "M " + period.getDays() + "D");
        vAge = age.getText().toString().trim();
    }
}
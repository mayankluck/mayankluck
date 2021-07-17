package com.example.adminpannel.ui.VoterData;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


public class crtVoter extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    public String vFullName, vFather, vAge, vSex = "sex", vcaste = "caste", vsubcaste, vMobile, vadharNo, vEpic, vMale18p, vOccupation, vFemale18p, vMale18m, vFemale18m, vAddressLine1, vAddressLine2 = "", vbooth, vsector, vDistrict, vZone, vState;
    public int vTotal18p = -1, vTotal18m = -1;
    private final int REQ = 1;
    private Bitmap bitmap;
    String downloadUrl = "";
    private EditText fullName, father, mobile, adhar, epic, male18p, occupation, female18p, male18m, female18m, addressLine1, addressLine2;
    private TextView total18p, total18m;
    private RadioButton caste, msex, fsex, osex, gcaste, ocaste, scaste;
    private RadioGroup sexGroup, casteGroup;
    private ImageView voterImgPrev;
    private Spinner stateSpinner, zoneSpinner, districtSpinner, sectorSpinner, boothSpinner, subCasteSpinner;
    private ValueEventListener stateListener, zoneListener, districtListener, sectorListener, boothListener, subCasteListener;
    private ArrayAdapter<String> stateAdapter, zoneAdapter, districtAdapter, sectorAdapter, boothAdapter, subcasteAdapter;
    List<String> allStates, allZone, allDistrict, allSector, allBooth, allsubcaste;
    private DatabaseReference databaseReference, stateReference, zoneReference, districtReference, sectorReference, boothReference, subCasteReference;

    private LinearLayout setDate;

    private StorageReference storageReference;
    private ProgressDialog pd;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private TextView age, ageYr;
    private int year, month, day;

    @SuppressLint({"ResourceType", "CutPasteId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("New Voter");
        View root = inflater.inflate(R.layout.fragment_crt_voter, container, false);
        allStates = new ArrayList<>();
        allDistrict = new ArrayList<>();
        allZone = new ArrayList<>();
        allSector = new ArrayList<>();
        allBooth = new ArrayList<>();
        allsubcaste = new ArrayList<>();

        TextView personal = root.findViewById(R.id.personal);
        fullName = root.findViewById(R.id.fullName);
        father = root.findViewById(R.id.fathName);
        caste = root.findViewById(R.id.casteSt);
        mobile = root.findViewById(R.id.fmobileNo);
        adhar = root.findViewById(R.id.adharNo);
        epic = root.findViewById(R.id.epicNo);
        occupation = root.findViewById(R.id.occup);
        male18p = root.findViewById(R.id.male18p);
        female18p = root.findViewById(R.id.female18p);
        male18m = root.findViewById(R.id.male18m);
        female18m = root.findViewById(R.id.female18m);
        addressLine1 = root.findViewById(R.id.address1);
        addressLine2 = root.findViewById(R.id.address2);
        total18p = root.findViewById(R.id.total18p);
        total18m = root.findViewById(R.id.total18m);
        sexGroup = root.findViewById(R.id.sexGroup);
        casteGroup = root.findViewById(R.id.casteGroup);
        msex = root.findViewById(R.id.sexMale);
        fsex = root.findViewById(R.id.sexFemale);
        osex = root.findViewById(R.id.sexOther);
        gcaste = root.findViewById(R.id.casteGeneral);
        ocaste = root.findViewById(R.id.casteObc);
        scaste = root.findViewById(R.id.casteSc);
        setDate = root.findViewById(R.id.setDate);
        setDate.setOnClickListener(this);
        total18p.setOnClickListener(this);
        total18m.setOnClickListener(this);
        addressLine1.setOnClickListener(this);


        stateSpinner = root.findViewById(R.id.sp_state);
        zoneSpinner = root.findViewById(R.id.sp_zone);
        districtSpinner = root.findViewById(R.id.sp_district);
        sectorSpinner = root.findViewById(R.id.sp_sector);
        boothSpinner = root.findViewById(R.id.sp_booth);
        subCasteSpinner = root.findViewById(R.id.sp_subcaste);

        subCasteReference = FirebaseDatabase.getInstance().getReference().child("SubCaste");
        stateReference = FirebaseDatabase.getInstance().getReference().child("Place");
        zoneReference = FirebaseDatabase.getInstance().getReference().child("Place");
        districtReference = FirebaseDatabase.getInstance().getReference().child("Place");
        sectorReference = FirebaseDatabase.getInstance().getReference().child("Place");
        boothReference = FirebaseDatabase.getInstance().getReference().child("Place");

        stateSpinner.setOnItemSelectedListener(this);
        zoneSpinner.setOnItemSelectedListener(this);
        districtSpinner.setOnItemSelectedListener(this);
        sectorSpinner.setOnItemSelectedListener(this);
        boothSpinner.setOnItemSelectedListener(this);
        subCasteSpinner.setOnItemSelectedListener(this);


        databaseReference = FirebaseDatabase.getInstance().getReference("VoterData");
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(getActivity());
        voterImgPrev = root.findViewById(R.id.voterImg);
        MaterialButton submit = root.findViewById(R.id.submitDetails);
        MaterialButton voterImg = root.findViewById(R.id.selectVoterImg);


        age = (TextView) root.findViewById(R.id.age);
        ageYr = (TextView) root.findViewById(R.id.ageNow);

//        showDate(year, month + 1, day);
        initDatePicker();
        age.setText(today());

//        ageYr.setText(yearCalculate(age.getText()));






        subcasteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allsubcaste);
        subCasteSpinner.setAdapter(subcasteAdapter);

        stateAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allStates);
        stateSpinner.setAdapter(stateAdapter);
        zoneAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allZone);
        zoneSpinner.setAdapter(zoneAdapter);

        districtAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allDistrict);
        districtSpinner.setAdapter(districtAdapter);

        sectorAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allSector);
        sectorSpinner.setAdapter(sectorAdapter);
        boothAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allBooth);
        boothSpinner.setAdapter(boothAdapter);
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sexMale:
                        vSex = "Male";
                        Toast.makeText(getActivity(), ""+vSex, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sexFemale:
                        vSex = "Female";
                        Toast.makeText(getActivity(), ""+vSex, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sexOther:
                        vSex = "Other";
                        Toast.makeText(getActivity(), ""+vSex, Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        });
        casteGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.casteGeneral:
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
                        vcaste = "GENENAL";
                        break;
                    case R.id.casteObc:
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
                        vcaste = "OBC";
                        break;
                    case R.id.casteSc:
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
                        vcaste = "SC";
                        break;
                    case R.id.casteSt:
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
                        vcaste = "ST";
                        break;

                }
            }
        });
        stateListener = stateReference.child("State").addValueEventListener(new ValueEventListener() {
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
                vadharNo = adhar.getText().toString().trim();
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
                } else if (vFather.isEmpty() || vFather.length() > 25) {
                    father.setError("Enter Voter Father's Name");
                    father.requestFocus();
                } else if (vAge.isEmpty()) {
                    age.setError("Enter valid Voter age");
                    age.requestFocus();
                } else if (vSex.equals("sex")) {
                    osex.setError("Select Voter Sex");
                    osex.requestFocus();
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

                } else {
                    vTotal18p = Integer.parseInt(vMale18p) + Integer.parseInt(vFemale18p);
                    vTotal18m = Integer.parseInt(vMale18m) + Integer.parseInt(vFemale18m);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

                }
            }
        });
        return root;
    }

    private String yearCalculate(CharSequence text) {
        String dob = (String) text;
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
        vAge = age.getText().toString().trim();
        return period.getYears() + "Y " + period.getMonths() + "M " + period.getDays() + "D";

    }

    private void uploadImage() {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, boas);
        byte[] finalImg = boas.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("VoterDataCollection").child(finalImg + "jpeg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(requireActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(getActivity(), "Something went Wrong Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadVoterDetails() {
        final String uniqueKey = databaseReference.push().getKey();
        VoterData voterData = new VoterData(downloadUrl, vFullName, vFather, vAge, vSex, vcaste, vsubcaste, vMobile, vadharNo, vEpic, vOccupation,
                vMale18p, vFemale18p, vMale18m, vFemale18m, vAddressLine1, vAddressLine2, vbooth, vsector, vDistrict, vZone, vState, uniqueKey);
        assert uniqueKey != null;
        databaseReference.child(vFullName + vadharNo).setValue(voterData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(getActivity(), "VOTER Data Upload Successfully", Toast.LENGTH_SHORT).show();
                nullsetall();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Something went Wrong upload data", Toast.LENGTH_LONG).show();
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
        msex.setChecked(false);
        fsex.setChecked(false);
        osex.setChecked(false);
        gcaste.setChecked(false);
        ocaste.setChecked(false);
        scaste.setChecked(false);
        caste.setChecked(false);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sp_state) {
            vState = parent.getItemAtPosition(position).toString();
            Toast.makeText(getActivity(), "Selected " + vState, Toast.LENGTH_SHORT).show();
            zoneListener = zoneReference.child(vState).child("Zone").addValueEventListener(new ValueEventListener() {
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
            Toast.makeText(getActivity(), "Selected " + vZone, Toast.LENGTH_SHORT).show();
            districtListener = districtReference.child(vState).child(vZone).addValueEventListener(new ValueEventListener() {
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
            Toast.makeText(getActivity(), "Selected " + vDistrict, Toast.LENGTH_SHORT).show();

            sectorListener = sectorReference.child(vState).child(vDistrict).addValueEventListener(new ValueEventListener() {
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
            Toast.makeText(getActivity(), "Selected " + vsector, Toast.LENGTH_SHORT).show();

            boothListener = boothReference.child(vState).child(vsector).addValueEventListener(new ValueEventListener() {
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
            Toast.makeText(getActivity(), "Selected " + vbooth, Toast.LENGTH_SHORT).show();
        }
        if (parent.getId() == R.id.sp_subcaste) {
            vsubcaste = parent.getItemAtPosition(position).toString();
            Toast.makeText(getActivity(), "SubCaste " + vsubcaste, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setDate) {
            datePickerDialog.show();
        }
        if (v.getId() == R.id.total18p){
            vMale18p = male18p.getText().toString().trim();
            vFemale18p = female18p.getText().toString().trim();

            vTotal18p = Integer.parseInt(vMale18p) + Integer.parseInt(vFemale18p);
            String s = Integer.toString(vTotal18p);
            total18p.setText(s);

        }
        if (v.getId() == R.id.total18m){
            vMale18m = male18m.getText().toString().trim();
            vFemale18m = female18m.getText().toString().trim();
            vTotal18m = Integer.parseInt(vMale18m) + Integer.parseInt(vFemale18m);
            String s = Integer.toString(vTotal18m);
            total18m.setText(s);
        }
        if (v.getId() == R.id.address1){
            vMale18p = male18p.getText().toString().trim();
            vFemale18p = female18p.getText().toString().trim();

            vMale18m = male18m.getText().toString().trim();
            vFemale18m = female18m.getText().toString().trim();

            vTotal18p = Integer.parseInt(vMale18p) + Integer.parseInt(vFemale18p);
            String p18 = Integer.toString(vTotal18p);
            total18p.setText(p18);

            vTotal18m = Integer.parseInt(vMale18m) + Integer.parseInt(vFemale18m);
            String m18 = Integer.toString(vTotal18m);
            total18m.setText(m18);
        }
    }

    private void openGallery() {
        Intent picImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picImg, REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ ) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            voterImgPrev.setImageBitmap(bitmap);
        }
    }
    private String today() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                age.setText(date);
            }
        };
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        String dob = (String) ""+day+"-"+month+"-"+year;
        vAge = dob;
        Toast.makeText(getActivity(), ""+vAge, Toast.LENGTH_SHORT).show();
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

        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        return "JAN";
    }
}
package com.example.adminpannel.ui.AdminData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminpannel.MainadminActivity;
import com.example.adminpannel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class NewAdmin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    DatabaseReference casteReference;
    List<String> castelist;
    ValueEventListener casteListener;
    ArrayAdapter<String> casteAdapter;

    private EditText username, fatherName, epicNo, aadharNo, mobileno, email, password, address, city, district, state, pin, newbooth;
    private CircleImageView userImgPrev;
    private ImageView userImgAadhar;
    private ProgressDialog pd;
    private final int REQ = 1, REQA = 2;
    private Bitmap bitmapImage, bitmapAadhaar;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    String downloadUrlImg = "", downloadUrlAadhaar = "", uname, ufatherName, ugender = "sex", ucaste = "caste", usubcaste, uepicNo, uaadharNo, uage, umobileno, uemail, upassword, uaddress, ucity,
            udistrict, ustate, upin;
    private Spinner casteSpinner;
    private Calendar calendar;
    private TextView age, ageYr;
    private int year, month, day;
    private RadioButton sex, caste;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_admin);

        castelist = new ArrayList<>();

        username = findViewById(R.id.et_username);
        fatherName = findViewById(R.id.et_fathername);
        epicNo = findViewById(R.id.et_epicNo);
        aadharNo = findViewById(R.id.et_aadharNo);
        mobileno = findViewById(R.id.et_mobileNo);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        address = findViewById(R.id.et_address1);
        city = findViewById(R.id.et_city);
        district = findViewById(R.id.et_district);
        state = findViewById(R.id.et_state);
        pin = findViewById(R.id.et_pin);
        sex = findViewById(R.id.sexOther);
        caste = findViewById(R.id.casteSt);
        age =  findViewById(R.id.age);
        ageYr = findViewById(R.id.ageNow);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        Button crtUser = findViewById(R.id.bt_crtuser);
        Button userImg = findViewById(R.id.selectUserImg);
        Button userAadhar = findViewById(R.id.selectUserAadhar);

        userImgPrev = findViewById(R.id.userImg);
        userImgAadhar = findViewById(R.id.userAadharImg);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        casteSpinner = findViewById(R.id.casteSpinner);
        casteSpinner.setOnItemSelectedListener(this);


        userImg.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View view) {
                Intent UserpicImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(UserpicImg, REQ);
            }
        });
        userAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AdharpicImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(AdharpicImg, REQA);

            }
        });
        crtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = username.getText().toString().trim();
                ufatherName = fatherName.getText().toString().trim();
                uepicNo = epicNo.getText().toString().trim();
                uaadharNo = aadharNo.getText().toString().trim();
                uage = age.getText().toString().trim();
                umobileno = mobileno.getText().toString().trim();
                uemail = email.getText().toString().trim();
                upassword = password.getText().toString().trim();
                uaddress = address.getText().toString().trim();
                ucity = city.getText().toString().trim();
                udistrict = district.getText().toString().trim();
                ustate = state.getText().toString().trim();
                upin = pin.getText().toString().trim();


                if (uname.isEmpty()) {
                    username.setError("Please Enter User Name");
                    username.requestFocus();
                } else if (ufatherName.isEmpty()) {
                    fatherName.setError("Please Enter Father's Name");
                    fatherName.requestFocus();
                } else if (uepicNo.isEmpty()) {
                    epicNo.setError("Please Enter User Epic Number");
                    epicNo.requestFocus();
                } else if (uaadharNo.isEmpty()) {
                    aadharNo.setError("Please Enter User Aadhar No");
                    aadharNo.requestFocus();
                } else if (uage.isEmpty()) {
                    age.setError("Please Enter User Age");
                    age.requestFocus();
                } else if (ugender.equals("sex")) {
                    sex.setError("Select Voter Sex");
                    sex.requestFocus();
                } else if (ucaste.equals("caste")) {
                    caste.setError("Select Voter caste");
                    caste.requestFocus();
                } else if (usubcaste.isEmpty()) {
                    caste.setError("Select Voter Subcaste");
                    casteSpinner.requestFocus();
                } else if (umobileno.length() != 10) {
                    mobileno.setError("Please Enter User Mobile Number with 10 digits");
                    mobileno.requestFocus();
                } else if (uemail.isEmpty()) {
                    email.setError("Please Enter User Email Id");
                    email.requestFocus();
                } else if (upassword.isEmpty()) {
                    password.setError("Please Enter Password");
                    password.requestFocus();
                } else if (uaddress.isEmpty()) {
                    address.setError("Please Enter User address");
                    address.requestFocus();
                } else if (ucity.isEmpty()) {
                    city.setError("Please Enter User City");
                    city.requestFocus();
                } else if (udistrict.isEmpty()) {
                    district.setError("Please Enter User District");
                    district.requestFocus();
                } else if (ustate.isEmpty()) {
                    state.setError("Please Enter User State");
                    state.requestFocus();
                } else if (upin.isEmpty()) {
                    pin.setError("Please Enter Area Pin Number");
                    pin.requestFocus();
                } else if (bitmapImage == null && bitmapAadhaar == null) {
                    uploadData();
                } else {
                    mAuth.fetchSignInMethodsForEmail(uemail)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                    if (isNewUser) {
                                        uploadImage();
                                        uploadImageAadhaar();
                                        Log.e("TAG", "Is New User!");
                                    } else {
                                        pd.dismiss();;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(NewAdmin.this);
                                        builder.setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Already Exits")
                                                .setMessage("This email is already exits please enter the new email-Id")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        uemail = null;
                                                        email.setText("");
                                                        password.setText("");
                                                        email.requestFocus();
                                                    }
                                                })
                                                .setNegativeButton("Edit", null)
                                                .show();
                                    }

                                }
                            });
                }
            }
        });
    }

    private void uploadData() {
        pd.setMessage("Uploading...");
        pd.setTitle("Please Wait");
        pd.show();
        mAuth.createUserWithEmailAndPassword(uemail, upassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @com.google.firebase.database.annotations.NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String flag = "admin";
                            String address = uaddress + " " + ucity + " " + udistrict + " " + ustate;
                            AdminData admin = new AdminData(uname, uepicNo, uaadharNo, uage, ufatherName, ugender, ucaste, usubcaste, umobileno, uemail, upassword, address, upin,
                                    downloadUrlImg, downloadUrlAadhaar, flag);
                            FirebaseDatabase.getInstance().getReference().child("Admin")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @com.google.firebase.database.annotations.NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Toast.makeText(NewAdmin.this, "User Created Successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(NewAdmin.this, MainadminActivity.class));

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(NewAdmin.this, "Fail to register! Try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(NewAdmin.this, "Fail to register!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void uploadImage() {
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream boasImg = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, boasImg);
        byte[] finalUserImg = boasImg.toByteArray();
        final StorageReference filePathImg;
        filePathImg = storageReference.child("Admin").child(finalUserImg + "jpeg");
        final UploadTask imgUploadTask = filePathImg.putBytes(finalUserImg);
        imgUploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imgUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePathImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrlImg = String.valueOf(uri);
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(NewAdmin.this, "Something went Wrong Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadImageAadhaar() {
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmapAadhaar.compress(Bitmap.CompressFormat.JPEG, 50, boas);
        byte[] finalImg = boas.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Admin").child(finalImg + "jpeg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrlAadhaar = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(NewAdmin.this, "Something went Wrong Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void casteButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        casteReference = FirebaseDatabase.getInstance().getReference().child("SubCaste");
        casteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, castelist);
        casteSpinner.setAdapter((casteAdapter));

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.casteGeneral:
                if (checked) {
                    casteListener = casteReference.child("General").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                            castelist.clear();
                            for (DataSnapshot casteData : snapshot1.getChildren())
                                castelist.add(casteData.getValue().toString());
                            casteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
                ucaste = "GEN";
                Toast.makeText(this, "General", Toast.LENGTH_LONG).show();
                break;
            case R.id.casteObc:
                if (checked) {
                    casteListener = casteReference.child("Obc").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                            castelist.clear();
                            for (DataSnapshot casteData : snapshot1.getChildren())
                                castelist.add(casteData.getValue().toString());
                            casteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
                ucaste = "OBC";
                Toast.makeText(this, "OBC", Toast.LENGTH_LONG).show();
                break;
            case R.id.casteSc:
                if (checked) {
                    casteListener = casteReference.child("Sc").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                            castelist.clear();
                            for (DataSnapshot castData : snapshot1.getChildren())
                                castelist.add(castData.getValue().toString());
                            casteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
                ucaste = "SC";
                Toast.makeText(this, "SC", Toast.LENGTH_LONG).show();
                break;
            case R.id.casteSt:
                if (checked) {
                    casteListener = casteReference.child("St").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                            castelist.clear();
                            for (DataSnapshot castData : snapshot1.getChildren())
                                castelist.add(castData.getValue().toString());
                            casteAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        }
                    });
                }
                ucaste = "ST";
                Toast.makeText(this, "ST", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner castespinner = (Spinner) parent;

        if (castespinner.getId() == R.id.casteSpinner) {
            String item = parent.getItemAtPosition(position).toString();
            if (item.equals("Select Subcaste")) {
                usubcaste = "";
//            Toast.makeText(parent.getContext(), "Please Select State", Toast.LENGTH_LONG).show();
            } else {
                usubcaste = item;
                Toast.makeText(parent.getContext(), "Selected: " + usubcaste, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(NewAdmin.this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            userImgPrev.setImageBitmap(bitmapImage);
        }
        if (requestCode == REQA && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                bitmapAadhaar = MediaStore.Images.Media.getBitmap(NewAdmin.this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            userImgAadhar.setImageBitmap(bitmapAadhaar);
        }

    }

    public void sexButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.sexMale:
                if (checked)
                    ugender = "Male";
                Toast.makeText(this, "Male", Toast.LENGTH_LONG).show();
                break;
            case R.id.sexFemale:
                if (checked)
                    ugender = "Female";
                Toast.makeText(this, "Female", Toast.LENGTH_LONG).show();

                break;
            case R.id.sexOther:
                if (checked)
                    ugender = "Other";
                Toast.makeText(this, "Other", Toast.LENGTH_LONG).show();
                break;

        }
    }

    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
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
        uage = dob;
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
        uage = age.getText().toString().trim();
    }
}
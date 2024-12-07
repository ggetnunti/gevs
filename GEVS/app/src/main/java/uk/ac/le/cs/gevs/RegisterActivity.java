package uk.ac.le.cs.gevs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.ac.le.cs.gevs.databinding.ActivityMainBinding;
import uk.ac.le.cs.gevs.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    String[] constituencyList = {"Shangri-la-Town", "Northern-Kunlun-Mountain", "Western-Shangri-la", "Naboo-Vallery", "New-Felucia"};
    private EditText editTxtRegisterFullName, editTxtRegisterEmail, editTxtRegisterDob, editTxtRegisterUVC, editTxtRegisterPassword, editTxtRegisterConfirmPassword;
    private ProgressBar progressBar;
    private DatePickerDialog picker;
    private static final String TAG = "RegisterActivity";
    private boolean isValid = false;

    FirebaseFirestore firebaseFirestore;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showCamara();
                } else {
                    
                }
            });

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            setResult(result.getContents());
        }
    });

    private void setResult(String contents) {
        binding.editTxtRegisterUVC.setText(contents);
    }

    private void showCamara() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan QR code");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initViews();

        Spinner dropdown = findViewById(R.id.editTxtRegisterConstituency);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, constituencyList);
        dropdown.setAdapter(adapter);

        Toast.makeText(RegisterActivity.this, "Please enter your details to register", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar);
        editTxtRegisterFullName = findViewById(R.id.editTxtRegisterFullName);
        editTxtRegisterEmail = findViewById(R.id.editTxtRegisterEmail);
        editTxtRegisterDob = findViewById(R.id.editTxtRegisterDob);
        editTxtRegisterUVC = findViewById(R.id.editTxtRegisterUVC);
        editTxtRegisterPassword = findViewById(R.id.editTxtRegisterPassword);
        editTxtRegisterConfirmPassword = findViewById(R.id.editTxtRegisterConfirmPassword);

        editTxtRegisterDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTxtRegisterDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.btn_Register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFullName = editTxtRegisterFullName.getText().toString();
                String textEmail = editTxtRegisterEmail.getText().toString();
                String textDob = editTxtRegisterDob.getText().toString();
                String textConstituency = dropdown.getSelectedItem().toString();
                String textUVC = editTxtRegisterUVC.getText().toString();
                String textPassword = editTxtRegisterPassword.getText().toString();
                String textConfirmPassword = editTxtRegisterConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    editTxtRegisterFullName.setError("Full name is required!");
                    editTxtRegisterFullName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editTxtRegisterEmail.setError("Email is required!");
                    editTxtRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    editTxtRegisterEmail.setError("Valid email is required!");
                    editTxtRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDob)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    editTxtRegisterDob.setError("Date of birth is required!");
                    editTxtRegisterDob.requestFocus();
                } else if (TextUtils.isEmpty(textUVC)) {
                    Toast.makeText(RegisterActivity.this, "Please enter 8-digit Unique Voter Code (UVC)", Toast.LENGTH_LONG).show();
                    editTxtRegisterUVC.setError("UVC is required!");
                    editTxtRegisterUVC.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    editTxtRegisterPassword.setError("Password is required!");
                    editTxtRegisterPassword.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    editTxtRegisterConfirmPassword.setError("Password confirmation is required!");
                    editTxtRegisterConfirmPassword.requestFocus();
                } else if (!textPassword.equals(textConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please re-confirm password", Toast.LENGTH_LONG).show();
                    editTxtRegisterConfirmPassword.setError("Invalid confirm password!");
                    editTxtRegisterConfirmPassword.requestFocus();
                    editTxtRegisterPassword.clearComposingText();
                    editTxtRegisterConfirmPassword.clearComposingText();
                } else if (!TextUtils.isEmpty(textUVC)) {

                    isValid =FirebaseFirestore.getInstance().collection("UVC code").get().equals(textUVC.trim());

                    FirebaseFirestore.getInstance().collection("UVC code")
                            .document(textUVC.trim())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        try {
                                            if (task.getResult().getBoolean("isUsed") == false){
                                                progressBar.setVisibility(View.VISIBLE);
                                                registerUser(textFullName, textEmail, textDob, textConstituency, textUVC, textPassword);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Invalid UVC code or UVC code is Already used", Toast.LENGTH_LONG).show();
                                                editTxtRegisterUVC.setError("Invalid UVC code!");
                                                editTxtRegisterUVC.requestFocus();
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Invalid UVC code or UVC code is Already used", Toast.LENGTH_LONG).show();
                                            editTxtRegisterUVC.setError("Invalid UVC code!");
                                            editTxtRegisterUVC.requestFocus();
                                        }
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Invalid UVC code or UVC code is Already used", Toast.LENGTH_LONG).show();
                                        editTxtRegisterUVC.setError("Invalid UVC code!");
                                        editTxtRegisterUVC.requestFocus();
                                    }
                                }
                            });
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDob, textConstituency, textUVC, textPassword);
                }
            }
        });
    }

    private void initViews() {
        binding.btnRegisterScan.setOnClickListener(v -> {
            checkPermissionAndShowActivity(this);
        });
    }

    private void checkPermissionAndShowActivity(Context context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            showCamara();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(context, "Camera permission required!", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void initBinding() {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void registerUser(String textFullName, String textEmail, String textDob, String textConstituency, String textUVC, String textPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textDob, textConstituency, textUVC);

                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered User");
                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("constituency", textConstituency);
                                        userMap.put("UVC", textUVC);
                                        userMap.put("finish","");

                                        firebaseFirestore.collection("Users")
                                                .document(firebaseUser.getUid()).set(userMap);

                                        Map<String, Object> UVCMap = new HashMap<>();
                                        UVCMap.put("isUsed", true);

                                        firebaseFirestore.collection("UVC code")
                                                .document(textUVC.trim()).update(UVCMap);

                                        Toast.makeText(RegisterActivity.this, "User register successfully", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(RegisterActivity.this, VoterDashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "User register failed", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                editTxtRegisterEmail.setError("Your email is already in use.");
                                editTxtRegisterEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
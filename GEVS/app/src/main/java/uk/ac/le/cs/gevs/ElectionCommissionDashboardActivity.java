package uk.ac.le.cs.gevs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ElectionCommissionDashboardActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnStart, btnStop;
    private FirebaseAuth authProfile;
    private FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_commission_dashboard);

        firebaseFirestore = FirebaseFirestore.getInstance();

        btnStart = findViewById(R.id.btnStartVoting);
        btnStop = findViewById(R.id.btnStopVoting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String uid = firebaseUser.getUid();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> adminMap = new HashMap<>();
                adminMap.put("isVoting",true);

                firebaseFirestore.collection("Admin")
                        .document(uid).update(adminMap);

                btnStop.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.GONE);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> adminMap = new HashMap<>();
                adminMap.put("isVoting",false);

                firebaseFirestore.collection("Admin")
                        .document(uid).update(adminMap);

                btnStop.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void onStart() {
        super.onStart();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnStart = findViewById(R.id.btnStartVoting);
        btnStop = findViewById(R.id.btnStopVoting);

        FirebaseFirestore.getInstance().collection("Admin")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        boolean isVoting = task.getResult().getBoolean("isVoting");

                        if (isVoting){
                            btnStop.setVisibility(View.VISIBLE);
                            btnStart.setVisibility(View.GONE);
                        } else {
                            btnStop.setVisibility(View.GONE);
                            btnStart.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    //action menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(ElectionCommissionDashboardActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ElectionCommissionDashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_result) {
            startActivity(new Intent(ElectionCommissionDashboardActivity.this, ResultActivity.class));
        } else if (id == R.id.menu_overallResult) {
            startActivity(new Intent(ElectionCommissionDashboardActivity.this, OverallResultActivity.class));
        } else {
            Toast.makeText(ElectionCommissionDashboardActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
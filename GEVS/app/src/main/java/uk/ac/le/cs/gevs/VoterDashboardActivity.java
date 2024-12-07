package uk.ac.le.cs.gevs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class VoterDashboardActivity extends AppCompatActivity {

    private TextView txtVoterFullName;
    private ProgressBar progressBar;
    private Button btnVoting;
    private FirebaseAuth authProfile;
    private Boolean isVoting;
    private String finish;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_dashboard);

        btnVoting = findViewById(R.id.btnVoting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtVoterFullName = findViewById(R.id.txtVoterFullName);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();



        btnVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVoting) {
                    if (finish.equals("voted")) {
                        Toast.makeText(VoterDashboardActivity.this,"You already voted!", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(VoterDashboardActivity.this, AllCandidateActivity.class));
                    }
                } else {
                    Toast.makeText(VoterDashboardActivity.this,"Voting is not started!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (firebaseUser == null) {
            Toast.makeText(VoterDashboardActivity.this,"Error: User is not available!", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    protected void onStart() {
        super.onStart();

        FirebaseFirestore.getInstance().collection("Admin")
                .document("tP6zYyYZl5Rso5ZOvMFeCAH4Sqt2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        isVoting = task.getResult().getBoolean("isVoting");
                    }
                });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        FirebaseFirestore.getInstance().collection("Users")
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        finish = task.getResult().getString("finish");
                    }
                });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtVoterFullName.setText("Voter ID: " + firebaseUser.getEmail());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VoterDashboardActivity.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
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
            Toast.makeText(VoterDashboardActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VoterDashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_result) {
            startActivity(new Intent(VoterDashboardActivity.this, ResultActivity.class));
        } else if (id == R.id.menu_overallResult) {
            startActivity(new Intent(VoterDashboardActivity.this, OverallResultActivity.class));
        } else {
            Toast.makeText(VoterDashboardActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
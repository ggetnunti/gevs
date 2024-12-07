package uk.ac.le.cs.gevs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.ac.le.cs.gevs.adapter.CandidateAdapter;
import uk.ac.le.cs.gevs.model.Candidate;

public class AllCandidateActivity extends AppCompatActivity {

    private String constituency;
    private RecyclerView rvCandidate;
    private Button btnStart;
    private List<Candidate> list;
    private CandidateAdapter adapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_candidate);

        rvCandidate = findViewById(R.id.rvCandidate);
        firebaseFirestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new CandidateAdapter(AllCandidateActivity.this, list);
        rvCandidate.setLayoutManager(new LinearLayoutManager(AllCandidateActivity.this));
        rvCandidate.setAdapter(adapter);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        constituency = task.getResult().getString("constituency");
                    }
                });

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            firebaseFirestore.collection("Candidate")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot snapshot : Objects.requireNonNull(task.getResult())){
                                    if (constituency.equals(snapshot.getString("constituency"))){
                                        list.add(new Candidate(
                                                snapshot.getString("name"),
                                                snapshot.getString("party"),
                                                snapshot.getString("constituency"),
                                                snapshot.getString("image"),
                                                snapshot.getId()
                                        ));
                                    }
                                }

                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(AllCandidateActivity.this, "Candidate not found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        String finish = task.getResult().getString("finish");

                        assert finish != null;
                        if (finish.equals("voted")){
                            Toast.makeText(AllCandidateActivity.this, "You already voted!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AllCandidateActivity.this, ResultActivity.class));
                            finish();
                        }
                    }
                });
    }
}
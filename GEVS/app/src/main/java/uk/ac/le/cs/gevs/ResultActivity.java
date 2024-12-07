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
import uk.ac.le.cs.gevs.adapter.CandidateResultAdapter;
import uk.ac.le.cs.gevs.model.Candidate;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView rvResult;
    private List<Candidate> list;
    private CandidateResultAdapter adapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        rvResult = findViewById(R.id.rvResult);
        firebaseFirestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new CandidateResultAdapter(ResultActivity.this, list);
        rvResult.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
        rvResult.setAdapter(adapter);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            firebaseFirestore.collection("Candidate")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot snapshot : Objects.requireNonNull(task.getResult())){
                                    list.add(new Candidate(
                                            snapshot.getString("name"),
                                            snapshot.getString("party"),
                                            snapshot.getString("constituency"),
                                            snapshot.getString("image"),
                                            snapshot.getId()
                                    ));
                                }

                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ResultActivity.this, "Candidate not found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
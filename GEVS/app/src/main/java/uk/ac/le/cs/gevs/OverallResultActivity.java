package uk.ac.le.cs.gevs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import uk.ac.le.cs.gevs.model.Candidate;

public class OverallResultActivity extends AppCompatActivity {

    private TextView redSeat, blueSeat, yellowSeat, independentSeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_result);

        redSeat = findViewById(R.id.redSeat);
        blueSeat = findViewById(R.id.blueSeat);
        yellowSeat = findViewById(R.id.yellowSeat);
        independentSeat = findViewById(R.id.independentSeat);

        FirebaseFirestore.getInstance().collection("party")
                .document("Red Party")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        redSeat.setText(task.getResult().getString("vote"));
                    }
                });

        FirebaseFirestore.getInstance().collection("party")
                .document("Blue Party")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        blueSeat.setText(task.getResult().getString("vote"));
                    }
                });

        FirebaseFirestore.getInstance().collection("party")
                .document("Yellow Party")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        yellowSeat.setText(task.getResult().getString("vote"));
                    }
                });

        FirebaseFirestore.getInstance().collection("party")
                .document("Independent")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        independentSeat.setText(task.getResult().getString("vote"));
                    }
                });
    }
}
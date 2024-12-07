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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VotingActivity extends AppCompatActivity {

    private ImageView image;
    private TextView name, party, constituency;
    private Button btnVote;
    private FirebaseFirestore firebaseFirestore;
    
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        firebaseFirestore = FirebaseFirestore.getInstance();

        image = findViewById(R.id.img);
        name = findViewById(R.id.name);
        party = findViewById(R.id.party);
        constituency = findViewById(R.id.constituency);
        btnVote = findViewById(R.id.btnVote);

        String imageIntent = getIntent().getStringExtra("image");
        String nameIntent = getIntent().getStringExtra("name");
        String partyIntent = getIntent().getStringExtra("party");
        String constituencyIntent = getIntent().getStringExtra("constituency");
        String id = getIntent().getStringExtra("id");

        Glide.with(this).load(imageIntent).into(image);
        name.setText(nameIntent);
        party.setText(partyIntent);
        constituency.setText(constituencyIntent);

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        
        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finish = "voted";
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("finish",finish);

                firebaseFirestore.collection("Users")
                        .document(uid).update(userMap);

                Map<String, Object> candidateMap = new HashMap<>();
                candidateMap.put("deviceIp", getDeviceIP());
                candidateMap.put("timestamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("Candidate/"+id+"/Vote")
                        .document(uid)
                        .set(candidateMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    startActivity(new Intent(VotingActivity.this, ResultActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(VotingActivity.this,"Voted successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                FirebaseFirestore.getInstance().collection("party")
                        .document(partyIntent)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                int count = Integer.valueOf(task.getResult().getString("vote")) + 1;
                                String vote = String.valueOf(count);
                                Map<String, Object> partyMap = new HashMap<>();
                                partyMap.put("vote",vote);

                                firebaseFirestore.collection("party")
                                        .document(partyIntent).update(partyMap);
                            }
                        });
            }
        });
    }

    private String getDeviceIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface inf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = inf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()){
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e){
            Toast.makeText(VotingActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
package uk.ac.le.cs.gevs.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import uk.ac.le.cs.gevs.R;
import uk.ac.le.cs.gevs.VotingActivity;
import uk.ac.le.cs.gevs.model.Candidate;

public class CandidateResultAdapter extends RecyclerView.Adapter<CandidateResultAdapter.ViewHolder> {

    private Context context;
    private List<Candidate> list;
    private FirebaseFirestore firebaseFirestore;

    public CandidateResultAdapter(Context context, List<Candidate> list) {
        this.context = context;
        this.list = list;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_result_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (list.get(position).getName() != ""){
            holder.name.setText("Name : "+list.get(position).getName());
            holder.name.setVisibility(View.VISIBLE);
        } else {
            holder.name.setVisibility(View.GONE);
        }
        holder.party.setText("Party : "+list.get(position).getParty());
        holder.constituency.setText("Constituency : "+list.get(position).getConstituency());

        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        firebaseFirestore.collection("Candidate/"+list.get(position).getId()+"/Vote")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (!documentSnapshots.isEmpty()){
                            int count = documentSnapshots.size();
                            Candidate candidate = list.get(position);
                            candidate.setCount(count);
                            list.set(position, candidate);
                            notifyDataSetChanged();
                        }
                    }
                });
        holder.result.setText("Vote: "+list.get(position).getCount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name, party, constituency, result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            party = itemView.findViewById(R.id.party);
            constituency = itemView.findViewById(R.id.constituency);
            result = itemView.findViewById(R.id.candidate_result);
        }
    }
}

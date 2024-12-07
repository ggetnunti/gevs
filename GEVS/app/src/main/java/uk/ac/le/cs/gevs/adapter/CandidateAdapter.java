package uk.ac.le.cs.gevs.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import uk.ac.le.cs.gevs.R;
import uk.ac.le.cs.gevs.VotingActivity;
import uk.ac.le.cs.gevs.model.Candidate;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {

    private Context context;
    private List<Candidate> list;

    public CandidateAdapter(Context context, List<Candidate> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_layout,parent,false);
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

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VotingActivity.class);
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("party",list.get(position).getParty());
                intent.putExtra("constituency",list.get(position).getConstituency());
                intent.putExtra("image",list.get(position).getImage());
                intent.putExtra("id",list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name, party, constituency;
        private Button btnVote;

        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            party = itemView.findViewById(R.id.party);
            constituency = itemView.findViewById(R.id.constituency);
            //btnVote = itemView.findViewById(R.id.voteCandidate);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}

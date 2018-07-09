package com.example.android.bakeit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeit.Model.BakingInstructions;
import com.example.android.bakeit.R;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private ArrayList<BakingInstructions> mBakingInstructions;
    private LayoutInflater mInflater;
    private Context mContext;
    private sCardClickListener sCardClickListener;

    public StepsAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mBakingInstructions = new ArrayList<>();
    }

    public void setTreatsInstructions(ArrayList<BakingInstructions> bakingInstructions) {
        this.mBakingInstructions.clear();
        this.mBakingInstructions.addAll(bakingInstructions);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }


    @Override
    public StepsAdapter.StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sView = mInflater.inflate(R.layout.steps_cards, parent, false);
        final StepsViewHolder sViewHolder = new StepsViewHolder(sView);

        return sViewHolder;
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsViewHolder holder, int position) {

        BakingInstructions bakingInstructions = mBakingInstructions.get(position);

        int id = bakingInstructions.getId();
        String newId = String.valueOf(id);
        holder.stepName.setText("Step: " + newId);

        holder.stepShortDescription.setText(bakingInstructions.getShortDescription());

    }

    @Override
    public int getItemCount() {
        return (mBakingInstructions == null) ? 0 : mBakingInstructions.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stepName;
        TextView stepShortDescription;

        public StepsViewHolder(View itemView) {
            super(itemView);

            stepName = itemView.findViewById(R.id.step_number);
            stepShortDescription = itemView.findViewById(R.id.step_short_description);
            itemView.setOnClickListener(this);
            }

        @Override
        public void onClick(View v) {
            sCardClickListener.onCardClicked(v, getAdapterPosition());
        }
    }

    /**
     * Interface for when recipe in recycler view is clicked
     */
    public interface sCardClickListener {

        void onCardClicked(View view, int position);
    }

    /**
     * Custom click sCardClickListener
     */
    public void setOnItemClickListener(StepsAdapter.sCardClickListener listener) {
        this.sCardClickListener = listener;
    }
}

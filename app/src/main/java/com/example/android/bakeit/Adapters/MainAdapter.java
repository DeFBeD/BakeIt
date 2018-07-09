package com.example.android.bakeit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakeit.Model.BakedTreats;
import com.example.android.bakeit.R;

import java.util.ArrayList;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.TreatsViewHolder> {
    private ArrayList<BakedTreats> mBakedTreats;
    private LayoutInflater mInflater;
    private Context mContext;
    private cardClickListener mCardClickListener;

    public MainAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mBakedTreats = new ArrayList<>();
    }

    @Override
    public TreatsViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = mInflater.inflate(R.layout.main_recipe_card_chooser, parent, false);
        final TreatsViewHolder viewHolder = new TreatsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TreatsViewHolder holder, int position) {
        BakedTreats bakedTreats = mBakedTreats.get(position);
        holder.treatName.setText(bakedTreats.getName());

    }

    @Override
    public int getItemCount() {
        return (mBakedTreats == null) ? 0 : mBakedTreats.size();
    }

    public void setBakedTreats(ArrayList<BakedTreats> bakedTreats) {
        this.mBakedTreats.clear();
        this.mBakedTreats.addAll(bakedTreats);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    public class TreatsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView treatName;
        ImageView treatImageView;

        public TreatsViewHolder(View itemView) {
            super(itemView);
            treatName = itemView.findViewById(R.id.treat_tv_name);
            treatImageView = itemView.findViewById(R.id.treat_imageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mCardClickListener.onCardClicked(v, getAdapterPosition());
        }
    }

    /**
     * Interface for when recipe in recycler view is clicked
     */
    public interface cardClickListener {

        void onCardClicked(View view, int position);
    }

    /**
     * Custom click mCardClickListener
     */
    public void setOnItemClickListener(cardClickListener listener) {
        this.mCardClickListener = listener;
    }

}


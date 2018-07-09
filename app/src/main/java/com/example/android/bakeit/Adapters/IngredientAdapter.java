package com.example.android.bakeit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeit.Model.BakingIngredients;
import com.example.android.bakeit.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientsViewHolder> {

    private ArrayList<BakingIngredients> mBakedIngredients;
    private LayoutInflater mInflater;
    private Context mContext;

    public IngredientAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mBakedIngredients = new ArrayList<>();
    }

    public void setTreatsIngredients(ArrayList<BakingIngredients> bakingIngredients) {
        this.mBakedIngredients.clear();
        this.mBakedIngredients.addAll(bakingIngredients);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    @Override
    public IngredientAdapter.IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iView = mInflater.inflate(R.layout.ingredients_card, parent, false);
        final IngredientsViewHolder iViewHolder = new IngredientsViewHolder(iView);

        return iViewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.IngredientsViewHolder holder, int position) {

        BakingIngredients bakedTreatsIngredients = mBakedIngredients.get(position);
        holder.name.setText("Ingredient: " + bakedTreatsIngredients.getIngredient());
        float quantity = bakedTreatsIngredients.getQuantity();
        String newMeasure = String.valueOf(quantity);
        holder.quantity.setText("Quantity: " + newMeasure);
        holder.measure.setText("Unit of Measure: " + bakedTreatsIngredients.getMeasure());
    }

    @Override
    public int getItemCount() {
        return (mBakedIngredients == null) ? 0 : mBakedIngredients.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView measure;
        TextView quantity;

        public IngredientsViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ingredient_name);
            measure = itemView.findViewById(R.id.ingredient_unit_of_measure);
            quantity = itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}

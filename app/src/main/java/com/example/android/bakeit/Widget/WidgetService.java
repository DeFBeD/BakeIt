package com.example.android.bakeit.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakeit.Model.BakingIngredients;
import com.example.android.bakeit.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewFactory(this.getApplicationContext());
    }
}

class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<BakingIngredients> mIngredients;

    public WidgetRemoteViewFactory(Context applicationContext) {
        mContext = applicationContext;
        mIngredients = new ArrayList<>();

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");


        Type type = new TypeToken<List<BakingIngredients>>() {
        }.getType();
        mIngredients = gson.fromJson(json, type);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        return (mIngredients == null) ? 0 : mIngredients.size();

    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredients == null || mIngredients.size() == 0) return null;

        BakingIngredients bakedTreatsIngredients = mIngredients.get(position);

        String name = bakedTreatsIngredients.getIngredient();
        String treatsIngredientsMeasure = bakedTreatsIngredients.getMeasure();
        float bakedTreatsIngredientsQuantity = bakedTreatsIngredients.getQuantity();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);

        views.setTextViewText(R.id.titleTextView, name);
        views.setTextViewText(R.id.submeasure, treatsIngredientsMeasure);
        views.setTextViewText(R.id.subQuantity, String.valueOf(bakedTreatsIngredientsQuantity));

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}



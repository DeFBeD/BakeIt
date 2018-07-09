package com.example.android.bakeit.UI;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.bakeit.Adapters.IngredientAdapter;
import com.example.android.bakeit.Adapters.StepsAdapter;
import com.example.android.bakeit.Model.BakingIngredients;
import com.example.android.bakeit.Model.BakingInstructions;
import com.example.android.bakeit.R;
import com.example.android.bakeit.Widget.BakingWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class IngredientFragment extends Fragment {
    //ingredients
    ArrayList<BakingIngredients> mBakingIngredients;
    RecyclerView iRecyclerView;
    Button addWidget;
    IngredientAdapter mIngredientAdapter;
    RecyclerView.LayoutManager iLayoutManager;

    //Steps
    ArrayList<BakingInstructions> mBakingSteps;
    RecyclerView sRecyclerView;
    StepsAdapter mStepsAdapter;
    RecyclerView.LayoutManager sLayoutManager;

    private int stepId;
    private String videoUri;
    private String description;
    private String thumbnails;


    public IngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_ingredients_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        mBakingIngredients = bundle.getParcelableArrayList("baking_Ingredients");

        addWidget = view.findViewById(R.id.add_widget);

        iRecyclerView = view.findViewById(R.id.ingredients_rc);

        iLayoutManager = new LinearLayoutManager(getContext());

        iRecyclerView.setLayoutManager(iLayoutManager);

        mIngredientAdapter = new IngredientAdapter(getContext());

        iRecyclerView.setAdapter(mIngredientAdapter);

        mIngredientAdapter.setTreatsIngredients(mBakingIngredients);

        addWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BakingIngredients> ingredients = new ArrayList<BakingIngredients>();
                ingredients.addAll(mBakingIngredients);

                SharedPreferences appSharedPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

                Gson gson = new Gson();
                String jsonIngredients = gson.toJson(ingredients);

                prefsEditor.putString("MyObject", jsonIngredients);
                prefsEditor.commit();
                Log.d("TAG", "ingredients = " + jsonIngredients);
                Toast.makeText(getContext(), R.string.addedWidgetToast, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), BakingWidgetProvider.class);
                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                int ids[] = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(new ComponentName(getActivity(), BakingWidgetProvider.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                getActivity().sendBroadcast(intent);

            }
        });

        mBakingSteps = bundle.getParcelableArrayList("baking_instructions");

        sRecyclerView = view.findViewById(R.id.steps_rc);

        sLayoutManager = new LinearLayoutManager(getContext());

        sRecyclerView.setLayoutManager(sLayoutManager);

        mStepsAdapter = new StepsAdapter(getContext());

        sRecyclerView.setAdapter(mStepsAdapter);

        mStepsAdapter.setTreatsInstructions(mBakingSteps);

        mStepsAdapter.setOnItemClickListener(new StepsAdapter.sCardClickListener() {
            @Override
            public void onCardClicked(View view, int position) {
                mBakingSteps.get(position);

                boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                if (tabletSize) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("baking_steps", mBakingSteps);
                    bundle.putInt("baking_instructions_video_id", mBakingSteps.get(position).getId());
                    bundle.putString("baking_instructions_video_string", mBakingSteps.get(position).getVideoURL());
                    bundle.putString("baking_instructions_description", mBakingSteps.get(position).getDescription());
                    bundle.putString("baking_thumbnail", mBakingSteps.get(position).getThumbnailURL());

                    VideoFragment videoFragment = new VideoFragment();
                    videoFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.video_two_container, videoFragment).commit();

                } else {

                    Intent intent = new Intent(getContext(), VideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("baking_steps", mBakingSteps);
                    bundle.putInt("baking_instructions_video_id", mBakingSteps.get(position).getId());
                    bundle.putString("baking_instructions_video_string", mBakingSteps.get(position).getVideoURL());
                    bundle.putString("baking_instructions_description", mBakingSteps.get(position).getDescription());
                    bundle.putString("baking_thumbnail", mBakingSteps.get(position).getThumbnailURL());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

            }

        });

    }

}

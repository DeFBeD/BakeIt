package com.example.android.bakeit.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.bakeit.Adapters.MainAdapter;
import com.example.android.bakeit.Data.BakedTreatsService;
import com.example.android.bakeit.Data.RetrieveBakedTreats;
import com.example.android.bakeit.IdilingResource.SimpleIdlingResource;
import com.example.android.bakeit.Model.BakedTreats;
import com.example.android.bakeit.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MainAdapter mMainAdapter;
    FrameLayout homeContainer;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // If there is a network connection, fetch data
        if (isNetworkAvailable(this)) {

            getIdlingResource();

            mRecyclerView = findViewById(R.id.rc);

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            mMainAdapter = new MainAdapter(this);

            mRecyclerView.setAdapter(mMainAdapter);

            BakedTreatsService treatsService = RetrieveBakedTreats.Retrieve();
            Call<ArrayList<BakedTreats>> recipes = treatsService.getBakedTreatsRecipes();

            recipes.enqueue(new Callback<ArrayList<BakedTreats>>() {
                @Override
                public void onResponse(Call<ArrayList<BakedTreats>> call, final Response<ArrayList<BakedTreats>> response) {

                    final ArrayList<BakedTreats> recipes = response.body();

                    mMainAdapter.setBakedTreats(recipes);

                    // Set up recycler item click listener
                    mMainAdapter.setOnItemClickListener(new MainAdapter.cardClickListener() {
                        @Override
                        public void onCardClicked(View view, int position) {

                            recipes.get(position);

                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("baking_Ingredients", recipes.get(position).getBakingIngredients());
                            bundle.putParcelableArrayList("baking_instructions", recipes.get(position).getBakingInstructions());
                            bundle.putInt("ID", recipes.get(position).getId());
                            //bundle.putParcelableArrayList("bakedTreats",recipes);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                    });

                }

                @Override
                public void onFailure(Call<ArrayList<BakedTreats>> call, Throwable t) {

                    Log.v("http fail: ", t.getMessage());

                }
            });

        } else {

            Toast.makeText(getApplicationContext(), R.string.noNetworkToastText, Toast.LENGTH_LONG).show();
        }

        }

    /**
     * Returns true if network is available or about to become available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}








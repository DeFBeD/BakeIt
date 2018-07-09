package com.example.android.bakeit.UI;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.android.bakeit.IdilingResource.SimpleIdlingResource;
import com.example.android.bakeit.R;

public class DetailActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_detail);

        getIdlingResource();


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            Bundle bundle = getIntent().getExtras();

            IngredientFragment ingredientsDetailFragment = new IngredientFragment();
            ingredientsDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_container, ingredientsDetailFragment)
                    .addToBackStack("tag")
                    .commit();

        }

    }

}

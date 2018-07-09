package com.example.android.bakeit.Data;

import com.example.android.bakeit.Model.BakedTreats;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface BakedTreatsService {
        @GET("baking.json")
        Call<ArrayList<BakedTreats>> getBakedTreatsRecipes();
}

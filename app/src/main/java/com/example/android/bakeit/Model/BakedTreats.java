package com.example.android.bakeit.Model;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BakedTreats implements Parcelable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<BakingIngredients> bakingIngredients = null;
    @SerializedName("steps")
    private ArrayList<BakingInstructions> bakingInstructions = null;
    @SerializedName("servings")
    private Integer servings;
    @SerializedName("image")
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<BakingIngredients> getBakingIngredients() {
        return bakingIngredients;
    }

    public void setBakingIngredients(ArrayList<BakingIngredients> bakingIngredients) {
        this.bakingIngredients = bakingIngredients;
    }

    public ArrayList<BakingInstructions> getBakingInstructions() {
        return bakingInstructions;
    }

    public void setBakingInstructions(ArrayList<BakingInstructions> bakingInstructions) {
        this.bakingInstructions = bakingInstructions;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeList(this.bakingIngredients);
        dest.writeList(this.bakingInstructions);
        dest.writeValue(this.servings);
        dest.writeString(this.image);
    }

    public BakedTreats() {
    }

    protected BakedTreats(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.bakingIngredients = new ArrayList<BakingIngredients>();
        in.readList(this.bakingIngredients, BakingIngredients.class.getClassLoader());
        this.bakingInstructions = new ArrayList<BakingInstructions>();
        in.readList(this.bakingInstructions, BakingInstructions.class.getClassLoader());
        this.servings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.readString();
    }

    public static final Parcelable.Creator<BakedTreats> CREATOR = new Parcelable.Creator<BakedTreats>() {
        @Override
        public BakedTreats createFromParcel(Parcel source) {
            return new BakedTreats(source);
        }

        @Override
        public BakedTreats[] newArray(int size) {
            return new BakedTreats[size];
        }

    };


}

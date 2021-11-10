package com.example.autoplaces;

import android.widget.Spinner;

import com.example.autoplaces.pojos.Placess;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlacesAPI {

    @GET("maps/api/place/nearbysearch/json")
    Call<Placess> getPlaces(@Query("location") String location, @Query("radius") String radius, @Query("types") String types, @Query("key")String key);
}

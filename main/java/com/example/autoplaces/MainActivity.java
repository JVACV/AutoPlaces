package com.example.autoplaces;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoplaces.pojos.Placess;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView1,textView2,textView3,textView4;
    String coor1,coor2,coor3;
    ListView lview1;
    String radius = "1000";
    String key = "AIzaSyBtGvgZeAiR4vay_NM7j9LD3vmCHfC3Uvk";
    Spinner spType;
    String[] placeTypeLIst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lview1=findViewById(R.id.lview1);
        editText = findViewById(R.id.edit_text);
        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        textView3 = findViewById(R.id.text_view3);
        textView4 = findViewById(R.id.text_view4);
        spType = findViewById(R.id.sp_type);

        //Initialize array of place type
        placeTypeLIst = new String[]{"atm", "bank", "hospital", "movie_theater", "restaurant"};
        //Initialize array of place name
        String[] placeNameList = {"ATM", "Bank", "Hospital", "Movie Theater", "Restaurant"};

        //Set adapter on spinner
        spType.setAdapter(new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, placeNameList));
        //Initialize places
        Places.initialize(getApplicationContext(), "AIzaSyBtGvgZeAiR4vay_NM7j9LD3vmCHfC3Uvk");

        //set edittext on focusable text
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize place field list
                List<Place.Field> fieldList = Arrays
                        .asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME,Place.Field.TYPES,
                                Place.Field.OPENING_HOURS,Place.Field.PHONE_NUMBER,Place.Field.WEBSITE_URI,
                                Place.Field.RATING,Place.Field.USER_RATINGS_TOTAL);
                //create intent
                Intent intent = new Autocomplete.IntentBuilder
                        (AutocompleteActivityMode.OVERLAY,fieldList)
                        .build(MainActivity.this);
                //start activity result
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            //when success
            //initialize place
            Place place = Autocomplete.getPlaceFromIntent(data);
            coor1 = String.valueOf(place.getLatLng());
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(coor1);
            while(m.find()) {
                coor2 = (m.group(1));
//                Toast.makeText(this, coor2, Toast.LENGTH_LONG).show();
            }
            String types = String.valueOf(place.getTypes());
//            Toast.makeText(this, types, Toast.LENGTH_SHORT).show();
            String[]types1 = types.replaceAll("^\\s*\\[|\\]\\s*$", "").split("\\s*,\\s*");
            coor3=types1[0].toLowerCase();
//            Toast.makeText(this, coor3, Toast.LENGTH_SHORT).show();
            //set address on edittext
            editText.setText(place.getAddress());
            //set locality name
            textView1.setText(String.format("Locality Name : "+place.getName()));
            //set lat & lng
            textView2.setText(String.format("Phone number : "+place.getPhoneNumber()));
            textView3.setText(String.format("Website : "+place.getWebsiteUri()));
            textView4.setText(String.format("Rating : "+place.getRating()));
        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            //when error
            //initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            //display toast
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void getPlaces(View view) {
        int i = spType.getSelectedItemPosition();
        final ProgressDialog ringProgressDialog=ProgressDialog.show(MainActivity.this,"Please Wait","Loading",true);
        ringProgressDialog.setCancelable(true);
        Retrofit r = new Retrofit.Builder().
                baseUrl("https://maps.googleapis.com/").
                addConverterFactory(GsonConverterFactory.create()).build();
        PlacesAPI places = r.create(PlacesAPI.class);
        Call<Placess> call = places.getPlaces(coor2,radius,placeTypeLIst[i],key);
        call.enqueue(new Callback<Placess>() {
            @Override
            public void onResponse(Call<Placess> call, Response<Placess> response) {
                Toast.makeText(MainActivity.this, "Response Successfull", Toast.LENGTH_SHORT).show();
                ringProgressDialog.dismiss();
                Placess places_ = response.body();
                lview1.setAdapter(new MyAdapter(MainActivity.this,places_));
            }

            @Override
            public void onFailure(Call<Placess> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                ringProgressDialog.dismiss();
            }
        });
    }
}
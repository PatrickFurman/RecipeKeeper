package com.recipes.recipekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewRecipeActivity extends AppCompatActivity {
    // Declaring variable names
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private ToggleButton[] tags = new ToggleButton[6];
    private ArrayList<Integer> translatedTags;
    private String cuisine = "N/A";
    private String directions = "N/A";
    private String recipeTitle = "N/A";
    private ArrayList<String> tagTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        // Assigning views to variables/arrays
        Spinner spinner = findViewById(R.id.cuisine);
        translatedTags = new ArrayList<>(6);
        tagTitles = new ArrayList<>(6);
        tags[0] = findViewById(R.id.toggleHot);
        tags[1] = findViewById(R.id.toggleCold);
        tags[2] = findViewById(R.id.toggleSpicy);
        tags[3] = findViewById(R.id.toggleSweet);
        tags[4] = findViewById(R.id.toggleSavory);
        tags[5] = findViewById(R.id.toggleBitter);
        tagTitles.add(0, "Hot");
        tagTitles.add(1, "Cold");
        tagTitles.add(2, "Spicy");
        tagTitles.add(3, "Sweet");
        tagTitles.add(4, "Savory");
        tagTitles.add(5, "Bitter");
        findViewById(R.id.addImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go to Homecooked and figure out how the imageButton worked there to upload photos
            }
        });

        findViewById(R.id.goToIngredientsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < tags.length; i++) {
                    if (tags[i].isChecked()) {
                        translatedTags.add(i, 1);
                    } else {
                        translatedTags.add(i, 0);
                    }
                }
                directions =  ((EditText) findViewById(R.id.directions)).getText().toString();
                recipeTitle = ((EditText) findViewById(R.id.recipeName)).getText().toString();
                Intent i = new Intent(NewRecipeActivity.this, IngredientsActivity.class);
                i.putExtra("Cuisine", cuisine);
                i.putExtra("Directions", directions);
                i.putExtra("Recipe Title", recipeTitle);
                i.putIntegerArrayListExtra("Tags", translatedTags);
                i.putStringArrayListExtra("Tag Titles", tagTitles);
                startActivity(i);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cuisine = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}

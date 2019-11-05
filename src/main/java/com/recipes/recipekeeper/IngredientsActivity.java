package com.recipes.recipekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class IngredientsActivity extends AppCompatActivity {
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private TableLayout table;
    private String cuisine;
    private String directions;
    private String recipeTitle;
    private String photoKey;
    private ArrayList<String> tags = new ArrayList<>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        // Finding views
        table = findViewById(R.id.ingredientsTable);

        // importing info from starting intent
        Bundle b = getIntent().getExtras();
        cuisine = b.get("Cuisine").toString();
        photoKey = b.get("Photo Key").toString();
        directions = b.get("Directions").toString();
        recipeTitle = b.get("Recipe Title").toString();
        ArrayList<Integer> tagValues = (ArrayList<Integer>) b.get("Tags");
        ArrayList<String> tagTitles = (ArrayList<String>) b.get("Tag Titles");
        for (int i = 0; i < tagValues.size(); i++) {
            if (tagValues.get(i) == 1) {
                tags.add(tagTitles.get(i));
            }
        }
        // Setting listeners for buttons
        findViewById(R.id.addIngredientButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                TableRow row = (TableRow) inflater.inflate(R.layout.ingredients_row,null);
                table.addView(row);
            }
        });
        findViewById(R.id.submitRecipeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootRef.child("Recipes").child(recipeTitle).child("Cuisine").setValue(cuisine);
                rootRef.child("Recipes").child(recipeTitle).child("Directions").setValue(directions);
                rootRef.child("Recipes").child(recipeTitle).child("Photo Key").setValue(photoKey);
                for (int i = 0; i < table.getChildCount(); i++) {
                    TableRow row = (TableRow) table.getChildAt(i);
                    for (int j = 0; j < row.getChildCount() - 1; j++) {
                        String name = ((EditText) row.getChildAt(j)).getText().toString();
                        j++;
                        String amount = ((EditText) row.getChildAt(j)).getText().toString();
                        rootRef.child("Recipes").child(recipeTitle).child("Ingredients").child(name).setValue(amount);
                    }
                }
                for (int i = 0; i < tags.size(); i++) {
                    rootRef.child("Recipes").child(recipeTitle).child("Tags").child("" + i).setValue(tags.get(i));
                }
                Toast.makeText(getApplicationContext(), "Recipe Submitted", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(IngredientsActivity.this, HomepageActivity.class);
                startActivity(i);
            }
        });
    }
}

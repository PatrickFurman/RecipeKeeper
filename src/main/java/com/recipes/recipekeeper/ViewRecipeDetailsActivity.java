package com.recipes.recipekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewRecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_details);
        Intent i = getIntent();
        ((TextView) findViewById(R.id.titleText)).setText(i.getStringExtra("Recipe Name"));
        String directions = "Directions: \n" + i.getStringExtra("Directions");
        TextView t = new TextView(getApplicationContext());
        t.setText(directions);
        ((ScrollView) findViewById(R.id.directions)).addView(t);

        //TODO also need to add a query to storageRef to find the associated picture for the recipe
        ArrayList<String> ingredientsList = i.getStringArrayListExtra("Ingredients");
        ArrayList<String> amountsList = i.getStringArrayListExtra("Amounts");
        LinearLayout outerLayout = findViewById(R.id.outerLayout);
        for (int j = 0; j < i.getStringArrayListExtra("Ingredients").size(); j ++) {
            String s = ingredientsList.get(j) + "   " + amountsList.get(j);
            TextView v = new TextView(getApplicationContext());
            v.setText(s);
            v.setTextSize(18);
            v.setTextColor(getColor(R.color.colorBlack));
            outerLayout.addView(v);
        }
    }
}

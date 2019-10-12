package com.recipes.recipekeeper;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        findViewById(R.id.viewRecipesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomepageActivity.this, ViewRecipesActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.newRecipeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomepageActivity.this, NewRecipeActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.goToSettingsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomepageActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.viewGroceryListButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomepageActivity.this, ViewGroceryListActivity.class);
                startActivity(i);
            }
        });

    }
}

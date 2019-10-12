package com.recipes.recipekeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipesActivity extends AppCompatActivity {
    private DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");
    private ListView lv;
    private String sortType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipes);
        sortType = null;
        loadMore(null);
        ((SearchView) findViewById(R.id.searchBar)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                    loadMore(null);
                loadMore(newText);
                return false;
            }
        });
        Spinner filters = findViewById(R.id.filters);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filters.setAdapter(adapter);
        filters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO  Add options to choose from and then a way to functionally search based on those options
                String temp = parent.getItemAtPosition(position).toString();
                if (temp.equals("Hot Foods")) {
                    sortType = "Hot";
                } else if (temp.equals("Cold Foods")) {
                    sortType = "Cold";
                } else if (temp.equals("Sweet Foods")) {
                    sortType = "Sweet";
                } else if (temp.equals("Savory Foods")) {
                    sortType = "Savory";
                } else if (temp.equals("Spicy Foods")) {
                    sortType = "Spicy";
                } else {
                    sortType = "Bitter";
                }
                loadMore(null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadMore (String newText) {
        lv = findViewById(R.id.list);
        Query query;
        if (newText != null)
            query = recipeRef.orderByKey().startAt(newText);
        else if (sortType == null)
            query = recipeRef.orderByKey();
        else
            query = recipeRef.orderByChild(sortType);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> viewList = new ArrayList<>();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.list_row, viewList);
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    viewList.add(child.getKey());
                }
                lv.setAdapter(arrayAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        Query q = recipeRef.child(lv.getItemAtPosition(position).toString());
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Intent i = new Intent(getApplicationContext(), ViewRecipeDetailsActivity.class);
                                i.putExtra("Recipe Name", lv.getItemAtPosition(position).toString()); // need to test
                                i.putExtra("Directions", dataSnapshot.child("Directions").getValue(String.class));
                                ArrayList<String> ingredientsList = new ArrayList<>();
                                ArrayList<String> amountsList = new ArrayList<>();
                                int j = 0;
                                for (DataSnapshot child : dataSnapshot.child("Ingredients").getChildren()) {
                                    amountsList.add(j, child.getValue(String.class));
                                    ingredientsList.add(j, child.getKey());
                                }
                                i.putStringArrayListExtra("Ingredients", ingredientsList);
                                i.putStringArrayListExtra("Amounts", amountsList);
                                startActivity(i);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package com.recipes.recipekeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;

public class NewRecipeActivity extends AppCompatActivity {
    // Declaring variable names
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private ToggleButton[] tags = new ToggleButton[6];
    private ArrayList<Integer> translatedTags;
    private String cuisine = "N/A";
    private Uri imageUri = null;
    private String photoKey = "";
    private String directions = "N/A";
    private String recipeTitle = "N/A";
    private ArrayList<String> tagTitles;
    private ImageButton selectImageButton;
    private static final int Gallery_Pick = 1;

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
        selectImageButton = findViewById(R.id.addImageButton);

        findViewById(R.id.goToIngredientsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    Timestamp ts = new Timestamp((int) System.currentTimeMillis());
                    photoKey = imageUri.getLastPathSegment() + ts.toString() + ".jpg";
                    //TODO need to make sure the file goes into the photos folder and that the photo key is being properly generated (UPDATED, NEEDS VALIDATION)
                    storageRef.child("Photos").child(photoKey).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewRecipeActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(NewRecipeActivity.this, "Error occurred, check logs", Toast.LENGTH_SHORT).show();
                                Log.e("Photo upload error", message);
                            }
                        }
                    });
                    for (int i = 0; i < tags.length; i++) {
                        if (tags[i].isChecked()) {
                            translatedTags.add(i, 1);
                        } else {
                            translatedTags.add(i, 0);
                        }
                    }
                    directions = ((EditText) findViewById(R.id.directions)).getText().toString();
                    recipeTitle = ((EditText) findViewById(R.id.recipeName)).getText().toString();
                    Intent i = new Intent(NewRecipeActivity.this, IngredientsActivity.class);
                    i.putExtra("Cuisine", cuisine);
                    i.putExtra("Directions", directions);
                    i.putExtra("Recipe Title", recipeTitle);
                    i.putExtra("Photo Key", photoKey);
                    i.putIntegerArrayListExtra("Tags", translatedTags);
                    i.putStringArrayListExtra("Tag Titles", tagTitles);

                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Please pick a photo", Toast.LENGTH_LONG).show();
                }
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
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            selectImageButton.setImageURI(imageUri);
        }
    }
}

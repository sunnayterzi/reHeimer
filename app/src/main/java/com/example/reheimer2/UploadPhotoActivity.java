package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPhotoActivity extends AppCompatActivity {

    private Button uploadbtn;
    private ImageView photo;
    private ProgressBar progressBar;
    private EditText description;
    private FirebaseDatabase database;
    private DatabaseReference mReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        uploadbtn=(Button) findViewById(R.id.button_UploadImage);
        photo=(ImageView) findViewById(R.id.imageAddPhoto);
        progressBar=(ProgressBar) findViewById(R.id.progressBar_uploadImage);
        progressBar.setVisibility(View.INVISIBLE);
        description=(EditText) findViewById(R.id.editText_descriptionPhoto);
        database = FirebaseDatabase.getInstance();
        mReference = database.getReference("Photos");
        storageReference= FirebaseStorage.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // get a photo from gallery
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });


        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null){

                    uploadFirebase(imageUri);

                }
                else {
                    Toast.makeText(UploadPhotoActivity.this, "Please Select an Photo", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri=data.getData();
            photo.setImageURI(imageUri);
        }
    }

    // upload photos to database
    private void uploadFirebase(Uri uri){
        Intent AlbumIntent = new Intent(this, PhotosActivity.class);

        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri) );
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Photo photos = new Photo(uri.toString(), description.getText().toString());
                        String modelId = mReference.push().getKey();
                        mReference.child(mAuth.getCurrentUser().getUid()).child(modelId).setValue(photos);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(UploadPhotoActivity.this, "Photo Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                        photo.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                        startActivity(AlbumIntent);

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadPhotoActivity.this, "Uploading Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private String getFileExtension( Uri mUri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(mUri));
    }
}
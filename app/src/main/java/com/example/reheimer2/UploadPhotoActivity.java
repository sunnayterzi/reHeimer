package com.example.reheimer2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

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
    String currentDesc, currentPhoto;

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
        Bundle intent = getIntent().getExtras();
        if(intent!=null){
            currentDesc = intent.getString("currentDesc");
            currentPhoto = intent.getString("currentPhoto");
            description.setText(currentDesc);
            Picasso.get().load(currentPhoto).into(photo);
            uploadbtn.setText("Update");
        }

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
                if (uploadbtn.getText().equals("Upload")){
                    if (imageUri != null) {

                        uploadFirebase(imageUri);

                    } else {
                        Toast.makeText(UploadPhotoActivity.this, "Please Select an Photo", Toast.LENGTH_LONG).show();
                    }
                }

                else{
                    beginUpdate();
                }
            }
        });


    }

    private void beginUpdate() {
        progressBar.setVisibility(View.VISIBLE);

        deletePreviousImage();
        
    }

    private void deletePreviousImage() {
        StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentPhoto);
        mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
               // Toast.makeText(UploadPhotoActivity.this, "Previous image deleted", Toast.LENGTH_SHORT).show();
                uploadNewImage();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPhotoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void uploadNewImage() {
        String ImageName = System.currentTimeMillis() +".png";
        StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + ImageName);
        Bitmap bitmap = ((BitmapDrawable)photo.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte [] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = storageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(UploadPhotoActivity.this, "New Image Uploaded", Toast.LENGTH_SHORT).show();
                Task <Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                updateDatabase(downloadUri.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPhotoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateDatabase(String s) {
        String title = description.getText().toString();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mFirebaseDatabase.getReference("Photos").child(mAuth.getCurrentUser().getUid());
        Query query = mRef.orderByChild("imageDescription").equalTo(currentDesc);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ds.getRef().child("imageDescription").setValue(title);
                    ds.getRef().child("imageUrl").setValue(s);
                }
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadPhotoActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadPhotoActivity.this,PhotosActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                       // Toast.makeText(UploadPhotoActivity.this, "Photo Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
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
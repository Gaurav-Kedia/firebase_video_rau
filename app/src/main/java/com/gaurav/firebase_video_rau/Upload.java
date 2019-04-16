package com.gaurav.firebase_video_rau;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload extends AppCompatActivity {

    private EditText video_name;
    private Uri videouri;
    private static final int REQUEST_CODE = 101;
    private FirebaseDatabase database;
    private StorageReference storageRef;
    private StorageReference videoref;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        FirebaseApp.initializeApp(this);
        video_name = (EditText) findViewById(R.id.video_name);
        storageRef = FirebaseStorage.getInstance().getReference();
    }
    public void upload(View view) {
        videoref = storageRef.child(String.valueOf(video_name.getText()) + ".mp4");
        if (videouri != null) {
            UploadTask uploadTask = videoref.putFile(videouri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload.this,
                            "Upload failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Upload.this, "Upload complete",
                                    Toast.LENGTH_LONG).show();

                           // String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            /*String child = String.valueOf(video_name.getText()) + ".mp4";
                            String url = String.valueOf(storageRef.child(child).getDownloadUrl());*/
                            storageRef.child(String.valueOf(video_name.getText()) + ".mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //url = String.valueOf(uri);
                                    DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
                                    dataref.child(String.valueOf(video_name.getText()) + "").setValue(uri.toString());
                                }
                            });
                            /*DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
                            dataref.child(String.valueOf(video_name.getText()) + "").setValue(url);*/
                        }
                    }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            updateProgress(taskSnapshot);

                        }
                    });
        } else {
            Toast.makeText(Upload.this, "Nothing to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void upload_to_database(Uri uri) {

    }

    public void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {

        @SuppressWarnings("VisibleForTests") long fileSize =
                taskSnapshot.getTotalByteCount();

        @SuppressWarnings("VisibleForTests")
        long uploadBytes = taskSnapshot.getBytesTransferred();

        long progress = (100 * uploadBytes) / fileSize;

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pbar);
        progressBar.setProgress((int) progress);
    }

    public void record(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        videouri = data.getData();
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +
                        videouri, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}

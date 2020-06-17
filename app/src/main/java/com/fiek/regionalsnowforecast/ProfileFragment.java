package com.fiek.regionalsnowforecast;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private static final int CHOOSE_IMAGE = 101;
    private CircleImageView profilePicture;
    private Uri uriProfilePicture;
    private ProgressBar progressBar;
    private Button btnSave;
    private String profilePicURL;
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private TextView tvProfileRegion;
    private FirebaseUser user;
    private FirebaseDatabase userDatabase;
    private DatabaseReference dbReference;
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("imageuploads");
        userDatabase = FirebaseDatabase.getInstance();
        dbReference = userDatabase.getReference("users");
        user = mAuth.getCurrentUser();

        Intent intent = new Intent();
        email = intent.getStringExtra("email");

    }

    @Override
    public void onStart() {
        super.onStart();
        getUserData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePicture = view.findViewById(R.id.myprofilepicture);
        progressBar = view.findViewById(R.id.progressbar);
        btnSave = view.findViewById(R.id.btnSaveProfile);
        tvProfileName = view.findViewById(R.id.myName);
        tvProfileEmail = view.findViewById(R.id.myEmail);
        tvProfileRegion = view.findViewById(R.id.myRegion);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }


        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        if (user != null) {
            if (user.getPhotoUrl() != null) {

                Glide.with(this).load(user.getPhotoUrl()).into(profilePicture);

            }
        } else {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfilePicture = data.getData();
            try {
                Picasso.get().load(uriProfilePicture).into(profilePicture);
                uploadeImagetoFirebaseStorage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getUserData(){
        dbReference.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String name = datas.child("name").getValue().toString();
                    String email = datas.child("email").getValue().toString();
                    String region = datas.child("region").getValue().toString();
                    tvProfileName.setText(name);
                    tvProfileEmail.setText(email);
                    tvProfileRegion.setText(region);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose profile picture"), CHOOSE_IMAGE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadeImagetoFirebaseStorage() {
        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uriProfilePicture));

        if (uriProfilePicture != null) {
            progressBar.setVisibility(View.VISIBLE);
            fileReference.putFile(uriProfilePicture).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 5000);

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profilePicURL = uri.toString();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }

    }

    private void saveUserInfo() {

        if (user != null && profilePicURL != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName("Profile Pic")
                    .setPhotoUri(Uri.parse(profilePicURL))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

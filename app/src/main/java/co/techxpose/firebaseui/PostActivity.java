package co.techxpose.firebaseui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PostActivity extends AppCompatActivity {

    ImageButton mselectimage;
    Button submitpost;
    EditText mtitle,mdesc;
    private FirebaseAuth mAuth;
    private StorageReference mstroragerefrence;
    private DatabaseReference mdatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mdatabaseuser;
    Uri imageuri =null;
    private static final int GALLERY_REQUEST =2;
    ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mselectimage= (ImageButton)findViewById(R.id.msellectbutton);
        submitpost=(Button)findViewById(R.id.submitpost);
        mtitle= (EditText) findViewById(R.id.edittitle);
        mdesc=(EditText)findViewById(R.id.editdescription);
        mprogress=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        mstroragerefrence= FirebaseStorage.getInstance().getReference();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Blog");

        mdatabaseuser= FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser.getUid());

       mselectimage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

        submitpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startposting();
            }
        });
    }
    private void startposting()
    {

        final String title_val = mtitle.getText().toString().trim();
        final String desc_val= mdesc.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && imageuri!=null)
        {
        mprogress.setMessage("uploading");
            mprogress.show();
        StorageReference file_path= mstroragerefrence.child("Blog_image").child(imageuri.getLastPathSegment());

        file_path.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        final Uri downloadurl=taskSnapshot.getDownloadUrl();
        final    DatabaseReference newpost=mdatabase.push();



        mdatabaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                newpost.child("Title").setValue(title_val);
                newpost.child("Desc").setValue(desc_val);
                newpost.child("image").setValue(downloadurl.toString());
                newpost.child("userID").setValue(mCurrentUser.getUid());
                final String pushid=newpost.getRef().toString();

                newpost.child("Username").setValue(dataSnapshot.child("Museum").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {

                            Intent intent = new Intent(PostActivity.this,QrgeneratorActivity.class);
                            intent.putExtra("tivalue", title_val);
                            intent.putExtra("pushid", pushid);

                            startActivity(intent);
                            Toast.makeText(PostActivity.this, "Post completed", Toast.LENGTH_LONG).show();

                        }
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PostActivity.this, "Connection lost", Toast.LENGTH_SHORT).show();

            }
        });

        mprogress.dismiss();


    }
});
         }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode==GALLERY_REQUEST &&   resultCode==RESULT_OK) {

           imageuri = data.getData();
           CropImage.activity(imageuri)
                   .setGuidelines(CropImageView.Guidelines.ON)
                   .setAspectRatio(4,3)
                   .start(this);

       }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();

                mselectimage.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}

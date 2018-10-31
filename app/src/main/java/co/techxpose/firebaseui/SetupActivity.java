package co.techxpose.firebaseui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;





public class SetupActivity extends AppCompatActivity {

     private ImageButton mprofileimage;
    private EditText meuseumfield;
    private EditText managernamefield;
    private EditText placefield;
    private EditText phonefield;
    private EditText countryfield;
    private EditText pincodefield;
    private FirebaseAuth mAuth;
    Uri imageuri=null;
    private static  final  int GALLERY_REQUEST =   1;
    private DatabaseReference mDatabaseUser;
    private StorageReference  mStorageimage;
    ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_setup);
        mprofileimage=(ImageButton)findViewById(R.id.profileImage);
        meuseumfield=(EditText)findViewById(R.id.Museum_name);
        managernamefield=(EditText)findViewById(R.id.Museum_name);
        placefield=(EditText)findViewById(R.id.Place);
        phonefield=(EditText)findViewById(R.id.Phone);
        countryfield=(EditText)findViewById(R.id.Country);
        pincodefield=(EditText)findViewById(R.id.Pincode);

        mAuth=FirebaseAuth.getInstance();
        mStorageimage= FirebaseStorage.getInstance().getReference().child("Profile_images");
        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("users");
        mprogress=new ProgressDialog(this);



        mprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);

            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {

           Uri  imageUri = data.getData();



            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
               imageuri = result.getUri();

                mprofileimage.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setup_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.action_submitpost)
        {
            startSetupAccount();


        }


        return super.onOptionsItemSelected(item);
    }

    private void startSetupAccount() {

        final String user_id = mAuth.getCurrentUser().getUid();
        final String museum = meuseumfield.getText().toString().trim();
        final String manager = managernamefield.getText().toString().trim();
        final String place = placefield.getText().toString().trim();
        final String country = countryfield.getText().toString().trim();
        final String phone = phonefield.getText().toString().trim();
        final String pincode = pincodefield.getText().toString().trim();


        if (!TextUtils.isEmpty(museum) && imageuri != null && !TextUtils.isEmpty(manager) && !TextUtils.isEmpty(place) && !TextUtils.isEmpty(country) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pincode)) {
            mprogress.setMessage("finishing Setup.......");
            mprogress.show();
            StorageReference filepath = mStorageimage.child(imageuri.getLastPathSegment());
            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloaduri = taskSnapshot.getDownloadUrl().toString();
                    mDatabaseUser.child(user_id).child("Museum").setValue(museum);
                    mDatabaseUser.child(user_id).child("Manager").setValue(manager);
                    mDatabaseUser.child(user_id).child("Place").setValue(place);
                    mDatabaseUser.child(user_id).child("Country").setValue(country);
                    mDatabaseUser.child(user_id).child("Phone").setValue(phone);
                    mDatabaseUser.child(user_id).child("Pincode").setValue(pincode);
                    mDatabaseUser.child(user_id).child("image").setValue(downloaduri);

                    mprogress.dismiss();
                    Intent mainintent = new Intent(SetupActivity.this, MainActivity.class);
                    mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainintent);

                }
            });


        } else {

            Toast.makeText(this, "All fields are Mandatory", Toast.LENGTH_SHORT).show();
        }

    }
}

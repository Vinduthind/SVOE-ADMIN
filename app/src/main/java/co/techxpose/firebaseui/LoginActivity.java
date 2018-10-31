package co.techxpose.firebaseui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;

public class LoginActivity extends AppCompatActivity {
private EditText memailtextfield;
    private EditText mpasswordtextfield;
    private Button mloginbutton;
    private FirebaseAuth mAuth;
    private DatabaseReference mrefrence;
    ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        mrefrence= FirebaseDatabase.getInstance().getReference().child("users");
        mrefrence.keepSynced(true);
        mprogress=new ProgressDialog(this);
        mprogress.setCanceledOnTouchOutside(false);
        memailtextfield=(EditText)findViewById(R.id.editemail);
        mpasswordtextfield=(EditText)findViewById(R.id.editpassword);
        mloginbutton=(Button)findViewById(R.id.btnlogin);





        mloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklogin();
            }
        });

    }

    private void checklogin() {
        mprogress.setMessage("Login.....");
        mprogress.show();
        String emailtext= memailtextfield.getText().toString().trim();
        String passwordtext= mpasswordtextfield.getText().toString().trim();
        if(!TextUtils.isEmpty(emailtext)&&!TextUtils.isEmpty(passwordtext))
        {

            mAuth.signInWithEmailAndPassword(emailtext,passwordtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {

                        checkUserExist();
                        mprogress.dismiss();

                    }
                    else
                    {
                        mprogress.dismiss();
                        Toast.makeText(LoginActivity.this, "login Error", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        else{

            Toast.makeText(this, "Enter Email and password", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserExist() {
    final String user_id=mAuth.getCurrentUser().getUid();
mrefrence.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if(dataSnapshot.hasChild(user_id))
        {


            Intent mainintent = new Intent(LoginActivity.this,MainActivity.class);
            mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainintent);

        }
        else
        {
            Intent setupintent = new Intent(LoginActivity.this,SetupActivity.class);
            setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(setupintent);

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(LoginActivity.this, "Connection lost", Toast.LENGTH_SHORT).show();


    }
});
    }



}

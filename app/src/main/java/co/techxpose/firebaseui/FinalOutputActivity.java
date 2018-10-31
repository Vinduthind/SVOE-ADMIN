package co.techxpose.firebaseui;




import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import java.util.Map;


public class FinalOutputActivity extends AppCompatActivity {


    private DatabaseReference mdatabase;
    private TextView titletxtview ,desctxtview;
    private Firebase mref;
    private ImageButton imageButton;
    String   adminpostkey=null;
    String   usergetdata=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_output);
        titletxtview=(TextView)findViewById(R.id.output_title);
        desctxtview=(TextView)findViewById(R.id.output_desc);
        imageButton=(ImageButton)findViewById(R.id.msellectbutton);

        adminpostkey = getIntent().getExtras().getString("postadminkey");

        usergetdata = getIntent().getExtras().getString("h_id");

        if (usergetdata==null) {
            mref = new Firebase("https://v-o-e-27f80.firebaseio.com/Blog/" + adminpostkey);

        }
        else {

            mref=new Firebase(usergetdata);
        }


        mref.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                Map<String, String> map =dataSnapshot.getValue(Map.class);
                String title=map.get("Title");
                String desc=map.get("Desc");
                String imageurl=map.get("image");
                Log.v("E_Value","Title : "+ title);
                Log.v("E_Value","Desc: "+ desc);
                Log.v("E_Value","Image : "+ imageurl);
                Picasso.with(FinalOutputActivity.this).load(imageurl).fit().centerCrop().into(imageButton);
                titletxtview.setText(title);
                desctxtview.setText(desc);



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





    }



}

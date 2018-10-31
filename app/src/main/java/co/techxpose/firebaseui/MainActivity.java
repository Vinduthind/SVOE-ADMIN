package co.techxpose.firebaseui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthsateListner;
    private RecyclerView mbloglist;
    private DatabaseReference mdatabase, mDatabaseusers;
    private DatabaseReference mdatabasecurrentuser;
    private Query mQuerycurrentuser;
    private String currentUserid;
    ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mprogress=new ProgressDialog(this);

        mAuth= FirebaseAuth.getInstance();
        mAuthsateListner =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


          if (firebaseAuth.getCurrentUser()==null)
          {


              Intent loginintent = new Intent(MainActivity.this,LoginActivity.class);
              loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(loginintent);

            finish();


          }
                else
          {


              checkUserExist();

          }

            }
        };
        if (mAuth.getCurrentUser()!=null)
        {
            currentUserid=mAuth.getCurrentUser().getUid();
        }



        mbloglist=(RecyclerView)findViewById(R.id.recycleview);
        mbloglist.setHasFixedSize(true);
        mbloglist.setLayoutManager(new LinearLayoutManager(this));


        mdatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseusers=FirebaseDatabase.getInstance().getReference().child("users");
        mdatabase.keepSynced(true);
        mDatabaseusers.keepSynced(true);


        mdatabasecurrentuser= FirebaseDatabase.getInstance().getReference().child("Blog");
        mQuerycurrentuser=mdatabasecurrentuser.orderByChild("userID").equalTo(currentUserid);



    }



    @Override
    protected void onStart() {
        super.onStart();




        mAuth.addAuthStateListener(mAuthsateListner);

        FirebaseRecyclerAdapter<Blog, BlogViewHolder>  firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,R.layout.blog_view,
                BlogViewHolder.class,mQuerycurrentuser


        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
            final String Post_key= getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setdesc(model.getDesc());
                viewHolder.setimage(getApplicationContext(), model.getImage());

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      Intent finaloutputintent = new Intent(MainActivity.this,FinalOutputActivity.class);
                        finaloutputintent.putExtra("postadminkey",Post_key);

                        startActivity(finaloutputintent);

                    }
                });

            }
        };

mbloglist.setAdapter(firebaseRecyclerAdapter);
    }



    public  static  class BlogViewHolder extends RecyclerView.ViewHolder {
        View mview;

        public BlogViewHolder(View itemView) {
            super(itemView);

        mview=itemView;



        }


        public  void  setTitle(String title)
        {
            TextView post_title = (TextView)mview.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public  void setdesc(String desc)
        {
            TextView post_desc =(TextView)mview.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }
        public void setimage(Context ctx, String image)

        {
            ImageView post_image=(ImageView)mview.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }
    }



    private void checkUserExist() {
        final String user_id=mAuth.getCurrentUser().getUid();
        mDatabaseusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(user_id))
                {


                    Intent setupintent = new Intent(MainActivity.this,SetupActivity.class);
                    setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupintent);
                }
                else
                {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.action_add)
        {
            startActivity(new Intent(MainActivity.this,PostActivity.class));

        }

        if (item.getItemId()==R.id.action_refresh)
        {
            Intent intent = getIntent();
            finish();
            startActivity(intent);

        }
        if (item.getItemId()==R.id.action_edit_profile) {


            Intent setupintent = new Intent(MainActivity.this, SetupActivity.class);
            setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(setupintent);

        }

        if (item.getItemId()==R.id.action_finaloutput) {


            Intent finalintent = new Intent(MainActivity.this, FinalOutputActivity.class);
            finalintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(finalintent);
            finish();

        }
        if (item.getItemId()==R.id.action_logout)
        {
            mAuth.signOut();

        }
        return super.onOptionsItemSelected(item);
    }
}

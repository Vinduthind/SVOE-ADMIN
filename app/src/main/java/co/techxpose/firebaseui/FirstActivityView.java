package co.techxpose.firebaseui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.Locale;

public class FirstActivityView extends AppCompatActivity {

    TextView scantext;
    String correcttext;
    int lol;
    Button buttonspeak,buttonstop;
    TextToSpeech t1;
    String textfromscanner;
    int textlenght;
    int totalspeaklength;
    ViewFlipper viewflipper;
    ProgressBar progressBar;
    private int progressBarStatus = 0;
    float speed =1;
    int getinteger;
    private long fileSize;
    private Handler progressBarHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_view);

        buttonstop=(Button)findViewById(R.id.buttonpause);
        scantext = (TextView) findViewById(R.id.scantxt);
        buttonspeak=(Button) findViewById(R.id.buttonplay);
        buttonspeak.setVisibility(View.GONE);
        buttonstop.setVisibility(View.GONE);
        // fab working here
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FirstActivityView.this,CameraScanner.class);
                startActivity(myIntent);
                finish();

            }
        });


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setSpeechRate(speed);
                    t1.setLanguage(new Locale ("hi_IN","ENGLISH"));


                }
            }
        });

         Intent in = getIntent();
         textfromscanner = in.getStringExtra("h_id");
        //chech wheather  speakind engine is busy on free


        if (textfromscanner!=null)
        {

            buttonspeak.setVisibility(View.VISIBLE);
        }
        buttonspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                t1.speak(textfromscanner, TextToSpeech.QUEUE_FLUSH, null);

                do {
                    buttonspeak.setVisibility(View.GONE);

                    buttonstop.setVisibility(View.VISIBLE);
                }
                while(t1.isSpeaking());
                if (!t1.isSpeaking())
                {
                    buttonspeak.setVisibility(View.VISIBLE);

                    buttonstop.setVisibility(View.GONE);
                }

            }
        });
        buttonstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPause();

            }
        });


    }




    public void onPause(){
        if(t1 !=null){
            t1.stop();
            buttonstop.setVisibility(View.GONE);
            buttonspeak.setVisibility(View.VISIBLE);
        }

        super.onPause();
    }

    @Override
    protected void onResume() {

        scantext.setText(textfromscanner);
        if(textfromscanner !=null)
        {
            viewflipper.setVisibility(View.GONE);
            buttonspeak.setVisibility(View.VISIBLE);
            buttonstop.setVisibility(View.GONE);
        }
        else
        {
            buttonspeak.setVisibility(View.GONE);
            buttonstop.setVisibility(View.GONE);
        }

        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_firstactivityview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Admin) {

            Intent myIntent = new Intent(FirstActivityView.this, MainActivity.class);
            startActivity(myIntent);

            return true;

        }

        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        else if (id== R.id.action_scan){

            Intent myIntent = new Intent(FirstActivityView.this, CameraScanner.class);
            startActivity(myIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }







}
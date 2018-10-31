package co.techxpose.firebaseui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.utilities.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.OutputStream;

public class QrgeneratorActivity extends AppCompatActivity {
    TextView titletxt;
    ImageView mImage;
    String adminpostkey=null;
    String adminfullurl;
    OutputStream output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);


        String intentvalue = getIntent().getExtras().getString("tivalue");
        String nmurl = getIntent().getExtras().getString("pushid");
        adminpostkey = getIntent().getExtras().getString("postadminkey");
        adminfullurl="https://v-o-e-27f80.firebaseio.com/Blog/"+adminpostkey;

        titletxt = (TextView) findViewById(R.id.titleqr);
        mImage = (ImageView) findViewById(R.id.imageqr);

        if (adminpostkey != null) {

            titletxt.setText(adminfullurl);
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(adminfullurl, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                mImage.setImageBitmap(bitmap);


            } catch (WriterException e) {
                e.printStackTrace();
            }


        } else {
            titletxt.setText(intentvalue);
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(nmurl, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                mImage.setImageBitmap(bitmap);

              } catch (WriterException e) {
                e.printStackTrace();
            }

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qrgenerator_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.action_save)
        {
            File filepath = Environment.getExternalStorageDirectory();

            // Create a new folder in SD Card
            File dir = new File(filepath.getAbsolutePath()
                    + "/V_O_E/");
            dir.mkdirs();



        }




        return super.onOptionsItemSelected(item);
    }
}

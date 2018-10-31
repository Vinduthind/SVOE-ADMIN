package co.techxpose.firebaseui;

/**
 * Created by vindu-thind on 24/3/17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vindu-thind on 4/3/17.
 */

public class MyDataBaseforHistory extends SQLiteOpenHelper{


    public  static  final String scannervalue = "HISTORY";
    public  static  final String Table_name = "history";






    public MyDataBaseforHistory(Context context) {
        super(context, "history.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("Create TABLE "+Table_name+"(SNO INTEGER PRIMARY KEY AUTOINCREMENT ,HISTORY TEXT)");

    }




    public  boolean saveData( String value) {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(scannervalue,value);

        long result=    db.insert(Table_name,null,contentValues);
        if (result==-1)

            return false;

        else
            return true;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIT "+Table_name);
    }
    public Cursor getalldata()
    {

        SQLiteDatabase db=getWritableDatabase();

        Cursor res=db.rawQuery("SELECT * FROM "+Table_name,null);
        return res;

    }

}







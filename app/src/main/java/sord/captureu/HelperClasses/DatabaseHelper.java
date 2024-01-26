package sord.captureu.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public DatabaseHelper(Context context) {

        super(context, "databose", null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE blackBox ( passNo INTEGER, rndStr TEXT, encRndStr TEXT, encMastr TEXT)");
        db.execSQL("CREATE TABLE datatab (id INTEGER PRIMARY KEY AUTOINCREMENT, cap TEXT, data TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS blackBox");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS datatab");
        onCreate(sqLiteDatabase);
    }

    public void insertKey1(String rndText, String encryptedRndText, String encryptedMaster){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("passNo","1");
        contentValues.put("rndStr",rndText);
        contentValues.put("encRndStr",encryptedRndText);
        contentValues.put("encMastr",encryptedMaster);
        db.insert("blackBox",null,contentValues);
    }
    public void insertKey2(String rndText, String encryptedRndText, String encryptedMaster){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("passNo","2");
        contentValues.put("rndStr",rndText);
        contentValues.put("encRndStr",encryptedRndText);
        contentValues.put("encMastr",encryptedMaster);
        db.insert("blackBox",null,contentValues);
    }
    public void insertKey3(String rndText, String encryptedRndText, String encryptedMaster){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("passNo","3");
        contentValues.put("rndStr",rndText);
        contentValues.put("encRndStr",encryptedRndText);
        contentValues.put("encMastr",encryptedMaster);
        db.insert("blackBox",null,contentValues);
    }

    public void insertKey(int passno, String rndStr, String encryStr, String encryMaster){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM blackBox where passNo="+passno+"");
        ContentValues contentValues=new ContentValues();
        contentValues.put("passNo",passno);
        contentValues.put("rndStr",rndStr);
        contentValues.put("encRndStr",encryStr);
        contentValues.put("encMastr",encryMaster);
        db.insert("blackBox",null,contentValues);
    }
    public void deleteKey(int passno){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM blackBox where passNo="+passno+"");
    }

    /*public String getKeyData(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM blackBox",null);
        String result="";
        while(cursor.moveToNext()){
            result+= "Pass No. : "+cursor.getInt(0)+"\n"
                    +"Rnd String : "+cursor.getString(1)+"\n"
                    +"EncRnd String : "+cursor.getString(2)+"\n"
                    +"EncKey : "+cursor.getString(3)+"\n\n";
        }
        cursor.close();
        db.close();
        return result;
    }
    public String getEncData() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM datatab",null);
        String result="";
        while(cursor.moveToNext()){
            result+= "Int : "+cursor.getInt(0)+"\n"
                    +"Data : "+cursor.getString(2)+"\n\n"+"" +
                    " Decrypted : "+new EncryDecry().decrypt_with_master(cursor.getString(2),context);
        }
        cursor.close();
        db.close();
        return result;
    } */

    public Cursor getList(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT * FROM datatab ORDER BY cap ASC",null);
        return cursor;
    }

    public void insert_data_entry(String data, String cap, Context context) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("cap",cap);
        contentValues.put("data",new EncryDecry().encrypt_with_master(data,context));
        db.insert("datatab",null,contentValues);
        db.close();
    }

    public void update_data_entry(String data, String cap, int id, Context context) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("UPDATE datatab SET cap='"+cap+"', data='"+new EncryDecry().encrypt_with_master(data,context)+"' WHERE id="+id+"");
        db.close();
    }

    public boolean if_pass_exists(){
        SQLiteDatabase db=this.getReadableDatabase();
        boolean flag=false;
        Cursor cursor=db.rawQuery("SELECT * FROM blackBox",null);
        while (cursor.moveToNext()){
            flag=true;
        }
        cursor.close();
        db.close();
        return flag;
    }

    public Cursor login_request(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM blackBox",null);
        return cursor;
    }
    public boolean check_passes(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        boolean flag=false;
        Cursor cursor=db.rawQuery("SELECT * FROM blackBox WHERE passNo="+id+"",null);
        while (cursor.moveToNext()){
            flag=true;
        }
        return flag;
    }

    public void deleteEntry(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM datatab WHERE id="+id);
        db.close();
    }
    public int getKeyCount(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT COUNT(*) FROM blackBox ",null);
        int count=-1;
        while (cursor.moveToNext()){
            count=cursor.getInt(0);
        }
        return count;
    }
}

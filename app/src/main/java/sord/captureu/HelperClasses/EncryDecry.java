package sord.captureu.HelperClasses;

import android.content.Context;
import android.database.Cursor;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import sord.captureu.Activities.GG;

public class EncryDecry {
    private static String key;
    final private String AB ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*():'|/,`~";
    private String rndString1,rndString2, rndString3,hash1,hash2,hash3,encryptedMaster1, encryptedMaster2, encryptedMaster3,encryptedString1, encryptedString2, encryptedString3;
    private SecureRandom rnd = new SecureRandom();
    private DatabaseHelper databaseHelper;
    private Context context;
    private String currentKey;

    public void doTheThing(String pass1, String pass2, String pass3, String args, Context context) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, JSONException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        String masterKey=createMasterKey();
        this.context=context;
        databaseHelper=new DatabaseHelper(context);
        if(args.charAt(0)=='1'){
            rndString1=randomString(157);
            hash1=createHash(pass1.toCharArray());
            encryptedMaster1=toHex(encrypt(masterKey,hash1));
            encryptedString1=toHex(encrypt(rndString1,hash1));
            databaseHelper.insertKey1(rndString1,encryptedString1,encryptedMaster1);
        }
        if(args.charAt(1)=='1'){
            rndString2=randomString(157);
            hash2=createHash(pass2.toCharArray());
            encryptedMaster2=toHex(encrypt(masterKey,hash2));
            encryptedString2=toHex(encrypt(rndString2,hash2));
            databaseHelper.insertKey2(rndString2,encryptedString2,encryptedMaster2);
        }
        if(args.charAt(2)=='1'){
            rndString3=randomString(157);
            hash3=createHash(pass3.toCharArray());
            encryptedMaster3=toHex(encrypt(masterKey,hash3));
            encryptedString3=toHex(encrypt(rndString3,hash3));
            databaseHelper.insertKey3(rndString3,encryptedString3,encryptedMaster3);
        }
        currentKey=masterKey;
        ((GG) context.getApplicationContext()).setKey(masterKey);
    }

    public void addExtraPass(String pass, int passno, Context ctx) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, JSONException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        this.context=ctx;
        databaseHelper=new DatabaseHelper(ctx);
        String hashe=createHash(pass.toCharArray());
        String randStr=randomString(157);
        String encryptedMaster=toHex(encrypt(((GG)ctx.getApplicationContext()).getKey(),hashe));
        String encryptedString=toHex(encrypt(randStr,hashe));
        databaseHelper.insertKey(passno,randStr,encryptedString,encryptedMaster);
    }

    public void deletePass(int passNo, Context ctx){
        this.context=ctx;
        databaseHelper=new DatabaseHelper(ctx);
        databaseHelper.deleteKey(passNo);
    }

    public String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, JSONException {
        char _0=password[0];
        char _1=password[1];
        char _2=password[2];
        char _3=password[3];
        char _4=password[2];
        char _5=password[0];
        char _6=password[2];
        char _7=password[1];

        String s="H09^hf(*d"+_1+_2+_5+_4+_7+_3+_3+_3+_3+_6+new String(password)+_4+_2+_5+_7+_6+_3+_5+_4+_6+_3+_5+_1+_2+_1+_0+_0;
        char[] pp=s.toCharArray();
        byte[] salte= {5,3,6,7,4,8,2,'f',5,'d','i',6,2,7,1,'h',6,7,3,'q',5,1,'P',3,5,4,'L',3,7,1,3,5,3,4,6};
        byte[] hash = pbkdf2(pp, salte, 1000, 24);

        return toHex(hash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        //key=(Base64.encodeToString(skf.generateSecret(spec).getEncoded(),Base64.DEFAULT));
        return skf.generateSecret(spec).getEncoded();
    }

    public SecretKey getKey(){
        byte[] aesKeyBytes=fromHex(key);
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, "AES");
        return aesKey;
    }

    public byte[] encrypt(String message,String kkey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher =Cipher.getInstance("AES");
        byte[] aesKeyBytes=fromHex(kkey);
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, "AES");
        cipher.init(Cipher.ENCRYPT_MODE,aesKey);
        byte []encryptedBytes=cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return encryptedBytes;
    }

    private String decrypt(byte[] encrytpedMessage,String kkey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher =Cipher.getInstance("AES");
        byte[] aesKeyBytes=fromHex(kkey);
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, "AES");
        cipher.init(Cipher.DECRYPT_MODE,aesKey);
        byte[] decryptedBytes;
        try {
            decryptedBytes = cipher.doFinal(encrytpedMessage);
        }catch(BadPaddingException e){
            return "";
        }
        return new String(decryptedBytes);
    }

    public String createMasterKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keyGen.init(256,random);
        SecretKey secretKey = keyGen.generateKey();
        String keyString=toHex(secretKey.getEncoded());
        return keyString;
    }

    public String encrypt_with_master(String input, Context contexta) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] aesKeyBytes=fromHex(((GG)contexta.getApplicationContext()).getKey());
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, "AES");
        Cipher cipher =Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,aesKey);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return toHex(encryptedBytes);
    }

    public String decrypt_with_master(String input,Context contexta) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] aesKeyBytes=fromHex(((GG)contexta.getApplicationContext()).getKey());
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, "AES");
        Cipher cipher =Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,aesKey);
        byte[] decryptedBytes = cipher.doFinal(fromHex(input));
        return new String(decryptedBytes);
    }



    public boolean make_login(String pass,Context context2) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, JSONException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        Cursor cursor=new DatabaseHelper(context2).login_request();
        int passnu=1;
        String keyst="";
        int passd=0;
        boolean flag=false;
        while (cursor.moveToNext()){
            if( decrypt(fromHex(cursor.getString(2)),createHash(pass.toCharArray())).equals(cursor.getString(1))  ){
                keyst=decrypt(fromHex(cursor.getString(3)),createHash(pass.toCharArray()));
                passd=passnu;
                flag=true;
            }
            else {
                passnu++;
            }
        }

        if(flag) {
            ((GG) context2.getApplicationContext()).setKey(keyst);
            ((GG) context2.getApplicationContext()).setPass(passd);
            return true;
        }
        else return false;
    }

    public JSONObject checkLogin(String pass, Context ctx) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, JSONException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        JSONObject jsonObject=new JSONObject();
        Cursor cursor=new DatabaseHelper(ctx).login_request();
        DatabaseHelper databaseHelper=new DatabaseHelper(ctx);
        int passnu=1;
        String keyst="";
        int passd=0;
        boolean flag=false;
        while (cursor.moveToNext()){
            if( decrypt(fromHex(cursor.getString(2)),createHash(pass.toCharArray())).equals(cursor.getString(1))  ){
                keyst=decrypt(fromHex(cursor.getString(3)),createHash(pass.toCharArray()));
                passd=cursor.getInt(0);
                jsonObject.put("active_pass","yes");
                flag=true;
            }
            else {
                passnu++;
            }
        }

        if(!flag){
            jsonObject.put("active_pass","no");
        }

        if(databaseHelper.check_passes(1)){
            if(passd==1) {
                jsonObject.put("first_pass", 4);
            }
            else jsonObject.put("first_pass", 1);
        }
        else jsonObject.put("first_pass",0);



        if(databaseHelper.check_passes(2)){
            if(passd==2) {
                jsonObject.put("second_pass", 4);
            }
            else jsonObject.put("second_pass", 1);
        }
        else jsonObject.put("second_pass",0);


        if(databaseHelper.check_passes(3)){
            if(passd==3) {
                jsonObject.put("third_pass", 4);
            }
            else jsonObject.put("third_pass", 1);
        }
        else jsonObject.put("third_pass",0);
        return jsonObject;
    }


    private static byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }


    private static String toHex(byte[] array){
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        }
        else
            return hex;
    }
    private String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}

package sord.captureu.Activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sord.captureu.AsyncHelpers.FetchData;
import sord.captureu.Dialogs.AddPassword;
import sord.captureu.HelperClasses.DatabaseHelper;
import sord.captureu.HelperClasses.EncryDecry;
import sord.captureu.R;

public class Settings extends AppCompatActivity {
    private EditText settings_pass_text;
    private Button settings_pass_continue,settings_delete_add,settings_edit;
    private ImageButton settings_img_pass1,settings_img_pass2,settings_img_pass3;
    private String pass1,pass2,pass3;
    private String selected_pass;
    private int select;
    private LinearLayout settings_lower_layout,settings_upper_layout;
    private TextView settings_selected_pass_text,settings_to_change_pass_text,settings_pass_head;
    private Typeface typeface,typeface1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings_layout);
        typeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"lato_regular.ttf");
        typeface1= Typeface.createFromAsset(getApplicationContext().getAssets(),"lato_light.ttf");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        settings_pass_head=findViewById(R.id.settings_pass_head);
        settings_pass_head.setTypeface(typeface);
        settings_pass_text=findViewById(R.id.settings_pass_text);
        settings_pass_text.setTypeface(typeface1);
        settings_delete_add=findViewById(R.id.settings_delete_add);
        settings_pass_continue=findViewById(R.id.settings_pass_continue);
        settings_edit=findViewById(R.id.settings_edit);
        settings_selected_pass_text=findViewById(R.id.settings_selected_pass_text);
        settings_selected_pass_text.setTypeface(typeface);
        settings_to_change_pass_text=findViewById(R.id.settings_to_change_pass_text);
        settings_to_change_pass_text.setTypeface(typeface);

        settings_upper_layout=findViewById(R.id.settings_upper_layout);
        settings_lower_layout=findViewById(R.id.settings_lower_layout);
        settings_img_pass1=findViewById(R.id.settings_img_pass1);
        settings_img_pass2=findViewById(R.id.settings_img_pass2);
        settings_img_pass3=findViewById(R.id.settings_img_pass3);
        settings_img_pass1.setClickable(false);
        settings_img_pass2.setClickable(false);
        settings_img_pass3.setClickable(false);
        settings_img_pass1.setEnabled(false);
        settings_img_pass2.setEnabled(false);
        settings_img_pass3.setEnabled(false);

        settings_selected_pass_text.setText("...");
        settings_to_change_pass_text.setText("...");
        settings_delete_add.setClickable(false);
        settings_delete_add.setEnabled(false);
        settings_edit.setEnabled(false);
        settings_edit.setClickable(false);

        settings_pass_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                settings_selected_pass_text.setText("...");
                settings_to_change_pass_text.setText("...");
                settings_img_pass1.setBackgroundResource(R.drawable.no);
                settings_img_pass2.setBackgroundResource(R.drawable.no);
                settings_img_pass3.setBackgroundResource(R.drawable.no);
                pass1 = "0";
                pass2 = "0";
                pass3 = "0";
                settings_img_pass1.setClickable(false);
                settings_img_pass2.setClickable(false);
                settings_img_pass3.setClickable(false);
                settings_img_pass1.setEnabled(false);
                settings_img_pass2.setEnabled(false);
                settings_img_pass3.setEnabled(false);
                settings_delete_add.setClickable(false);
                settings_delete_add.setEnabled(false);
                settings_edit.setEnabled(false);
                settings_edit.setClickable(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        settings_pass_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settings_pass_text.getText().toString().length()>=4) {
                    try {
                        JSONObject jsonObject = new EncryDecry().checkLogin(settings_pass_text.getText().toString(), getApplication());
                        if (jsonObject.get("active_pass").equals("yes")) {
                            settings_img_pass1.setClickable(true);
                            settings_img_pass2.setClickable(true);
                            settings_img_pass3.setClickable(true);
                            settings_img_pass1.setEnabled(true);
                            settings_img_pass2.setEnabled(true);
                            settings_img_pass3.setEnabled(true);

                            if (jsonObject.get("first_pass").equals(4)) {
                                settings_img_pass1.setBackgroundResource(R.drawable.yes_chosen);
                                pass1 = "4";
                                settings_selected_pass_text.setText("You have entered the 1st password");
                                settings_to_change_pass_text.setText("Please select the password from the three images above to select the one you " +
                                        "want to add/edit/delete..");

                            } else if (jsonObject.get("first_pass").equals(1)) {
                                settings_img_pass1.setBackgroundResource(R.drawable.yes);
                                pass1 = "1";

                            } else if (jsonObject.get("first_pass").equals(0)) {
                                settings_img_pass1.setBackgroundResource(R.drawable.no);
                                pass1 = "0";

                            }

                            if (jsonObject.get("second_pass").equals(4)) {
                                settings_img_pass2.setBackgroundResource(R.drawable.yes_chosen);
                                pass2 = "4";
                                settings_selected_pass_text.setText("You have entered the 2nd password");
                                settings_to_change_pass_text.setText("Please select the password from the three images above to select the one you " +
                                        "want to add/edit/delete..");

                            } else if (jsonObject.get("second_pass").equals(1)) {
                                settings_img_pass2.setBackgroundResource(R.drawable.yes);
                                pass2 = "1";

                            } else if (jsonObject.get("second_pass").equals(0)) {
                                settings_img_pass2.setBackgroundResource(R.drawable.no);
                                pass2 = "0";

                            }

                            if (jsonObject.get("third_pass").equals(4)) {
                                settings_img_pass3.setBackgroundResource(R.drawable.yes_chosen);
                                pass3 = "4";
                                settings_selected_pass_text.setText("You have entered the 3rd password");
                                settings_to_change_pass_text.setText("Please select the password from the three images above to select the one you " +
                                        "want to add/edit/delete..");

                            } else if (jsonObject.get("third_pass").equals(1)) {
                                settings_img_pass3.setBackgroundResource(R.drawable.yes);
                                pass3 = "1";

                            } else if (jsonObject.get("third_pass").equals(0)) {
                                settings_img_pass3.setBackgroundResource(R.drawable.no);
                                pass3 = "0";

                            }
                        } else {
                            settings_delete_add.setClickable(false);
                            settings_delete_add.setEnabled(false);
                            settings_edit.setEnabled(false);
                            settings_edit.setClickable(false);
                            settings_selected_pass_text.setText("...");
                            settings_to_change_pass_text.setText("...");
                                settings_selected_pass_text.setText("You have entered an incorrect password!!");
                                settings_to_change_pass_text.setText("...");
                                settings_img_pass1.setBackgroundResource(R.drawable.no);
                                settings_img_pass2.setBackgroundResource(R.drawable.no);
                                settings_img_pass3.setBackgroundResource(R.drawable.no);
                                pass1 = "0";
                                pass2 = "0";
                                pass3 = "0";
                                settings_img_pass1.setClickable(false);
                                settings_img_pass2.setClickable(false);
                                settings_img_pass3.setClickable(false);
                                settings_img_pass1.setEnabled(false);
                                settings_img_pass2.setEnabled(false);
                                settings_img_pass3.setEnabled(false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(Settings.this, "The Passphrase has to be atleast 4 characters long!!", Toast.LENGTH_SHORT).show();
                    settings_img_pass1.setBackgroundResource(R.drawable.no);
                    settings_img_pass2.setBackgroundResource(R.drawable.no);
                    settings_img_pass3.setBackgroundResource(R.drawable.no);
                    pass1 = "0";
                    pass2 = "0";
                    pass3 = "0";
                    settings_img_pass1.setClickable(false);
                    settings_img_pass2.setClickable(false);
                    settings_img_pass3.setClickable(false);
                    settings_img_pass1.setEnabled(false);
                    settings_img_pass2.setEnabled(false);
                    settings_img_pass3.setEnabled(false);
                }
            }
        });


        settings_img_pass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_pass="a";

                if(pass1.equals("4")){
                    settings_img_pass1.setBackgroundResource(R.drawable.yes_chosen_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(true);
                    settings_edit.setClickable(true);
                    settings_delete_add.setText("Delete");
                    settings_edit.setText("Edit");
                }
                else if(pass1.equals("1")){
                    settings_img_pass1.setBackgroundResource(R.drawable.yes_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(true);
                    settings_edit.setClickable(true);
                    settings_delete_add.setText("Delete");
                    settings_edit.setText("Edit");
                }
                else if(pass1.equals("0")){
                    settings_img_pass1.setBackgroundResource(R.drawable.no_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(false);
                    settings_edit.setClickable(false);
                    settings_delete_add.setText("Add");
                    settings_edit.setText("...");
                }


                if(pass2.equals("4")){
                    settings_img_pass2.setBackgroundResource(R.drawable.yes_chosen);
                }
                else if(pass2.equals("1")){
                    settings_img_pass2.setBackgroundResource(R.drawable.yes);
                }
                else if(pass2.equals("0")){
                    settings_img_pass2.setBackgroundResource(R.drawable.no);
                }

                if(pass3.equals("4")){
                    settings_img_pass3.setBackgroundResource(R.drawable.yes_chosen);
                }
                else if(pass3.equals("1")){
                    settings_img_pass3.setBackgroundResource(R.drawable.yes);
                }
                else if(pass3.equals("0")){
                    settings_img_pass3.setBackgroundResource(R.drawable.no);
                }
            }
        });

        settings_img_pass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_pass="b";

                if(pass1.equals("4")){
                    settings_img_pass1.setBackgroundResource(R.drawable.yes_chosen);

                }
                else if(pass1.equals("1")){
                    settings_img_pass1.setBackgroundResource(R.drawable.yes);

                }
                else if(pass1.equals("0")){
                    settings_img_pass1.setBackgroundResource(R.drawable.no);

                }


                if(pass2.equals("4")){
                    settings_img_pass2.setBackgroundResource(R.drawable.yes_chosen_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(true);
                    settings_edit.setClickable(true);
                    settings_delete_add.setText("Delete");
                    settings_edit.setText("Edit");
                }
                else if(pass2.equals("1")){
                    settings_img_pass2.setBackgroundResource(R.drawable.yes_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(true);
                    settings_edit.setClickable(true);
                    settings_delete_add.setText("Delete");
                    settings_edit.setText("Edit");
                }
                else if(pass2.equals("0")){
                    settings_img_pass2.setBackgroundResource(R.drawable.no_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(false);
                    settings_edit.setClickable(false);
                    settings_delete_add.setText("Add");
                    settings_edit.setText("...");

                }

                if(pass3.equals("4")){
                    settings_img_pass3.setBackgroundResource(R.drawable.yes_chosen);

                }
                else if(pass3.equals("1")){
                    settings_img_pass3.setBackgroundResource(R.drawable.yes);

                }
                else if(pass3.equals("0")){
                    settings_img_pass3.setBackgroundResource(R.drawable.no);

                }

            }
        });

        settings_img_pass3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_pass="c";
                if(pass1.equals("4")){
                    settings_img_pass1.setBackgroundResource(R.drawable.yes_chosen);

                }
                else if(pass1.equals("1")){
                    settings_img_pass1.setBackgroundResource(R.drawable.yes);

                }
                else if(pass1.equals("0")){
                    settings_img_pass1.setBackgroundResource(R.drawable.no);

                }


                if(pass2.equals("4")){
                    settings_img_pass2.setBackgroundResource(R.drawable.yes_chosen);

                }
                else if(pass2.equals("1")){
                    settings_img_pass2.setBackgroundResource(R.drawable.yes);

                }
                else if(pass2.equals("0")){
                    settings_img_pass2.setBackgroundResource(R.drawable.no);
                }

                if(pass3.equals("4")){
                    settings_img_pass3.setBackgroundResource(R.drawable.yes_chosen_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(true);
                    settings_edit.setClickable(true);
                    settings_delete_add.setText("Delete");

                    settings_edit.setText("Edit");
                }
                else if(pass3.equals("1")){
                    settings_img_pass3.setBackgroundResource(R.drawable.yes_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(true);
                    settings_edit.setClickable(true);
                    settings_delete_add.setText("Delete");
                    settings_edit.setText("Edit");
                }
                else if(pass3.equals("0")){
                    settings_img_pass3.setBackgroundResource(R.drawable.no_selected);
                    settings_delete_add.setClickable(true);
                    settings_delete_add.setEnabled(true);
                    settings_edit.setEnabled(false);
                    settings_edit.setClickable(false);
                    settings_delete_add.setText("Add");
                    settings_edit.setText("...");
                }
            }
        });

        settings_delete_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_pass.equals("a")){
                    select=1;
                }
                else if(selected_pass.equals("b")){
                    select=2;
                }
                else if(selected_pass.equals("c")){
                    select=3;
                }

                if(settings_delete_add.getText().equals("Add")){
                    AddPassword addPassword = new AddPassword(select, Settings.this);
                    addPassword.show(getSupportFragmentManager(), "AddPass");
                }


                if(settings_delete_add.getText().equals("Delete")) {
                    try {
                        if (checkNumberOfPass()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    new DatabaseHelper(getApplicationContext()).deleteKey(select);
                                    //=============================================================
                                    settings_delete_add.setClickable(false);
                                    settings_delete_add.setEnabled(false);
                                    settings_edit.setEnabled(false);
                                    settings_edit.setClickable(false);
                                    settings_selected_pass_text.setText("...");
                                    settings_to_change_pass_text.setText("...");
                                    settings_img_pass1.setBackgroundResource(R.drawable.no);
                                    settings_img_pass2.setBackgroundResource(R.drawable.no);
                                    settings_img_pass3.setBackgroundResource(R.drawable.no);
                                    pass1 = "0";
                                    pass2 = "0";
                                    pass3 = "0";
                                    settings_img_pass1.setClickable(false);
                                    settings_img_pass2.setClickable(false);
                                    settings_img_pass3.setClickable(false);
                                    settings_img_pass1.setEnabled(false);
                                    settings_img_pass2.setEnabled(false);
                                    settings_img_pass3.setEnabled(false);
                                    settings_pass_text.setText("");

                                    //===========================================================
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setTitle("Delete Passphrase");
                            builder.setMessage("Are you sure you want to delete this Passphrase? You won't be able to undo this!!");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else
                            Toast.makeText(Settings.this, "You cannot delete the only Passphrase left!!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        settings_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPassword addPassword=new AddPassword(select,Settings.this);
                addPassword.show(getSupportFragmentManager(),"AddPass");
                //=============================================================
                settings_delete_add.setClickable(false);
                settings_delete_add.setEnabled(false);
                settings_edit.setEnabled(false);
                settings_edit.setClickable(false);
                settings_selected_pass_text.setText("...");
                settings_to_change_pass_text.setText("...");
                settings_img_pass1.setBackgroundResource(R.drawable.no);
                settings_img_pass2.setBackgroundResource(R.drawable.no);
                settings_img_pass3.setBackgroundResource(R.drawable.no);
                pass1 = "0";
                pass2 = "0";
                pass3 = "0";
                settings_img_pass1.setClickable(false);
                settings_img_pass2.setClickable(false);
                settings_img_pass3.setClickable(false);
                settings_img_pass1.setEnabled(false);
                settings_img_pass2.setEnabled(false);
                settings_img_pass3.setEnabled(false);
                settings_pass_text.setText("");

                //===========================================================
            }
        });
    }

    private boolean checkNumberOfPass() {


           if(new DatabaseHelper(this).getKeyCount()>1) return true;
           else return false;
    }



}

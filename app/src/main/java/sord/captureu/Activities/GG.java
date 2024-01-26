package sord.captureu.Activities;

import android.app.Application;

public class GG extends Application {

    private String masterKey="";
    private int pass=0;

    public String getKey() {
        return masterKey;
    }

    public void setKey(String key) {
        this.masterKey = key;
    }

    public int getPass(){return pass;}

    public void setPass(int pass){this.pass=pass;
        }

}

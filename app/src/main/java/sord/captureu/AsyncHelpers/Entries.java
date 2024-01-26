package sord.captureu.AsyncHelpers;

public class Entries {
    private String cap,title,data;
    private int id;
     public Entries(int id, String cap, String title, String data){
         this.setId(id);
         this.setCap(cap);
         this.setTitle(title);
         this.setData(data);
     }


    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

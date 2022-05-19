package com.example.reheimer2;

import com.google.firebase.database.Exclude;

public class Photo {

    public String imageUrl;
    public String imageDescription;
    private String mkey;

    public Photo(){

    }

    public Photo(String imageUrl, String imageDescription){
        this.imageUrl = imageUrl;
        this.imageDescription = imageDescription;

    }
    @Exclude
    public String getKey(){
        return mkey;
    }
    @Exclude
    public void setKey(String key){
        mkey = key;
    }
}

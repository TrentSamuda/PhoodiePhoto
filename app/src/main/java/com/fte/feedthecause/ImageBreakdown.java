package com.fte.feedthecause;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

/**
 * Created by trenton on 11/1/16.
 */

//this may handle each individual image

public class ImageBreakdown extends ImageView{

    private String title;
    private String description;
    private String keywords;
    private String ingredients;
    private String instructions;
    private String other;
    private String picture;
    private String owner;
    private String timePosted;
    private String photoID;
    private String forBitmap;//Its really a byte array coming in. Get ready


    private Bitmap bp;

    private int likeCounter;


    public ImageBreakdown(Context context, String _title, String _keywords, String _description,
                          String _ingredients, String _instructions, String _other,
                          String _picture, String _owner, String _timePosted,
                          String _photoID, String _forBitmap, int _likeCounter){
        super(context);
        title = _title;
        keywords = _keywords;
        description = _description;
        ingredients = _ingredients;
        instructions = _ingredients;
        other = _other;
        bp = stringToBitMap(_picture);
        this.setImageBitmap(bp);
        owner = _owner;
        timePosted = _timePosted;
        photoID = _photoID;
        forBitmap = _forBitmap;
        likeCounter = _likeCounter;

    }

    public ImageBreakdown(Context context){
        super(context);
        title = "";
        keywords = "";
        description = "";
        ingredients = "";
        instructions = "";
        other = "";
        bp = null;

        owner = "";
        timePosted = "";
    }

    public ImageBreakdown(Context context, String picVal){
        this(context);
        picture = picVal;
        this.setImageBitmap(stringToBitMap(picture));

    }

    /*converts a string  back into a bitmap*/
    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Bitmap getBp() {
        return bp;
    }

    public void setBp(Bitmap bp) {
        this.bp = bp;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getForBitmap() {
        return forBitmap;
    }

    public void setForBitmap(String forBitmap) {
        this.forBitmap = forBitmap;

    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }
}

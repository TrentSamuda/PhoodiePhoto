package com.fte.feedthecause;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trenton on 11/17/16.
 */

public class PhotoLoader {

    private List<ImageBreakdown> photoItems;// { get; private set; }
    private boolean IsBusy; //  { get; set; }
    private int CurrentPageValue; // { get; set; }
    private boolean CanLoadMoreItems; // { get; private set; }

    public PhotoLoader(){
        photoItems = new ArrayList<ImageBreakdown>();
    }

    public List<ImageBreakdown> getMySimpleItems() {
        return photoItems;
    }

    public void addPhoto(ImageBreakdown x){
        photoItems.add(x);
    }

    public int amountOfPhotos(){
        return  photoItems.size();
    }

    public void setMySimpleItems(List<ImageBreakdown> mySimpleItems) {
        photoItems = mySimpleItems;
    }

    public boolean isBusy() {
        return IsBusy;
    }

    public void setBusy(boolean busy) {
        IsBusy = busy;
    }

    public int getCurrentPageValue() {
        return CurrentPageValue;
    }

    public void setCurrentPageValue(int currentPageValue) {
        CurrentPageValue = currentPageValue;
    }

    public boolean isCanLoadMoreItems() {
        return CanLoadMoreItems;
    }

    public void setCanLoadMoreItems(boolean canLoadMoreItems) {
        CanLoadMoreItems = canLoadMoreItems;
    }
}

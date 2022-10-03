package com.example.pigalev_practical1;

import android.os.Parcel;
import android.os.Parcelable;

public class Mask implements Parcelable{

    private int ID;
    private String Marka;
    private String Model;
    private String YearProduction;
    private String Picture;

    protected Mask(Parcel in) {
        ID = in.readInt();
        Marka = in.readString();
        Model = in.readString();
        YearProduction = in.readString();
        Picture = in.readString();
    }

    public static final Parcelable.Creator<Mask> CREATOR = new Parcelable.Creator<Mask>() {
        @Override
        public Mask createFromParcel(Parcel in) {
            return new Mask(in);
        }

        @Override
        public Mask[] newArray(int size) {
            return new Mask[size];
        }
    };

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setMarka(String marka) {
        Marka = marka;
    }

    public void setModel(String model) {
        Marka = model;
    }

    public void setYearProduction(String yearProduction) {
        YearProduction = yearProduction;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public int getID() {
        return ID;
    }

    public String getMarka() {
        return Marka;
    }

    public String getModel() {
        return Model;
    }

    public String getYearProduction() {
        return YearProduction;
    }

    public String getPicture() {
        return Picture;
    }

    public Mask(int ID, String Marka, String Model, String YearProduction, String Picture) {
        this.ID = ID;
        this.Marka = Marka;
        this.Model = Model;
        this.YearProduction = YearProduction;
        this.Picture = Picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Marka);
        dest.writeString(Model);
        dest.writeString(YearProduction);
        dest.writeString(Picture);
    }
}

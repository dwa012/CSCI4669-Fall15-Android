package com.deitel.twittersearches;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Daniel Ward <drward3@uno.edu>
 * @since 9/18/14
 */
public class Item implements Parcelable {

    String s;
    int i;


    public Item() {
        this("");
    }

    public Item(String s) {
       this(0, s);
    }

    public Item(int i, String s) {
        this.i = i;
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (i != item.i) return false;
        if (s != null ? !s.equals(item.s) : item.s != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = s != null ? s.hashCode() : 0;
        result = 31 * result + i;
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.s);
        dest.writeInt(this.i);
    }

    private Item(Parcel in) {
        this.s = in.readString();
        this.i = in.readInt();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}

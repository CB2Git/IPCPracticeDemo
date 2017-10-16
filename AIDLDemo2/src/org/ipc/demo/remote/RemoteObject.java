
package org.ipc.demo.remote;

import java.util.Random;

import android.os.Parcel;
import android.os.Parcelable;

public class RemoteObject implements Parcelable {

    public int id;

    public String name;

    public RemoteObject() {
        Random random = new Random(System.currentTimeMillis());
        id = random.nextInt();
    }

    @Override
    public String toString() {
        return "id = " + id + ",name = " + name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected RemoteObject(Parcel source) {
        this.id = source.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
    }

    public void readFromParcel(Parcel dest) {
        this.id = dest.readInt();
    }

    public static final Parcelable.Creator<RemoteObject> CREATOR = new Creator<RemoteObject>() {

        @Override
        public RemoteObject[] newArray(int size) {
            return new RemoteObject[size];
        }

        @Override
        public RemoteObject createFromParcel(Parcel source) {
            return new RemoteObject(source);
        }
    };
}

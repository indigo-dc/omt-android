package pl.psnc.indigo.omt.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by michalu on 21.03.16.
 */
public class Task implements Serializable, Parcelable {

    private String id;
    private String date;
    private String last_change;
    private String application;
    private String description;
    private String status;
    private String user;
    private String creation;
    private String iosandbox;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLast_change() {
        return last_change;
    }

    public void setLast_change(String last_change) {
        this.last_change = last_change;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getIosandbox() {
        return iosandbox;
    }

    public void setIosandbox(String iosandbox) {
        this.iosandbox = iosandbox;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.date);
        dest.writeString(this.last_change);
        dest.writeString(this.application);
        dest.writeString(this.description);
        dest.writeString(this.status);
        dest.writeString(this.user);
        dest.writeString(this.creation);
        dest.writeString(this.iosandbox);
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.id = in.readString();
        this.date = in.readString();
        this.last_change = in.readString();
        this.application = in.readString();
        this.description = in.readString();
        this.status = in.readString();
        this.user = in.readString();
        this.creation = in.readString();
        this.iosandbox = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}

package pl.psnc.indigo.omt.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 21.03.16.
 */
public class Task implements Serializable, Parcelable {

    @SerializedName("id") private String mId;
    @SerializedName("date") private String mDate;
    @SerializedName("last_change") private String mLastChange;
    @SerializedName("application") private String mApplication;
    @SerializedName("description") private String mDescription;
    @SerializedName("status") private String mStatus;
    @SerializedName("user") private String mUser;
    @SerializedName("creation") private String mCreation;
    @SerializedName("iosandbox") private String mIosandbox;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getLast_change() {
        return mLastChange;
    }

    public void setLast_change(String last_change) {
        this.mLastChange = last_change;
    }

    public String getApplication() {
        return mApplication;
    }

    public void setApplication(String application) {
        this.mApplication = application;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String mUser) {
        this.mUser = mUser;
    }

    public String getCreation() {
        return mCreation;
    }

    public void setCreation(String mCreation) {
        this.mCreation = mCreation;
    }

    public String getIosandbox() {
        return mIosandbox;
    }

    public void setIosandbox(String mIosandbox) {
        this.mIosandbox = mIosandbox;
    }

    @Override public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Task task = (Task) o;

        return new EqualsBuilder().append(mId, task.mId)
            .append(mDate, task.mDate)
            .append(mLastChange, task.mLastChange)
            .append(mApplication, task.mApplication)
            .append(mDescription, task.mDescription)
            .append(mStatus, task.mStatus)
            .append(mUser, task.mUser)
            .append(mCreation, task.mCreation)
            .append(mIosandbox, task.mIosandbox)
            .isEquals();
    }

    @Override public final int hashCode() {
        return new HashCodeBuilder().append(mId)
            .append(mDate)
            .append(mLastChange)
            .append(mApplication)
            .append(mDescription)
            .append(mStatus)
            .append(mUser)
            .append(mCreation)
            .append(mIosandbox)
            .toHashCode();
    }

    @Override public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", mId)
            .append("date", mDate)
            .append("lastChange", mLastChange)
            .append("application", mApplication)
            .append("description", mDescription)
            .append("status", mStatus)
            .append("user", mUser)
            .append("creation", mCreation)
            .append("iosandbox", mIosandbox)
            .toString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mDate);
        dest.writeString(this.mLastChange);
        dest.writeString(this.mApplication);
        dest.writeString(this.mDescription);
        dest.writeString(this.mStatus);
        dest.writeString(this.mUser);
        dest.writeString(this.mCreation);
        dest.writeString(this.mIosandbox);
    }

    public Task() {
    }

    public Task(String username, String description, String application) {
        mUser = username;
        mDescription = description;
        mApplication = application;
    }

    protected Task(Parcel in) {
        mId = in.readString();
        mDate = in.readString();
        mLastChange = in.readString();
        mApplication = in.readString();
        mDescription = in.readString();
        mStatus = in.readString();
        mUser = in.readString();
        mCreation = in.readString();
        mIosandbox = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}

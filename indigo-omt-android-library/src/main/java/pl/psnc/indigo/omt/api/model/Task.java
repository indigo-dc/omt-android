package pl.psnc.indigo.omt.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 21.03.16.
 */
public class Task implements Parcelable, Comparable<Task> {

    @SerializedName("id") private String mId;
    @SerializedName("date") private String mDate;
    @SerializedName("last_change") private String mLastChange;
    @SerializedName("application") private String mApplication;
    @SerializedName("description") private String mDescription;
    @SerializedName("status") private String mStatus;
    @SerializedName("user") private String mUser;
    @SerializedName("creation") private String mCreation;
    @SerializedName("iosandbox") private String mIosandbox;
    @SerializedName("arguments") private List<String> mArguments = Collections.emptyList();
    @SerializedName("input_files") private List<InputFile> mInputFiles = Collections.emptyList();
    @SerializedName("output_files") private List<OutputFile> mOutputFiles = Collections.emptyList();


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

    public List<OutputFile> getOutputFiles() {
        return this.mOutputFiles;
    }

    public void setOutputFiles(List<OutputFile> mOutputFiles) {
        this.mOutputFiles = mOutputFiles;
    }

    public List<InputFile> getInputFiles() {
        return this.mInputFiles;
    }

    public void setInputFiles(List<InputFile> inputFiles) {
        this.mInputFiles = inputFiles;
    }

    public List<String> getArguments() {
        return this.mArguments;
    }

    public void setArguments(List<String> mArguments) {
        this.mArguments = mArguments;
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

    public Task() {
    }

    public Task(String description, String application) {
        mDescription = description;
        mApplication = application;
    }

    @Override public int compareTo(Task task) {
        Integer lId = Integer.parseInt(this.getId());
        Integer rId = Integer.parseInt(task.getId());
        return lId.compareTo(rId);
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
        mArguments = in.createStringArrayList();
        mInputFiles = in.createTypedArrayList(InputFile.CREATOR);
        mOutputFiles = in.createTypedArrayList(OutputFile.CREATOR);
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mDate);
        dest.writeString(mLastChange);
        dest.writeString(mApplication);
        dest.writeString(mDescription);
        dest.writeString(mStatus);
        dest.writeString(mUser);
        dest.writeString(mCreation);
        dest.writeString(mIosandbox);
        dest.writeStringList(mArguments);
        dest.writeTypedList(mInputFiles);
        dest.writeTypedList(mOutputFiles);
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}

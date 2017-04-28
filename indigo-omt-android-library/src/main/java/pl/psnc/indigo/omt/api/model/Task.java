package pl.psnc.indigo.omt.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
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
  @SerializedName("_links") private List<Link> mLinks;
  @SerializedName("arguments") private List<String> mArguments = Collections.emptyList();
  @SerializedName("input_files") private List<InputFile> mInputFiles = Collections.emptyList();
  @SerializedName("output_files") private List<OutputFile> mOutputFiles = Collections.emptyList();
  @SerializedName("runtime_data") private List<RuntimeData> mRuntimeData = Collections.emptyList();

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

  public List<Link> getLinks() {
    return this.mLinks;
  }

  public void setLinks(List<Link> mLinks) {
    this.mLinks = mLinks;
  }

  public List<RuntimeData> getRuntimeData() {
    return mRuntimeData;
  }

  public void setRuntimeData(List<RuntimeData> mRuntimeData) {
    this.mRuntimeData = mRuntimeData;
  }

  public String getUploadUrl() {
    if (mLinks != null && !mLinks.isEmpty()) {
      for (Link l : mLinks) {
        if (l.getRel().equals("input")) {
          return l.getHref();
        }
      }
    }
    return null;
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
        .append(mRuntimeData, task.mRuntimeData)
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
        .append(mRuntimeData)
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
    dest.writeList(this.mLinks);
    dest.writeStringList(this.mArguments);
    dest.writeTypedList(this.mInputFiles);
    dest.writeTypedList(this.mOutputFiles);
    dest.writeList(this.mRuntimeData);
  }

  protected Task(Parcel in) {
    this.mId = in.readString();
    this.mDate = in.readString();
    this.mLastChange = in.readString();
    this.mApplication = in.readString();
    this.mDescription = in.readString();
    this.mStatus = in.readString();
    this.mUser = in.readString();
    this.mCreation = in.readString();
    this.mIosandbox = in.readString();
    this.mLinks = new ArrayList<Link>();
    in.readList(this.mLinks, Link.class.getClassLoader());
    this.mArguments = in.createStringArrayList();
    this.mInputFiles = in.createTypedArrayList(InputFile.CREATOR);
    this.mOutputFiles = in.createTypedArrayList(OutputFile.CREATOR);
    this.mRuntimeData = new ArrayList<RuntimeData>();
    in.readList(this.mRuntimeData, RuntimeData.class.getClassLoader());
  }

  public static final Creator<Task> CREATOR = new Creator<Task>() {
    @Override public Task createFromParcel(Parcel source) {
      return new Task(source);
    }

    @Override public Task[] newArray(int size) {
      return new Task[size];
    }
  };
}

package pl.psnc.indigo.omt.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by michalurbaniak on 28.04.2017.
 */

public class RuntimeData implements Parcelable {
  @Expose @SerializedName("name") private String name;
  @Expose @SerializedName("proto") private String proto;
  @Expose @SerializedName("last_change") private String lastChange;
  @Expose @SerializedName("value") private String value;
  @Expose @SerializedName("type") private String type;
  @Expose @SerializedName("description") private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProto() {
    return proto;
  }

  public void setProto(String proto) {
    this.proto = proto;
  }

  public String getLastChange() {
    return lastChange;
  }

  public void setLastChange(String lastChange) {
    this.lastChange = lastChange;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override public String toString() {
    return "RuntimeData{"
        + "name='"
        + name
        + '\''
        + ", lastChange='"
        + lastChange
        + '\''
        + ", value='"
        + value
        + '\''
        + '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.proto);
    dest.writeString(this.lastChange);
    dest.writeString(this.value);
    dest.writeString(this.type);
    dest.writeString(this.description);
  }

  public RuntimeData() {
  }

  protected RuntimeData(Parcel in) {
    this.name = in.readString();
    this.proto = in.readString();
    this.lastChange = in.readString();
    this.value = in.readString();
    this.type = in.readString();
    this.description = in.readString();
  }

  public static final Parcelable.Creator<RuntimeData> CREATOR =
      new Parcelable.Creator<RuntimeData>() {
        @Override public RuntimeData createFromParcel(Parcel source) {
          return new RuntimeData(source);
        }

        @Override public RuntimeData[] newArray(int size) {
          return new RuntimeData[size];
        }
      };
}

package pl.psnc.indigo.omt.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 13.09.16.
 */
public class InputFile implements Serializable, Parcelable {
    private static final long serialVersionUID = -8629464708321890767L;
    @SerializedName("name") private String mName;
    @SerializedName("status") private String mStatus;
    @SerializedName("url") private String mUrl;
    private File mFile;

    public final String getName() {
        return mName;
    }

    public final void setName(final String mName) {
        this.mName = mName;
    }

    public final String getStatus() {
        return mStatus;
    }

    public final void setStatus(final String status) {
        this.mStatus = status;
    }

    public File getFile() {
        return this.mFile;
    }

    public void setFile(File mFile) {
        this.mFile = mFile;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InputFile inputFile = (InputFile) o;

        return new EqualsBuilder().append(mName, inputFile.mName)
            .append(mStatus, inputFile.mStatus)
            .isEquals();
    }

    @Override public final int hashCode() {
        return new HashCodeBuilder().append(mName).append(mStatus).toHashCode();
    }

    @Override public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", mName)
            .append("status", mStatus)
            .toString();
    }

    public InputFile() {
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mStatus);
        dest.writeString(this.mUrl);
        dest.writeSerializable(this.mFile);
    }

    protected InputFile(Parcel in) {
        this.mName = in.readString();
        this.mStatus = in.readString();
        this.mUrl = in.readString();
        this.mFile = (File) in.readSerializable();
    }

    public static final Parcelable.Creator<InputFile> CREATOR =
            new Parcelable.Creator<InputFile>() {
            @Override public InputFile createFromParcel(Parcel source) {
                return new InputFile(source);
            }

            @Override public InputFile[] newArray(int size) {
                return new InputFile[size];
            }
        };
}
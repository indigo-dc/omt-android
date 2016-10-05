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
 * Created by michalu on 13.09.16.
 */
public class OutputFile implements Parcelable, Serializable {
    private static final long serialVersionUID = -6923395958538037455L;
    @SerializedName("name") private String mName;
    @SerializedName("url") private String mUrl;

    public final String getName() {
        return mName;
    }

    public final void setName(String name) {
        this.mName = name;
    }

    public final String getUrl() {
        return mUrl;
    }

    public final void setUrl(String url) {
        this.mUrl = url;
    }

    @Override public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutputFile that = (OutputFile) o;

        return new EqualsBuilder().append(mName, that.mName).append(mUrl, that.mUrl).isEquals();
    }

    @Override public final int hashCode() {
        return new HashCodeBuilder().append(mName).append(mUrl).toHashCode();
    }

    @Override public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", mName)
            .append("url", mUrl)
            .toString();
    }

    protected OutputFile(Parcel in) {
        mName = in.readString();
        mUrl = in.readString();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mUrl);
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<OutputFile> CREATOR = new Creator<OutputFile>() {
        @Override public OutputFile createFromParcel(Parcel in) {
            return new OutputFile(in);
        }

        @Override public OutputFile[] newArray(int size) {
            return new OutputFile[size];
        }
    };
}

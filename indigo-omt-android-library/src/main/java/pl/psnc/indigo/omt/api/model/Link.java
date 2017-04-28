package pl.psnc.indigo.omt.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 21.03.16.
 */
public class Link implements Parcelable {

    @SerializedName("rel") private String mRel;
    @SerializedName("href") private String mHref;

    public Link() {

    }

    public String getRel() {
        return mRel;
    }

    public void setRel(String rel) {
        this.mRel = rel;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String href) {
        this.mHref = href;
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("rel", mRel).append("href", mHref).toString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mRel);
        dest.writeString(this.mHref);
    }

    protected Link(Parcel in) {
        this.mRel = in.readString();
        this.mHref = in.readString();
    }

    public static final Parcelable.Creator<Link> CREATOR = new Parcelable.Creator<Link>() {
        @Override public Link createFromParcel(Parcel source) {
            return new Link(source);
        }

        @Override public Link[] newArray(int size) {
            return new Link[size];
        }
    };
}

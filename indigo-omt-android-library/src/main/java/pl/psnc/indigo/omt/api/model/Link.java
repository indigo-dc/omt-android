package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 21.03.16.
 */
public class Link {

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
}

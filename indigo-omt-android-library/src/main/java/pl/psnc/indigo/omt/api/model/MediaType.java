package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 21.03.16.
 */
public class MediaType {
    public MediaType(String mType) {
        this.mType = mType;
    }

    @SerializedName("type") private String mType;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("type", mType)
            .build();
    }
}
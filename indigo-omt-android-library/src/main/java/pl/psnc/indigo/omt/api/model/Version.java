package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 21.03.16.
 */
public class Version {

    @SerializedName("status") private String mStatus;
    @SerializedName("updated") private String mUpdated;
    @SerializedName("media-types") private MediaType mMediaTypes;
    @SerializedName("_links") private List<Link> mLinks;
    private String mId;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getUpdated() {
        return mUpdated;
    }

    public void setUpdated(String updated) {
        this.mUpdated = updated;
    }

    public MediaType getMediaTypes() {
        return mMediaTypes;
    }

    public void setMediaTypes(MediaType mediaTypes) {
        this.mMediaTypes = mediaTypes;
    }

    public List<Link> getLinks() {
        return mLinks;
    }

    public void setLinks(List<Link> links) {
        this.mLinks = links;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @Override public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Version version = (Version) o;

        return new EqualsBuilder().append(mStatus, version.mStatus)
            .append(mUpdated, version.mUpdated)
            .append(mMediaTypes, version.mMediaTypes)
            .append(mLinks, version.mLinks)
            .append(mId, version.mId)
            .isEquals();
    }

    @Override public final int hashCode() {
        return new HashCodeBuilder().append(mStatus)
            .append(mUpdated)
            .append(mMediaTypes)
            .append(mLinks)
            .append(mId)
            .toHashCode();
    }

    @Override public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("status", mStatus)
            .append("updated", mUpdated)
            .append("mediaType", mMediaTypes)
            .append("links", mLinks)
            .append("id", mId)
            .toString();
    }
}

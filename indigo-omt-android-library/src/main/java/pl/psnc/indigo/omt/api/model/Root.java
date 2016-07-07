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
public class Root {
    @SerializedName("_links") private List<Link> mLinks;
    @SerializedName("versions") private List<Version> mVersions;

    public List<Link> getLinks() {
        return mLinks;
    }

    public void setLinks(List<Link> links) {
        this.mLinks = links;
    }

    public List<Version> getVersions() {
        return mVersions;
    }

    public void setVersions(List<Version> versions) {
        this.mVersions = versions;
    }

    @Override public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Root root = (Root) o;

        return new EqualsBuilder().append(mLinks, root.mLinks)
            .append(mVersions, root.mVersions)
            .isEquals();
    }

    @Override public final int hashCode() {
        return new HashCodeBuilder().append(mLinks).append(mVersions).toHashCode();
    }

    @Override public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("links", mLinks)
            .append("versions", mVersions)
            .toString();
    }
}

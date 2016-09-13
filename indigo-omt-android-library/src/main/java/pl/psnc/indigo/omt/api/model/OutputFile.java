package pl.psnc.indigo.omt.api.model;

import java.io.Serializable;
import java.net.URI;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 13.09.16.
 */
public class OutputFile implements Serializable {
    private static final long serialVersionUID = -6923395958538037455L;
    private String mName;
    private URI mUrl;

    public final String getName() {
        return mName;
    }

    public final void setName(final String name) {
        this.mName = name;
    }

    public final URI getUrl() {
        return mUrl;
    }

    public final void setUrl(final URI url) {
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
}

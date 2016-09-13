package pl.psnc.indigo.omt.api.model.json;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by michalu on 13.09.16.
 */
public class InputFile implements Serializable {
    private static final long serialVersionUID = -8629464708321890767L;
    private String mName;
    private String mStatus;

    public final String getmName() {
        return mName;
    }

    public final void setmName(final String mName) {
        this.mName = mName;
    }

    public final String getStatus() {
        return mStatus;
    }

    public final void setStatus(final String status) {
        this.mStatus = status;
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
}
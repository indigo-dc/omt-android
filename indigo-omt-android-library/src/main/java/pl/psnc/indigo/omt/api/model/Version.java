package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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

    public MediaType getmMediaTypes() {
        return mMediaTypes;
    }

    public void setmMediaTypes(MediaType mMediaTypes) {
        this.mMediaTypes = mMediaTypes;
    }

    public List<Link> getmLinks() {
        return mLinks;
    }

    public void setmLinks(List<Link> mLinks) {
        this.mLinks = mLinks;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}

package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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
}

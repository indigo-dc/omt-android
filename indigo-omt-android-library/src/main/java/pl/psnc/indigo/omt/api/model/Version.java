package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by michalu on 21.03.16.
 */
public class Version {

    private String status;
    private String updated;
    @SerializedName("media-types")
    private MediaType mediaTypes;
    @SerializedName("_links")
    private List<Link> links;
    private String id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public MediaType getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(MediaType mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

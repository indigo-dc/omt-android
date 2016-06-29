package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by michalu on 21.03.16.
 */
public class Root {
  @SerializedName("_links") private List<Link> links;
  private List<Version> versions;

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public List<Version> getVersions() {
    return versions;
  }

  public void setVersions(List<Version> versions) {
    this.versions = versions;
  }
}

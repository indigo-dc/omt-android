package pl.psnc.indigo.omt.api.model;

/**
 * Created by michalu on 21.03.16.
 */
public class Link {

  private String rel;
  private String href;

  public Link() {

  }

  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }
}

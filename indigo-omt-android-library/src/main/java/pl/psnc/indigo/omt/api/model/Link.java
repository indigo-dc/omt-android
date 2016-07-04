package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michalu on 21.03.16.
 */
public class Link {

    @SerializedName("rel") private String mRel;
    @SerializedName("href") private String mHref;

    public Link() {

    }

    public String getmRel() {
        return mRel;
    }

    public void setmRel(String rel) {
        this.mRel = rel;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String href) {
        this.mHref = href;
    }
}

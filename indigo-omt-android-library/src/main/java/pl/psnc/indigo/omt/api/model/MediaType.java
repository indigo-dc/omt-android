package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by michalu on 21.03.16.
 */
public class MediaType {

    @SerializedName("type") private String mType;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
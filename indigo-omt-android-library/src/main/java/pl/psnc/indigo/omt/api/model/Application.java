package pl.psnc.indigo.omt.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by michalu on 06.02.17.
 */

public class Application {
    @Expose @SerializedName("id") private String mId;
    @Expose @SerializedName("name") private String mName;
    @Expose @SerializedName("infrastructure") private String mInfrastructure;
    @Expose @SerializedName("date") private String mDate;
    @Expose @SerializedName("outcome") private String mOutcome;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getInfrastructure() {
        return this.mInfrastructure;
    }

    public void setInfrastructure(String infrastructure) {
        this.mInfrastructure = infrastructure;
    }

    public String getDate() {
        return this.mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getOutcome() {
        return this.mOutcome;
    }

    public void setOutcome(String outcome) {
        this.mOutcome = outcome;
    }

    @Override public String toString() {
        return "Application{"
            + "id='"
            + mId
            + '\''
            + ", mName='"
            + mName
            + '\''
            + ", mInfrastructure='"
            + mInfrastructure
            + '\''
            + ", mDate='"
            + mDate
            + '\''
            + ", outcome='"
            + mOutcome
            + '\''
            + '}';
    }
}

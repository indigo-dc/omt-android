package pl.psnc.indigo.omt.api;

import pl.psnc.indigo.omt.root.RootAPI;
import pl.psnc.indigo.omt.root.RootOperations;

/**
 * Created by michalu on 12.01.17.
 */

public class BaseAPI implements RootOperations {
    private RootAPI mRootAPI;

    public BaseAPI(RootAPI rootAPI) {
        mRootAPI = rootAPI;
    }

    @Override public String getRoot() {
        return mRootAPI.getRoot();
    }
}

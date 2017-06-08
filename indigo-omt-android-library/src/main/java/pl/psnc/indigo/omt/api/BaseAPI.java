package pl.psnc.indigo.omt.api;

import pl.psnc.indigo.omt.exceptions.IndigoException;
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

  @Override public String getRoot() throws IndigoException {
    if (mRootAPI == null) throw new IndigoException("RootAPI is null");
    return mRootAPI.getRoot();
  }
}

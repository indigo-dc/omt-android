package pl.psnc.indigo.omt.root;

import android.content.AsyncTaskLoader;
import android.content.Context;
import pl.psnc.indigo.omt.api.model.Root;

/**
 * Created by michalu on 13.01.17.
 */

public class RootLoader extends AsyncTaskLoader<Root> {
    private RootOperations mRootAPI;
    public RootLoader(Context context) {
        super(context);
    }

    @Override public Root loadInBackground() {
        return null;
    }
}

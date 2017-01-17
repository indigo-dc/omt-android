package pl.psnc.indigo.omt.iam;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import net.openid.appauth.AuthState;
import org.json.JSONException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by michalu on 06.10.16.
 */

//TODO rethink/move to app?
public class IAMHelper {
    @NonNull public static AuthState readAuthState(Context ctx) {
        SharedPreferences authPrefs = ctx.getSharedPreferences("auth", MODE_PRIVATE);
        String stateJson = authPrefs.getString("stateJson", null);
        AuthState state = null;
        if (stateJson != null) {
            try {
                state = AuthState.jsonDeserialize(stateJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            state = new AuthState();
        }
        return state;
    }

    public static void writeAuthState(@NonNull AuthState state, Context ctx) {
        SharedPreferences authPrefs = ctx.getSharedPreferences("auth", MODE_PRIVATE);
        authPrefs.edit().putString("stateJson", state.jsonSerializeString()).apply();
    }

    @NonNull public static boolean readForceReAuth(Context ctx) {
        SharedPreferences authPrefs = ctx.getSharedPreferences("auth", MODE_PRIVATE);
        boolean state = authPrefs.getBoolean("needsReAuth", true);
        return state;
    }

    public static void writeForceReAuth(boolean state, Context ctx) {
        SharedPreferences authPrefs = ctx.getSharedPreferences("auth", MODE_PRIVATE);
        authPrefs.edit().putBoolean("needsReAuth", state).apply();
    }
}

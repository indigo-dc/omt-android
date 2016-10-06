package pl.psnc.indigo.omt.iam;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import net.openid.appauth.AuthState;
import org.json.JSONException;
import pl.psnc.indigo.omt.Indigo;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by michalu on 06.10.16.
 */

public class IAMHelper {
    public static final String IAM_CLIENT_ID = "6158f403-b6a0-4b17-b181-0543da55c7ef";
    public static final Uri IAM_URL = Uri.parse("https://iam-test.indigo-datacloud.eu");
    public static final String IAM_REDIRECT_URL_SCHEME =
        "pl.psnc.indigo.omt.sampleapp://oauth2redirect";
    public static final String[] IAM_SCOPES =
        new String[] { "profile", "openid", "offline_access" };

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
        Indigo.setAccessToken(state.getAccessToken());
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

package pl.psnc.indigo.omt.threads;

import net.openid.appauth.AuthState;

/**
 * Created by michalu on 15.07.16.
 */
public interface ApiCallWorkflow {
    //prepare worker handler (for background jobs)
    void prepare();

    //get the access token
    void authenticate(AuthState authState);

    //do any network/background work with the accessToken
    void networkWork(String accessToken);
}

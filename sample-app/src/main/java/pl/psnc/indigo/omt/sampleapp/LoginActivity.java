package pl.psnc.indigo.omt.sampleapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.Date;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;
import pl.psnc.indigo.omt.iam.IAMHelper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.login_button_iam) Button mLoginButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        final Uri issuerUri = IAMHelper.IAM_URL;
        final Context ctx = this;
        final AuthState authState = IAMHelper.readAuthState(ctx);
        if (authState.isAuthorized()) {
            Intent i = new Intent(ctx, MainActivity.class);
            startActivity(i);
            finish();
        }
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                long now = new Date().getTime();
                if (IAMHelper.readForceReAuth(ctx) == true) {
                    newAuthenticationProcess(ctx, issuerUri);
                } else if (authState.getAccessToken() != null
                    && authState.getRefreshToken() != null
                    && authState.getAccessTokenExpirationTime() <= now) {
                    Log.i(TAG, getString(R.string.access_token_expired_toast));
                    Toast.makeText(ctx, R.string.access_token_expired_toast,
                        Toast.LENGTH_SHORT).show();
                    final AuthorizationService service = new AuthorizationService(ctx);
                    service.performTokenRequest(authState.createTokenRefreshRequest(),
                        new AuthorizationService.TokenResponseCallback() {
                            @Override
                            public void onTokenRequestCompleted(@Nullable TokenResponse response,
                                @Nullable AuthorizationException ex) {
                                if (response != null) {
                                    Log.i(TAG, "Refreshed access_token: " + response.accessToken);
                                    Log.i(TAG, "Refreshed refresh_token: " + response.refreshToken);
                                    authState.update(response, ex);
                                    IAMHelper.writeAuthState(authState, ctx);
                                    Intent postAuthIntent = new Intent(ctx, MainActivity.class);
                                    startActivity(postAuthIntent);
                                    service.dispose();
                                    finish();
                                } else {
                                    ex.printStackTrace();
                                    IAMHelper.writeForceReAuth(true, ctx);
                                }
                            }
                        });
                } else if (authState.getAccessToken() != null
                    && authState.getRefreshToken() != null
                    && authState.getAccessTokenExpirationTime() > now) {
                    Log.i(TAG, "The access token is ok, so go to activity");
                    Intent postAuthIntent = new Intent(ctx, MainActivity.class);
                    startActivity(postAuthIntent);
                    finish();
                } else {
                    newAuthenticationProcess(ctx, issuerUri);
                }
            }
        });
    }

    public void newAuthenticationProcess(final Context ctx, final Uri issuerUri) {
        AuthorizationServiceConfiguration.fetchFromIssuer(issuerUri,
            new AuthorizationServiceConfiguration.RetrieveConfigurationCallback() {
                @Override public void onFetchConfigurationCompleted(
                    @Nullable AuthorizationServiceConfiguration serviceConfiguration,
                    @Nullable AuthorizationException ex) {
                    if (ex != null) {
                        Log.w(TAG, "Failed to retrieve configuration for " + issuerUri, ex);
                    } else {
                        // service configuration retrieved, proceed to authorization...
                        Log.i(TAG, serviceConfiguration.toJsonString());

                        AuthorizationService service = new AuthorizationService(ctx);

                        AuthorizationRequest req =
                            new AuthorizationRequest.Builder(serviceConfiguration,
                                IAMHelper.IAM_CLIENT_ID, ResponseTypeValues.CODE,
                                Uri.parse(IAMHelper.IAM_REDIRECT_URL_SCHEME)).setScopes(
                                IAMHelper.IAM_SCOPES).build();
                        Intent postAuthIntent = new Intent(ctx, MainActivity.class);
                        service.performAuthorizationRequest(req,
                            PendingIntent.getActivity(ctx, req.hashCode(), postAuthIntent, 0));
                        service.dispose();
                        //finish();
                    }
                }
            });
    }
}

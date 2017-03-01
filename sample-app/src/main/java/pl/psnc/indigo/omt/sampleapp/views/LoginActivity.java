package pl.psnc.indigo.omt.sampleapp.views;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;
import pl.psnc.indigo.omt.iam.IAMHelper;
import pl.psnc.indigo.omt.sampleapp.BuildConfig;
import pl.psnc.indigo.omt.sampleapp.R;

public class LoginActivity extends IndigoActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.login_button_iam) Button mLoginButton;
    @BindView(R.id.login_progressbar) ProgressBar mProgressBar;
    private AuthState mAuthState;
    private AuthorizationService mService;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        final Uri issuerUri = Uri.parse(BuildConfig.IAM_URL);
        final Context ctx = this;
        mService = new AuthorizationService(ctx);
        mAuthState = IAMHelper.readAuthState(ctx);
        if (isAuthorized(mAuthState)) {
            goToTasks(ctx);
        }
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (isAuthorized(mAuthState)) {
                    goToTasks(ctx);
                } else {
                    showProgressBarAndDisableButton();
                    reAuth(ctx);
                }
            }
        });
    }

    private boolean isAuthorized(AuthState authState) {
        try {
            if (authState.getLastAuthorizationResponse() != null && authState.isAuthorized()) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void newAuthenticationProcess(final Context ctx, final Uri issuerUri) {
        AuthorizationServiceConfiguration.fetchFromIssuer(issuerUri,
            new AuthorizationServiceConfiguration.RetrieveConfigurationCallback() {
                @Override public void onFetchConfigurationCompleted(
                    @Nullable AuthorizationServiceConfiguration serviceConfiguration,
                    @Nullable AuthorizationException ex) {
                    if (ex != null) {
                        hideProgressBarAndEnableButton();
                        Log.w(TAG, "Failed to retrieve configuration for " + issuerUri, ex);
                    } else {
                        // service configuration retrieved, proceed to authorization...
                        hideProgressBarAndEnableButton();
                        Log.i(TAG, serviceConfiguration.toJsonString());
                        AuthorizationRequest req =
                            new AuthorizationRequest.Builder(serviceConfiguration,
                                BuildConfig.IAM_CLIENT_ID, ResponseTypeValues.CODE,
                                Uri.parse(BuildConfig.IAM_REDIRECT_URL_SCHEME)).setScopes(
                                BuildConfig.IAM_SCOPES).build();
                        Intent postAuthIntent = new Intent(ctx, TasksActivity.class);
                        mService.performAuthorizationRequest(req,
                            PendingIntent.getActivity(ctx, req.hashCode(), postAuthIntent, 0));
                    }

                    //finish();
                }
            });
    }

    public void goToTasks(Context ctx) {
        hideProgressBarAndEnableButton();
        Log.i(TAG, "The access token is ok, so go to activity");
        Intent postAuthIntent = new Intent(ctx, TasksActivity.class);
        startActivity(postAuthIntent);
        finish();
    }

    public void reAuth(final Context ctx) {
        try {
            Toast.makeText(ctx, "Checking authorization", Toast.LENGTH_SHORT).show();
            mService.performTokenRequest(mAuthState.createTokenRefreshRequest(),
                new AuthorizationService.TokenResponseCallback() {
                    @Override public void onTokenRequestCompleted(@Nullable TokenResponse response,
                        @Nullable AuthorizationException ex) {
                        if (response != null) {
                            Log.i(TAG, "Refreshed access_token: " + response.accessToken);
                            Log.i(TAG, "Refreshed refresh_token: " + response.refreshToken);
                            mAuthState.update(response, ex);
                            IAMHelper.writeAuthState(mAuthState, ctx);
                            Intent postAuthIntent = new Intent(ctx, TasksActivity.class);
                            startActivity(postAuthIntent);
                            mService.dispose();
                            hideProgressBarAndEnableButton();
                            finish();
                        } else {
                            hideProgressBarAndEnableButton();
                            ex.printStackTrace();
                            IAMHelper.writeAuthState(new AuthState(), getApplicationContext());
                        }
                    }
                });
        } catch (IllegalStateException e) {
            IAMHelper.writeAuthState(new AuthState(), getApplicationContext());
            newAuthenticationProcess(this, Uri.parse(BuildConfig.IAM_URL));
        }
    }

    private void showProgressBarAndDisableButton() {
        mLoginButton.setClickable(false);
        mLoginButton.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarAndEnableButton() {
        mLoginButton.setClickable(true);
        mLoginButton.setEnabled(true);
        mProgressBar.setVisibility(View.GONE);
    }
}

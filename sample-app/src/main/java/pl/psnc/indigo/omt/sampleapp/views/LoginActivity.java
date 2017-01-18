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
import net.openid.appauth.ClientSecretPost;
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

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        final Uri issuerUri = Uri.parse(BuildConfig.IAM_URL);
        final Context ctx = this;
        mAuthState = IAMHelper.readAuthState(ctx);
        if (mAuthState.isAuthorized()) {
            reAuth(ctx);
        }
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showProgressBarAndDisableButton();
                if (!mAuthState.isAuthorized() && mAuthState.getAccessToken() == null) {
                    Toast.makeText(getApplicationContext(),
                        "No access token and authstate not authorized\nRun full process",
                        Toast.LENGTH_SHORT).show();
                    newAuthenticationProcess(ctx, issuerUri);
                } else if (!mAuthState.isAuthorized() && mAuthState.getAccessToken() != null
                    || mAuthState != null && mAuthState.getNeedsTokenRefresh()) {
                    Toast.makeText(getApplicationContext(),
                        "Not authorized, but we have old token = only token refresh request",
                        Toast.LENGTH_SHORT).show();
                    reAuth(ctx);
                } else if (mAuthState.isAuthorized()) {
                    Toast.makeText(getApplicationContext(),
                        "Token is valid and active\nGo to tasks list", Toast.LENGTH_SHORT).show();
                    goToTasks(ctx);
                } else {
                    Toast.makeText(getApplicationContext(),
                        "Something odd. Full process should be started", Toast.LENGTH_SHORT).show();
                    hideProgressBarAndEnableButton();
                    IAMHelper.writeAuthState(new AuthState(), getApplicationContext());
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
                        hideProgressBarAndEnableButton();
                        Log.w(TAG, "Failed to retrieve configuration for " + issuerUri, ex);
                    } else {
                        // service configuration retrieved, proceed to authorization...
                        hideProgressBarAndEnableButton();
                        Log.i(TAG, serviceConfiguration.toJsonString());

                        AuthorizationService service = new AuthorizationService(ctx);

                        AuthorizationRequest req =
                            new AuthorizationRequest.Builder(serviceConfiguration,
                                BuildConfig.IAM_CLIENT_ID, ResponseTypeValues.CODE,
                                Uri.parse(BuildConfig.IAM_REDIRECT_URL_SCHEME)).setScopes(
                                BuildConfig.IAM_SCOPES).build();
                        Intent postAuthIntent = new Intent(ctx, TasksActivity.class);
                        service.performAuthorizationRequest(req,
                            PendingIntent.getActivity(ctx, req.hashCode(), postAuthIntent, 0));
                        service.dispose();
                        //finish();
                    }
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
        Toast.makeText(ctx, "Checking authorization", Toast.LENGTH_SHORT).show();
        final AuthorizationService service = new AuthorizationService(ctx);
        service.performTokenRequest(mAuthState.createTokenRefreshRequest(),
            new ClientSecretPost(BuildConfig.IAM_CLIENT_SECRET),
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
                        service.dispose();
                        hideProgressBarAndEnableButton();
                        finish();
                    } else {
                        hideProgressBarAndEnableButton();
                        ex.printStackTrace();
                        IAMHelper.writeAuthState(new AuthState(), getApplicationContext());
                    }
                }
            });
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

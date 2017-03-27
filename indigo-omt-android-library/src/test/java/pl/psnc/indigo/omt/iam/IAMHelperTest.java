package pl.psnc.indigo.omt.iam;

import android.content.Context;
import android.content.SharedPreferences;
import junit.framework.Assert;
import net.openid.appauth.AuthState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

/**
 * Created by michalu on 27.03.17.
 */
public class IAMHelperTest {
    AuthState authState;
    @Mock SharedPreferences prefsOk;
    @Mock SharedPreferences.Editor editor;
    @Mock Context contextOk;

    @Mock SharedPreferences prefsBad;
    @Mock Context contextBad;

    @Before public void setup() {
        MockitoAnnotations.initMocks(this);
        authState = new AuthState();
        Mockito.when(contextOk.getSharedPreferences(anyString(), anyInt())).thenReturn(prefsOk);
        Mockito.when(prefsOk.getString(anyString(), anyString()))
            .thenReturn("{}");
        Mockito.when(prefsOk.edit()).thenReturn(editor);
        Mockito.when(editor.putString(anyString(), anyString())).thenReturn(editor);

        Mockito.when(contextBad.getSharedPreferences(anyString(), anyInt())).thenReturn(prefsBad);
        Mockito.when(prefsBad.getString(anyString(), anyString())).thenReturn(null);
    }

    @Test public void test_readAuthStateOk() throws Exception {
        AuthState authState = IAMHelper.readAuthState(contextOk);
        Assert.assertNotNull(authState);
    }

    @Test public void test_readAuthStateBad() throws Exception {
        AuthState authState = IAMHelper.readAuthState(contextBad);
        Assert.assertNotNull(authState);
    }

    @Test public void test_writeAuthState() throws Exception {
        IAMHelper.writeAuthState(authState, contextOk);

    }
}
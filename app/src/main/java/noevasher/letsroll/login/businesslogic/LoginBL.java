package noevasher.letsroll.login.businesslogic;

import android.content.Context;

import io.reactivex.Single;
import noevasher.letsroll.proxies.AuthProxy;

public class LoginBL {
    
    private AuthProxy mAuthProxy;
    
    public LoginBL(Context context){
        mAuthProxy = AuthProxy.getInstance(context);
    }
    public Single<Object> startSessionUser(String email, String password) {
        return mAuthProxy.startSessionUser(email, password);
    }
    
}

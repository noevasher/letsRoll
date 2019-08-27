package noevasher.letsroll.register.businessLogic;

import android.content.Context;

import io.reactivex.Single;
import noevasher.letsroll.proxies.AuthProxy;

public class RegisterBL {

    private AuthProxy mAuthProxy;

    public RegisterBL(Context context) {
        mAuthProxy = AuthProxy.getInstance(context);
    }

    public Single<Object> createAccount(String name, String email, int age, String password) {
        return mAuthProxy.createAccount(name, email, age, password);
    }

    public Single<Object> startSessionUser(String email, String password) {
        return mAuthProxy.startSessionUser(email, password);
    }
}

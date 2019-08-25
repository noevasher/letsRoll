package noevasher.letsroll.proxies;

import android.content.Context;

import com.google.firebase.FirebaseApp;

public abstract class AFirebase {

    final Context mContext;

    protected AFirebase(Context context) {
        this.mContext = context;
    }

    public FirebaseApp initializeApp() {
        return FirebaseApp.initializeApp(mContext);
    }


}

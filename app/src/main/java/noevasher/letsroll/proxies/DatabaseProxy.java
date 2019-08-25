package noevasher.letsroll.proxies;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import io.reactivex.Single;

public final class DatabaseProxy extends AFirebase {
    private static DatabaseProxy databaseProxy;
    private static boolean enabledPersistence = false;

    private DatabaseReference mDatabase;

    public DatabaseProxy(Context context) {
        super(context);
        //if (!enabledPersistence) enablePersistance();
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

    }

    public static DatabaseProxy getInstance(Context context) {
        if (databaseProxy == null)
            databaseProxy = new DatabaseProxy(context);
        //if (!enabledPersistence) enablePersistance();
        return databaseProxy;
    }


    public Single<Boolean> updateChildrenSingle(String path, Map<String, Object> value) {
        return Single.create(e -> {
            mDatabase.child(path).updateChildren(value).addOnSuccessListener(success -> {
                e.onSuccess(true);
            }).addOnFailureListener(e::onError);
        });
        //return Single.create(emitter -> getRootRef().subscribe(response-> response.child(path).updateChildren(value).addOnSuccessListener(success-> emitter.onSuccess(true)).addOnFailureListener(emitter::onError)));
    }

}
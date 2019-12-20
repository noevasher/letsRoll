package noevasher.letsroll.proxies;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Observable;

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
    }


    public io.reactivex.Observable<DataSnapshot> getInformation(String path) {
        return io.reactivex.Observable.create(e -> {
            mDatabase.child(path).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Post post = dataSnapshot.getValue(Post.class);
                    //System.out.println(post);
                    e.onNext(dataSnapshot);
                    e.onComplete();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                    e.onError(databaseError.toException());
                }
            });

        });
    }

}
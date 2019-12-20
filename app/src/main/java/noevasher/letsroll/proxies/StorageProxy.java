package noevasher.letsroll.proxies;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import io.reactivex.Single;

public class StorageProxy {

    private Context mcontext;

    private static StorageProxy instance;
    private StorageReference storageRef;

    private StorageProxy(Context context){
        this.mcontext = context;
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://letsroll-721e2.appspot.com");
        storageRef = storage.getReference();
    }

    public static StorageProxy getInstance(Context context){
        if(instance == null){
            instance = new StorageProxy(context);
        }
        return instance;
    }

    //example ref --> images/mountains.jpg
    //Sube archivos desde datos en la memoria
    public Single<Boolean> uploadImage(String ref, Bitmap bitmap){
        return Single.create(emitter -> {
            StorageReference imageRef = storageRef.child(ref);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(exception -> emitter.onError(exception))
                    .addOnSuccessListener(taskSnapshot -> emitter.onSuccess(true));

            //uploadTask = storageRef.child(ref).putFile(file, metadata);

        });

    }

    /*
    public void getURL (String ref, Bitmap bitmap){
        StorageReference imageRef = storageRef.child(ref);
        uploadTask = imageRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


        ///PROGRESS
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        });
    }
    //*/
}

package noevasher.letsroll.profile.controllers.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import noevasher.letsroll.R;
import noevasher.letsroll.commons.parents.fragments.BaseFragment;
import noevasher.letsroll.main.controllers.activities.OnSingleClickListener;
import noevasher.letsroll.models.UserModel;
import noevasher.letsroll.proxies.AuthProxy;
import noevasher.letsroll.proxies.DatabaseProxy;
import noevasher.letsroll.proxies.StorageProxy;
import noevasher.letsroll.services.Utilities;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class ProfileFragment extends BaseFragment {
    public static ProfileFragment profileFragment;
    private static final int PICK_IMAGE = 10;
    private static final int PERMISSIONS_REQUEST_CAMERA = 555;

    @BindView(R.id.imageView_addImage)
    public ImageView addImage;

    @BindView(R.id.imageView_editUserName)
    public ImageView editImage;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.imageView_profile)
    public de.hdodenhof.circleimageview.CircleImageView imageProfile;

    private StorageProxy storage;
    private DatabaseProxy mDataProxy;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false); //setContentView(R.layout.fragment_login);
        ButterKnife.bind(this, view);
        profileFragment = this;
        mDataProxy = DatabaseProxy.getInstance(getContext());
        addImage.setColorFilter(getContext().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        editImage.setColorFilter(getContext().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        addImage.setOnClickListener(imageListener);

        storage = StorageProxy.getInstance(getContext());
        return view;
    }

    private OnSingleClickListener imageListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            //progressBar.setVisibility(View.VISIBLE);
            showPictureDialog(progressBar);
        }
    };

    private void showPictureDialog(ProgressBar progressbar) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            choosePhotoFromGallary();
                            break;
                        case 1:
                            requestCameraPermission();
                            break;
                    }
                });
        pictureDialog.setOnDismissListener(dialogInterface -> {
            //progressbar.setVisibility(View.GONE);
        });

        pictureDialog.show();
    }


    public void takePhotoFromCamera() {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void requestCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        } else {
            //Do your work
            takePhotoFromCamera();
        }
    }


    private void choosePhotoFromGallary() {
        progressBar.setVisibility(View.VISIBLE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    imageProfile.setImageURI(data.getData());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    bitmap = compressImage(bitmap);
                    imageProfile.buildDrawingCache();
                    //String encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    loadToSave(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Fallo al cargar Imagen!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            thumbnail = compressImage(thumbnail);

            imageProfile.setImageBitmap(thumbnail);
            imageProfile.buildDrawingCache();
            String encodedImageData = getEncoded64ImageStringFromBitmap(thumbnail);
            loadToSave(thumbnail);
        }


    }

    private Bitmap compressImage(Bitmap original) {
        int ONE_MB = 1024;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap resized = Bitmap.createScaledBitmap(original, (int) (original.getWidth() * 0.8), (int) (original.getHeight() * 0.8), true);

        resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int realSize = baos.toByteArray().length;
        System.out.println("realSize: " + realSize);

        while (realSize / ONE_MB > ONE_MB) {
            baos.reset();
            resized = Bitmap.createScaledBitmap(resized, (int) (resized.getWidth() * 0.8), (int) (resized.getHeight() * 0.8), true);
            resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            realSize = baos.toByteArray().length;
            System.out.println("realSize: " + realSize);

        }

        return resized;
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }


    private void loadToSave(Bitmap bitmap) {

        StorageProxy storageProxy = StorageProxy.getInstance(getContext());
        AuthProxy authProxy = AuthProxy.getInstance(getContext());

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageProxy.uploadImage("users/" + userId + "/user.jpg", bitmap).subscribe(response -> {
            if (response) {
                progressBar.setVisibility(View.GONE);
                HashMap<String, Object> imageProfile = new HashMap<>();
                imageProfile.put("imageProfile", "user.jpg");
                authProxy.writeInformation("users/" + userId + "/summary/", imageProfile);
            }
        }, t -> {
            Utilities.showMessage(getContext(), t.getMessage(), true);
        });
    }


}

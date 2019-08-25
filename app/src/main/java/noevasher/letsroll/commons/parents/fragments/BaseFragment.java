package noevasher.letsroll.commons.parents.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Created by holmes on 20/11/17.
 */

public class BaseFragment extends Fragment implements BaseFragmentInterface {
    //private final ExtLogger mlog = ExtLogger.getLogger(BaseFragment.class);
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void customBackButton() {

    }

    public int getBundleSizeInBytes(Bundle bundle) {
        Parcel parcel = Parcel.obtain();
        int size;

        parcel.writeBundle(bundle);
        size = parcel.dataSize();
        parcel.recycle();

        return size;
    }

    /*
    public void handleFacebookAccessToken(AccessToken token) {
        RegisterAccountBL mAccountBL;
        mAccountBL = new RegisterAccountBL(getContext());
        DialogFragment dialogFragment = ProgressBarDialog.newInstance();
        dialogFragment.show(getActivity().getFragmentManager(), ProgressBarDialog.class.getSimpleName());
        mAccountBL.onFacebookAccessTokenChange(token).subscribe(response -> {
            if(response.equals("null")) {
                //Intent intent = new Intent(this, MotoFragment.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //String email = user.getEmail();
                //String userId = user.getUid();
                assert user != null;
                Uri url = user.getPhotoUrl();
                if(url != null) {
                    String facebookUserId = "";
                    // find the Facebook profile and get the user's id
                    for(UserInfo profile : user.getProviderData()) {
                        // check if the provider id matches "facebook.com"
                        if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                            facebookUserId = profile.getUid();
                        }
                    }
        
                    String urlPicture = String.format(getActivity().getApplicationContext().getString(R.string.facebook_photo_url), facebookUserId);//user.getPhotoUrl().toString();
        
                    final String[] tokenFireBase = new String[1];
                    //mEmailEngineProxy.sendEmail(userName, email).subscribe();
                    LocalConfigService localConfigService = LocalConfigService.getInstance();
                    String targetBkt = localConfigService.getString(Consts.TARGET_BKT);
                    String baseURL = localConfigService.getString(Consts.EMAIL_CREDENTIALS_URL);
                    user.getToken(true)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                tokenFireBase[0] = task.getResult().getToken();
                                // ...
                                try {
                                    EmailEngineProxy mEmailEngineProxy = new EmailEngineProxy(getContext(),
                                                                                              tokenFireBase[0], baseURL);
                                    mEmailEngineProxy.saveImageFB(urlPicture, targetBkt, "/").subscribe(r -> {
                                        mlog.info("end save image: " + r);
                                        Date dt = new Date();
                                        UserManager userManager = UserManager.getInstance(getContext());
                                        String timestamp = Long.toString(dt.getTime());
                                        userManager.updateProfilePicture(timestamp);
                                    }, t -> {
                                        t.printStackTrace();
                                        dialogFragment.dismiss();
                                    });
                                } catch(AException e) {
                                    dialogFragment.dismiss();
                                    e.printStackTrace();
                                }
                                dialogFragment.dismiss();
                            }
                        });
                }else{
                    dialogFragment.dismiss();
                    mlog.info("no image");
                }
            }
            else if(response instanceof FirebaseUser){
                dialogFragment.dismiss();
            } else if(response.equals("false")) {
                dialogFragment.dismiss();
                Toast.makeText(getActivity(), getActivity().getApplicationContext().getString(R.string.facebook_login_error), Toast.LENGTH_SHORT).show();
                getDialogAlertServiceFB(getActivity(), (dialog1, id) -> {});

                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                //mPreferences.edit().remove("session").commit();
            } else {
                dialogFragment.dismiss();
                requestPassword((String) response, token);
                dialogFragment.dismiss();
            }
        });
    }

    private void requestPassword(String email, AccessToken token) {
        mlog.info("requestPassword...");
        // inflater.inflate(R.layout.custom_button, mLinearLayout, true);

        View view = getView(getActivity(), R.layout.fragment_request_password);
        EditText editor = view.findViewById(R.id.editText_request_password);
        TextInputLayout text = view.findViewById(R.id.textInput_request_text);
        
        TextView textWarning = view.findViewById(R.id.textView_warning);
        String warning = getString(R.string.dialog_request_password_link_account_msg, email);
        mlog.info("warning : " + warning);
        textWarning.setText(warning);
        
        mlog.info("editor_: " + editor);
        mlog.info("text " + text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        builder.setPositiveButton(R.string.accept_button, (dialog, which) -> {
        });

        builder.setNegativeButton(R.string.cancel_button, (dialog, which)->{
            getAvailableActivity(new IActivityEnabledListener() {
                @Override
                public void onActivityEnabled(FragmentActivity activity) {
                    Toast.makeText(getContext(), activity.getApplicationContext().getString(R.string.alert_link_account_fb), Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                    dialog.dismiss();
                }
            });
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorDialogNegativeText));
            button.setOnClickListener(view1 -> {
                String password = editor.getText().toString();
                try{
                    if(!password.isEmpty()) {
                        linkAccount(email, password, dialog, token);
                    }else{
                        getAvailableActivity(new IActivityEnabledListener() {
                            @Override
                            public void onActivityEnabled(FragmentActivity activity) {
                                Toast.makeText(view1.getContext(), activity.getApplicationContext().getString(R.string.update_invalid_password), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch(AException e) {
                    e.printStackTrace();
                }
            });
        });
        dialog.show();
    }


    private void linkAccount(String email, String password, AlertDialog dialog, AccessToken token) throws AException {
        mlog.info("linkAccount...");
        RegisterAccountBL mAccountBL = new RegisterAccountBL(getContext());
        mAccountBL.startSession(email, password).subscribe(response -> {
            mlog.info("response linkAccount: " + response);
            if(response) {
                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                mAccountBL.linkAccount(credential).subscribe(link -> {
                    mlog.info("link: " + link);
                    if(link) {
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), getActivity().getApplicationContext().getString(R.string.update_invalid_password), Toast.LENGTH_SHORT).show();
                        getDialogAlertLinkFB(getActivity(), (dialog1, id) -> {

                        });
                    }
                });
            } else {
                Toast.makeText(getContext(), getActivity().getApplicationContext().getResources().getString(R.string.start_fail_login), Toast.LENGTH_SHORT)
                     .show();
                LoginManager.getInstance().logOut();
            }
        });
    }
//*/
    private View getView(Activity activity, @LayoutRes int resource) {
        //LayoutInflater inflater = activity.getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        return inflater.inflate(resource, null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        int osVersion = android.os.Build.VERSION.SDK_INT;
        if (osVersion < Build.VERSION_CODES.LOLLIPOP) {
            super.onSaveInstanceState(outState);
        }
    }


    private IActivityEnabledListener aeListener;

    protected interface IActivityEnabledListener {
        void onActivityEnabled(FragmentActivity activity);
    }

    protected void getAvailableActivity(IActivityEnabledListener listener) {
        if (getActivity() == null) {
            aeListener = listener;

        } else {
            listener.onActivityEnabled(getActivity());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (aeListener != null) {
            aeListener.onActivityEnabled((FragmentActivity) activity);
            aeListener = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (aeListener != null) {
            aeListener.onActivityEnabled((FragmentActivity) context);
            aeListener = null;
        }
    }

}

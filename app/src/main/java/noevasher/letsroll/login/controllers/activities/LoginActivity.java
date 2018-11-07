package noevasher.letsroll.login.controllers.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import noevasher.letsroll.R;
import noevasher.letsroll.login.businesslogic.LoginBL;
import noevasher.letsroll.main.controllers.activities.MainActivity;
import noevasher.letsroll.main.controllers.activities.MainActivity_;
import noevasher.letsroll.proxies.AuthProxy;
import noevasher.letsroll.register.controllers.activities.RegisterActivity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[] {
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    @BindView(R.id.startSessionButon)
    public Button startSessionButton;
    
    @BindView(R.id.registerButton)
    public Button registerSessionButton;
    
    @OnClick(R.id.registerButton)
    public void registerBtn(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    
    /*
    @OnClick(R.id.startSessionButon)
    public void startBtn(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //*/
    
    @BindView(R.id.editText_email_login)
    public EditText emailE;
    
    @BindView(R.id.editText_password_login)
    public EditText passwordE;
    
    private AuthProxy mAuthProxy;
    private LoginBL loginBL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        //Intent mainIntent = new Intent(this, MainActivity.class);
        Intent mainIntent = new Intent(this, MainActivity_.class);
        loginBL = new LoginBL(getApplicationContext());
        
        mAuthProxy = AuthProxy.getInstance(getApplicationContext());
        mAuthProxy.getFirebaseUser().subscribe(user -> {
            if(!user.equals("null")) {// user not registered
                startActivity(mainIntent);
                finish();
            }else{
                Toast.makeText(this, "user registrado", Toast.LENGTH_LONG);
            }
            /*
            if (!user1.equals("null")){
                FirebaseUser user = (FirebaseUser) user1;
                if (!user.isAnonymous()){
                    /*
                    intent[1].putExtra("auth", true);
                    startActivity(intent[1]);
                    finish();
                    //*/
                //}else{
                    /*
                    singleInit[0].subscribe(response -> {
                        mlog.info("response singIn: " + response);
                        intent[0].putExtra("auth", (Boolean) response);
                        startActivity(intent[1]);
                        startActivity(intent[0]);
                    
                        finish();
                    }, t -> {
                        mlog.info("auth: " + false);
                        mlog.error(((Throwable) t).getMessage());
                        intent[0].putExtra("auth", false);
                        startActivity(intent[0]);
                        finish();
                    });
                    //*/
                    /*
                }
            }else {
            
            }
            //*/
        });
        //FirebaseApp.initializeApp(getApplicationContext());
    
        // Set up the login form.
    
        startSessionButton.setOnClickListener(l ->{
            String email = emailE.getText().toString();
            String password = passwordE.getText().toString();
            
            loginBL.startSessionUser(email, password).subscribe(response ->{
                if(response instanceof FirebaseUser){
                    startActivity(mainIntent);
                    finish();
        
                } else if(response.equals("false")) {
                    //dialogFragment.dismiss();
                    Toast.makeText(this, getString(R.string.start_fail_login), Toast.LENGTH_SHORT)
                         .show();
        
                } else {
                    //dialogFragment.dismiss();
                    Toast.makeText(this, getString(R.string.account_with_facebook), Toast.LENGTH_SHORT).show();
                }
            });
        });
        
    }

    
}


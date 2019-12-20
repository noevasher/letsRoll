package noevasher.letsroll.login.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import noevasher.letsroll.R;
import noevasher.letsroll.login.businesslogic.LoginBL;
import noevasher.letsroll.main.controllers.activities.MainActivity;
import noevasher.letsroll.main.controllers.activities.ParentActivity;
import noevasher.letsroll.register.controllers.activities.RegisterActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ParentActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    @BindView(R.id.startSessionButon)
    public Button startSessionButton;

    @BindView(R.id.registerButton)
    public Button registerSessionButton;

    @OnClick(R.id.registerButton)
    public void registerBtn() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @BindView(R.id.editText_email_login)
    public EditText emailE;

    @BindView(R.id.editText_password_login)
    public EditText passwordE;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    private LoginBL loginBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //FirebaseApp.initializeApp(this);
        //Intent mainIntent = new Intent(this, MainActivity.class);
        Intent mainIntent = new Intent(this, MainActivity.class);
        loginBL = new LoginBL(getApplicationContext());

        emailE.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                if (emailE.getText().toString().isEmpty()) {
                    emailE.setError(getString(R.string.field_required));
                } else {
                    if (!isValidEmail(emailE.getText().toString())) {
                        emailE.setError(getString(R.string.invalid_format));
                    }
                }
            }
        });
        passwordE.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                if (passwordE.getText().toString().isEmpty()) {
                    passwordE.setError(getString(R.string.field_required));
                } else {
                    if (!isValidLength(passwordE.getText().toString())) {
                        //passwordEditText.setError("Longitud de Contraseña debe ser mayor a 5 caracteres");
                    }
                }
            }
        });

        passwordE.setOnEditorActionListener((v, actionId, event) -> false);


        mAuthProxy.getFirebaseUser().subscribe(user -> {
            if (!user.equals("null")) {// user not registered
                startActivity(mainIntent);
                finish();
            } else {
                Toast.makeText(this, getString(R.string.error_registered_user), Toast.LENGTH_LONG);
            }

        });

        startSessionButton.setOnClickListener(l -> {
            String email = emailE.getText().toString();
            String password = passwordE.getText().toString();
            if(!email.isEmpty() && !password.isEmpty() && isValidEmail(emailE.getText().toString())) {
                progressBar.setVisibility(View.VISIBLE);
                loginBL.startSessionUser(email, password).subscribe(response -> {
                    if (response instanceof FirebaseUser) {
                        progressBar.setVisibility(View.GONE);
                        startActivity(mainIntent);
                        sessionManager.createLoginSession(email, ((FirebaseUser) response).getUid());
                        finish();

                    } else if (response.equals("false")) {
                        Toast.makeText(this, getString(R.string.start_fail_login), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    } else {
                        //dialogFragment.dismiss();
                        Toast.makeText(this, getString(R.string.account_with_facebook), Toast.LENGTH_SHORT).show();
                    }
                },t ->{
                    Toast.makeText(this, getString(R.string.start_fail_login), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                });
            }else{
                displayEmptyField(emailE);
                displayEmptyField(passwordE);

            }
        });

    }


}


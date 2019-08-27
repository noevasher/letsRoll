package noevasher.letsroll.main.controllers.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import noevasher.letsroll.R;
import noevasher.letsroll.proxies.AuthProxy;

public class ParentActivity extends AppCompatActivity {
    private static final int WORD_LENGTH = 6;
    protected AuthProxy mAuthProxy;
    protected Toolbar toolbar;
    private static String fieldRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthProxy = AuthProxy.getInstance(getApplicationContext());

        fieldRequired = getString(R.string.field_required);
    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidLength(String target) {
        return target.length() >= WORD_LENGTH;
    }

    public static void displayEmptyField(Spinner spinner) {
        if (spinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Selecciona una edad");//changes the selected item text to this
        }
    }

    public static void displayEmptyField(EditText editText) {
        if (editText.getText().toString().isEmpty())

            editText.setError(fieldRequired);
        //return editText.getText().toString().isEmpty();
    }

    public void validPassword(EditText editText) {
        boolean isValid = false;

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                if (password.trim().isEmpty()) {
                    editText.setError(getResources().getString(R.string.account_invalid_require_field));
                } else if (validatePassword(password).equals("false")) {
                    editText.setError(getResources().getString(R.string.account_invalid_password));
                    /*
                    } else if(validatePassword().equals("repeat") && flag_passr_focus) {
                        textInputLayout.setError(null);
                        textInputLayout.setErrorEnabled(false);
            /*textInputLayout = findViewById(R.id.textInputLayout_account_passwordR);
            textInputLayout.setError(passwordMatch);*/
                } else {
                    editText.setError(null);
                    //editText.setErrorEnabled(false);
                }
            }

        });
    }

    private String validatePassword(String password) {
        //boolean isAlphaNumeric = !StringUtils.isAlphanumeric(password);
        boolean isAlphaNumeric = isAlphaNumeric(password);

        if (password.length() < 6 || !isAlphaNumeric) {
            return "false";
        }
        /*
        if(!validatePasswordR()) {
            return "repeat";
        }
//*/
        return "true";
    }


    private boolean isAlphaNumeric(String string) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public void configToolBar(Toolbar toolbar, String title) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (title != null)
                getSupportActionBar().setTitle(title);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(getDrawable(R.drawable.ic_lr_arrow_back_white));
            toolbar.setNavigationOnClickListener(v -> {
                finish();
                //overridePendingTransition(10, 30);
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setToolbar(Toolbar toolbar, String title, boolean hasArrow) {
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
        }

        if (hasArrow) {
            setArrowToolbar(toolbar);
        }
    }

    public void setArrowToolbar(Toolbar toolbar) {
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_lr_arrow_back_white);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

    }

    public static boolean isEmptyField(EditText editText) {
        return editText.getText().toString().isEmpty();
        //return editText.getText().toString().isEmpty();
    }

    public static boolean comparePassword(EditText password, EditText passwordConfirm) {
        if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
            passwordConfirm.setError("La contraseña debe de coincidir");
            return false;
        } else {
            return true;
        }
    }

}

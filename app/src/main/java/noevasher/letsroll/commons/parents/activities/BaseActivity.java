package noevasher.letsroll.commons.parents.activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import noevasher.letsroll.R;

public class BaseActivity extends AppCompatActivity {

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
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_lr_arrow_back_white));
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
}

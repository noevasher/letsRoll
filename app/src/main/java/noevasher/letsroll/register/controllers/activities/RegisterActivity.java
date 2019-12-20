package noevasher.letsroll.register.controllers.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import noevasher.letsroll.R;
import noevasher.letsroll.main.controllers.activities.MainActivity;
import noevasher.letsroll.main.controllers.activities.ParentActivity;
import noevasher.letsroll.register.businessLogic.RegisterBL;

public class RegisterActivity extends ParentActivity {

    @BindView(R.id.editText_email_register)
    public EditText email;

    @BindView(R.id.editText_username_register)
    public EditText userName;

    @BindView(R.id.editText_password_register)
    public EditText password;

    @BindView(R.id.editText_password_register_repeat)
    public EditText passwordRepeat;

    @BindView(R.id.spinner_age)
    public Spinner age;

    @BindView(R.id.button_register)
    public Button registerBtn;

    private int ageData = 0;
    private RegisterBL registerBL;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setItemsAgeSpinner();
        FirebaseApp.initializeApp(this);

        registerBL = new RegisterBL(getApplicationContext());
        registerBtn.setOnClickListener(l -> {
            String usernameE = userName.getText().toString();
            String emailE = email.getText().toString();
            String passwordE = password.getText().toString();
            //String genderE = gender.get.getText().toString();
            progressBar.setVisibility(View.VISIBLE);

            if(ifValidForm()) {
                registerBL.createAccount(usernameE, emailE, ageData, passwordE).subscribe(response -> {
                    System.out.println("usuario guardado");
                    if (response instanceof FirebaseUser) {
                        registerBL.startSessionUser(emailE, passwordE).subscribe(init -> {
                            progressBar.setVisibility(View.GONE);
                            Intent returnIntent = new Intent(getApplication(), MainActivity.class);
                            startActivity(returnIntent);
                            finish();
                        });
                    }
                    else if(response.equals("duplicated")){
                        Toast.makeText(RegisterActivity.this, getString(R.string.duplicated_user_error), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    }
                },t->{
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_error), Toast.LENGTH_LONG).show();
                });
            }else{
                progressBar.setVisibility(View.GONE);
            }

        });

        setToolbar(toolbar, "Registrar Usuario", true);
        validEmptyFields();
    }

    private void setItemsAgeSpinner() {
        ArrayList<String> ageList = new ArrayList<>();
        ageList.add("Edad: ");
        for (int i = 15; i < 100; i++) {
            ageList.add(i + "  años");
        }

        //final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, ageList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        age.setAdapter(spinnerArrayAdapter);

        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    ageData = position + 15;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean ifValidForm() {
        displayEmptyField(userName);
        displayEmptyField(email);
        displayEmptyField(password);
        displayEmptyField(passwordRepeat);
        displayEmptyField(age);

        validEmptyFields();
        comparePassword(password, passwordRepeat);

        return !isEmptyField(userName) && !isEmptyField(email) && !isEmptyField(password)
                && !isEmptyField(passwordRepeat) && isValidSpinner(age)
                && comparePassword(password, passwordRepeat);

    }

    private boolean isValidSpinner(Spinner spinner){
        return spinner.getSelectedItemPosition() != 0;
    }

    private void validEmptyFields(){
        userName.setOnFocusChangeListener((view, focus) -> {
            if(!focus){
                displayEmptyField(userName);

            }
        });
        email.setOnFocusChangeListener((view, focus) -> {
            if(!focus){
                displayEmptyField(email);
                if(!isValidEmail(email.getText().toString())){
                    email.setError(getString(R.string.invalid_format));
                }
            }
        });

        password.setOnFocusChangeListener((view, focus) -> {
            if(!focus){
                displayEmptyField(password);
                if(!isValidLength(password.getText().toString())){
                    password.setError("Contraseña debe de tener 6 o más caracteres");
                }
            }
        });
        passwordRepeat.setOnFocusChangeListener((view, focus) -> {
            if(!focus){
                displayEmptyField(passwordRepeat);

                if(!isValidLength(passwordRepeat.getText().toString())){
                    passwordRepeat.setError("Contraseña debe de tener 6 o más caracteres");
                }else {
                    comparePassword(password, passwordRepeat);
                }
            }
        });

    }


}

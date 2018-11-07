package noevasher.letsroll.register.controllers.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import noevasher.letsroll.R;
import noevasher.letsroll.main.controllers.activities.MainActivity;
import noevasher.letsroll.proxies.DatabaseProxy;
import noevasher.letsroll.register.businessLogic.RegisterBL;

public class RegisterActivity extends AppCompatActivity {
    
    @BindView(R.id.editText_email_register)
    public EditText email;
    
    @BindView(R.id.editText_username_register)
    public EditText userName;
    
    @BindView(R.id.editText_password_register)
    public EditText password;
    
    @BindView(R.id.editText_password_register_repeat)
    public EditText passwordRepeat;
    
    @BindView(R.id.spinner_gender)
    public Spinner gender;
    
    @BindView(R.id.spinner_age)
    public Spinner age;
    
    @BindView(R.id.button_register)
    public Button registerBtn;
    
    private String genderData;
    private int ageData = 0;
    private RegisterBL registerBL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setItemsGenderSpinner();
        setItemsAgeSpinner();
        FirebaseApp.initializeApp(this);
    
        registerBL = new RegisterBL(getApplicationContext());
        registerBtn.setOnClickListener(l ->{
            String usernameE = userName.getText().toString();
            String emailE = email.getText().toString();
            String passwordE = password.getText().toString();
            //String genderE = gender.get.getText().toString();
    
            registerBL.createAccount(usernameE,emailE,genderData,ageData, passwordE).subscribe(response ->{
                System.out.println("usuario guardado");
                if(response instanceof FirebaseUser) {
                    registerBL.startSessionUser(emailE, passwordE).subscribe(init -> {
                        Intent returnIntent = new Intent(getApplication(), MainActivity.class);
                        startActivity(returnIntent);
                        //returnIntent.putExtra("result", "close");
                        //setResult(Activity.RESULT_OK, returnIntent);
                        //dialogFragment.dismiss();
                        finish();
                    });
                }
            });
        });
    }
    
    private void setItemsGenderSpinner(){
        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_gender);
    
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.gender_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                         .show();
                    switch(position){
                        case 1:
                            genderData = "masculino";
                        case 2:
                            genderData = "femenino";
                            
                    }
                }
            }
        
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            
            }
        });
    }
    
    private void setItemsAgeSpinner(){
        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_age);
        ArrayList<String> plantsList = new ArrayList<>();
        plantsList.add("Edad: ");
        for(int i = 15; i < 100; i++){
            plantsList.add(i+"");
        }
        
        //final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));
        
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
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
}

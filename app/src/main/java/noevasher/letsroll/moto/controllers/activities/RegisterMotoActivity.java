package noevasher.letsroll.moto.controllers.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import noevasher.letsroll.R;
import noevasher.letsroll.commons.parents.activities.BaseActivity;
import noevasher.letsroll.models.BrandModel;
import noevasher.letsroll.models.MotorcycleModel;

public class RegisterMotoActivity extends BaseActivity {
    @BindView(R.id.spinner_brand)
    public Spinner brandSpinner;

    @BindView(R.id.spinner_model)
    public Spinner modelSpinner;

    @BindView(R.id.editText_alias)
    public EditText aliasText;

    private static final String BRANDS_FILE = "moto_brands.json";
    private static final String MODELS_FILE = "moto_models.json";

    //@BindView(R.id.toolbar_registerMoto)
    public Toolbar toolbar;

    @BindView(R.id.button_register_moto)
    public Button registerBtn;

    @OnClick(R.id.button_register_moto)
    public void saveMoto() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_moto);
        ButterKnife.bind(this);

        setItemsAgeSpinner();
        toolbar = findViewById(R.id.toolbar);
        configToolBar(toolbar, getResources().getString(R.string.register_motocycler));
        setEventInput();
        //validName();
    }

    private Gson gson = new Gson();

    private void setItemsAgeSpinner() {
        // Get reference of widgets from XML layout
        brandSpinner = findViewById(R.id.spinner_brand);
        modelSpinner = findViewById(R.id.spinner_model);
        modelSpinner.setEnabled(false);

        ArrayList brandList = getDataList(BRANDS_FILE);
        ArrayList brandsName = new ArrayList();
        brandsName.add(getResources().getString(R.string.register_brand));
        for (Object obj : brandList) {
            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) obj;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.equals("name")) {
                    brandsName.add(value);
                }
            }
        }

        //final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, brandsName) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    //((TextView)brandSpinner.getChildAt(0)).setError("Message");
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
        brandSpinner.setAdapter(spinnerArrayAdapter);

        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //int selectedItemText = (int) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();

                    HashMap<String, Object> modelList = getModelList(MODELS_FILE);
                    ArrayList modelsName = new ArrayList();
                    String model_ = getResources().getString(R.string.register_model);

                    //for(Object obj: modelList){
                    for (String key : modelList.keySet()) {
                        // ...
                        //System.out.println(key + " => " + value);
                        System.out.println("obj --> " + key);
                        if (key.equals("brand_id_" + position)) {
                            ArrayList list = (ArrayList) modelList.get("brand_id_" + position);
                            for (Object map_ : list) {
                                LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) map_;
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    String key_ = entry.getKey();
                                    Object value = entry.getValue();
                                    System.out.println("datos:" + key_ + " => " + value);
                                    if (key_.equals("name")) {
                                        modelsName.add(value);
                                    }
                                }
                            }

                        }

                    }
                    //*/
                    Collections.sort(modelsName, String.CASE_INSENSITIVE_ORDER);
                    modelsName.add(0, model_);
                    modelSpinner.setEnabled(true);
                    setModelSpinnerData(modelsName);
                    //ageData = position + 15;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*/
    }

    private ArrayList getDataList(String fileName) {
        Reader reader = null;
        try {
            InputStream file = getApplicationContext().getResources()
                    .getAssets()
                    .open(fileName, Context.MODE_PRIVATE);
            reader = new InputStreamReader(file, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BrandModel data = gson.fromJson(reader, BrandModel.class); // contains the whole reviews list
        ArrayList dataList = data.getBrands();
        return dataList;
    }

    private void setModelSpinnerData(ArrayList modelsName) {
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, modelsName) {
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
        modelSpinner.setAdapter(spinnerArrayAdapter);

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //int selectedItemText = (int) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    /*
                    ArrayList modelList = getModelList(MODELS_FILE);
                    ArrayList modelsName = new ArrayList();
                    modelsName.add(getResources().getString(R.string.register_model));
                
                    for(Object obj: modelList){
                        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>)obj;
                        for(Map.Entry<String,Object> entry : map.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            if(key.equals("name")){
                                modelsName.add(value);
                            }
                            System.out.println(key + " => " + value);
                        }
                    
                        System.out.println("obj: " + map);
                        String jsonInString = gson.toJson(obj);
                        System.out.println("jsonInString: " + jsonInString);
                    
                    }
                
                    modelSpinner.setEnabled(true);
                
                    //ageData = position + 15;
                    //*/
                } else {
                    //modelSpinner.set
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private HashMap getModelList(String fileName) {
        Reader reader = null;
        try {
            InputStream file = getApplicationContext().getResources()
                    .getAssets()
                    .open(fileName, Context.MODE_PRIVATE);
            reader = new InputStreamReader(file, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MotorcycleModel data = gson.fromJson(reader, MotorcycleModel.class);
        HashMap dataList = data.getModels();
        System.out.println("data model: " + data.getModels());
        return dataList;

    }


    private void setEventInput() {
        String invalidEmail = getResources().getString(R.string.invalid_email);
        String requiredField = getResources().getString(R.string.account_invalid_require_field);

        aliasText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validEmail();
                //hideKeyboard(v);
                //flag_email = true;
            }
        });

    }

    private void validEmail() {

        if (aliasText.getText().toString().trim().isEmpty()) {
            aliasText.setError(getResources().getString(R.string.account_invalid_require_field));
        } else if (!validateEmail()) {
            aliasText.setError(getResources().getString(R.string.account_invalid_email));
        } else {
            aliasText.setError(null);
            //aliasText.setErrorEnabled(false);
        }
    }

    private boolean validateEmail() {
        String emailString = aliasText.getText().toString().trim();
        if (emailString.isEmpty() || !isValidEmail(emailString)) {
            setEnabledSendButton(false);
            return false;
        }
        return true;
    }

    protected boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setEnabledSendButton(Boolean flag) {
        if (!flag) {
            registerBtn.setEnabled(false);
            aliasText.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
        } else {
            registerBtn.setEnabled(true);
        }
    }


}

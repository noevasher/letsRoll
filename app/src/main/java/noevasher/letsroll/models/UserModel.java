package noevasher.letsroll.models;

import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;

import java.sql.Timestamp;
import java.util.HashMap;

public class UserModel {
    
    
    private String email;
    private String userName;
    private String gender;
    private int age;
    private String userId;
    
    private HashMap<String, Object> summary;
    private HashMap<String, Object> general;
    
    public UserModel(){
    
    }
    
    public UserModel(String email, String userName, String gender, int age){
        this.email = email;
        this.userName = userName;
        this.gender = gender;
        this.age = age;
    }
    
    public UserModel(HashMap<String, Object> summary, HashMap<String, Object> general){
        this.summary = summary;
        this.general = general;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public HashMap<String, Object> getSummary() {
        return summary;
    }
    
    public void setSummary(HashMap<String, Object> summary) {
        this.summary = summary;
    }
    
    public HashMap<String, Object> getGeneral() {
        return general;
    }
    
    public void setGeneral(HashMap<String, Object> general) {
        this.general = general;
    }
}

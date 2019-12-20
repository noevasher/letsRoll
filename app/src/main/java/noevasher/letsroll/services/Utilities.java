package noevasher.letsroll.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utilities {

    public static void showMessage(Context context, String message, boolean isError){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        if(isError)
            logError(message);
        else{
            Log.i("INFO", message);
        }
    }

    public static void logError(String message){
        Log.e("ERROR", message);
    }
}

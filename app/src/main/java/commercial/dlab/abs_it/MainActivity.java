package commercial.dlab.abs_it;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import commercial.dlab.abs_it.Adapter.Grid_Adapter;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 99;
    public GridView gridLayout;
    int[] user_pic=new int[]{
        R.drawable.ic_smile,
        R.drawable.ic_cool,
        R.drawable.ic_neutral,
        R.drawable.ic_happy,
    };
    String[] user_name=new String[]{
            "Pratik",
            "Mr. Saurbh",
            "Vikash Kumar",
            "Adil Qureshi",
            "Neha",
            "Pooja Maam",
            "Megha Rathi",
            "Pooja Maam",
            "Prakash",
            "Omega Classes",
            "Pest Control"
    };
    String[] user_location=new String[]{
            "Ganganagar",
            "Coimbatore",
            "Madurai",
            "Porbandar",
            "Mahesana",
            "Navsari",
            "Jamnagar",
            "Bongaigaon",
            "Ramanathapuram",
            "Firozpur",
            "Valsad"
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout = findViewById(R.id.grid_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        offerRequestPermissions();
        offerReplacingDefaultDialer();
        Grid_Adapter grid_adapter=new Grid_Adapter(MainActivity.this,user_name,user_location,user_pic);
        gridLayout.setAdapter(grid_adapter);
        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeCall();
            }
        });
    }
    private void makeCall() {
        if (ContextCompat.checkSelfPermission(this, CALL_PHONE) == PERMISSION_GRANTED) {
            Uri uri = Uri.parse("tel:7869927919");
            startActivity(new Intent(Intent.ACTION_CALL, uri));
        }
    }
    private void offerRequestPermissions() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, CALL_PHONE)!= PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{CALL_PHONE},REQUEST_CALL);
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,CALL_PHONE)){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{CALL_PHONE},REQUEST_CALL);
        }else
            Toast.makeText(this, "Permission Granted 1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]== PERMISSION_GRANTED){
            }
        }else
            offerRequestPermissions();
    }

    private void offerReplacingDefaultDialer() {
        TelecomManager telecomManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);

            if (!getPackageName().equals(telecomManager.getDefaultDialerPackage())) {
                Intent intent = new Intent(ACTION_CHANGE_DEFAULT_DIALER)
                        .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
                startActivity(intent);
            }
        }

    }
}
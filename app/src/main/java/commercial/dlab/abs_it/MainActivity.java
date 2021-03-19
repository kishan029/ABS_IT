package commercial.dlab.abs_it;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import commercial.dlab.abs_it.Adapter.Grid_Adapter;
import commercial.dlab.abs_it.Adapter.onGridViewClick;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 99;
    public GridView gridLayout;
    public DatabaseReference mDatabase;
    List<UserData> refData;

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
    public int posiition;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout = findViewById(R.id.grid_layout);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData");
        Log.w("Dataa",""+mDatabase.child("0"));
        refData=new ArrayList();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        offerRequestPermissions();
        offerReplacingDefaultDialer();
        getUserData();
        Grid_Adapter grid_adapter=new Grid_Adapter(MainActivity.this,user_name,user_location,user_pic);
        gridLayout.setAdapter(grid_adapter);
        grid_adapter.setOnItemClickListener(new onGridViewClick() {
            @Override
            public void onClick(int itemPosition) {
                posiition=itemPosition;
                makeCall();
            }
        });
    }

    private void getUserData() {
        //mDatabase.child("0").child("Cities").setValue("Indore");
        mDatabase.child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //refData.add(userData);
                Log.w("Data",""+ snapshot.getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Data",error.getMessage());
            }
        });

    }

    public void makeCall() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, CALL_PHONE) == PERMISSION_GRANTED) {

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
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onStart() {
        super.onStart();
        getUserData();
    }

}
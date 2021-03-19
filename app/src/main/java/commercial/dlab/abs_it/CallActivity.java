package commercial.dlab.abs_it;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.SyncStateContract;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import timber.log.Timber;

public class CallActivity extends AppCompatActivity {
    public CompositeDisposable disposables;
    public String number;
    public OnGoingCall ongoingCall;
    List<Integer> callStatus=new ArrayList<>();
    public Button hangup,answer,speaker;
    TextView status,username,user_location;
    Chronometer timertxt;
    public boolean onCall=false;
    public CountDownTimer timer;
    String timerValue;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ongoingCall = new OnGoingCall();
        disposables = new CompositeDisposable();
        callStatus.add(Call.STATE_DIALING);
        callStatus.add(Call.STATE_RINGING);
        callStatus.add(Call.STATE_ACTIVE);
        hangup=findViewById(R.id.hangup);
        answer=findViewById(R.id.answer);
        speaker=findViewById(R.id.speaker);
        status=findViewById(R.id.status);
        timertxt=findViewById(R.id.timer);
        username=findViewById(R.id.username);
        user_location=findViewById(R.id.user_location);

        number = Objects.requireNonNull(getIntent().getData()).getSchemeSpecificPart();

        hangup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(onCall){
                    stopTimer();
                }
                ongoingCall.hangup();
            }
        });
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ongoingCall.answer();
                timertxt.setVisibility(View.VISIBLE);
                startTimer();
            }
        });
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ongoingCall.speaker(CallActivity.this);
            }
        });
        Bundle bundle=getIntent().getExtras();
        username.setText("Kishan"+bundle.getString("name"));
        user_location.setText(getIntent().getExtras().getString("location"));
    }

    private void stopTimer() {
        timertxt.stop();
    }

    private void startTimer() {
        timertxt.start();
        //Log.w("Timer",""+timertxt.getText());
        onCall=true;
    }


    @Override
    protected void onStart() {
        super.onStart();
       // assert updateUi(-1) != null;
        disposables.add(
                OnGoingCall.state
                        .subscribe(new Consumer<Integer>() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void accept(Integer integer) throws Exception {
                                updateUi(integer);
                            }
                        }));
        disposables.add(
                OnGoingCall.state
                        .filter(new Predicate<Integer>() {
                            @Override
                            public boolean test(Integer integer) throws Exception {
                                return integer == Call.STATE_DISCONNECTED;
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .firstElement()
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                finish();
                            }
                        }));
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private Consumer<? super Integer> updateUi(Integer state) {

        //callInfo.setText(Constants.asString(state) + "\n" + number);

        if (state != Call.STATE_RINGING) {

        }

        if (callStatus.contains(state)) {
            status.setText(Constants.asString(state));
            hangup.setVisibility(View.VISIBLE);
        } else;
            //hangup.setVisibility(View.GONE);

        return null;
    }

    private static class Constants {
        public static String asString(int data) {
            String value;
            switch (data) {
                case 0:
                    value = "NEW";
                    break;
                case 1:
                    value = "DIALING";
                    break;
                case 2:
                    value = "RINGING";
                    break;
                case 3:
                    value = "HOLDING";
                    break;
                case 4:
                    value = "ACTIVE";
                    break;
                case 7:
                    value = "DISCONNECTED";
                    break;
                case 8:
                    value = "SELECT_PHONE_ACCOUNT";
                    break;
                case 9:
                    value = "CONNECTING";
                    break;
                case 10:
                    value = "DISCONNECTING";
                    break;
                default:
                    Timber.w("Unknown state %s", data);
                    value = "UNKNOWN";
                    break;
            }
            return value;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void start(Context context, Call call) {
        Intent intent = new Intent(context, CallActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.getDetails().getHandle());
        context.startActivity(intent);
    }
}
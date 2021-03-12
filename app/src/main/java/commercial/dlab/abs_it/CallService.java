package commercial.dlab.abs_it;

import android.os.Build;
import android.telecom.Call;
import android.telecom.InCallService;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CallService extends InCallService {


    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        new OnGoingCall().setCall(call);
        CallActivity.start(this, call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        new OnGoingCall().setCall(null);
    }
}

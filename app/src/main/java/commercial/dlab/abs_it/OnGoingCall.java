package commercial.dlab.abs_it;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.telecom.Call;
import android.telecom.VideoProfile;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import io.reactivex.subjects.BehaviorSubject;

import static android.content.Context.AUDIO_SERVICE;

public class OnGoingCall {
    public static BehaviorSubject<Integer> state = BehaviorSubject.create();
    private static Call call;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private Object callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            state.onNext(newState);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    public final void setCall(@Nullable Call value) {
        if (call != null) {
            call.unregisterCallback((Call.Callback)callback);
        }

        if (value != null) {
            value.registerCallback((Call.Callback)callback);
            state.onNext(value.getState());
        }

        call = value;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void answer() {
        assert call != null;
        call.answer(VideoProfile.STATE_AUDIO_ONLY);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hangup() {
        assert call != null;
        call.disconnect();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void speaker(Context context ) {
        assert call != null;
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setSpeakerphoneOn(!audioManager.isSpeakerphoneOn());

    }

}

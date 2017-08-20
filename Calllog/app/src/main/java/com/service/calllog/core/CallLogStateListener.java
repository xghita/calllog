package com.service.calllog.core;

/**
 * Created by ghitaistrate on 31/07/2017.
 */

import android.content.Context;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.service.calllog.ui.MainActivity;

public class CallLogStateListener extends PhoneStateListener {

    //    private static final String TAG = "CallLogStateListener";

    private QueryCallLog queryCallLog;
    CallLogPrefs prefs;


    public CallLogStateListener(QueryCallLog queryCallLog, Context context) {
        super();
        Log.d("xtag", "listener_init");
        prefs = new CallLogPrefs(context);
        this.queryCallLog = queryCallLog;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        Log.d("xtag", "listener_state_change");
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                //when Idle i.e no call
                Log.d("xtag", "listener_idle");

                if(prefs.getWasRinging()){
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        public void run() {
                            queryCallLog.getCallDetails();
                        }
                    };
                    handler.postDelayed(runnable, 1000);
                }
                prefs.setWasRinging(false);

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //when Off hook i.e in call
                //Make intent and start your service here
                Log.d("xtag", "listener_offhook");
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                //when Ringing
                Log.d("xtag", "listener_ringing");
                prefs.setWasRinging(true);
                break;
            default:
                break;
        }
    }
}

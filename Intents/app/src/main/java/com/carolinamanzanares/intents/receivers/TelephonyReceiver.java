package com.carolinamanzanares.intents.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TelephonyReceiver extends BroadcastReceiver {

    private final String RECEIVER = "RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(RECEIVER, "TelephonyReceiver onReceive()");

        if(intent.getAction() == Intent.ACTION_NEW_OUTGOING_CALL){
            Log.d(RECEIVER, "ACTION: " + intent.getAction());
        }
    }
}

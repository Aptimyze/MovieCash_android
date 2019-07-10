package jonomoneta.juno.moviecash;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jonomoneta.juno.moviecash.Activity.MainActivity;
import jonomoneta.juno.moviecash.services.HistoryService;

public class BroadCastReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Log.e("received", "received");
//        if (!isMyServiceRunning(HistoryService.class)) {
            context.startService(new Intent(context, HistoryService.class));
//        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.e("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.e("isMyServiceRunning?", false + "");
        return false;
    }
}

package jonomoneta.juno.moviecash;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import jonomoneta.juno.moviecash.Activity.MainActivity;
import jonomoneta.juno.moviecash.Utils.Utility;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("received", "received");
        try {
            Date quizDate = (Date) intent.getExtras().get("time");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            String quizTimeString = simpleDateFormat.format(quizDate);
            String message = "Get ready for next quiz.\nStarting at " + quizTimeString;

            Intent intent1 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1
                    .putExtra("title", "quiz")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
            Utility.sendNotification(context, "MovieCash Millionaire", message, pendingIntent);

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }
        MyApplication.getInstance().getPreferenceSettings().setReminder(false);
    }
}

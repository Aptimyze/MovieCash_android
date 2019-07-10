package jonomoneta.juno.moviecash.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.core.app.NotificationCompat;
import jonomoneta.juno.moviecash.Activity.MainActivity;
import jonomoneta.juno.moviecash.Activity.MobileNumberActivity;
import jonomoneta.juno.moviecash.Activity.PredictionActivity;
import jonomoneta.juno.moviecash.Model.Response.UserDetailsResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsService extends FirebaseMessagingService {

    String TAG = "Tag";
    RemoteViews customView;

    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "new token generated : " + s);
        MyApplication.getInstance().getPreferenceSettings().setFirebaseToken(s);
        saveUserTokenAPI(s);
    }

    private void saveUserTokenAPI(String newtoken) {

        String token = newtoken;
        String mobileno = MyApplication.getInstance().getPreferenceSettings().getMobileNumber();

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<UserDetailsResponse> saveToken = retroInterface.saveUserToken(mobileno, token);
        saveToken.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {

            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData() != null) {

            Log.e(TAG, "onMessageReceived: remote mssg : " + remoteMessage.getData().get("title") + "-----" +
                    remoteMessage.getData().get("body") + "-----" + remoteMessage.getData().get("redirectid") + "----" +
                    remoteMessage.getData().get("datetime") + "----" + remoteMessage.getData().get("imageurl"));
            PendingIntent pendingIntent = null;

            if (MyApplication.getInstance().getPreferenceSettings().getIsLogin()) {
                int movId = 0;
                if (remoteMessage.getData().get("redirectid") != null) {
                    movId = Integer.parseInt(remoteMessage.getData().get("redirectid"));
                }
                pendingIntent = PendingIntent.getActivity(this, 0,
                        new Intent(getApplicationContext(), MainActivity.class)
                                .putExtra("title", remoteMessage.getData().get("redirect"))
                                .putExtra("movId", movId)
                                .putExtra("image", remoteMessage.getData().get("imageurl"))
                                .putExtra("desc", remoteMessage.getData().get("body"))
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);

            } else {
                pendingIntent = PendingIntent.getActivity(this, 0,
                        new Intent(getApplicationContext(), MobileNumberActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
            }


            String message = remoteMessage.getData().get("body");
            String quizTime = "";
            if (remoteMessage.getData().get("datetime") != null &&
                    remoteMessage.getData().get("datetime").length() > 0) {
                quizTime = remoteMessage.getData().get("datetime");

                Log.e("quizTimeUTC", quizTime);
                SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.US);
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = null;
                try {
                    date = df.parse(quizTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat ndf = new SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.US);
                ndf.setTimeZone(TimeZone.getDefault());
                String formattedDate = ndf.format(date);

                formattedDate = formattedDate.replace("am", "AM").replace("pm", "PM");
                message = message.replace("##DATETIME##", formattedDate);
            }

            Bitmap banner;

            Notification.BigPictureStyle pictureStyle = new Notification.BigPictureStyle();
            if (remoteMessage.getData().get("imageurl") != null && remoteMessage.getData().get("imageurl").length() > 0) {
                String imgUrl = remoteMessage.getData().get("imageurl");
                URL url;
                try {
                    url = new URL(imgUrl);
                    banner = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    pictureStyle.bigPicture(banner);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                pictureStyle = null;
            }

            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo_white_bg);


            PreferenceSettings mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
            NotificationManager notif = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            int uniqueNumber = mPreferenceSettings.getUniqueNotiId();
            mPreferenceSettings.setUniqueNotiId(uniqueNumber + 1);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .build();
                NotificationChannel mChannel = new NotificationChannel("moviecash_01", "MovieCash", NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableVibration(true);
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);
                notif.createNotificationChannel(mChannel);

                if (pictureStyle != null) {
                    Notification notify = new Notification.Builder(getApplicationContext(), "moviecash_01")
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.drawable.logo_white_bg)
                            .setLargeIcon(
                                    Bitmap.createScaledBitmap(icon, 128, 128, false))
                            .setContentText(message)
                            .setWhen(System.currentTimeMillis())
                            .setStyle(pictureStyle)
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(uniqueNumber, notify);
                } else {
                    Notification notify = new Notification.Builder(getApplicationContext(), "moviecash_01")
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.drawable.logo_white_bg)
                            .setLargeIcon(
                                    Bitmap.createScaledBitmap(icon, 128, 128, false))
                            .setContentText(message)
                            .setWhen(System.currentTimeMillis())
                            .setStyle(new Notification.BigTextStyle().bigText(message))
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(uniqueNumber, notify);
                }
            } else {
                if (pictureStyle != null) {
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.drawable.logo_white_bg)
                            .setLargeIcon(
                                    Bitmap.createScaledBitmap(icon, 128, 128, false))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setVibrate(new long[]{500, 500})
                            .setContentText(message)
                            .setWhen(System.currentTimeMillis())
                            .setStyle(pictureStyle)
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(uniqueNumber, notify);
                } else {
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.drawable.logo_white_bg)
                            .setLargeIcon(
                                    Bitmap.createScaledBitmap(icon, 128, 128, false))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setVibrate(new long[]{500, 500})
                            .setContentText(message)
                            .setWhen(System.currentTimeMillis())
                            .setStyle(new Notification.BigTextStyle().bigText(message))
                            .build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notif.notify(uniqueNumber, notify);
                }
            }
        }
    }


}

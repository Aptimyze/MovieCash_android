package jonomoneta.juno.moviecash.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jonomoneta.juno.moviecash.Activity.MainActivity;
import jonomoneta.juno.moviecash.Activity.MobileNumberActivity;
import jonomoneta.juno.moviecash.BroadCastReceiver;
import jonomoneta.juno.moviecash.Model.Response.CommonResponse;
import jonomoneta.juno.moviecash.Model.Response.PlaceResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;

import static jonomoneta.juno.moviecash.Activity.MainActivity.holdSec;
import static jonomoneta.juno.moviecash.Activity.MainActivity.spendTime;

public class HistoryService extends Service implements LocationListener {

    Location location;


    public HistoryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.e("service", "start");
        GPSTracker gpsTracker = new GPSTracker(this);
        Log.e("Current location", gpsTracker.getLatitude() + ", " + gpsTracker.getLongitude());
        getUpdates(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        getLocation();
        return START_STICKY;
    }

    private void getLocation() {
        LocationManager locationManager;
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service", "destroy");

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("service", "task_removed");
        Intent intent = new Intent(getBaseContext(), BroadCastReceiver.class)
                .putExtra("time", "");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        if (Build.VERSION.SDK_INT < 23) {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onLocationChanged(Location curLocation) {
        if (curLocation != null) {
            location = curLocation;
//            Log.e("New location", location.getLatitude() + ", " + location.getLongitude());
            getUpdates(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getUpdates(final double latitude, final double longitude) {

        RetroInterface retroInterface = RetrofitAdapter.retrofitPlace.create(RetroInterface.class);
        Call<PlaceResponse> call = retroInterface.getPlaces(latitude + "," + longitude, "15", "", "AIzaSyAIRN8g2l9lbtQoAfLajXsLpAvWuWfnTM8");
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, retrofit2.Response<PlaceResponse> response) {

                Log.e("response", response.body().getResultsArrayList().toString());


                if (response.body().getResultsArrayList() != null && response.body().getResultsArrayList().size() > 0) {

                    double tmpLat = latitude,
                            tmpLng = longitude;
                    double latdistance = Math.abs(response.body().getResultsArrayList().get(0).getGeometry().getLocation().getLat() - tmpLat),
                            lngdistance = Math.abs(response.body().getResultsArrayList().get(0).getGeometry().getLocation().getLng() - tmpLng);
                    int idxlat = 0;
                    for (int c = 1; c < response.body().getResultsArrayList().size(); c++) {
                        double cdistancelat = Math.abs(response.body().getResultsArrayList().get(c).getGeometry().getLocation().getLat() - tmpLat),
                                cdistancelng = Math.abs(response.body().getResultsArrayList().get(c).getGeometry().getLocation().getLng() - tmpLng);
                        if (cdistancelat < latdistance && cdistancelng < lngdistance) {
                            idxlat = c;
                            latdistance = cdistancelat;
                            lngdistance = cdistancelng;
                        }
                    }

                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    String address = "";

                    try {
                        addresses = geocoder.getFromLocation(response.body().getResultsArrayList().get(idxlat).getGeometry().getLocation().getLat(),
                                response.body().getResultsArrayList().get(idxlat).getGeometry().getLocation().getLng(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses != null && addresses.size() > 0) {
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    }

                    Log.e("lastPlaceId", MyApplication.getInstance().getPreferenceSettings().getLastPlace() +
                            ",\nCurPlaceId: " + response.body().getResultsArrayList().get(idxlat).getPlace_id());
                    if (!response.body().getResultsArrayList().get(idxlat).getPlace_id()
                            .equalsIgnoreCase(MyApplication.getInstance().getPreferenceSettings().getLastPlace())) {

                        spendTime = holdSec;
                        Log.e("spend", spendTime + "");
//                        sendRewardNotification();
                        saveHistory(response.body().getResultsArrayList().get(idxlat), address, spendTime);
                        holdSec = 0;
                        MyApplication.getInstance().getPreferenceSettings().setHoldSec(0);
                    } else {
                        holdSec = MyApplication.getInstance().getPreferenceSettings().getHoldSec();
                    }
                    MyApplication.getInstance().getPreferenceSettings().setLastPlace(response.body().getResultsArrayList().get(idxlat).getPlace_id());

                    Log.e("nearest", response.body().getResultsArrayList().get(idxlat).getName());
                    Utility.stopTimer();
                    Utility.startTimer(getApplicationContext(), latitude, longitude);


                } else {
                    Log.e("zero", "Zero");
                }
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {

            }
        });
    }

    private void saveHistory(PlaceResponse.results data, String address, int time) {
        String name = data.getName(),
                placeId = data.getPlace_id(),
                lati = String.valueOf(data.getGeometry().getLocation().getLat()),
                longi = String.valueOf(data.getGeometry().getLocation().getLng()),
                types = data.getTypesArrayList().toString();

        int hours = (int) time / 3600;
        int remainder = (int) time - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        Log.e("time", hours + ":" + mins + ":" + secs);
        int uid = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getID();
        Call<CommonResponse> saveData = RetrofitAdapter.retrofit.create(RetroInterface.class).saveHistory(uid,
                name, placeId, lati, longi, address, types.substring(1, types.length() - 1), hours, mins, secs);
        saveData.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Utility.getProfileDetails();
                    if (response.body().getResponseID()==2) {
                        sendRewardNotification();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });

    }

    private void sendRewardNotification() {
        PendingIntent pendingIntent;
        if (MyApplication.getInstance().getPreferenceSettings().getIsLogin()) {

            pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(getApplicationContext(), MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);

        } else {
            pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(getApplicationContext(), MobileNumberActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo_white_bg);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.reward_notification);
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
            mChannel.setSound(sound, audioAttributes);
            notif.createNotificationChannel(mChannel);


            Notification notify = new Notification.Builder(getApplicationContext(), "moviecash_01")
                    .setContentTitle("MovieCash")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo_white_bg)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentText("Congratulations! $ 5 JM credited to your account.")
                    .setWhen(System.currentTimeMillis())
                    .setStyle(new Notification.BigTextStyle().bigText("Congratulations! $ 5 JM credited to your account."))
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(uniqueNumber, notify);
        } else {

            Notification notify = new Notification.Builder(getApplicationContext())
                    .setContentTitle("MovieCash")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo_white_bg)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setSound(sound)
                    .setVibrate(new long[]{500, 500})
                    .setContentText("Congratulations! $ 5 JM credited to your account.")
                    .setWhen(System.currentTimeMillis())
                    .setStyle(new Notification.BigTextStyle().bigText("Congratulations! $ 5 JM credited to your account."))
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(uniqueNumber, notify);
        }
    }


}

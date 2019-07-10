package jonomoneta.juno.moviecash.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import jonomoneta.juno.moviecash.Activity.MainActivity;
import jonomoneta.juno.moviecash.CustomEditText;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Model.Response.SaveQuizAnswerResponse;
import jonomoneta.juno.moviecash.Model.Response.UserDetailsResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static jonomoneta.juno.moviecash.Activity.MainActivity.holdSec;

public class Utility {

    public static String API_KEY = "AIzaSyCevLWdckspwsMRtfFfqsCnPtbC6fy0I6M";
    public static String earn_count_ad = "2";
    public static String earn_count_trailer = "3";
    public static long offline_quiz_winner_prize = 5;
    public static int live_quiz_attend_reward = 1;
    public static int live_quiz_winner_reward_fix = 100;
    public static int live_mega_quiz_winner_reward = 500;
    public static int live_mega_quiz_player_reward = 5;
    public static int life_to_apply = 1;
    public static long golden_buzzer_count = 100000;
    public static double worlds_best_subscription_charge_amount = 10;
    public static double worlds_best_subscription_charge_amount_for_judge = 150;
    public static double lotto_subscription_charge = 5;
    public static String rules_link = "https://junomoneta.io/movie-cash/#MiniMovieCashMillionaireContestOfficialRules";
    public static String terms_link = "https://junomoneta.io/movie-cash/#MiniMovieCashMillionaireTermsofUse";
    public static String privacy_link = "https://junomoneta.io/movie-cash/#MiniMovieCashMillionairePrivacyPolicy";
    public static String terms_link_lotto = "https://junomoneta.io/lottoterms";
    public static String privacy_link_lotto = "https://junomoneta.io/lottoprivacy";

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void noInternetError(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder1.setTitle("Oops");
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Internet connection not available.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void permissionDialog(final Context context, final Activity activity) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder1.setTitle("Allow Permissions");
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("You need to allow all permissions from setting to access all the features of the application.");
        builder1.setCancelable(true);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, 111);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public static String getBase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public static void getProfileDetails() {
        final PreferenceSettings mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);

        Call<UserDetailsResponse> getUserDetails = retroInterface.getUserProfile(mPreferenceSettings.getMobileNumber());
        getUserDetails.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    mPreferenceSettings.setUserDetails(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Log.e("profileFail", t.getMessage());
            }
        });
    }

    public static void disableAllView(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableAllView((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    public static void enableAllView(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableAllView((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                activity.getCurrentFocus().getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }


    public static void updateAlert(final Context context, String versn) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder1.setTitle("New version available. (v" + versn + ")");
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Please update application to latest version for best performance.");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=movieosis.mdadil2019.movieosis"));
                        context.startActivity(i);
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity) context).finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void alertDialog(final Context context, String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder1.setTitle("Oops!");
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage(msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void claimDialog(final Context context, String title, String msg, int icon, final int amount, final String type) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder1.setTitle(title);
        builder1.setIcon(icon);
        builder1.setMessage(msg);
        builder1.setCancelable(true);


        builder1.setPositiveButton(
                "Redeem",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (Integer.parseInt(MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getTotalEarning()) >= amount) {
                            showFormDialog(context, type);
                        } else {
                            Toast.makeText(context, "Sorry, you don't have enough points to redeem this reward.\nPlease earn some points and get back to us.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void showFormDialog(final Context context, final String type) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.redeem_form_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.flag_transparent));
        final String mobNo = MyApplication.getInstance().getPreferenceSettings().getMobileNumber();


        final CustomEditText fullNameET = dialog.findViewById(R.id.fullNameET),
                emailET = dialog.findViewById(R.id.emailET),
                addressET = dialog.findViewById(R.id.addressET);
        final CustomTextView mobileNoTV = dialog.findViewById(R.id.mobileNoTV);
        final CustomTextViewBold
                cancelTV = dialog.findViewById(R.id.cancelTV),
                redeemTV = dialog.findViewById(R.id.redeemTV);
        final ProgressBar loader = dialog.findViewById(R.id.loader);

        mobileNoTV.setText(mobNo);

        redeemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email, fullName, address;

                fullName = fullNameET.getText().toString().trim();
                email = emailET.getText().toString().trim();
                address = addressET.getText().toString().trim();

                if (!fullName.isEmpty()) {
                    if (!email.isEmpty()) {
                        if (isValid(email)) {
                            if (!address.isEmpty()) {

                                if (isOnline(context)) {
                                    redeemTV.setVisibility(View.GONE);
                                    loader.setVisibility(View.VISIBLE);
                                    int uid = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getID();
                                    redeemPrizeAPI(context, redeemTV, loader, dialog, uid, mobNo, fullName, email, address, type);
                                } else {
                                    noInternetError(context);
                                }
                            } else {
                                Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Please enter your full name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public static void redeemPrizeAPI(final Context context, final CustomTextViewBold redeemTV, final ProgressBar loader,
                                      final Dialog dialog, int uid, String mob, String fullName,
                                      String email, String address, String type) {

        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<SaveQuizAnswerResponse> redeemPrize = retroInterface.redeemPrize(uid, mob, fullName, email, address, type);
        redeemPrize.enqueue(new Callback<SaveQuizAnswerResponse>() {
            @Override
            public void onResponse(Call<SaveQuizAnswerResponse> call, Response<SaveQuizAnswerResponse> response) {
                redeemTV.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(context, "You will receive your reward in 60 working days.\nReward is subject to availability and other conditions apply.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    getProfileDetails();
                } else {
                    Toast.makeText(context, response.body().getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SaveQuizAnswerResponse> call, Throwable t) {
                redeemTV.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static Timer mTimer1;
    public static TimerTask mTt1;
    public static Handler mTimerHandler = new Handler();

    public static void startTimer(final Context context, final double lat, final double lng) {
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        //TODO

                        holdSec += 1;
                        Log.e("seconds", String.valueOf(holdSec));
                        MyApplication.getInstance().getPreferenceSettings().setHoldSec(holdSec);
//                        if (holdSec == 60) {
//                            spendTime = holdSec;
//                            getCurrentPlace(context, lat, lng, placeList.get(placeList.size() - 1).getName());
//                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 0, 1000);
    }

    public static void stopTimer() {
        if (mTimer1 != null) {
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    public static void sendNotification(Context context, String title, String message, PendingIntent pendingIntent) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.logo_white_bg);
        NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
            NotificationChannel mChannel = new NotificationChannel("moviecash_01", "MovieCash", NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);
            mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);
            notif.createNotificationChannel(mChannel);

            Notification notify = new Notification.Builder(context, "moviecash_01")
                    .setContentTitle(title)
                    .setStyle(new Notification.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo_white_bg)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentText(message)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);
        } else {
            Notification notify = new Notification.Builder(context)
                    .setContentTitle(title)
                    .setStyle(new Notification.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo_white_bg)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{500, 500})
                    .setContentText(message)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);
        }

    }

//    public static void getCurrentPlace(final Context context, final double lat, final double lng, final String name) {
//
//        RetroInterface retroInterface = RetrofitAdapter.retrofitPlace.create(RetroInterface.class);
//        Call<PlaceResponse> call = retroInterface.getPlaces(lat + "," + lng, "15", "", "AIzaSyDilAKJ_vJ3rCCxR34RsEa2nyK3S2h6gL0");
//        call.enqueue(new Callback<PlaceResponse>() {
//            @Override
//            public void onResponse(Call<PlaceResponse> call, retrofit2.Response<PlaceResponse> response) {
//                if (response.body().getResultsArrayList() != null && response.body().getResultsArrayList().size() > 0) {
//
//                    double tmpLat = lat,
//                            tmpLng = lng;
//                    double latdistance = Math.abs(response.body().getResultsArrayList().get(0).getGeometry().getLocation().getLat() - tmpLat),
//                            lngdistance = Math.abs(response.body().getResultsArrayList().get(0).getGeometry().getLocation().getLng() - tmpLng);
//                    int idxlat = 0,
//                            idxlng = 0;
//                    for (int c = 1; c < response.body().getResultsArrayList().size(); c++) {
//                        double cdistancelat = Math.abs(response.body().getResultsArrayList().get(c).getGeometry().getLocation().getLat() - tmpLat),
//                                cdistancelng = Math.abs(response.body().getResultsArrayList().get(c).getGeometry().getLocation().getLng() - tmpLng);
//                        if (cdistancelat < latdistance && cdistancelng < lngdistance) {
//                            idxlat = c;
//                            idxlng = c;
//                            latdistance = cdistancelat;
//                            lngdistance = cdistancelng;
//                        }
//                    }
//
//
//                    Geocoder geocoder;
//                    List<Address> addresses = null;
//                    geocoder = new Geocoder(context, Locale.getDefault());
//
//                    try {
//                        addresses = geocoder.getFromLocation(response.body().getResultsArrayList().get(idxlat).getGeometry().getLocation().getLat(),
//                                response.body().getResultsArrayList().get(idxlat).getGeometry().getLocation().getLng(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                    saveHistory(response.body().getResultsArrayList().get(idxlat), address, spendTime);
////                    for (int i = 0; i < response.body().getResultsArrayList().size(); i++) {
////                        if (response.body().getResultsArrayList().get(i).getTypesArrayList().toString().contains("food")) {
////                        if (response.body().getResultsArrayList().get(idxlat).getName().equalsIgnoreCase(name)) {
////                            saveHistory(response.body().getResultsArrayList().get(idxlat), address, spendTime);
////                            alertDialog(context, name);
////                                Toast.makeText(context, name, Toast.LENGTH_LONG).show();
////                            break;
////                        }
////                        }
////                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PlaceResponse> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//    private static void saveHistory(PlaceResponse.results data, String address, int time) {
//        String name = data.getName(),
//                placeId = data.getPlace_id(),
//                lati = String.valueOf(data.getGeometry().getLocation().getLat()),
//                longi = String.valueOf(data.getGeometry().getLocation().getLng()),
//                types = data.getTypesArrayList().toString();
//
//        int hours = (int) time / 3600;
//        int remainder = (int) time - hours * 3600;
//        int mins = remainder / 60;
//        remainder = remainder - mins * 60;
//        int secs = remainder;
//
//        Log.e("time", hours + ":" + mins + ":" + secs);
//        int uid = MyApplication.getInstance().getPreferenceSettings().getUserDetails().getResponseData().getID();
//        Call<PlaceResponse> saveData = RetrofitAdapter.retrofit.create(RetroInterface.class).saveHistory(uid,
//                name, placeId, lati, longi, address, types.substring(1, types.length() - 1), hours, mins, secs);
//        saveData.enqueue(new Callback<PlaceResponse>() {
//            @Override
//            public void onResponse(Call<PlaceResponse> call, retrofit2.Response<PlaceResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<PlaceResponse> call, Throwable t) {
//
//            }
//        });
//
//    }

}



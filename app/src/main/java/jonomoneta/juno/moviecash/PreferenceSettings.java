package jonomoneta.juno.moviecash;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import jonomoneta.juno.moviecash.Model.Response.CountryResponse;
import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
import jonomoneta.juno.moviecash.Model.Response.UserDetailsResponse;


/**
 * Created by EbitM9 on 4/27/2017.
 */

public class PreferenceSettings {
    private static final String PREFERENCE_NAME = "moviecash";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private int PRIVATE_MODE = 0;

    private String KEY_FIRST_TIME = "first time";
    private String KEY_IS_LOGIN = "is login";
    private String KEY_MOBILE = "mobile no";
    private String KEY_USER_DETAILS = "user details";
    private String KEY_IS_NEW = "new user";
    private String KEY_MOVIE_TYPES = "movie types";
    private String KEY_TOKEN = "token";
    private String KEY_AD_ID = "ad id";
    private String KEY_COUNTRY = "country";
    private String KEY_CATEGORY = "category";
    private String KEY_CAT_POS = "category pos";
    private String KEY_GENERES = "generes";
    private String KEY_COUNTRY_POS = "country pos";
    private String KEY_LANGUAGE_POS = "language pos";
    private String KEY_DATE = "release date";
    private String KEY_FIRST_TOKEN = "first token";
    private String KEY_HOLD_SEC = "hold sec";
    private String KEY_LAST_PLACE = "last place";
    private String KEY_QUIZ_PRIZE = "quiz prize";
    private String KEY_REMINDER = "reminder";
    private String KEY_STREAM_ID = "stream id";
    private String KEY_STORY_ID = "story id";
    private String KEY_UNIQUE_NOTI_ID = "unique noti id";

    public PreferenceSettings(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public void setFirstTime(boolean isFirst) {
        mEditor.putBoolean(KEY_FIRST_TIME, isFirst).commit();
    }

    public boolean getFirstTime() {
        boolean data = mSharedPreferences.getBoolean(KEY_FIRST_TIME, true);
        return data;
    }

    public void setFirstTimeForToken(boolean isFirst) {
        mEditor.putBoolean(KEY_FIRST_TOKEN, isFirst).commit();
    }

    public boolean getFirstTimeForToken() {
        boolean data = mSharedPreferences.getBoolean(KEY_FIRST_TOKEN, true);
        return data;
    }

    public void setIsLogin(boolean isLogin) {
        mEditor.putBoolean(KEY_IS_LOGIN, isLogin).commit();
    }

    public boolean getIsLogin() {
        boolean data = mSharedPreferences.getBoolean(KEY_IS_LOGIN, false);
        return data;
    }

    public void setMobileNumber(String mobileNo) {
        mEditor.putString(KEY_MOBILE, mobileNo).commit();
    }

    public String getMobileNumber() {
        String data = mSharedPreferences.getString(KEY_MOBILE, "");
        return data;
    }

    public void setUserDetails(UserDetailsResponse userDetailsResponse) {
        Gson gson = new Gson();
        String data = gson.toJson(userDetailsResponse);
        mEditor.putString(KEY_USER_DETAILS, data).commit();
    }

    public UserDetailsResponse getUserDetails() {
        Gson gson = new Gson();
        UserDetailsResponse data = gson.fromJson(mSharedPreferences.getString(KEY_USER_DETAILS, null), UserDetailsResponse.class);
        return data;
    }

    public void setMovieTypes(MovieTypesResponse movieTypesResponse) {
        Gson gson = new Gson();
        String data = gson.toJson(movieTypesResponse);
        mEditor.putString(KEY_MOVIE_TYPES, data).commit();
    }

    public MovieTypesResponse getMovieTypes() {
        Gson gson = new Gson();
        MovieTypesResponse data = gson.fromJson(mSharedPreferences.getString(KEY_MOVIE_TYPES, null), MovieTypesResponse.class);
        return data;
    }

    public void setCountries(CountryResponse countryResponse) {
        Gson gson = new Gson();
        String data = gson.toJson(countryResponse);
        mEditor.putString(KEY_COUNTRY, data).commit();
    }

    public CountryResponse getCountries() {
        Gson gson = new Gson();
        CountryResponse data = gson.fromJson(mSharedPreferences.getString(KEY_COUNTRY, null), CountryResponse.class);
        return data;
    }

    public void setCategory(MovieTypesResponse movieTypesResponse) {
        Gson gson = new Gson();
        String data = gson.toJson(movieTypesResponse);
        mEditor.putString(KEY_CATEGORY, data).commit();
    }

    public MovieTypesResponse getCategory() {
        Gson gson = new Gson();
        MovieTypesResponse data = gson.fromJson(mSharedPreferences.getString(KEY_CATEGORY, null), MovieTypesResponse.class);
        return data;
    }

    public void setIsNewUser(boolean isNew) {
        mEditor.putBoolean(KEY_IS_NEW, isNew).commit();
    }

    public boolean getIsNewUser() {
        boolean data = mSharedPreferences.getBoolean(KEY_IS_NEW, false);
        return data;
    }

    public void setFirebaseToken(String token) {
        mEditor.putString(KEY_TOKEN, token).commit();
    }

    public String getFirebaseToken() {
        String data = mSharedPreferences.getString(KEY_TOKEN, null);
        return data;
    }

    public void setAdId(String adId) {
        mEditor.putString(KEY_AD_ID, adId).commit();
    }

    public String getAdId() {
        String data = mSharedPreferences.getString(KEY_AD_ID, null);
        return data;
    }

    public void setFilterCategory(int pos) {
        mEditor.putInt(KEY_CAT_POS, pos).commit();
    }

    public int getFilterCategory() {
        int data = mSharedPreferences.getInt(KEY_CAT_POS, 0);
        return data;
    }

    public void setFilterGeneres(String data) {
        mEditor.putString(KEY_GENERES, data).commit();
    }

    public String getFilterGeneres() {
        String data = mSharedPreferences.getString(KEY_GENERES, null);
        return data;
    }

    public void setFilterCountry(int pos) {
        mEditor.putInt(KEY_COUNTRY_POS, pos).commit();
    }

    public int getFilterCountry() {
        int data = mSharedPreferences.getInt(KEY_COUNTRY_POS, 0);
        return data;
    }

    public void setFilterLanguage(int pos) {
        mEditor.putInt(KEY_LANGUAGE_POS, pos).commit();
    }

    public int getFilterLanguage() {
        int data = mSharedPreferences.getInt(KEY_LANGUAGE_POS, 0);
        return data;
    }

    public void setFilterDate(String data) {
        mEditor.putString(KEY_DATE, data).commit();
    }

    public String getFilterDate() {
        String data = mSharedPreferences.getString(KEY_DATE, null);
        return data;
    }

    public void setHoldSec(int data) {
        mEditor.putInt(KEY_HOLD_SEC, data).commit();
    }

    public int getHoldSec() {
        int data = mSharedPreferences.getInt(KEY_HOLD_SEC, 0);
        return data;
    }

    public void setLastPlace(String data) {
        mEditor.putString(KEY_LAST_PLACE, data).commit();
    }

    public String getLastPlace() {
        String data = mSharedPreferences.getString(KEY_LAST_PLACE, "");
        return data;
    }

    public void setQuizPrize(long price) {
        mEditor.putLong(KEY_QUIZ_PRIZE, price).commit();
    }

    public long getQuizPrize() {
        long data = mSharedPreferences.getLong(KEY_QUIZ_PRIZE, 0);
        return data;
    }

    public void setReminder(boolean data) {
        mEditor.putBoolean(KEY_REMINDER, data).commit();
    }

    public boolean getReminder() {
        boolean data = mSharedPreferences.getBoolean(KEY_REMINDER, false);
        return data;
    }

    public void setUniqueNotiId(int data) {
        mEditor.putInt(KEY_UNIQUE_NOTI_ID, data).commit();
    }

    public int getUniqueNotiId() {
        int data = mSharedPreferences.getInt(KEY_UNIQUE_NOTI_ID, 0);
        return data;
    }
//
//    public void setStoryId(String data) {
//        mEditor.putString(KEY_STORY_ID, data).commit();
//    }
//
//    public String getStoryId() {
//        String data = mSharedPreferences.getString(KEY_STORY_ID, "");
//        return data;
//    }
//
//    public void setMultiAnchorId(String data) {
//        mEditor.putString(KEY_MULTIANCHOR_ID, data).commit();
//    }
//
//    public String getMultiAnchorId() {
//        String data = mSharedPreferences.getString(KEY_MULTIANCHOR_ID, "");
//        return data;
//    }
}

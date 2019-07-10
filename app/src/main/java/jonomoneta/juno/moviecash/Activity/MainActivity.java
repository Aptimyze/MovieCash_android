package jonomoneta.juno.moviecash.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.CustomTextViewBold;
import jonomoneta.juno.moviecash.Fragment.LottoFragment;
import jonomoneta.juno.moviecash.Fragment.RewardSystemFragment;
import jonomoneta.juno.moviecash.Model.Response.WatchTrailerResponse;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;
import jonomoneta.juno.moviecash.Retrofit.RetroInterface;
import jonomoneta.juno.moviecash.Retrofit.RetrofitAdapter;
import jonomoneta.juno.moviecash.Utils.PermissionUtils;
import jonomoneta.juno.moviecash.Utils.Utility;
import jonomoneta.juno.moviecash.services.HistoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LinearLayout talentLL, quizLL, profileLL;
    private CustomTextView titleTextView;
    public static boolean fromAd = false, fromFilter = false;
    private ImageView menuBtn;
    private Animation slideAnim, slideOut;
    public static String categoryid = "", typeids = "", languageid = "", releasedate = "", countryid = "";
    private PreferenceSettings mPreferenceSettings;
    private boolean isUpdate = false;
    private CustomTextViewBold textView;

    //    public static ArrayList<PlaceResponse.results> placeList;
    //    Intent serviceIntent;
    public static MainActivity mainActivity;
    public static int holdSec, spendTime = 0;
    boolean fromOffer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();

        mainActivity = this;

        try {
            Log.e("curToken", FirebaseInstanceId.getInstance().getToken());
        } catch (Exception e) {

        }


        if (PermissionUtils.requestPermission(MainActivity.this, 0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECEIVE_BOOT_COMPLETED)) {

        } else {
            Utility.permissionDialog(MainActivity.this, MainActivity.this);
        }


        initialize();

//        mPreferenceSettings.setReminder(false);
        onNewIntent(getIntent());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (PermissionUtils.requestPermission(MainActivity.this, 0,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED)) {

            } else {
                Utility.permissionDialog(MainActivity.this, MainActivity.this);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String title = intent.getStringExtra("title");
            if (title != null) {
                Log.e("newIntent", title);
                if (title.equalsIgnoreCase("quiz")) {
                    startActivity(new Intent(MainActivity.this, PredictionActivity.class));
                } else if (title.equalsIgnoreCase("lotto")) {
                    viewPager.setCurrentItem(1);
                } else if (title.equalsIgnoreCase("offer")) {
                    startActivity(new Intent(MainActivity.this, OfferNotificationActivity.class)
                            .putExtra("image", intent.getStringExtra("image"))
                            .putExtra("desc", intent.getStringExtra("desc")));
                    finish();
                }
            }
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.e("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.e("isMyServiceRunning?", false + "");
        return false;
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc", String.valueOf(e));
        }
    }

    private void initialize() {

//        addAutoStartup();
        if (Utility.isOnline(MainActivity.this)) {
            new GooglePlayStoreAppVersionNameLoader().execute();
        }

        startService(new Intent(MainActivity.this, HistoryService.class));

        fromOffer = getIntent().getBooleanExtra("fromOffer", false);
        mPreferenceSettings.setFilterGeneres(null);
        mPreferenceSettings.setFilterCategory(0);
        mPreferenceSettings.setFilterLanguage(0);
        mPreferenceSettings.setFilterCountry(0);
        mPreferenceSettings.setFilterDate(null);

        slideAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);

        textView = findViewById(R.id.textView);
        titleTextView = findViewById(R.id.titleTextView);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        talentLL = findViewById(R.id.talentLL);
        quizLL = findViewById(R.id.quizLL);
        menuBtn = findViewById(R.id.menuBtn);
        profileLL = findViewById(R.id.profileLL);

        titleTextView.setText("Location Base Frequent Rewards");

        if (Utility.isOnline(MainActivity.this)) {
            Utility.getProfileDetails();

        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        if (fromOffer) {
            viewPager.setCurrentItem(2);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Utility.hideKeyboard(MainActivity.this);

//                titleTextView.setText(tab.getText());

                if (tab.getPosition() == 0) {
                    titleTextView.setText("Location Base Frequent Rewards");
                } else if (tab.getPosition() == 1) {
                    titleTextView.setText("ATM Lotto");
                    titleTextView.setVisibility(View.VISIBLE);
                } else {
//                    searchEditText.setText("");
//                    titleTextView.setVisibility(View.VISIBLE);
//                    searchEditText.setVisibility(View.GONE);
//                    searchBtn.setImageResource(R.drawable.ic_search);
//                    searchBtn.setVisibility(View.VISIBLE);
//                    filterBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        talentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, WorldsBestActivity.class));
                } else {
                    Utility.noInternetError(MainActivity.this);
                }
            }
        });

        profileLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isOnline(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else {
                    Utility.noInternetError(MainActivity.this);
                }
            }
        });

        quizLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, PredictionActivity.class));
                } else {
                    Utility.noInternetError(MainActivity.this);
                }
            }
        });


        menuBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, menuBtn);
                //Inflating the Popup using xml file
                popup.inflate(R.menu.main);
//                        .inflate(R.menu.main, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_refer:
                                startActivity(new Intent(MainActivity.this, RefferalActivity.class));
                                break;
                            case R.id.action_redeem:
                                startActivity(new Intent(MainActivity.this, RedeemActivity.class));
                                break;
                        }
                        return true;
                    }
                });

                MenuPopupHelper menuHelper = new MenuPopupHelper(MainActivity.this, (MenuBuilder) popup.getMenu(), menuBtn);
                menuHelper.setForceShowIcon(true);
                menuHelper.show(); //showing popup menu
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new ReleasedMovieFragment(), "REWARD");
//        adapter.addFrag(new UpcomingMovieFragment(), "UPCOMING");
        adapter.addFrag(new RewardSystemFragment(), "REWARD");
        adapter.addFrag(new LottoFragment(), "LOTTO");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fromAd) {
            fromAd = false;
            watchAdAPI();
        }
        if (fromFilter) {
            fromFilter = false;
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
            typeids = "";

            PreferenceSettings mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
            mPreferenceSettings.setFilterGeneres(typeids);
        }
        if (isUpdate) {
            isUpdate = false;
            new GooglePlayStoreAppVersionNameLoader().execute();
        }

    }

    private void watchAdAPI() {
        PreferenceSettings mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        String mobileno = mPreferenceSettings.getMobileNumber(),
                ad_id = mPreferenceSettings.getAdId();
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        Call<WatchTrailerResponse> watchAdvertisment = retroInterface.watchAdvertisment(mobileno, ad_id, "1", Utility.earn_count_ad);
        watchAdvertisment.enqueue(new Callback<WatchTrailerResponse>() {
            @Override
            public void onResponse(Call<WatchTrailerResponse> call, Response<WatchTrailerResponse> response) {

                if (!response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(MainActivity.this, "You have reached limit for this advertisement.", Toast.LENGTH_LONG).show();
                } else {
//                    Utility.showCongratulationDialog(MainActivity.this, "Congratulations.!\n\nYou have earned 1 $ JM.");

                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Congratulations.!\n\nYou have earned $ 1 JM.");
                    final Animation zoomIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in),
                            zoomOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_out);
                    textView.startAnimation(zoomIn);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            textView.startAnimation(zoomOut);
                            textView.setVisibility(View.GONE);


                        }
                    }, 2000);
                }

                Utility.getProfileDetails();
            }

            @Override
            public void onFailure(Call<WatchTrailerResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupTabIcons() {

//        View view1 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        CustomTextView tabOneTV = view1.findViewById(R.id.tabTextView);
//        ImageView tabIconImageView = view1.findViewById(R.id.tabIconImageView);
//        tabOneTV.setText("RELEASED");
//        tabIconImageView.setImageResource(R.drawable.released_selector);
//        tabLayout.getTabAt(0).setCustomView(view1);

//        View view2 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        CustomTextView tabTwoTV = view2.findViewById(R.id.tabTextView);
//        ImageView tabIconImageView2 = view2.findViewById(R.id.tabIconImageView);
//        tabTwoTV.setText("UPCOMING");
//        tabIconImageView2.setImageResource(R.drawable.upcoming_selector);
//        tabLayout.getTabAt(1).setCustomView(view2);

        View view1 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CustomTextView tabOneTV = view1.findViewById(R.id.tabTextView);
        ImageView tabIconImageView = view1.findViewById(R.id.tabIconImageView);
        tabOneTV.setText("REWARD");
        tabIconImageView.setImageResource(R.drawable.reward_system_selector);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view3 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        CustomTextView tabThreeTV = view3.findViewById(R.id.tabTextView);
        ImageView tabIconImageView3 = view3.findViewById(R.id.tabIconImageView);
        tabThreeTV.setText("LOTTO");
        tabIconImageView3.setImageResource(R.drawable.lotto_selector);
        tabLayout.getTabAt(1).setCustomView(view3);

//        View view4 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        CustomTextView tabFourTV = view4.findViewById(R.id.tabTextView);
//        ImageView tabIconImageView4 = view4.findViewById(R.id.tabIconImageView);
//        tabFourTV.setText("QUIZ");
//        tabIconImageView4.setImageResource(R.drawable.quiz_selector);
//        tabLayout.getTabAt(3).setCustomView(view4);

    }

    public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, Void, String> {

        String newVersion;

        protected String doInBackground(String... urls) {
            try {
                return Jsoup.connect("https://play.google.com/store/apps/details?id=" + "movieosis.mdadil2019.movieosis" + "&hl=en")
                        .timeout(15000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();

            } catch (Exception e) {
                return e.getMessage();
            }
        }

        protected void onPostExecute(String string) {
            newVersion = string;
            Log.e("new Version", newVersion);
            try {
                String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "";

                if (newVersion != null && !newVersion.isEmpty()) {

                    Log.e("curVer:", currentVersion + "");
                    try {
                        String cur[] = currentVersion.split("\\."),
                                newV[] = newVersion.split("\\.");
                        int size = 0;
                        if (cur.length < newV.length) {
                            size = newV.length;
                        } else {
                            size = cur.length;
                        }

                        for (int i = 0; i < size; i++) {
                            if (Integer.parseInt(cur[i]) < Integer.parseInt(newV[i])) {
                                isUpdate = true;
                                Utility.updateAlert(MainActivity.this, newVersion);
                                break;
                            }
                        }

                    } catch (Exception e) {
                        Log.e("exc", e.getMessage());
                        e.printStackTrace();
                    }
                }
                Log.e("update", "Current version " + currentVersion + "playstore version " + newVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}

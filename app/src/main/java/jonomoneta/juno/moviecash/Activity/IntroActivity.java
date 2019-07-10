package jonomoneta.juno.moviecash.Activity;

import android.content.Intent;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import jonomoneta.juno.moviecash.Adapter.MyViewPagerAdapter;
import jonomoneta.juno.moviecash.CustomTextView;
import jonomoneta.juno.moviecash.MyApplication;
import jonomoneta.juno.moviecash.PreferenceSettings;
import jonomoneta.juno.moviecash.R;


public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private CustomTextView btnDone;
    private PreferenceSettings mPreferenceSettings;
    LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        params = new LinearLayout.LayoutParams(20, 20);
        params.setMargins(2, 0, 2, 0);

        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        initialize();

        if (!mPreferenceSettings.getFirstTime()) {
            startActivity(new Intent(IntroActivity.this, MobileNumberActivity.class));
            finish();
            mPreferenceSettings.setFirstTime(false);
        } else {
            mPreferenceSettings.setFirstTime(false);
        }

        layouts = new int[]{
                R.layout.slider1,
                R.layout.slider2,
                R.layout.slider3};
        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter(IntroActivity.this, layouts);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, MobileNumberActivity.class));
                finish();
            }
        });
    }

    private void initialize() {
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnDone = findViewById(R.id.btnDone);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setBackgroundResource(R.drawable.ic_ring);
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dots[i].setLayoutParams(params);
            dotsLayout.addView(dots[i]);
        }

        if (currentPage == 0) {
            dots[currentPage].setBackgroundResource(R.drawable.ic_filled_circle);
            dots[currentPage].setLayoutParams(params);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            dots[position].setBackgroundResource(R.drawable.ic_filled_circle);
            dots[position].setLayoutParams(params);
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnDone.setVisibility(View.VISIBLE);
            } else {
                // still pages are left
                btnDone.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}

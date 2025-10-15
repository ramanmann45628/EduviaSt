package com.example.eduviast;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;
    private LinearLayout layoutDots;

    private int activeColor;
    private int inactiveColor;
    private FrameLayout[] dots;
    private List<ImageView> fillViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        layoutDots = findViewById(R.id.layoutDots);

        activeColor = ContextCompat.getColor(this, R.color.black);
        inactiveColor = ContextCompat.getColor(this, R.color.gray);

        List<OnboardItem> items = new ArrayList<>();
        items.add(new OnboardItem(R.drawable.illustration1,
                "Welcome to Eduvia St.",
                "Your smart companion for Learning life.",
                "Access all your school updates, classes, and performance in one place.",
                "Next"));
        items.add(new OnboardItem(R.drawable.illustration2,
                "Welcome to Eduvia St.",
                "Track attendance, homework, and exams easily.",
                "Get results, notices, and stay on top of your learning journey.",
                "Get Started"));

        adapter = new OnboardingAdapter(items, this::finishOnboarding);
        viewPager.setAdapter(adapter);

        setupDots(items.size());
        setCurrentDot(0);

        // Handle dot progress animation on scroll
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < fillViews.size() - 1) {
                    ImageView current = fillViews.get(position);
                    ImageView next = fillViews.get(position + 1);

                    current.setScaleX(1 - positionOffset);
                    next.setScaleX(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
            }
        });
    }

    private void setupDots(int count) {
        dots = new FrameLayout[count];
        fillViews.clear();
        layoutDots.removeAllViews();

        for (int i = 0; i < count; i++) {
            // FrameLayout as container
            FrameLayout dotContainer = new FrameLayout(this);

            // Background circle (inactive)
            ImageView background = new ImageView(this);
            background.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_shape));
            background.setColorFilter(inactiveColor);

            // Foreground circle (active fill)
            ImageView fill = new ImageView(this);
            fill.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_shape));
            fill.setColorFilter(activeColor);
            fill.setScaleX(0f); // start as empty (not filled)

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dotContainer.addView(background, params);
            dotContainer.addView(fill, params);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(10, 0, 10, 0);

            layoutDots.addView(dotContainer, layoutParams);
            dots[i] = dotContainer;
            fillViews.add(fill);
        }
    }

    private void setCurrentDot(int index) {
        for (int i = 0; i < fillViews.size(); i++) {
            ImageView fill = fillViews.get(i);
            if (i == index) {
                animateDotFill(fill, 0f, 1f);
            } else {
                animateDotFill(fill, fill.getScaleX(), 0f);
            }
        }
    }

    private void animateDotFill(ImageView dot, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(a -> dot.setScaleX((float) a.getAnimatedValue()));
        anim.setDuration(350);
        anim.start();
    }

    private void finishOnboarding() {
        SharedPreferences.Editor editor = getSharedPreferences("onboarding", MODE_PRIVATE).edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
        startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
        finish();
    }
}

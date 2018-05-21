package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.juancoob.nanodegree.and.showjokes.ShowJokes;


public class MainActivity extends AppCompatActivity {

    public static final String JOKE = "joke";
    private CountingIdlingResource mCountingIdlingResource;
    private InterstitialAd mInterstitialAd;
    private Intent mIntentToShowJokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if(mIntentToShowJokes != null) {
                    startActivity(mIntentToShowJokes);
                }
            }
        });

        getCountingIdlingResource().increment();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        showLoader();
        new EndpointsAsyncTask(new JokeCallBack() {
            @Override
            public void showJoke(String joke) {
                hideLoader();
                mIntentToShowJokes = new Intent(MainActivity.this, ShowJokes.class);
                mIntentToShowJokes.putExtra(JOKE, joke);

                if(!getCountingIdlingResource().isIdleNow()) {
                    getCountingIdlingResource().decrement();
                }

                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    startActivity(mIntentToShowJokes);
                }
            }
        }).execute();
    }

    private void hideLoader() {
        MainActivityFragment mainActivityFragment =
                (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_tag));
        mainActivityFragment.hideProgressBar();
    }

    private void showLoader() {
        MainActivityFragment mainActivityFragment =
                (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_tag));
        mainActivityFragment.showProgressBar();
    }

    @VisibleForTesting
    public CountingIdlingResource getCountingIdlingResource() {
        return mCountingIdlingResource == null ?
                new CountingIdlingResource(MainActivity.class.getName()) : mCountingIdlingResource;
    }

}

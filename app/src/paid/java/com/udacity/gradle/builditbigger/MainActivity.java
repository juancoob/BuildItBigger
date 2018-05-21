package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.juancoob.nanodegree.and.showjokes.ShowJokes;


public class MainActivity extends AppCompatActivity {

    public static final String JOKE = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Intent intent = new Intent(MainActivity.this, ShowJokes.class);
                intent.putExtra(JOKE, joke);
                startActivity(intent);
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
}

package com.udacity.gradle.builditbigger;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MyAsyncTaskTest {

    @Test
    public void endPointsValidator_getJoke_returnJoke() {

        final CountDownLatch signal = new CountDownLatch(1);

        new EndpointsAsyncTask(new JokeCallBack() {
            @Override
            public void showJoke(String joke) {
                assertThat(joke, is(not("")));
                assertNotNull(joke);
                signal.countDown();
            }
        }).execute();

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

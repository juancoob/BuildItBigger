package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Created by Juan Antonio Cobos Obrero on 16/05/18.
 */
public class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

    private static MyApi mMyApiService;
    private JokeCallBack mJokeCallBack;

    public EndpointsAsyncTask(JokeCallBack jokeCallBack) {
        mJokeCallBack = jokeCallBack;
        if (mMyApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(),
                    null)
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                            request.setDisableGZipContent(true);
                        }
                    });

            mMyApiService = builder.build();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return mMyApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String joke) {
        super.onPostExecute(joke);
        mJokeCallBack.showJoke(joke);
    }
}

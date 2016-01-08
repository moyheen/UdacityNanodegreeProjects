package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.moyinoluwa.myapplication.backend.myApi.MyApi;
import com.moyinoluwa.mylibrary.LibraryActivity;

import java.io.IOException;

/**
 * Created by moyinoluwa on 1/7/16.
 */
public class EndPointsAsyncTask extends AsyncTask<Context, Void, String> {
    private MyApi myApiService = null;
    private Context context;

    @Override
    protected void onPreExecute() {
        // Please comment this out before the test is run
        // It will throw an error because onCreate() in MainActivity has not yet been initialized
        MainActivity.progressDialog.show();
    }

    @Override
    protected String doInBackground(Context... params) {
        if (myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://build-it-bigger-1183.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0];

        try {
            return myApiService.tellJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Please comment this out before the test is run
        // It will throw an error because onCreate() in MainActivity has not yet been initialized
        MainActivity.progressDialog.cancel();

        Intent intent = new Intent(context, LibraryActivity.class);
        intent.putExtra("joke", result);
        context.startActivity(intent);
    }
}
package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    public void testAsyncTaskResult() throws InterruptedException {

        EndPointsAsyncTask endpoints = new EndPointsAsyncTask();

        endpoints.execute(this.getContext());

        assertEquals("Why do programmers always mix up Halloween and Christmas?\n" +
                "Because Oct 31 == DEC 25!", endpoints.doInBackground(this.getContext()));
    }
}
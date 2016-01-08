/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.moyinoluwa.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.jokewizard.JokeSmith;


/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.moyinoluwa.com",
                ownerName = "backend.myapplication.moyinoluwa.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that loads jokes from a Java Library
     */
    @ApiMethod(name = "tellJoke")
    public MyBean tellJoke() {
        MyBean response = new MyBean();
        JokeSmith jokeSmith = new JokeSmith();

        response.setData(jokeSmith.getJokes());

        return response;
    }

}

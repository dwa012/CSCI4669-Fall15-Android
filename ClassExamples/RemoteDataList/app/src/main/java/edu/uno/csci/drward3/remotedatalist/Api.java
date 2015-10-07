package edu.uno.csci.drward3.remotedatalist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by danielward on 10/6/15.
 */
public class Api {
    public static List<Post> getPosts() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://jsonplaceholder.typicode.com/posts")
                .build();

        Response response = client.newCall(request).execute();

        String json = response.body().string();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List<Post> posts = new ArrayList<Post>();
        posts = Arrays.asList(gson.fromJson(json, Post[].class));

        return posts;
    }
}

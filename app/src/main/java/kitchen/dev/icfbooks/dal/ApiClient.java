package kitchen.dev.icfbooks.dal;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import kitchen.dev.icfbooks.model.chapters.Chapter;
import kitchen.dev.icfbooks.model.media.Media;

/**
 * Created by noc on 19.02.16.
 */
public class ApiClient {
    private static final String BASE_URL = "https://rhino.dev.kitchen/de/";

    private RequestQueue queue;
    private static ApiClient instance;
    private static HashMap<Context, ApiClient> instances = new HashMap<>();

    private ApiClient(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (!instances.containsKey(context))
            instances.put(context, new ApiClient(context));

        return instances.get(context);
    }

    public void getChapters(int bookId, final ApiResultHandler<Chapter[]> handler) {
        String url = BASE_URL + "books/" + bookId + "/chapters.json";
        queue.add(new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                handler.onResult(gson.fromJson(response,Chapter[].class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.onError(error);
            }
        }));
    }

    public Media getItem(String id) {
        return null;
    }

    //TODO: getVideo
    //TODO: getPic
}

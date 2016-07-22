package kitchen.dev.icfbooks.dal;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import kitchen.dev.icfbooks.model.media.Media;
import kitchen.dev.icfbooks.model.media.MediaContract;
import kitchen.dev.icfbooks.model.media.MediaTypes;

/**
 * Created by noc on 19.02.16.
 */
public class ApiClient {
    public static final String BASE_URL = "https://icfbooks.herokuapp.com/";

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

    private String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public void getMedia(String id, final ApiResultHandler<Media> handler) {
        String url = BASE_URL + getLanguage() + "/media/" + id + ".json";
        queue.add(new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().create();
                Class<?> cl = null;
                try {
                    switch(response.getString(MediaContract.MediaEntry.COLUMN_NAME_TYPE)){
                        case "movie":
                            cl = MediaTypes.MovieMedia.class;
                            break;
                        case "two_movies_and_text":
                            cl = MediaTypes.TwoMoviesAndTextMedia.class;
                            break;
                    }

                    Media media = (Media)gson.fromJson(response.toString(),cl);
                    handler.onResult(media);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.onError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.onError(error);
            }
        }));
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();
    }

    public Media getItem(String id) {
        return null;
    }
}

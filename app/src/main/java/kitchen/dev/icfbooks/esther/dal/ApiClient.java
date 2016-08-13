package kitchen.dev.icfbooks.esther.dal;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;

import kitchen.dev.icfbooks.esther.model.media.Media;
import kitchen.dev.icfbooks.esther.model.media.MediaContract;
import kitchen.dev.icfbooks.esther.model.media.MediaTypes;

/**
 * Created by noc on 19.02.16.
 */
public class ApiClient {
    public static final String BASE_URL = "https://icfbooks.herokuapp.com/";

    private RequestQueue queue;
    private final Context context;
    private static ApiClient instance;
    private static HashMap<Context, ApiClient> instances = new HashMap<>();

    private ApiClient(Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (!instances.containsKey(context))
            instances.put(context, new ApiClient(context));

        return instances.get(context);
    }

    private String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public void getMedia(final String id, final ApiResultHandler<Media> handler) {
        String url = BASE_URL + getLanguage() + "/media/" + id + ".json";
        queue.add(new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new GsonBuilder().create();
                Class<?> cl = null;
                try {
                    switch (response.getString(MediaContract.MediaEntry.COLUMN_NAME_TYPE)) {
                        case "movie":
                            cl = MediaTypes.MovieMedia.class;
                            break;
                        case "two_movies_and_text":
                            cl = MediaTypes.TwoMoviesAndTextMedia.class;
                            break;
                    }

                    final Media media = (Media) gson.fromJson(response.toString(), cl);
                    queue.add(new ImageRequest(media.getThumbnail_url(), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            try {
                                FileOutputStream fos = context.openFileOutput(id, Context.MODE_PRIVATE);
                                response.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                handler.onResult(media);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER, null, null));

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

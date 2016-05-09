package kitchen.dev.icfbooks.dal;

import com.android.volley.VolleyError;

/**
 * Created by samue on 5/8/2016.
 */
public interface ApiResultHandler<T> {
    void onResult(T result);

    void onError(Object error);
}

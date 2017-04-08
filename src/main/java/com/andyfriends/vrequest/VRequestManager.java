package com.andyfriends.vrequest;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;

/**
 * The manager of the request queue
 */
public class VRequestManager {

    private static volatile VRequestManager singleton;
    private RequestQueue mRequestQueue;

    /**
     * Assures that {@link VRequestManager} cannot be instantiated more than one time
     *
     * @param context of the application
     */
    private VRequestManager(final Context context) {
        mRequestQueue = getRequestQueue(context);
    }

    /**
     * Retrieves the {@link VRequestManager} singleton instance
     *
     * @param context of the application
     * @return the singleton instance of {@link VRequestManager}
     */
    public static VRequestManager singleton(Context context) {
        if (singleton == null) {
            synchronized (VRequestManager.class) {
                if (singleton == null) {
                    singleton = new VRequestManager.Builder(context).build();
                }
            }
        }

        return singleton;
    }

    /**
     * Creates (if necessary) and retrieves the {@link VRequestManager} request queue
     *
     * @param context of the application
     * @return the {@link VRequestManager} request queue
     */
    private RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    /**
     * Adds a {@link Request} to the {@link VRequestManager} queue
     *  the TAG for canceling the queue is the request's URL
     *  the {@link RetryPolicy} is the {@link Volley} default
     *
     * @param req some {@link Volley} {@link Request}
     */
    public void addToRequestQueue(Request req) {
        String tag = req.getUrl();

        addToRequestQueue(req, tag);
    }

    /**
     * Adds a {@link Request} to the {@link VRequestManager} queue
     *  setting a custom TAG enabling the request to be canceled
     *  the {@link RetryPolicy} is the {@link Volley} default
     *
     * @param req some {@link Volley} {@link Request}
     * @param tag enabling the request to be canceled
     */
    public void addToRequestQueue(Request req, String tag) {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        addToRequestQueue(req, tag, retryPolicy);
    }

    /**
     * Adds a {@link Request} to the {@link VRequestManager} queue
     *  setting a custom TAG enabling the request to be canceled
     *  setting a custom {@link RetryPolicy}
     *
     * @param req some {@link Volley} {@link Request}
     * @param tag enabling the request to be canceled
     * @param retryPolicy customized for this request
     */
    public void addToRequestQueue(Request req, String tag, RetryPolicy retryPolicy) {
        req.setRetryPolicy(retryPolicy);

        req.setTag(tag);
        mRequestQueue.add(req);
    }


    /**
     * Fluent API for creating {@link VRequestManager} instances.
     */
    public static class Builder {

        private final Context context;

        /**
         * Start building a new {@link VRequestManager} instance.
         */
        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        /**
         * Create the {@link VRequestManager} instance.
         */
        public VRequestManager build() {
            return new VRequestManager(this.context);
        }
    }
}
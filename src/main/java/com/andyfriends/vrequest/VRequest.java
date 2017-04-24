package com.andyfriends.vrequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * An extended {@link Request} class for {@link Volley} network requests.
 *
 * @param <T> The type of parsed response this request expects.
 */
public class VRequest<T> extends Request<T> {

    /**
     * Default charset for JSON request. (grabbed from {@link JsonRequest})
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request. (grabbed from {@link JsonRequest})
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);


    protected Context mContext;
    protected int mMethod;
    protected String mUrl;
    protected JSONObject mRequestBody;
    protected Response.Listener<T> mListener;
    protected Response.Listener<T> mErrorListener;
    protected Response.ErrorListener mVolleyErrorListener;
    protected IVRequestLoad mRequestLoad;

    /**
     * The empty constructor enables us to create a request and use
     * the {@link VRequest} methos to set it up properly
     */
    public VRequest() {
        super(-1, null, null);
    }

    /**
     * Sets the context used later by the #fetch method to insert
     * the request into the queue
     *
     * @param context off the application
     * @return this instance of {@link VRequest}
     */
    public VRequest with(Context context) {
        this.mContext = context;
        return this;
    }

    /**
     * Sets the url for the request
     *
     * @param url to make the request
     * @return this instance of {@link VRequest}
     */
    public VRequest load(String url) {
        this.mUrl = url;
        return this;
    }

    /**
     * Sets the url for the request
     *
     * @param requestLoad to make the request
     * @return this instance of {@link VRequest}
     */
    public VRequest load(IVRequestLoad requestLoad) {
        this.mRequestLoad = requestLoad;
        return this;
    }

    /**
     * Sets the params for the request's body
     * <p>
     * The following methods can be used instead:
     * #post(@NonNull JSONObject params)
     * #patch(@NonNull JSONObject params)
     * #delete(@NonNull JSONObject params)
     *
     * @param params to create the request's body
     * @return this instance of {@link VRequest}
     */
    public VRequest params(int method, JSONObject params) {
        this.mMethod = method;
        this.mRequestBody = params;
        return this;
    }

    /**
     * Sets the params for the request's body
     * using a POST HTTP method
     *
     * @param params to create the request's body
     * @return this instance of {@link VRequest}
     */
    public VRequest post(JSONObject params) {
        return params(Method.POST, params);
    }

    /**
     * Sets the params for the request's body
     * using a PATCH HTTP method
     *
     * @param params to create the request's body
     * @return this instance of {@link VRequest}
     */
    public VRequest patch(JSONObject params) {
        return params(Method.PATCH, params);
    }

    /**
     * Sets the params for the request's body
     * using a DELETE HTTP method
     *
     * @param params to create the request's body
     * @return this instance of {@link VRequest}
     */
    public VRequest delete(JSONObject params) {
        return params(Method.DELETE, params);
    }

    /**
     * Sets the request's HTTP method to DELETE
     *
     * @return this instance of {@link VRequest}
     */
    public VRequest delete() {
        return this.delete(null);
    }

    /**
     * Set the listener to be notified in case of success
     *
     * @param listener to be used by the #deliverResponse method
     * @return this instance of {@link VRequest}
     */
    public VRequest<T> onSuccess(final Response.Listener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * Sets the listener to be notified in case of an error
     *
     * @param volleyErrorListener to be used by the #deliverError method
     * @return this instance of {@link VRequest}
     */
    public VRequest onError(final Response.ErrorListener volleyErrorListener) {
        this.mVolleyErrorListener = volleyErrorListener;
        return this;
    }

    /**
     * Sets the listener to be notified in case of an error
     * You must Override it and use along with the @deliverError method
     * to return your application's specific errors
     *
     * @param errorListener to be used by the #deliverError method
     * @return this instance of {@link VRequest}
     */
    public VRequest onError(final Response.Listener errorListener) {
        this.mErrorListener = errorListener;
        return this;
    }

    /**
     * Carry out the request and returns itself in case of future reuse needs
     *
     * @return this instance of {@link VRequest}
     */
    public VRequest fetch() {
        VRequestManager.singleton(mContext).addToRequestQueue(this);
        return this;
    }

    /**
     * Same as #fetch() method, except it can set a tag for the request
     */
    public VRequest fetch(String tag) {
        VRequestManager.singleton(mContext).addToRequestQueue(this, tag);
        return this;
    }

    /**
     * Same as #fetch(String tag) method, except it can set a {@link RetryPolicy}
     */
    public VRequest fetch(String tag, RetryPolicy retryPolicy) {
        VRequestManager.singleton(mContext).addToRequestQueue(this, tag, retryPolicy);
        return this;
    }

    /**
     * Same as #fetch() method, except it can set a {@link RetryPolicy}
     */
    public VRequest fetch(RetryPolicy retryPolicy) {
        String tag = mUrl;
        VRequestManager.singleton(mContext).addToRequestQueue(this, tag, retryPolicy);
        return this;
    }

    public Response.Listener getSuccessListener() {
        return this.mListener;
    }

    //* THE REMAINING OF THIS CLASS ARE @Override METHODS FROM {@link Request} *//
    /**
     * You must want to re-override them to meet your own purposes
     *  like to create an application's general #deliverResponse
     *  or #deliverError that could have some general behavior
     */

    /**
     * Returns the request's HTTP headers
     *  You must like to override it to custom your headers
     *  like sending some authentication token for example
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    /**
     * Returns the request's body content type (grabbed from {@link JsonRequest})
     *
     * @return the body's content type
     */
    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    /**
     * Returns the request's body (grabbed from {@link JsonRequest})
     *
     * @return the request's body
     */
    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.toString().getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    /**
     * Returns HTTP method to make the request
     *
     * @return the request's HTTP method
     */
    @Override
    public int getMethod() {
        return mMethod;
    }

    /**
     * Returns the request's URL set using the #load method
     *
     * @return the request's URL
     */
    @Override
    public String getUrl() {
        return null != mRequestLoad ? mRequestLoad.getUrl() : mUrl;
    }

    /**
     * Parses the network response to something understandable for the
     * #deliverResponse method
     *
     * @param response Response from the network
     * @return The parsed response, or null in the case of an error
     */
    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    /**
     * Delivers the response to the Listener set with onSuccess method
     *
     * @param volleyResponse the T response expected
     */
    @Override
    protected void deliverResponse(T volleyResponse) {
        if (null != mListener) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

            try {
                JSONObject jsonResponse = (JSONObject) volleyResponse;
                String json = jsonResponse.toString();

                Type[] types = mListener.getClass().getGenericInterfaces();
                ParameterizedType parameterizedType = (ParameterizedType) types[0];
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                Type thisClass = typeArguments[0];

                T obj = gson.fromJson(json, thisClass);

                if (null != mRequestLoad) mRequestLoad.onSuccessListener(obj);
                if (null != mListener) mListener.onResponse(obj);

            } catch (Exception e) {
                mListener.onResponse(null);
            }
        }
    }

    /**
     * Delivers error message to the ErrorListener set with onError method
     *
     * @param error Error details
     */
    @Override
    public void deliverError(VolleyError error) {
        if (null != mErrorListener) mErrorListener.onResponse((T) error);
        if (null != mVolleyErrorListener) mVolleyErrorListener.onErrorResponse(error);
    }

}

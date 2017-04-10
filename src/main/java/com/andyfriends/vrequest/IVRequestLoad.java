package com.andyfriends.vrequest;

/**
 * Enables a {@link VRequest} object to get the URL from a
 *  configuration class. One example is to automate the pagination
 *  of the requests that consume some API that returns limited results
 */
public interface IVRequestLoad<T> {

    /**
     * This method is called by the {@link VRequest} object right
     *  after making the request, so you can make some processes before
     *  returning it
     *
     * @return the URL to make the request to
     */
    String getUrl();

    /**
     * This method is called by the {@link VRequest} object as soon as it receives
     *  the HTTP response, receiving the expected T object
     *
     * @param response
     */
    void onSuccessListener(T response);

}

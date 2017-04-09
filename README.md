# Android Internet requests using Volley library

### A taste of how it works
```java
new VRequest()
                .with(context)
                .load(url)
                .post(params)
                .onSuccess(new Response.Listener<MyObject>() {
                    @Override
                    public void onResponse(MyObject myObject) {
                        //TODO be happy using your object
                    }
                })
                .fetch();
```

### How to import
###### Android Studio
Add this to your Project build.grandle:

```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
*reference:* [jitpack.io](https://jitpack.io)


and this to your App's build.gradle:
```java 
dependencies {
    compile 'com.github.andyfriends:vrequest:v1.0'
}
```

### Than you can start *getting*, *posting*, *patching*, *deleting*, or customising

#### You can now post something
```java
new VRequest()
                .with(context)
                .load(url)
		.post(params)
                .onSuccess(new Response.Listener<MyObject>() {
                    @Override
                    public void onResponse(MyObject myObject) {
                        //TODO be happy using your object
                    }
                })
                .fetch();
```

#### You must want to handle errors
```java
new VRequest()
                ....
                .onError(new Response.ErrorListener<>() {
                    @Override
                    public void onResponse(VolleyError error) {
                        //TODO you can extend VRequest and use your own
			//error class to make things easy for you
                    }
                })
                ...
```

#### Tip of advice:
###### extending [VRequest](https://github.com/AndyFriends/vrequest) you can `@Override` `deliverError()` method

```java
class MyRequest<T> extends VRequest<T> {
@Override
    public void deliverError(VolleyError error) {
        // here we provide a response for the default listener
        if (null != mVolleyErrorListener) mVolleyErrorListener.onErrorResponse(error);

        // here we customize
        if (null != mErrorListener) {
            //this is how we get the error data from our API
            NetworkResponse responseError = error.networkResponse;
            String dataError = new String(responseError.data);
            //maybe your API returns some Json
            JSONObject jsonError = new JSONObject(dataError);
            //Gson can give a hand
            MyUnderstandableErrorClass errorClass = new Gson().fromJson(jsonError, MyUnderstandableErrorClass.class);

            mErrorListener.onResponse((T) errorClass);
        }
    }
}
```

##### now it's easy to handle the errors on your Views or something
```java
new MyRequest()
                ....
                .onError(new Response.Listener<MyUnderstandableErrorClass>() {
                    @Override
                    public void onResponse(MyUnderstandableErrorClass error) {
                        //TODO give the user a better response than "something went wrong" everytime
                    }
                })
                ...
```

### More details coming up soon folks

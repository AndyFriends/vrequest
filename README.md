# Android Internet requests using Volley library

### A taste of how it works
```java
new VRequest()
                .with(this)
                .load(url)
                .post(jsonParams)
                .onSuccess(new Listener<MyObject>() {
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

### More details coming up soon folks

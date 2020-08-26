package com.alinz.parkerdan.shareextension;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import android.app.Activity;
import android.content.Intent;

import android.util.Log;

public class ShareModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private String newIntentValue = null;

    public ShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "ReactNativeShareExtension";
    }

    @ReactMethod
    public void close() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null && currentActivity.getIntent() != null) {
            currentActivity.getIntent().removeExtra(Intent.EXTRA_TEXT);
        }
        newIntentValue = null;
    }

    @ReactMethod
    public void data(Promise promise) {
        promise.resolve(processIntent());
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    public void onNewIntent(Intent intent) {
        newIntentValue = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    private String consumeNewIntentValue() {
        String value = newIntentValue;
        newIntentValue = null;
        return value;
    }

    public WritableMap processIntent() {
        WritableMap map = Arguments.createMap();
        Log.e(ShareModule.class.getCanonicalName(), "processIntent: ");
        String value = "";
        String type = "text/plain";

        Activity currentActivity = getCurrentActivity();

        if (currentActivity != null) {
            Intent intent = currentActivity.getIntent();
            value = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (value == null || value.length() == 0) {
                value = consumeNewIntentValue();
            }
            Log.e(ShareModule.class.getCanonicalName(), "value: " + value);
        } else {
            value = "";
            type = "";
        }
        Log.e(ShareModule.class.getCanonicalName(), "end value: " + value);
        map.putString("type", type);
        map.putString("value",value);

        return map;
    }
}

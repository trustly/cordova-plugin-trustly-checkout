package com.trustly.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.util.Log;
import android.webkit.WebViewClient;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class TrustlyPlugin extends CordovaPlugin {
	private static final int REQUEST_CODE = 0;
	private CallbackContext cordovaCallbackContext;

	@Override
	public boolean execute(String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {

		if (action.equals("showTrustlyCheckout")) {
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showTrustlyCheckout(args, callbackContext);
				}
			});

			return true;
		}
		
		return false;
	}

	private void showTrustlyCheckout(CordovaArgs args, CallbackContext callbackContext) {
		
		String trustlyCheckoutUrlString;	
		try {
			trustlyCheckoutUrlString = args.getString(0);
			new URL(trustlyCheckoutUrlString).toURI();
		}
		 catch (JSONException e) {
			respondWithError("error", "JsonError when unpacking arguments for showTrustlyCheckout");
			return;
		} catch (MalformedURLException e) {
			respondWithError("error", "Invalid URI passed to showTrustlyCheckout");
			return;
		} catch (URISyntaxException e) {
			respondWithError("error", "Invalid URI passed to showTrustlyCheckout");
			return;
		}

		this.cordovaCallbackContext = callbackContext;

		Intent trustlyIntent = new Intent(cordova.getActivity(), TrustlyActivity.class);
		trustlyIntent.putExtra(
			"TRUSTLY_CHECKOUT_URL",
			trustlyCheckoutUrlString);
		this.cordova.startActivityForResult(this, trustlyIntent, REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {

			switch (resultCode) {

				case 2:
					respondWithError("error", "error");
					break;
				case TrustlyActivity.RESULT_OK:
					PluginResult result = new PluginResult(PluginResult.Status.OK, intent.getDataString());
					this.cordovaCallbackContext.sendPluginResult(result);
					break;
				case TrustlyActivity.RESULT_CANCELED:
					respondWithError("abort", "abort");
					break;
				default:
					respondWithError("error", "error");
			}
		}
	}

	@Override
	public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
		this.cordovaCallbackContext = callbackContext;
	}
	private void respondWithError(String type, String message) {
		
		JSONObject pluginResultJsonObject = new JSONObject();

		try {
			pluginResultJsonObject.put("code", "error");
			pluginResultJsonObject.put("type", type);
			pluginResultJsonObject.put("message", message);
		} catch (JSONException e) {
			Log.e("cordova-plugin-trustly-checkout", "Error when populating JsonObject");
		}

		PluginResult r = new PluginResult(PluginResult.Status.ERROR, pluginResultJsonObject);
		this.cordovaCallbackContext.sendPluginResult(r);
	}

}


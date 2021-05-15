package com.trustly.sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.content.pm.PackageManager;

public class TrustlyActivity extends Activity implements TrustlyEventHandler {
	TrustlyWebView trustlyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = getIntent();
		String urlString = intent.getStringExtra("TRUSTLY_CHECKOUT_URL");
		
		trustlyView = new TrustlyWebView(this, urlString, this);
		setContentView(trustlyView);
	}

	 @Override
  public void onTrustlyCheckoutSuccess(TrustlySDKEventObject eventObject) {
  }

  @Override
  public void onTrustlyCheckoutRedirect(TrustlySDKEventObject eventObject) {
			
		if (isPackageInstalledAndEnabled(eventObject.getPackageName(), eventObject.getActivity())) {
			Intent intent = new Intent();
			intent.setPackage(eventObject.getPackageName());
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(eventObject.getUrl()));
			eventObject.getActivity().startActivityForResult(intent, 0);
		} else {
			Intent intent = new Intent(Intent.ACTION_VIEW);
      		intent.setData(Uri.parse(eventObject.getUrl()));
      		eventObject.getActivity().startActivityForResult(intent, 0);
		} 
  }

  @Override
  public void onTrustlyCheckoutAbort(TrustlySDKEventObject eventObject) {
			Intent result = new Intent();
			setResult(Activity.RESULT_CANCELED, result);
			finish();
  }

  @Override
  public void onTrustlyCheckoutError(TrustlySDKEventObject eventObject) {
			Intent result = new Intent();
			setResult(2, result);
			finish();
  }

  
private boolean isPackageInstalledAndEnabled(String packageName, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, 0);
			return ai.enabled;
		} catch (PackageManager.NameNotFoundException e) {
		}
		return false;
	}
	
}

	
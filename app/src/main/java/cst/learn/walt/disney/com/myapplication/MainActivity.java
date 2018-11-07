package cst.learn.walt.disney.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private AdvertisingIdClient.Info info = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("ADID", System.currentTimeMillis() + " // +onStart // thread = " + Thread.currentThread().getName());

        // start the thread with the getAdvertisingIdInfo()
        startGoogleAdvertisingIdRequest(this);

        Log.i("ADID", System.currentTimeMillis() + " // onStart // +sleeping // thread = " + Thread.currentThread().getName());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //
        }

        Log.i("ADID", System.currentTimeMillis() + " // onStart // -sleeping // thread = " + Thread.currentThread().getName());

        Log.i("ADID", System.currentTimeMillis() + " // -onStart // thread = " + Thread.currentThread().getName());

    }

    public void startGoogleAdvertisingIdRequest(final Activity activity) {

        Log.i("ADID", System.currentTimeMillis() + " // +startReq // thread = " + Thread.currentThread().getName());

        int res = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        Log.i("ADID", System.currentTimeMillis() + " // got Availability // thread = " + Thread.currentThread().getName());

        if (ConnectionResult.SUCCESS == res) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.i("ADID", System.currentTimeMillis() + " // startReq // run // thread = " + Thread.currentThread().getName());

                    AdvertisingIdClient.Info adInfo = null;
                    try {
                        Log.i("ADID", System.currentTimeMillis() + " // startReq // +getInfo // thread = " + Thread.currentThread().getName());
                        adInfo = AdvertisingIdClient.getAdvertisingIdInfo(activity);
                        Log.i("ADID", System.currentTimeMillis() + " // startReq // -getInfo // thread = " + Thread.currentThread().getName());
                    } catch (Exception e) {
                        Log.i("ADID", System.currentTimeMillis() + " // startReq // exception // thread = " + Thread.currentThread().getName(), e);
                    } finally {
                        finished(adInfo);
                    }
                }
            }).start();
        }

        Log.i("ADID", System.currentTimeMillis() + " // -startReq // thread = " + Thread.currentThread().getName());

    }

    private void finished(final AdvertisingIdClient.Info adInfo) {

        Log.i("ADID", System.currentTimeMillis() + " // +finished // thread = " + Thread.currentThread().getName());

        if (adInfo != null) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("ADID", System.currentTimeMillis() + " // finished // run // thread = " + Thread.currentThread().getName());

                    String adid = "info was null";

                    if (info != null) {
                        if (!info.isLimitAdTrackingEnabled()) {
                            adid = info.getId();
                        } else {
                            adid = "ad tracking limited";
                        }
                    }

                    Log.i("ADID", System.currentTimeMillis() + " // finished // run // AdID = " + adid + " // thread = " + Thread.currentThread().getName());

                }
            });
        }

        Log.i("ADID", System.currentTimeMillis() + " // -finished // thread = " + Thread.currentThread().getName());
    }

}

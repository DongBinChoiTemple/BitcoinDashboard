package edu.temple.bitcoindashboard;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class AddressService extends Service {
    Handler handler;
    IBinder mBinder = new TestBinder();

    public AddressService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        Notification.Builder n;

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setAction("SOME_ACTION");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        n = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Your AddressService is running")
                .setContentIntent(pIntent)
                .setAutoCancel(false);

        return mBinder;
    }

    public void getAddressInfo(final Handler handler, final String address) {
        this.handler = handler;
        Thread thd = new Thread() {
            public void run() {
                try {
                    URL url = new URL("https://btc.blockr.io/api/v1/address/info/" + address);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    url.openStream()));
                    String nextLine;
                    StringBuilder sb = new StringBuilder();
                    while ((nextLine = reader.readLine()) != null) {
                        sb.append(nextLine);
                    }
                    String response = sb.toString();
                    Message msg = Message.obtain();
                    msg.obj = response;
                    Log.v("Downloaded data", response);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thd.start();
    }

    public class TestBinder extends Binder {
        AddressService getService() {
            return AddressService.this;
        }
    }

}

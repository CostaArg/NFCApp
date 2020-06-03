package costas.firebasedb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class WifiListener extends BroadcastReceiver {
    Snackbar snackbar;
    @Override
    public void onReceive(Context context, Intent intent) {
        onNetworkChange();
    }

    protected abstract void onNetworkChange();
}

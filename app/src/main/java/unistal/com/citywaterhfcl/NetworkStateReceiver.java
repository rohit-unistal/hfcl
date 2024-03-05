package unistal.com.citywaterhfcl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;


public class NetworkStateReceiver extends BroadcastReceiver {

    protected Set<NetworkStateReceiverListener> listeners;
    protected Boolean connected,connectedWifi;

    public NetworkStateReceiver() {
        listeners = new HashSet<NetworkStateReceiverListener>();
        connected = null;connectedWifi = null;
    }

    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       /* NetworkInfo ni = manager.getActiveNetworkInfo();

        if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {

            connected = true;
        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            connected = false;
        }*/
        NetworkInfo[] netInfo = manager.getAllNetworkInfo();
        for (NetworkInfo nis : netInfo) {
            if (nis.getTypeName().equalsIgnoreCase("WIFI"))
                if (nis.isConnected()){
                    connectedWifi= true;}
                else{
                    connectedWifi= false; }
            if (nis.getTypeName().equalsIgnoreCase("MOBILE"))
                if (nis.isConnected())
                {
                    connected = true;}
                else {
                    connected =false;
                }
        }
        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for(NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if(connected == null || listener == null || connectedWifi == null)
            return;

        if(connected == true)
            listener.networkAvailable();
        else
            listener.networkUnavailable();
        if(connectedWifi == true)
            listener.networkWifiAvailable();
        else
            listener.networkWifiUnavailable();


    }

    public void addListener(NetworkStateReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    public void removeListener(NetworkStateReceiverListener l) {
        listeners.remove(l);
    }

    public interface NetworkStateReceiverListener {
        public void networkAvailable();
        public void networkUnavailable();
        public void networkWifiAvailable();
        public void networkWifiUnavailable();

    }
}

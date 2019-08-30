package de.blinkt.openvpn.base;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.VpnService;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.server.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.blinkt.openvpn.BindUtils;
import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VPNLaunchHelper;
import de.blinkt.openvpn.core.VpnStatus;

import static de.blinkt.openvpn.core.VpnStatus.ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT;

public abstract class VPNActivity extends AppCompatActivity implements VpnStatus.StateListener {

    private String TAG = VPNActivity.class.getSimpleName();
    private static final int START_VPN_PROFILE = 70;
    private static OpenVPNService mVPNService;
    private boolean isBindedService = false;
    private boolean filterAds = false;
    private VpnProfile mVpnProfile;
    private List<VPNStatusListener> mVPNStatusListener = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VpnStatus.addStateListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, OpenVPNService.class);
        intent.setAction(OpenVPNService.START_SERVICE);
        isBindedService = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case START_VPN_PROFILE:
                    VPNLaunchHelper.startOpenVpn(mVpnProfile, this);
                    break;
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            OpenVPNService.LocalBinder binder = (OpenVPNService.LocalBinder) service;
            mVPNService = binder.getService();
            if (mVPNService != null){
                mVPNService.setContentIntent(getJumpIntent());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mVPNService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    public void addVPNStatusListener(VPNStatusListener listener) {
        if (!mVPNStatusListener.contains(listener)) {
            mVPNStatusListener.add(listener);
        }
    }

    public void removeVPNStatusListener(VPNStatusListener listener) {
        if (!mVPNStatusListener.contains(listener)) {
            mVPNStatusListener.remove(listener);
        }
    }

    public VpnProfile getVpnProfile() {
        return mVpnProfile;
    }

    public boolean isRunning() {
        return VpnStatus.isVPNActive();
    }

    public boolean loadVpnProfile(String str) {
        return loadVpnProfile(str.getBytes());
    }

    public boolean loadVpnProfile(byte[] data) {
        ConfigParser cp = new ConfigParser();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(data));
        try {
            cp.parseConfig(isr);
            mVpnProfile = cp.convertProfile();
            if (filterAds) {
                mVpnProfile.mOverrideDNS = true;
                mVpnProfile.mDNS1 = "101.132.183.99";
                mVpnProfile.mDNS2 = "193.112.15.186";
            }

            ProfileManager.getInstance(this).addProfile(mVpnProfile);
            return true;
        } catch (IOException | ConfigParser.ConfigParseError e) {
            e.printStackTrace();
            return false;
        }
    }

    public void connectVpn() {
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            VpnStatus.updateStateString("USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
                    LEVEL_WAITING_FOR_USER_INPUT);
            // Start the query
            try {
                startActivityForResult(intent, START_VPN_PROFILE);
            } catch (ActivityNotFoundException ane) {
                // Shame on you Sony! At least one user reported that
                // an official Sony Xperia Arc S image triggers this exception
                VpnStatus.logError(R.string.no_vpn_support_image);
            }
        } else {
            onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);
        }
    }

    public void stopVpn() {
        ProfileManager.setConntectedVpnProfileDisconnected(this);
        if (mVPNService != null && mVPNService.getManagement() != null)
            mVPNService.getManagement().stopVPN(false);
    }

    public void setAccountAndPassword(String account, String password) {
        if (getVpnProfile() == null)
            throw new IllegalStateException("You need loadVpnProfile!");

        getVpnProfile().mUsername = account;
        getVpnProfile().mPassword = password;
    }

    public void filterAds(boolean filter) {
        this.filterAds = filter;
    }

    public abstract Intent getJumpIntent();

    @Override
    public void updateState(String state, String logmessage, int localizedResId, VpnStatus.ConnectionStatus level) {
        BindUtils.bindStatus(state, logmessage, localizedResId, level);
        for (VPNStatusListener vpnStatusListener : mVPNStatusListener) {
            switch (level) {
                case LEVEL_START:
                    vpnStatusListener.onConnectStart();
                    break;
                case LEVEL_CONNECTED:
                    vpnStatusListener.onConnected();
                    break;
                case LEVEL_VPNPAUSED:
                    vpnStatusListener.onPaused();
                    break;
                case LEVEL_NONETWORK:
                    vpnStatusListener.onNoNetwork();
                    break;
                case LEVEL_CONNECTING_SERVER_REPLIED:
                    break;
                case LEVEL_CONNECTING_NO_SERVER_REPLY_YET:
                    break;
                case LEVEL_NOTCONNECTED:
                    vpnStatusListener.onConnectClose();
                    break;
                case LEVEL_AUTH_FAILED:
                    vpnStatusListener.onAuthFailed();
                    break;
                case LEVEL_WAITING_FOR_USER_INPUT:
                    Log.d(TAG, "updateState: " + LEVEL_WAITING_FOR_USER_INPUT);
                    break;
                case UNKNOWN_LEVEL:
                    vpnStatusListener.onUnknown();
                    break;
            }
        }
    }

    public interface VPNStatusListener {
        void onConnectStart();

        void onConnected();

        void onPaused();

        void onNoNetwork();

        void onConnectClose();

        void onAuthFailed();

        void onUnknown();
    }
}

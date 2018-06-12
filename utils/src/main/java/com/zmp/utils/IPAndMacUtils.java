package com.zmp.utils;

import android.content.Context;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public class IPAndMacUtils {

        //        // 获取本机的MAC地址
//        public String getLocalMacAddress(Context context) {
//                if (com.uurobot.common.utils.NetworkUtils.checkNetworkConnection(context)) {
//                        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                        WifiInfo info = wifi.getConnectionInfo();
//                        String macAddress = info.getMacAddress();
//                        String mac = macAddress.replace(":", "");
//                        return mac;
//                }
//                return null;
//
//        }
        // 获取本机的MAC地址
        public static String getLocalMacAddressFromIp(Context context) {
                String mac_s = "";
                try {
                        byte[] mac;
                        NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
                        mac = ne.getHardwareAddress();
                        mac_s = byte2hex(mac);
                }
                catch (Exception e) {
                        e.printStackTrace();
                }
                return mac_s;
        }

        public static String byte2hex(byte[] b) {
                StringBuffer hs = new StringBuffer(b.length);
                String stmp = "";
                int len = b.length;
                for (int n = 0; n < len; n++) {
                        stmp = Integer.toHexString(b[n] & 0xFF);
                        if (stmp.length() == 1)
                                hs = hs.append("0").append(stmp);
                        else {
                                hs = hs.append(stmp);
                        }
                }
                return String.valueOf(hs);
        }

        public static String getLocalIpAddress() {
                try {
                        List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
                        for (NetworkInterface ni : nilist) {
                                List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                                for (InetAddress address : ialist) {
                                        if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                                                String hostAddress = address.getHostAddress();
                                                Log.e("ipv4", hostAddress);
                                                if (!"192.168.12.1".equals(hostAddress)) {
                                                        return hostAddress;
                                                }
                                        }
                                }
                        }
                }
                catch (SocketException ex) {
                        ex.printStackTrace();
                }
                return null;
        }
}

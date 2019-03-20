package com.hndt.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * IP 地址工具类
 *
 * @author Hystar
 * @date 2018/1/12
 */
public class IpUtil {

    /**
     * 获取本机IP
     *
     * @return
     */
    public static String getLocalIP(){
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        byte[] ipAddress = inetAddress.getAddress();
        String ipAddressStr = "";
        for (int i = 0; i < ipAddress.length; i++) {
            if (i > 0) {
                ipAddressStr += ".";
            }
            ipAddressStr += ipAddress[i] & 0xFF;
        }
        return ipAddressStr;
    }

    /**
     * 获取服务器IP（项目部署到服务器Linux系统下）
     *
     * @return
     */
    public static String getServerIp(){
        String SERVER_IP = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    SERVER_IP = ip.getHostAddress();
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return SERVER_IP;
    }
}

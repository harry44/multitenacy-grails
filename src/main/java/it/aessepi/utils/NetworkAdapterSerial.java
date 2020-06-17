/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author bogdan
 */
public class NetworkAdapterSerial {
    private static NetworkAdapterSerial instance;
    private String currentSerial;

    private NetworkAdapterSerial() {
        this.currentSerial = generateCurrentSerial();
    }

    public static NetworkAdapterSerial getInstance() {
        if(instance == null) {
            instance = new NetworkAdapterSerial();
        }
        return instance;
    }

    public String getCurrentSerial() {
        return currentSerial;
    }
    
    
    private String generateCurrentSerial() {
        TreeMap<String, String> adapterUUIDs = new TreeMap<String, String>();
        try {
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = ifaces.nextElement();
                if (!iface.isVirtual() && !iface.isLoopback() && !iface.isPointToPoint()) {
                    byte[] hwaddr = iface.getHardwareAddress();
                    if (hwaddr != null) {
                        adapterUUIDs.put(iface.getName(), UUID.nameUUIDFromBytes(DigestUtils.md5(hwaddr)).toString());
                    }
                }
            }
        } catch (Exception ex) {
        }
        if (adapterUUIDs.size() == 0) {
            try {
                adapterUUIDs.put("localhost", UUID.nameUUIDFromBytes(DigestUtils.md5(InetAddress.getLocalHost().getHostName())).toString());
            } catch (Exception ex) {
                adapterUUIDs.put("nohost", UUID.nameUUIDFromBytes(DigestUtils.md5("nohost")).toString());
            }
        }
        return adapterUUIDs.firstEntry().getValue();
    }
}

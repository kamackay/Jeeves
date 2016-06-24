package keithapps.mobile.com.jeeves.tools;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import keithapps.mobile.com.jeeves.R;

import static keithapps.mobile.com.jeeves.tools.SystemTools.getPrefs;

public class Utils {

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     *
     * @param str
     * @return array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     *
     * @param filename
     * @return
     * @throws java.io.IOException
     */
    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4, Context c) {
        try {
            if (!getPrefs(c).getBoolean(c.getString(R.string.permissions_internet), true))
                return "No Internet Permission";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return String.format(Locale.getDefault(), "Could not get IP Address: %s", ex.getMessage());
        } // for now eat exceptions
        return "";
    }

    public static String breakIntoLines(String s) {
        try {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            boolean f = true;
            for (String x : s.split("[\\.:]")) {
                if (x.contains("\n")) i = -6;
                if (i + x.length() > 30) {
                    sb.append("\n        .").append(x);
                    i = x.length();
                } else {
                    if (f) f = false;
                    else sb.append(".");
                    sb.append(x);
                    i += x.length();
                }
            }
            return sb.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getStackTraceString(Exception e) {
        return (e == null || e.getStackTrace() == null) ?
                "Could not get Stack Trace" :
                getStackTraceString(e.getStackTrace());
    }

    /**
     * Get the current Timestamp in the format
     * <p/>
     * MM/dd/yyyy:HH:mm:ss
     *
     * @return the current timestamp in string form
     */
    public static String getTimestamp() {
        return new SimpleDateFormat("MM/dd-HH:mm:ss", Locale.US).format(new Date());
    }

    public static String getStackTraceString(StackTraceElement[] stack) {
        if (stack == null) return "";
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stack) {
            if (element == null) continue;
            try {
                sb.append("    ");
                sb.append(element.getClassName());
                sb.append("    ");
                sb.append(String.format(Locale.getDefault(), "Line: %d\n", element.getLineNumber()));
            } catch (Exception e) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
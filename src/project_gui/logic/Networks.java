/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_gui.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 *
 * @author ahmadbarakat
 */
public abstract class Networks implements Runnable {

    public static int portNumber;

    private static String currentHostIpAddress;

    private static TextArea serversWritableArea;
    private static TextArea clientsWritableArea;

    protected static boolean working;

    public static void setServersWritableArea(TextArea writableAreaSent) {
        serversWritableArea = writableAreaSent;
    }

    public static void setClientsWritableArea(TextArea writableAreaSent) {
        clientsWritableArea = writableAreaSent;
    }

    protected static synchronized void writeToUserServers(String text) {
        Platform.runLater(() -> {
            serversWritableArea.appendText(text + "\n");
        });
    }

    protected static synchronized void writeToUserClients(String text) {
        Platform.runLater(() -> {
            clientsWritableArea.appendText(text + "\n");
        });
    }

    public static String getCurrentEnvironmentNetworkIp() {
        if (currentHostIpAddress == null) {
            Enumeration<NetworkInterface> netInterfaces = null;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> address = ni.getInetAddresses();
                    while (address.hasMoreElements()) {
                        InetAddress addr = address.nextElement();
                        if (!addr.isLoopbackAddress()
                                && addr.isSiteLocalAddress()
                                && !addr.getHostAddress().contains(":")) {
                            currentHostIpAddress = addr.getHostAddress();
                        }
                    }
                }
                if (currentHostIpAddress == null) {
                    currentHostIpAddress = "127.0.0.1";
                }

            } catch (SocketException e) {
                currentHostIpAddress = "127.0.0.1";
            }
        }
        return currentHostIpAddress;
    }
    protected int myNumber;
    protected Thread t;
    protected byte[] sendData = new byte[1024];
    protected byte[] receiveData = new byte[1024];

    Networks(int myNumber) {
        receiveData = new byte[1024];
        sendData = new byte[1024];
        this.myNumber = myNumber;
    }

    public void start() {
        t = new Thread(this);
        t.start();
    }

    protected void writeMessage(DataOutputStream dout, byte[] msg, int msgLen)
            throws IOException {
        dout.writeInt(msgLen);
        dout.write(msg, 0, msgLen);
        dout.flush();
    }

    protected byte[] readMessage(DataInputStream din) throws IOException {
        int msgLen = din.readInt();
        byte[] msg = new byte[msgLen];
        din.readFully(msg);
        return msg;
    }

    protected byte[] appendToByteArray(byte[] append, int value, int position) {
        --position;
        byte[] ret = new byte[position * 4 + 4];
        if (position > 0) {
            System.arraycopy(append, 0, ret, 0, position * 4);
        }

        int start = position * 4;
        int end = position * 4 + 4;
        int c = 0;
        for (int i = start; i < end; i++) {
            ret[i] = (byte) (value >> (24 - 8 * c));
            ++c;
        }
        return ret;
    }

    protected int fromByteArray(byte[] bytes, int position) {
        int beginning = (position - 1) * 4;
        return bytes[beginning] << 24 | (bytes[beginning + 1] & 0xFF) << 16
                | (bytes[beginning + 2] & 0xFF) << 8 | (bytes[beginning + 3]
                & 0xFF);
    }

}

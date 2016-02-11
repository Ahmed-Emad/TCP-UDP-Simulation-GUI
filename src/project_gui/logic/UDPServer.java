/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_gui.logic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author ahmadbarakat
 */
public class UDPServer extends Networks {

    public static DatagramSocket serverSocket;

    public static void initialize() {
        UDPServer.working = true;
        try {
            serverSocket = new DatagramSocket(portNumber);
        } catch (SocketException ex) {
            writeToUserServers("    " + ex.getMessage());
        }
    }

    public static void closeUdpServers() {
        UDPServer.working = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public UDPServer(int myNumber) {
        super(myNumber);
    }

    @Override
    public void run() {
        int num1, num2;
        while (UDPServer.working) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException ex) {
                ex.printStackTrace();
                writeToUserServers("    " + ex.getMessage());
                break;
            }
            num1 = fromByteArray(receivePacket.getData(), 1);
            num2 = fromByteArray(receivePacket.getData(), 2);

            writeToUserServers("UDP SERVER " + myNumber + "  RECEIVED: "
                    + num1 + ", " + num2);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            sendData = appendToByteArray(sendData, num1 + num2, 1);
            DatagramPacket sendPacket = new DatagramPacket(sendData,
                    sendData.length, IPAddress, port);
            try {
                serverSocket.send(sendPacket);
            } catch (IOException ex) {
                ex.printStackTrace();
                writeToUserServers("    " + ex.getMessage());
            }
        }
    }

}

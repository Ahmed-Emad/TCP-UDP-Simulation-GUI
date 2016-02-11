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
import java.net.UnknownHostException;

/**
 *
 * @author ahmadbarakat
 */
public class UDPClient extends Networks {

    private final DatagramSocket clientSocket;
    private final InetAddress IPAddress;

    private final int num1, num2;

    public UDPClient(int myNumber, int num1, int num2) throws SocketException,
            UnknownHostException {
        super(myNumber);
        clientSocket = new DatagramSocket();
        IPAddress = InetAddress.getByName(
                Networks.getCurrentEnvironmentNetworkIp());
        this.num1 = num1;
        this.num2 = num2;
        this.myNumber = myNumber;
    }

    @Override
    public void run() {
        sendData = appendToByteArray(sendData, num1, 1);
        sendData = appendToByteArray(sendData, num2, 2);
        DatagramPacket sendPacket = new DatagramPacket(sendData,
                sendData.length, IPAddress, portNumber);
        try {
            clientSocket.send(sendPacket);
        } catch (IOException ex) {
            ex.printStackTrace();
            writeToUserClients("    " + ex.getMessage());
        }

        writeToUserClients("CLIENT " + myNumber + "  Send: "
                + num1 + " + " + num2);

        DatagramPacket receivePacket = new DatagramPacket(receiveData,
                receiveData.length);
        try {
            clientSocket.receive(receivePacket);
        } catch (IOException ex) {
            ex.printStackTrace();
            writeToUserClients("    " + ex.getMessage());
        }
        int result = fromByteArray(receivePacket.getData(), 1);
        writeToUserClients("UDP SERVER Responded to Client " + myNumber
                + "  with: " + result);

        clientSocket.close();
    }

}

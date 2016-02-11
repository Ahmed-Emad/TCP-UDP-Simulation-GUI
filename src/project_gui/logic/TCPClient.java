/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_gui.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author ahmadbarakat
 */
public class TCPClient extends Networks {

    private Socket ClientSocket;

    private DataInputStream dIn;
    private DataOutputStream dOut;

    private final int num1, num2;

    public TCPClient(int myNumber, int num1, int num2) throws SocketException,
            UnknownHostException {
        super(myNumber);
        this.num1 = num1;
        this.num2 = num2;
    }

    @Override
    public void run() {
        try {
            ClientSocket = new Socket(
                    Networks.getCurrentEnvironmentNetworkIp(), portNumber);
        } catch (IOException ex) {
            ex.printStackTrace();
            writeToUserClients("    " + ex.getMessage());
        }        
        sendData = appendToByteArray(sendData, num1, 1);
        sendData = appendToByteArray(sendData, num2, 2);

        try {
            dOut = new DataOutputStream(ClientSocket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            writeToUserClients("    " + ex.getMessage());
        }
        try {
            writeMessage(dOut, sendData, 8);
        } catch (IOException ex) {
            ex.printStackTrace();
            writeToUserClients("    " + ex.getMessage());
        }

        writeToUserClients("CLIENT " + myNumber + "  Send: " + num1
                + " + " + num2);

        try {
            dIn = new DataInputStream(ClientSocket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            writeToUserClients("    " + ex.getMessage());
        }
        try {
            receiveData = readMessage(dIn);
        } catch (IOException ex) {
            ex.printStackTrace();
            writeToUserClients("    " + ex.getMessage());
        }

        int result = fromByteArray(receiveData, 1);
        writeToUserClients("TCP SERVER Responded to Client " + myNumber
                + "  with: " + result);
        try {
            ClientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            writeToUserClients("    " + ex.getMessage());
        }
    }

}

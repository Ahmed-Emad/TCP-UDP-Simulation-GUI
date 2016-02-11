/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_gui.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ahmadbarakat
 */
public class TCPServer extends Networks {

    public static ServerSocket WelcomeSocket;

    public static void initialize() {
        TCPServer.working = true;
        try {
            WelcomeSocket = new ServerSocket(portNumber);
        } catch (IOException ex) {
            writeToUserServers("    " + ex.getMessage());
        }
    }

    public static void closeTCPServers() throws InterruptedException {
        TCPServer.working = false;
        if (WelcomeSocket != null) {
            try {
                WelcomeSocket.close();
            } catch (IOException ex) {
                writeToUserServers("    " + ex.getMessage());
            }
        }
    }

    private Socket connectionSocket;

    private DataInputStream dIn;
    private DataOutputStream dOut;

    public TCPServer(int myNumber) {
        super(myNumber);
    }

    @Override
    public void run() {
        int num1, num2;
        while (TCPServer.working) {
            try {
                connectionSocket = WelcomeSocket.accept();
            } catch (IOException ex) {
                ex.printStackTrace();
                writeToUserServers("    " + ex.getMessage());
                break;
            }
            try {
                dIn = new DataInputStream(connectionSocket.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                writeToUserServers("    " + ex.getMessage());
            }

            try {
                dOut = new DataOutputStream(connectionSocket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                writeToUserServers("    " + ex.getMessage());
            }

            try {
                receiveData = readMessage(dIn);
            } catch (IOException ex) {
                ex.printStackTrace();
                writeToUserServers("    " + ex.getMessage());
            }

            num1 = fromByteArray(receiveData, 1);
            num2 = fromByteArray(receiveData, 2);

            writeToUserServers("TCP SERVER " + myNumber + "  RECEIVED: "
                    + num1 + ", " + num2);
            int result = num1 + num2;

            sendData = appendToByteArray(sendData, result, 1);
            try {
                writeMessage(dOut, sendData, 4);
            } catch (IOException ex) {
                ex.printStackTrace();
                writeToUserServers("    " + ex.getMessage());
            }
        }
    }

}

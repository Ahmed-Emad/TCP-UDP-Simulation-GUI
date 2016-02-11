/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_gui;

import com.guigarage.flatterfx.FlatterFX;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project_gui.logic.Networks;
import project_gui.logic.TCPClient;
import project_gui.logic.TCPServer;
import project_gui.logic.UDPClient;
import project_gui.logic.UDPServer;

/**
 *
 * @author ahmedbarakat
 */
public class Project_GUI extends Application {

    static TextArea serversConsoleArea;
    static TextArea clientsConsoleArea;
    static int maxValue;

    public static void main(String[] args) {
        launch(args);
    }

    GridPane grid;

    RadioButton tcpRadioButton;
    RadioButton udpRadioButton;
    NumericTextField serversCounterTextField;
    NumericTextField clientsCounterTextField;
    NumericTextField portTextField;
    NumericTextField maxTextField;
    Button closeServersButton;

    Random randomGenerator;
    int portNumber;
    int numberOfServers;
    int numberOfClients;

    @Override
    public void start(Stage primaryStage) {

        FlatterFX.style();

        String welcomeFont = "3Dumb.ttf";
        int welcomeFontSize = 120;
        String radioButtonsFont = "Action_Man_Bold.ttf";
        int radioButtonsFontSize = 40;
        String labelsFont = "GoodDog.otf";
        int labelsFontSize = 50;
        String textFieldsFont = "KomikaTitle-Paint.ttf";
        int textFieldsFontSize = 30;
        String textAreaFont = "DrawveticaMini.ttf";
        int textAreaFontSize = 25;
        String buttonFont = "PermanentMarker.ttf";
        int buttonFontSize = 35;

        String initialSentenceServers = "The Servers Messages will be "
                + "printed here";
        String initialSentenceClients = "The Clients Messages will be "
                + "printed here";
        String title = "Ahmed Emad Simulator";

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(40);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            grid.requestFocus();
        });

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.loadFont(
                "file:resources/fonts/" + welcomeFont, welcomeFontSize));
        scenetitle.setFill(Color.WHITE);
        GridPane.setHalignment(scenetitle, HPos.CENTER);
        GridPane.setValignment(scenetitle, VPos.CENTER);
        grid.add(scenetitle, 0, 0, 4, 1);

        ToggleGroup group = new ToggleGroup();
        tcpRadioButton = new RadioButton("TCP");
        tcpRadioButton.setToggleGroup(group);
        tcpRadioButton.setSelected(true);
        tcpRadioButton.setFont(Font.loadFont(
                "file:resources/fonts/" + radioButtonsFont,
                radioButtonsFontSize));
        udpRadioButton = new RadioButton("UDP");
        udpRadioButton.setToggleGroup(group);
        udpRadioButton.setFont(Font.loadFont(
                "file:resources/fonts/" + radioButtonsFont,
                radioButtonsFontSize));
        HBox rbContainer = new HBox(tcpRadioButton, udpRadioButton);
        rbContainer.setSpacing(70);
        rbContainer.setAlignment(Pos.CENTER);
        grid.add(rbContainer, 0, 1, 4, 1);

        Label serversCounterLabel = new Label("Servers");
        GridPane.setHalignment(serversCounterLabel, HPos.CENTER);
        GridPane.setValignment(serversCounterLabel, VPos.CENTER);
        serversCounterLabel.setFont(Font.loadFont(
                "file:resources/fonts/" + labelsFont, labelsFontSize));
        serversCounterTextField = new NumericTextField(5, 1, 0, 50);
        GridPane.setHalignment(serversCounterTextField, HPos.CENTER);
        GridPane.setValignment(serversCounterTextField, VPos.CENTER);
        serversCounterTextField.setAlignment(Pos.CENTER);
        serversCounterTextField.setFont(Font.loadFont(
                "file:resources/fonts/" + textFieldsFont, textFieldsFontSize));
        VBox serversCounterBox = new VBox();
        serversCounterBox.getChildren().add(serversCounterLabel);
        serversCounterBox.getChildren().add(serversCounterTextField);
        serversCounterBox.setAlignment(Pos.CENTER);
        serversCounterBox.setSpacing(15);
        grid.add(serversCounterBox, 0, 2, 1, 1);

        Label clientsCounterLabel = new Label("Clients");
        GridPane.setHalignment(clientsCounterLabel, HPos.CENTER);
        GridPane.setValignment(clientsCounterLabel, VPos.CENTER);
        clientsCounterLabel.setFont(Font.loadFont(
                "file:resources/fonts/" + labelsFont, labelsFontSize));
        clientsCounterTextField = new NumericTextField(5, 1, 0, 50);
        GridPane.setHalignment(clientsCounterTextField, HPos.CENTER);
        GridPane.setValignment(clientsCounterTextField, VPos.CENTER);
        clientsCounterTextField.setAlignment(Pos.CENTER);
        clientsCounterTextField.setFont(Font.loadFont(
                "file:resources/fonts/" + textFieldsFont, textFieldsFontSize));
        VBox clientsCounterBox = new VBox();
        clientsCounterBox.getChildren().add(clientsCounterLabel);
        clientsCounterBox.getChildren().add(clientsCounterTextField);
        clientsCounterBox.setAlignment(Pos.CENTER);
        clientsCounterBox.setSpacing(15);
        grid.add(clientsCounterBox, 1, 2, 1, 1);

        Label portLabel = new Label("Port");
        GridPane.setHalignment(portLabel, HPos.CENTER);
        GridPane.setValignment(portLabel, VPos.CENTER);
        portLabel.setFont(Font.loadFont(
                "file:resources/fonts/" + labelsFont, labelsFontSize));
        portTextField = new NumericTextField(7070, 10, 1024, 50000);
        GridPane.setHalignment(portTextField, HPos.CENTER);
        GridPane.setValignment(portTextField, VPos.CENTER);
        portTextField.setAlignment(Pos.CENTER);
        portTextField.setFont(Font.loadFont(
                "file:resources/fonts/" + textFieldsFont, textFieldsFontSize));
        VBox portBox = new VBox();
        portBox.getChildren().add(portLabel);
        portBox.getChildren().add(portTextField);
        portBox.setAlignment(Pos.CENTER);
        portBox.setSpacing(15);
        grid.add(portBox, 2, 2, 1, 1);

        Label maxLabel = new Label("Max Random");
        GridPane.setHalignment(maxLabel, HPos.CENTER);
        GridPane.setValignment(maxLabel, VPos.CENTER);
        maxLabel.setFont(Font.loadFont(
                "file:resources/fonts/" + labelsFont, labelsFontSize));
        maxTextField = new NumericTextField(100, 10, 10, 1000);
        maxTextField.setMaxHeight(50);
        GridPane.setHalignment(maxTextField, HPos.CENTER);
        GridPane.setValignment(maxTextField, VPos.CENTER);
        maxTextField.setAlignment(Pos.CENTER);
        maxTextField.setFont(Font.loadFont(
                "file:resources/fonts/" + textFieldsFont, textFieldsFontSize));
        VBox maxBox = new VBox();
        maxBox.getChildren().add(maxLabel);
        maxBox.getChildren().add(maxTextField);
        maxBox.setAlignment(Pos.CENTER);
        maxBox.setSpacing(15);
        grid.add(maxBox, 3, 2, 1, 1);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        shadow.setColor(Color.WHITE);

        Button simulateButton = new Button("Simulate");
        GridPane.setHalignment(simulateButton, HPos.CENTER);
        GridPane.setValignment(simulateButton, VPos.CENTER);
        simulateButton.setAlignment(Pos.CENTER);
        simulateButton.setDefaultButton(true);
        simulateButton.setCursor(Cursor.HAND);
        simulateButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                (MouseEvent e) -> {
                    simulateButton.setEffect(shadow);
                });
        simulateButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                (MouseEvent e) -> {
                    simulateButton.setEffect(null);
                });

        simulateButton.setFont(Font.loadFont(
                "file:resources/fonts/" + buttonFont, buttonFontSize));
        simulateButton.setOnAction((ActionEvent event) -> {
            try {
                run();
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        grid.add(simulateButton, 1, 3, 1, 1);

        closeServersButton = new Button("Close Servers");
        GridPane.setHalignment(closeServersButton, HPos.CENTER);
        GridPane.setValignment(closeServersButton, VPos.CENTER);
        closeServersButton.setAlignment(Pos.CENTER);
        closeServersButton.setDefaultButton(true);
        closeServersButton.setCursor(Cursor.HAND);
        closeServersButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                (MouseEvent e) -> {
                    closeServersButton.setEffect(shadow);
                });
        closeServersButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                (MouseEvent e) -> {
                    closeServersButton.setEffect(null);
                });

        closeServersButton.setFont(Font.loadFont(
                "file:resources/fonts/" + buttonFont, buttonFontSize));
        closeServersButton.setOnAction((ActionEvent event) -> {
            try {
                closeServers();
            } catch (InterruptedException ex) {
                Logger.getLogger(Project_GUI.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        });
        closeServersButton.setCancelButton(true);
        closeServersButton.setDisable(true);
        grid.add(closeServersButton, 2, 3, 1, 1);

        serversConsoleArea = new TextArea();
        serversConsoleArea.setFont(Font.loadFont(
                "file:resources/fonts/" + textAreaFont, textAreaFontSize));
        serversConsoleArea.setWrapText(true);
        serversConsoleArea.setEditable(false);
        serversConsoleArea.setText(initialSentenceServers + "\n");
        GridPane.setHalignment(serversConsoleArea, HPos.CENTER);
        serversConsoleArea.addEventHandler(MouseEvent.MOUSE_ENTERED,
                (MouseEvent e) -> {
                    serversConsoleArea.setEffect(shadow);
                });
        serversConsoleArea.addEventHandler(MouseEvent.MOUSE_EXITED,
                (MouseEvent e) -> {
                    serversConsoleArea.setEffect(null);
                });
        grid.add(serversConsoleArea, 0, 4, 2, 5);

        clientsConsoleArea = new TextArea();
        clientsConsoleArea.setFont(Font.loadFont(
                "file:resources/fonts/" + textAreaFont, textAreaFontSize));
        clientsConsoleArea.setWrapText(true);
        clientsConsoleArea.setEditable(false);
        clientsConsoleArea.setText(initialSentenceClients + "\n");
        GridPane.setHalignment(clientsConsoleArea, HPos.CENTER);
        clientsConsoleArea.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        clientsConsoleArea.setEffect(shadow);
                    }
                });
        clientsConsoleArea.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        clientsConsoleArea.setEffect(null);
                    }
                });
        grid.add(clientsConsoleArea, 2, 4, 2, 5);

        Scene scene = new Scene(grid, 1200, 1000);
        primaryStage.setScene(scene);

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.setTitle(title);
        primaryStage.show();

        grid.requestFocus();

        initialize();

    }

    private void initialize() {
        randomGenerator = new Random();
        maxValue = 50;
        Networks.setClientsWritableArea(clientsConsoleArea);
        Networks.setServersWritableArea(serversConsoleArea);
        System.out.println("Initialized");
        System.out.println(Networks.getCurrentEnvironmentNetworkIp());
    }

    private void run() throws SocketException, UnknownHostException,
            InterruptedException {
        serversConsoleArea.clear();
        clientsConsoleArea.clear();
        closeServersButton.setDisable(false);

        grid.requestFocus();

        portNumber = portTextField.getValue();
        numberOfServers = serversCounterTextField.getValue();
        numberOfClients = clientsCounterTextField.getValue();
        maxValue = maxTextField.getValue();

        serversConsoleArea.appendText("Servers: " + numberOfServers + "\n");
        clientsConsoleArea.appendText("Clients: " + numberOfClients + "\n");
        serversConsoleArea.appendText("Port Number: " + portNumber + "\n");
        clientsConsoleArea.appendText("Port Number: " + portNumber + "\n");

        if (tcpRadioButton.isSelected()) {
            serversConsoleArea.appendText("TCP Protocol\n\n");
            clientsConsoleArea.appendText("TCP Protocol\n\n");

            Networks.portNumber = portNumber;
            TCPServer.initialize();

            TCPServer[] tcpServers = new TCPServer[numberOfServers];
            for (int i = 0; i < numberOfServers; ++i) {
                tcpServers[i] = new TCPServer(i + 1);
                tcpServers[i].start();
            }
            TCPClient[] tcpClients = new TCPClient[numberOfClients];
            for (int i = 0; i < numberOfClients; ++i) {
                tcpClients[i] = new TCPClient(i + 1,
                        randomGenerator.nextInt(maxValue),
                        randomGenerator.nextInt(maxValue));
                tcpClients[i].start();
                TimeUnit.MILLISECONDS.sleep(50);
            }
        }

        if (udpRadioButton.isSelected()) {
            clientsConsoleArea.appendText("UDP Protocol\n\n");
            serversConsoleArea.appendText("UDP Protocol\n\n");

            Networks.portNumber = portNumber;
            UDPServer.initialize();

            UDPServer[] udpServers = new UDPServer[numberOfServers];
            for (int i = 0; i < numberOfServers; ++i) {
                udpServers[i] = new UDPServer(i + 1);
                udpServers[i].start();
            }
            UDPClient[] udpClients = new UDPClient[numberOfClients];
            for (int i = 0; i < numberOfClients; ++i) {
                udpClients[i] = new UDPClient(i + 1,
                        randomGenerator.nextInt(maxValue),
                        randomGenerator.nextInt(maxValue));
                udpClients[i].start();
                TimeUnit.MILLISECONDS.sleep(50);
            }
        }
    }

    private void closeServers() throws InterruptedException {
        TCPServer.closeTCPServers();
        UDPServer.closeUdpServers();
        closeServersButton.setDisable(true);
    }

}

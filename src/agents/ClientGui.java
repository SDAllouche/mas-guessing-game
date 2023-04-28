package agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ClientGui extends Application {

    Client client;
    TextField message;
    Button send;

    ObservableList<String> data= FXCollections.observableArrayList();
    public static void main(String[] args) throws ControllerException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        //Create entry interface
        BorderPane pane=new BorderPane();
        Button entry=new Button("Entre");
        entry.setPrefSize(100,40);
        Label label=new Label("Please Write Your Name : ");
        label.setFont(Font.font("Lato", FontWeight.BOLD, 30));
        TextField nameField=new TextField();
        nameField.setPrefSize(100,40);
        VBox vBox=new VBox(15);
        vBox.getChildren().addAll(label,nameField,entry);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0,100,0,100));
        pane.setCenter(vBox);
        Scene scene=new Scene(pane,700,600);
        stage.setScene(scene);
        stage.show();
        entry.setOnAction(actionEvent -> {
            String player=nameField.getText();
            data.add("Welcome "+player+" !!");
            data.add("Guess a number between 1 and 100");
            try {
                startGame(stage,player);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Name Already Exist !");
                alert.show();
            }
        });
    }

    public void startGame(Stage stage,String name) throws StaleProxyException {
        stratContainer(name);
        BorderPane root=new BorderPane();
        root.setPadding(new Insets(20,20,20,20));
        ListView<String> listView=new ListView<>(data);
        send=new Button("Send");
        message=new TextField();
        HBox hBox=new HBox(20,message,send);
        hBox.setPrefHeight(80);
        hBox.setAlignment(Pos.CENTER);
        root.setCenter(listView);
        root.setBottom(hBox);
        Scene scene=new Scene(root,700,600);
        stage.setScene(scene);
        stage.show();
        send.setOnAction(actionEvent -> {
            String msg=message.getText();
            if (msg != null ) {
                GuiEvent guiEvent=new GuiEvent(this,1);
                guiEvent.addParameter(msg);
                client.onGuiEvent(guiEvent);
                message.setText("");
            }
        });
    }

    public void stratContainer(String name) throws StaleProxyException {
        Runtime runtime= Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        jade.wrapper.AgentContainer agentContainer=runtime.createAgentContainer(profile);
        AgentController agentController=agentContainer.createNewAgent(name,"agents.Client",new Object[]{this});
        agentController.start();
    }


    public void setClient(Client client) {
        this.client=client;
    }

    public void setMessage(String msg) {
        data.add(msg);
        if (msg.contains("correct")) {
            message.setEditable(false);
            send.setDisable(true);
        }
    }
}

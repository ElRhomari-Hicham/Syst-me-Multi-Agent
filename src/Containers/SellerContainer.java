package Containers;

import Agents.SellerAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SellerContainer extends Application {
	protected SellerAgent sellerAgent;
	ObservableList<String> observableList ;
	AgentContainer agentContainer;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		startContainer();
		BorderPane pane = new BorderPane();
		primaryStage.setTitle("Seller");
		HBox hbox1 = new HBox();
		Label label = new Label("Agent Name : ");
		TextField text1 = new TextField();
		Button btn = new Button("Deploy");
		hbox1.getChildren().addAll(label,text1,btn);
		hbox1.setSpacing(10);
		hbox1.setAlignment(Pos.CENTER);
		pane.setTop(hbox1);
		VBox vbox1 = new VBox();
		observableList = FXCollections.observableArrayList();
		ListView<String> list = new ListView<String>(observableList);
		vbox1.getChildren().add(list);
		vbox1.setPrefWidth(500);
		pane.setCenter(vbox1);
		btn.setOnAction((evt) -> {
			AgentController agentController;
			try {
				if(!text1.getText().equals("")) {
					agentController = agentContainer.createNewAgent(text1.getText() , "Agents.SellerAgent", 
							new Object[] {this});
					agentController.start();
				}else {
					System.out.println("Impossible de creer un agent avec une chaine vide");
				}
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Scene scene = new Scene(pane,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void startContainer() {
		Runtime runtime = Runtime.instance();
		ProfileImpl profile = new ProfileImpl();
		profile.setParameter(profile.MAIN_HOST, "localhost");
		agentContainer = runtime.createAgentContainer(profile);
		try {
			agentContainer.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logMessages(ACLMessage msg) {
		Platform.runLater(() -> {
			observableList.add(msg.getContent()+" , "+msg.getSender().getName());
		});
	}

	public SellerAgent getSellerAgent() {
		return sellerAgent;
	}

	public void setSellerAgent(SellerAgent sellerAgent) {
		this.sellerAgent = sellerAgent;
	}

}

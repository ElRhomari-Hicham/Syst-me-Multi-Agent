package Containers;
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
import Agents.BuyerAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class BuyerContainer extends Application {
	
	protected BuyerAgent buyerAgent;
	ObservableList<String> observableList ;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		startContainer();
		BorderPane pane = new BorderPane();
		primaryStage.setTitle("BUYER");
		HBox hbox1 = new HBox();
		Label label = new Label("Liste des Livres");
		hbox1.getChildren().addAll(label);
		hbox1.setSpacing(10);
		hbox1.setAlignment(Pos.CENTER);
		pane.setTop(hbox1);
		VBox vbox1 = new VBox();
		observableList = FXCollections.observableArrayList();
		ListView<String> list = new ListView<String>(observableList);
		vbox1.getChildren().add(list);
		vbox1.setPrefWidth(500);
		pane.setCenter(vbox1);
		Scene scene = new Scene(pane,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void startContainer() {
		Runtime runtime = Runtime.instance();
		ProfileImpl profile = new ProfileImpl();
		profile.setParameter(profile.MAIN_HOST, "localhost");
		AgentContainer agentContainer = runtime.createAgentContainer(profile);
		AgentController agentController;
		try {
			agentController = agentContainer.createNewAgent("ACHETEUR", "Agents.BuyerAgent", new Object[] {this});
			agentController.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logMessages(ACLMessage msg) {
		Platform.runLater(() -> {
			observableList.add(msg.getContent());
		});
	}

	public BuyerAgent getBuyerAgent() {
		return buyerAgent;
	}

	public void setBuyerAgent(BuyerAgent buyerAgent) {
		this.buyerAgent = buyerAgent;
	}
}

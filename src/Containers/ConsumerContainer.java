package Containers;

import Agents.ConusmerAgent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;;

public class ConsumerContainer extends Application {
	
	protected ConusmerAgent consumerAgent;
	private ObservableList<String> observableList;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}
	
	public void CreateContainer() {
		Runtime runtime = Runtime.instance();
		ProfileImpl profile = new ProfileImpl();
		profile.setParameter(profile.MAIN_HOST, "localhost");
		AgentContainer container = runtime.createAgentContainer(profile);
		try {
			AgentController agentController = container.createNewAgent("Consumer", "Agents.ConusmerAgent", 
					new Object[] {this});
			agentController.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		CreateContainer();
		BorderPane pane = new BorderPane();
		primaryStage.setTitle("CONSUMOR");
		HBox hbox1 = new HBox();
		Label label = new Label("Livre : ");
		TextField text1 = new TextField();
		Button btn = new Button("Acheter");
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
		btn.setOnAction(evt -> {
			String livre = text1.getText();
			if(!livre.equals(" ")) {
				GuiEvent event = new GuiEvent(this,1);
				event.addParameter(livre);
				consumerAgent.onGuiEvent(event);
				text1.clear();
			}else {
				System.out.println("Impossible d'inserer une chaine vide");
			}
		});
		Scene scene = new Scene(pane,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void logMessages(ACLMessage msg) {
		Platform.runLater( () -> {
			observableList.add(msg.getContent()+" , "+msg.getSender());
		});
	}

	public ConusmerAgent getConsumerAgent() {
		return consumerAgent;
	}

	public void setConsumerAgent(ConusmerAgent consumerAgent) {
		this.consumerAgent = consumerAgent;
	}

}

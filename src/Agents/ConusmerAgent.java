package Agents;

import Containers.ConsumerContainer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class ConusmerAgent extends GuiAgent {
	
	private transient ConsumerContainer gui;
	
	protected void setup() {
		System.out.println("*********************");
		System.out.println("Consumor Agent Intialization : "+ this.getAID().getName());
		System.out.println("*********************");
		if(getArguments().length == 1) {
			System.out.println("Je vais acheter le livre : "+getArguments()[0]);
			gui = (ConsumerContainer) getArguments()[0];
			gui.setConsumerAgent(this);
		}
		
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);

		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				ACLMessage msg = receive();
				if(msg != null) {
					switch(msg.getPerformative()) {
						case ACLMessage.CONFIRM:
								gui.logMessages(msg);
							break;
						default:
							break;
					}
				}else { block(); }
			}
		});
	}
	
	protected void beforeMove() {
		System.out.println("*********************");
		System.out.println("Before Moving");
		System.out.println("*********************");
	}
	protected void afterMove() {
		System.out.println("*********************");
		System.out.println("After Moving");
		System.out.println("*********************");
	}
	protected void takeDown() {
		System.out.println("*********************");
		System.out.println("I am dying ...........");
		System.out.println("*********************");
	}
	@Override
	public void onGuiEvent(GuiEvent event) {
		// TODO Auto-generated method stub
		if(event.getType()==1) {
			String livre = (String) event.getParameter(0);
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.setContent(livre);
			message.addReceiver(new AID("ACHETEUR", AID.ISLOCALNAME));
			send(message);
		}
	}
}



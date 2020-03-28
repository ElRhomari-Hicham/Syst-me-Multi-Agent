package Agents;

import java.util.Random;

import Containers.BuyerContainer;
import Containers.SellerContainer;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class SellerAgent extends GuiAgent {
	
	private transient SellerContainer gui;
	
	protected void setup() {
		System.out.println("*********************");
		System.out.println("Seller Agent Intialization : "+ this.getAID().getName());
		System.out.println("*********************");
		if(getArguments().length == 1) {
			System.out.println("Je vais acheter le livre : "+getArguments()[0]);
			gui = (SellerContainer) getArguments()[0];
			gui.setSellerAgent(this);
		}
		
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		
		parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				DFAgentDescription agentDescription = new DFAgentDescription();
				agentDescription.setName(getAID());
				ServiceDescription sd = new ServiceDescription();
				sd.setType("book-selling");
				sd.setName("JADE-book-trading");
				agentDescription.addServices(sd);
				try {
					DFService.register(myAgent, agentDescription);
				} catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				ACLMessage msg = receive();
				if(msg != null) {
					gui.logMessages(msg);
					switch(msg.getPerformative()){
						case ACLMessage.CFP:
							ACLMessage propose = msg.createReply();
							propose.setPerformative(ACLMessage.PROPOSE);
							propose.setContent(String.valueOf((500 + new Random().nextInt(1000))));
							send(propose);
							break;
							
						case ACLMessage.ACCEPT_PROPOSAL:
							ACLMessage agree = msg.createReply();
							agree.setPerformative(ACLMessage.AGREE);
							agree.setContent("Le choix est ==>"+msg.getContent());
							send(agree);
							break;
					}	
				}else {
					block();
				}
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
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub	
	}
}




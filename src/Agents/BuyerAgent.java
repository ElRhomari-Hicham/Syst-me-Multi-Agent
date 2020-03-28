package Agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Containers.BuyerContainer;
import Containers.SellerContainer;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BuyerAgent extends GuiAgent {
	
	private transient BuyerContainer gui;
	private AID[] sellerAgents;
	private List<ACLMessage> replies = new ArrayList<ACLMessage>();
	
	protected void setup() {
		System.out.println("*********************");
		System.out.println("Buyer Agent Intialization : "+ this.getAID().getName());
		System.out.println("*********************");
		if(getArguments().length == 1) {
			System.out.println("Je vais acheter le livre : "+getArguments()[0]);
			gui = (BuyerContainer) getArguments()[0];
			gui.setBuyerAgent(this);
		}
		
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		
		parallelBehaviour.addSubBehaviour(new TickerBehaviour(this, 10000) {

			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("book-selling");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template);
					sellerAgents = new AID[result.length];
					for(int i=0; i<result.length; i++) {
						sellerAgents[i] = result[i].getName();
					}
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
				MessageTemplate aclTemplate = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), 
								MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.AGREE), 
										MessageTemplate.MatchPerformative(ACLMessage.REFUSE))));
				ACLMessage msg = receive(aclTemplate);
				if(msg != null) {
					
					gui.logMessages(msg);
					ACLMessage reply = msg.createReply();
					reply.setContent("OK pour "+msg.getContent());
					send(reply);
						
					switch(msg.getPerformative()) {
						case (ACLMessage.REQUEST):
							ACLMessage msgVente = new ACLMessage(ACLMessage.CFP);
							msgVente.setContent(msg.getContent());
							for(AID id:sellerAgents) {
								msgVente.addReceiver(id);
							}
							send(msgVente);
							break;
						
						case (ACLMessage.PROPOSE):
							replies.add(msg);
							if(replies.size() == sellerAgents.length) {
								ACLMessage bestOffer = replies.get(0);
								double min = Double.parseDouble(replies.get(0).getContent());
								for(ACLMessage msgs:replies) {
									double price = Double.parseDouble(msgs.getContent());
									if(price < min) {
										bestOffer = msgs;
										min = price;
									}
								}
								ACLMessage aclMessageAccept = bestOffer.createReply();
								aclMessageAccept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
								aclMessageAccept.setContent(bestOffer.getContent());
								send(aclMessageAccept);
							}
							break;
						
						case (ACLMessage.AGREE):
							ACLMessage confirm = new ACLMessage(ACLMessage.CONFIRM);
							confirm.setContent("Bien Confirmé : "+msg.getContent());
							confirm.addReceiver(new AID("Consumer", AID.ISLOCALNAME));
							send(confirm);
							break;
						
						case (ACLMessage.REFUSE):
							
							break;
						
						default:
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
	}

	@Override
	public void onGuiEvent(GuiEvent event) {
		// TODO Auto-generated method stub
	}

}

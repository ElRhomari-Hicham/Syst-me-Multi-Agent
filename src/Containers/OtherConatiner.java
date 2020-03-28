package Containers;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class OtherConatiner {

	public static void main(String[] args) {
		
		Runtime runtime = Runtime.instance();
		ProfileImpl profileImpl = new ProfileImpl();
		profileImpl.setParameter(profileImpl.MAIN_HOST, "localhost");
		AgentContainer conainer = runtime.createAgentContainer(profileImpl);
		try {
			conainer.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

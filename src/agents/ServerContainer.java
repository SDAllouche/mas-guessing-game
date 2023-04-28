package agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServerContainer {

    public static void main(String[] args) throws ControllerException {
        Runtime runtime= Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        AgentContainer agentContainer=runtime.createAgentContainer(profile);
        AgentController agentController=agentContainer.createNewAgent("Server","agents.Server",new Object[]{"Server"});
        agentController.start();
    }
}

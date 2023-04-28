package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server extends Agent {

    List<AID> players=new ArrayList<>();
    int number=new Random().nextInt(100);

    @Override
    protected void setup() {

        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);

        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage meesageRequest=receive();
                if (meesageRequest != null) {
                    AID player = meesageRequest.getSender();
                    int nbr=Integer.parseInt(meesageRequest.getContent());
                    startGame(nbr,player);
                } else{
                    block();
                }
            }
        });
    }

    public void startGame(int n,AID player){
        if (!players.contains(player)) {
            players.add(player);
        }
        if (number > n) {
            ACLMessage message=new ACLMessage(ACLMessage.INFORM);
            message.addReceiver(player);
            message.setContent("The number "+n+" is low");
            send(message);
        } else if (number<n) {
            ACLMessage message=new ACLMessage(ACLMessage.INFORM);
            message.addReceiver(player);
            message.setContent("The number "+n+" is high");
            send(message);
        } else if (number==n) {
            ACLMessage message=new ACLMessage(ACLMessage.INFORM);
            String msg="The number "+number+" is correct, Congratulations !!";
            message.addReceiver(player);
            message.setContent(msg);
            send(message);
            players.remove(player);
            for (AID p:players) {
                ACLMessage aclMessage=new ACLMessage(ACLMessage.INFORM);
                msg="Game over, "+number+" is the correct number";
                aclMessage.addReceiver(p);
                aclMessage.setContent(msg);
                send(aclMessage);
            }
        }
    }
}

package Radar_Simulator;

import ATC_Core.Message;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

/**
 * Created by saija on 2017-04-08.
 */
public class ATCCommunication extends TextArea {
    public enum Contact { PILOT, ATC };
    public ATCCommunication() {
        super();
        for (Node node : lookupAll(".scroll-pane"))
        {
            if (node instanceof ScrollPane)
            {
                ((ScrollPane) node).setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            }
        }
    }

    public void recordMessage(Contact contact, Message message) {
        if (contact == Contact.PILOT) {
            recordPilotMessage(message);
        }
        else {
            recordATCMessage(message);
        }
    }

    private void recordPilotMessage(Message message) {
        String messageStr = message.getTimestamp().toString() + " - PILOT: " + message.getMessage() + "\n";
        appendText(messageStr);
    }

    private void recordATCMessage(Message message) {
        String messageStr = message.getTimestamp().toString() + " - ATC: " + message.getMessage() + "\n";
        appendText(messageStr);
    }

    public void clearMessages() {
        clear();
    }
}

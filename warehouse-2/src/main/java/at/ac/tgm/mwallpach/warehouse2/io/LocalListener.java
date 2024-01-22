package at.ac.tgm.mwallpach.warehouse2.io;

import at.ac.tgm.mwallpach.warehouse2.Warehouse2Application;
import at.ac.tgm.mwallpach.warehouse2.model.WarehouseData;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LocalListener implements MessageListener {


    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final boolean isMain;
    private final String topic;

    public LocalListener(String topic, boolean isMain) {
        this.topic = topic;
        this.isMain = isMain;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();
            logger.info("Received message: " + text);



            if(this.topic.equals("WarehouseTopic_Main")) {
                // listened auf die ACKS (auf die Topic)
                logger.info("acknowledged: " + text);
                Warehouse2Application.timestampsToAcknowledge.remove(text);

            }
            else {
                // listened auf die JSON Nachrichten (auf die Queues)
                logger.info("got data from queue: " + text);
                Warehouse2Application.savedData.add(text);
            }

            (message).acknowledge();
        } catch (Exception e) {
            logger.error("Error processing message", e);
        }
    }

}
package at.ac.tgm.mwallpach.warehouse2.io;

import at.ac.tgm.mwallpach.warehouse2.model.WarehouseData;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LocalListener implements MessageListener {

    public static ArrayList<String> data = new ArrayList<>();

    Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final boolean isMain;

    public LocalListener(boolean isMain) {
        this.isMain = isMain;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();
            if(this.isMain && !text.startsWith("Main:")) {
                logger.info("Received " + text);
                data.add(text);
            }else if(!this.isMain && text.startsWith("Main:")) {
                logger.info("Time From " + text);
            }
//            ((TextMessage) message).acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

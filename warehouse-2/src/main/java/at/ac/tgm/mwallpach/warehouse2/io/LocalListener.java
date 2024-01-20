package at.ac.tgm.mwallpach.warehouse2.io;

import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LocalListener implements MessageListener {

    public static final List<String> data = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final boolean isMain;

    public LocalListener(boolean isMain) {
        this.isMain = isMain;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();
            if (this.isMain && !text.startsWith("Main:")) {
                logger.info("Received " + text);
                data.add(text);
            } else if (!this.isMain && text.startsWith("Main:")) {
                logger.info("Time From " + text);
            }
        } catch (Exception e) {
            logger.error("Error processing message", e);
        }
    }

    public static List<String> getData() {
        return data;
    }
}

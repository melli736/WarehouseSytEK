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
    // Statische Map zur Speicherung empfangener Daten für jeden Lagerstandort
    public static Map<String, List<String>> dataMap = new HashMap<>();

    // Statische Liste zur temporären Speicherung empfangener Daten
    public static ArrayList<String> data = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(Receiver.class);

    // Flag, um zu unterscheiden, ob der Hauptlistener (Zentrale) oder ein Nebenlistener (Lager) ist
    private final boolean isMain;

    // Sender für Bestätigungen
    private final Sender acknowledgementSender;

    // Konstruktor
    public LocalListener(boolean isMain, Sender acknowledgementSender) {
        this.isMain = isMain;
        this.acknowledgementSender = acknowledgementSender;
    }

    // Methode wird bei Empfang einer Nachricht aufgerufen
    @Override
    public void onMessage(Message message) {
        try {
            logger.info("message received, this is main: " + isMain);

            if (this.isMain) {
                if (message instanceof TextMessage) {
                    // Verarbeitung von Textnachrichten von Nebenstellen (Lagern)
                    String text = ((TextMessage) message).getText();
                    logger.info("Received " + text);
                    data.add(text);
                    logReceivedData(text, "Main");

                    // Senden einer Bestätigung an das entsprechende Lager
                    sendAcknowledgement();
                } else {
                    logger.warn("Unexpected message type received in main listener");
                }
            } else {
                if (message instanceof ObjectMessage) {
                    // Verarbeitung von Objektnachrichten von Hauptstelle (Zentrale)
                    WarehouseData warehouseData = ((ObjectMessage) message).getBody(WarehouseData.class);
                    logger.info("Time From " + warehouseData.getTimestamp());
                } else {
                    logger.warn("Unexpected message type received in non-main listener");
                }
            }
        } catch (Exception e) {
            logger.error("Error processing message: " + e.getMessage(), e);
        }
    }

    // Private Methode zum Protokollieren der empfangenen Daten
    private void logReceivedData(String data, String location) {
        logger.info("Logged data at " + location + ": " + data);
    }

    // Private Methode zum Senden einer Bestätigung an das entsprechende Lager
    public void sendAcknowledgement() {
        if (acknowledgementSender != null) {
            acknowledgementSender.sendMessageToTopic("SUCCESS: Acknowledgement for message received.");
            logger.info("Sent acknowledgement to warehouse");
        } else {
            logger.warn("No acknowledgement sender available");
        }
    }
}

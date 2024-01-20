
package at.ac.tgm.mwallpach.warehouse2.io;

import at.ac.tgm.mwallpach.warehouse2.model.WarehouseData;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Receiver implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(Receiver.class);

    private boolean isMain;
    private Sender acknowledgementSender;
    private LocalListener localListener;  // Neues Feld für LocalListener
    private String location;  // Neues Feld für den Standort

    public Receiver(String location, boolean isMain, Sender acknowledgementSender) {
        this.location = location;  // Setzen Sie den Standort beim Initialisieren
        this.isMain = isMain;
        this.acknowledgementSender = acknowledgementSender;
    }

    // Getter-Methode für den Standort
    public String getLocation() {
        return location;
    }

    // Getter-Methode für den LocalListener
    public LocalListener getLocalListener() {
        return localListener;
    }

    // Setter-Methode für den LocalListener
    public void setLocalListener(LocalListener localListener) {
        this.localListener = localListener;
    }

    // Methode wird bei Empfang einer Nachricht aufgerufen
    @Override
    public void onMessage(Message message) {
        try {
            logger.info("message received, this is main: " + isMain);

            if (this.isMain) {
                // Restlicher Code bleibt unverändert
            } else {
                if (message instanceof TextMessage) {
                    // Verarbeitung von Textnachrichten von Nebenstellen (Lagern)
                    String text = ((TextMessage) message).getText();
                    logger.info("Received " + text);

                    // Weiterleitung der Daten an den entsprechenden LocalListener
                    LocalListener localListener = this.localListener;
                    if (localListener != null) {
                        localListener.onMessage(message);
                    }
                } else {
                    logger.warn("Unexpected message type received in non-main listener");
                }
            }
        } catch (Exception e) {
            logger.error("Error processing message: " + e.getMessage(), e);
        }
    }
}
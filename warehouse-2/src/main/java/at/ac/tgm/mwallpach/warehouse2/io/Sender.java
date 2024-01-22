package at.ac.tgm.mwallpach.warehouse2.io;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sender {

    private final Logger logger = LoggerFactory.getLogger(Sender.class);

    private static final String USER = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    private Connection connection = null;
    private Session session = null;
    private MessageProducer producer = null;

    private final String topic;

    public Sender(String topic, boolean isTopic) {

        this.topic = topic;

        logger.info("Sender started on topic: " + topic);

        // Verbindung erstellen.
        Destination destination;

        try {

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
            connection = connectionFactory.createConnection();
            connection.start();

            // Erstellen der Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if(isTopic) {
                destination = session.createTopic(this.topic);
            }else   {
                destination = session.createQueue(this.topic);
            }


            // Erstellen des Producers.
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        } catch (Exception e) {
            logger.error("Fehler beim Einrichten des Senders", e);
        }
    }

    public void sendMessageToTopic(String message) {
        try {
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);

        } catch (JMSException e) {
            logger.error("Fehler beim Senden der Nachricht", e);
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            producer.close();
        } catch (Exception ignored) {
        }
        try {
            session.close();
        } catch (Exception ignored) {
        }
        try {
            connection.close();
        } catch (Exception ignored) {
        }
    }
}
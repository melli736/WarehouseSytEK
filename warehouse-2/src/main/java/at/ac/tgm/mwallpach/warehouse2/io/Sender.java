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

    public Sender(String topic) {

        this.topic = topic;

        logger.info("Sender started on topic: " + topic);

        // Create the connection.
        Destination destination;

        try {

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
            connection = connectionFactory.createConnection();
            connection.start();

            // Create the session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic(this.topic);

            // Create the producer.
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        } catch (Exception e) {
            logger.error("Error setting up sender", e);
        }
    }

    public void sendMessageToTopic(String message) {
        try {
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);

        } catch (JMSException e) {
            logger.error("Error sending message", e);
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

package at.ac.tgm.mwallpach.warehouse2.io;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver {

    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private static final String USER = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    private Connection connection = null;
    private Session session = null;
    private MessageConsumer consumer = null;

    private final String topic;

    public Receiver(String topic, boolean isTopic) {

        this.topic = topic;

        logger.info("Receiver started on topic: " + topic);

        Destination destination;

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
            connection = connectionFactory.createConnection();
            connection.start();

            // Erstellen der Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if(isTopic) {
                destination = session.createTopic(this.topic);
            }
            else {
                destination = session.createQueue(this.topic);
            }

            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new LocalListener(topic, isTopic));

        } catch (Exception e) {
            logger.error("Fehler beim Einrichten des Receivers", e);
        }
    }

    public void stop() {
        try {
            consumer.close();
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
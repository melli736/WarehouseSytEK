package at.ac.tgm.mwallpach.warehouse2.io;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class Sender {

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    private static final String user = ActiveMQConnection.DEFAULT_USER;
    private static final String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    private Connection connection = null;
    private Session session = null;
    private MessageProducer producer = null;

    private final String topic;

    public Sender(String topic) {
        this.topic = topic;

        logger.info("Sender started on topic: " + topic);

        Destination destination;

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
            connection = connectionFactory.createConnection();
            connection.start();

            // Create the session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if (topic.startsWith("war")) {
                destination = session.createQueue(this.topic);
            } else {
                destination = session.createTopic(this.topic);
            }

            // Create the producer.
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (Exception e) {
            logger.error("[MessageProducer] Caught: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToTopic(String message) {
        try {
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);
        } catch (JMSException e) {
            logger.error("Error sending message to topic: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToTopic(Object object) {
        try {
            ObjectMessage objectMessage = session.createObjectMessage();
            objectMessage.setObject((Serializable) object);
            producer.send(objectMessage);
        } catch (JMSException e) {
            logger.error("Error sending object message to topic: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String getTopic() {
        return topic;
    }

    public void stop() {
        try {
            producer.close();
        } catch (Exception e) {
            logger.error("Error closing producer: " + e.getMessage(), e);
        }
        try {
            session.close();
        } catch (Exception e) {
            logger.error("Error closing session: " + e.getMessage(), e);
        }
        try {
            connection.close();
        } catch (Exception e) {
            logger.error("Error closing connection: " + e.getMessage(), e);
        }
    }
}
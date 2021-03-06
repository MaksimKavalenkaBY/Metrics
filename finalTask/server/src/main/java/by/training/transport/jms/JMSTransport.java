package by.training.transport.jms;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import by.training.constants.JMSKeyConstants;

public class JMSTransport {

    private static BrokerService broker;
    private static Connection    connection;
    private static Session       session;

    public static void publish() throws URISyntaxException, Exception {
        broker = BrokerFactory.createBroker(new URI("broker:(tcp://localhost:8084)"));
        broker.start();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost:8084");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(JMSKeyConstants.SERVER_QUEUE);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new ServerMessageListener(session));
        connection.start();
    }

    public static void shutdown() throws Exception {
        session.close();
        if (connection != null) {
            connection.close();
        }
        broker.stop();
    }

}

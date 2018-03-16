package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.naming.InitialContext;

public class Receiver {
    public void Receive(MessageListener listener) {
        try
        {
            InitialContext ctx = new InitialContext();

            ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("bank");
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(listener);
            connection.start();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}

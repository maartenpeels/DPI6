package messaging;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import models.ISendable;

public class Sender {
    public void SendObject(ISendable obj) {
        Connection connection = null;
        try {
            Context ctx = new InitialContext();
            ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            connection = cf.createConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("bank");

            MessageProducer messageProducer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();
            message.setObject(obj);
            messageProducer.send(message);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    System.out.println(e);
                }
            }
        }
    }
}

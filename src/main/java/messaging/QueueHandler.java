package messaging;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import models.ISendable;
import models.bank.BankInterestReply;
import models.bank.BankInterestRequest;
import models.loan.LoanReply;
import models.loan.LoanRequest;


public class QueueHandler {
    private Connection connection = null;
    private Session session = null;
    private MessageProducer messageProducer = null;
    private MessageConsumer messageConsumer = null;

    private Queue loanRequestQueue = null;
    private Queue loanReplyQueue = null;
    private Queue bankInterestReplyQueue = null;
    private Queue bankInterestRequestQueue = null;

    private void OpenConnection() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        connection = cf.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        loanRequestQueue = session.createQueue(QUEUE.loanRequest);
        loanReplyQueue = session.createQueue(QUEUE.loanReply);
        bankInterestReplyQueue = session.createQueue(QUEUE.bankInterestReply);
        bankInterestRequestQueue = session.createQueue(QUEUE.bankInterestRequest);
    }

    public void SendObject(ISendable obj) {
        try {
            if (connection == null) {
                OpenConnection();
            }

            if (obj instanceof LoanRequest) {
                messageProducer = session.createProducer(loanRequestQueue);
            } else if (obj instanceof LoanReply) {
                messageProducer = session.createProducer(loanReplyQueue);
            } else if (obj instanceof BankInterestRequest) {
                messageProducer = session.createProducer(bankInterestRequestQueue);
            } else if (obj instanceof BankInterestReply) {
                messageProducer = session.createProducer(bankInterestReplyQueue);
            }

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

    public void Receive(String queueName, MessageListener listener) {
        try {
            if (connection == null) {
                OpenConnection();
            }

            if(queueName.equals(QUEUE.loanRequest)) {
                messageConsumer = session.createConsumer(loanRequestQueue);
            }else if(queueName.equals(QUEUE.loanReply)) {
                messageConsumer = session.createConsumer(loanReplyQueue);
            }else if(queueName.equals(QUEUE.bankInterestRequest)) {
                messageConsumer = session.createConsumer(bankInterestRequestQueue);
            }else if(queueName.equals(QUEUE.bankInterestReply)) {
                messageConsumer = session.createConsumer(bankInterestReplyQueue);
            }

            messageConsumer.setMessageListener(listener);
            connection.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

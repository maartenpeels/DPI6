package loanbroker;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import messaging.QUEUE;
import messaging.QueueHandler;
import models.ISendable;
import models.bank.BankInterestReply;
import models.bank.BankInterestRequest;
import models.loan.LoanReply;
import models.loan.LoanRequest;

public class LoanBrokerFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultListModel<JListLine> listModel = new DefaultListModel<JListLine>();
    private JList<JListLine> list;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoanBrokerFrame frame = new LoanBrokerFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Create the frame.
     */
    public LoanBrokerFrame() {
        new QueueHandler().Receive(QUEUE.loanRequest, new MessageListener() {
            public void onMessage(Message message) {
                if (message instanceof ObjectMessage) {
                    ISendable obj = null;
                    try {
                        obj = (ISendable)((ObjectMessage) message).getObject();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    if(obj instanceof LoanRequest){
                        LoanRequest req = (LoanRequest)obj;
                        System.out.println(req.toString());

                        BankInterestRequest bir = new BankInterestRequest();
                        bir.setTime(req.getTime());
                        bir.setAmount(req.getAmount());
                        bir.setLoanRequest(req);

                        add(req);
                        add(req, bir);

                        new QueueHandler().SendObject(bir);
                    }
                }
            }
        });

        new QueueHandler().Receive(QUEUE.bankInterestReply, new MessageListener() {
            public void onMessage(Message message) {
                if (message instanceof ObjectMessage) {
                    ISendable obj = null;
                    try {
                        obj = (ISendable)((ObjectMessage) message).getObject();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    if(obj instanceof BankInterestReply){
                        BankInterestReply req = (BankInterestReply)obj;
                        LoanRequest lreq = req.getLoanRequest();
                        System.out.println(req.toString());
                        System.out.println(lreq.toString());
                        add(lreq, req);

                        LoanReply loanReply = new LoanReply();
                        loanReply.setQuoteID(req.getQuoteId());
                        loanReply.setInterest(req.getInterest());
                        loanReply.setLoanRequest(lreq);
                        new QueueHandler().SendObject(loanReply);
                    }
                }
            }
        });

        setTitle("Loan Broker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{46, 31, 86, 30, 89, 0};
        gbl_contentPane.rowHeights = new int[]{233, 23, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 7;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);

        list = new JList<JListLine>(listModel);
        scrollPane.setViewportView(list);
    }

    private JListLine getRequestReply(LoanRequest request) {

        for (int i = 0; i < listModel.getSize(); i++) {
            JListLine rr = listModel.get(i);
            if(rr.getLoanRequest().getSsn() == request.getSsn()
                    && rr.getLoanRequest().getTime() == request.getTime()
                    && rr.getLoanRequest().getAmount() == request.getAmount()) {
                return rr;
            }
            if (rr.getLoanRequest() == request) {
                return rr;
            }
        }

        return null;
    }

    public void add(LoanRequest loanRequest) {
        listModel.addElement(new JListLine(loanRequest));
    }


    public void add(LoanRequest loanRequest, BankInterestRequest bankRequest) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankRequest != null) {
            rr.setBankRequest(bankRequest);
            list.repaint();
        }
    }

    public void add(LoanRequest loanRequest, BankInterestReply bankReply) {
        JListLine rr = getRequestReply(loanRequest);
        if (rr != null && bankReply != null) {
            rr.setBankReply(bankReply);
            list.repaint();
        }
    }


}

package models.loan;

import models.ISendable;

/**
 * This class stores all information about a bank offer
 * as a response to a client loan request.
 */
public class LoanReply implements ISendable{

    private double interest; // the interest that the bank offers
    private String bankID; // the unique quote identification
    private LoanRequest loanRequest;

    public LoanReply() {
        super();
        this.interest = 0;
        this.bankID = "";
        this.loanRequest = null;
    }

    public LoanReply(double interest, String quoteID) {
        super();
        this.interest = interest;
        this.bankID = quoteID;
        this.loanRequest = null;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getQuoteID() {
        return bankID;
    }

    public void setQuoteID(String quoteID) {
        this.bankID = quoteID;
    }

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    public void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    @Override
    public String toString() {
        return " interest=" + String.valueOf(interest) + " quoteID=" + String.valueOf(bankID);
    }
}

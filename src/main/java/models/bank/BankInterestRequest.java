package models.bank;

import models.ISendable;
import models.loan.LoanRequest;

/**
 * This class stores all information about an request from a bank to offer
 * a loan to a specific client.
 */
public class BankInterestRequest implements ISendable{

    private int amount; // the requested loan amount
    private int time; // the requested loan period
    private LoanRequest loanRequest;

    public BankInterestRequest() {
        super();
        this.amount = 0;
        this.time = 0;
        this.loanRequest = null;
    }

    public BankInterestRequest(int amount, int time, LoanRequest loanRequest) {
        super();
        this.amount = amount;
        this.time = time;
        this.loanRequest = loanRequest;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    public void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    @Override
    public String toString() {
        return " amount=" + amount + " time=" + time;
    }
}

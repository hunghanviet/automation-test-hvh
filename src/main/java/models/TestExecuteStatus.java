package models;

public class TestExecuteStatus {
    private int TC;
    private int TCPass;
    private int TCFail;
    private int TCPending;

    public TestExecuteStatus() {
        this.TC = 0;
        this.TCPass = 0;
        this.TCFail = 0;
        this.TCPending = 0;
    }

    public int getTC() {
        return TC;
    }

    public void setTC(int TC) {
        this.TC = TC;
    }

    public int getTCPass() {
        return TCPass;
    }

    public void setTCPass(int TCPass) {
        this.TCPass = TCPass;
    }

    public int getTCFail() {
        return TCFail;
    }

    public void setTCFail(int TCFail) {
        this.TCFail = TCFail;
    }

    public int getTCPending() {
        return TCPending;
    }

    public void setTCPending(int TCPending) {
        this.TCPending = TCPending;
    }
}

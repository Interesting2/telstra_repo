package au.com.telstra.simcardactivator.dto;

public class SimActivationRequest {
    private String iccid;
    private String customerEmail;


    public SimActivationRequest() {}
    public SimActivationRequest(String iccid) {
        this.iccid = iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getIccid() {
        return this.iccid;
    }

    public void setCustomerEmail(String email) {
        this.customerEmail = email;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 * AppointmentReport
 * @author shipleym
 * Definitions to be used by the system for the Reports applications
 * Class for object to place in list for Appointment Types by Month Report
 */
public class AppointmentReport {
    
    private String month;
    private String amount;
    private String type;    

    /**
     * Parameters used to create the detail for sql report of types of appointments by month
     * @param month
     * @param type
     * @param amount
     */
    public AppointmentReport(String month, String type, String amount) {
        this.month = month;
        this.type = type;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    //for troubleshooting during development
    @Override
    public String toString() {
    return "Month: " + this.month +
            " Type: " + this.type +
            " Amt: " + this.amount + ".\n" ;
    }
    
    
}

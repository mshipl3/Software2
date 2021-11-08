/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 * @author shipleym
 * Appointments
 * Definitions to be used by the system for the Appointments table
 */
public class Appointments {
    
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private int locationId;
    private String type;
    private String startD;
    private String start;
    private String end;
    private String createDate;
    private String createdBy;
    private String lastUpdated;
    private String lastUpdatedBy;
    private int customerId;
    private String customerName;
    private int userId;
    private String user;
    private int contactId;
    private String contactName;
    private int countryId;
    private String countryName;
    
    public Appointments() {
    }

    public Appointments(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     *
     * @param appointmentId
     * @param title
     * @param description
     * @param location
     * @param locationId
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param contactId
     * @param contactName
     * @param customerName
     * @param userId
     * @param user
     * @param countryId
     * @param countryName
     */
    public Appointments(int appointmentId, String title, String description, String location, int locationId, String type, String start, String end, int customerId, int contactId,
                        String contactName, String customerName, int userId, String user, int countryId, String countryName) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.locationId = locationId;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.contactId = contactId;
        this.contactName = contactName;
        this.customerName = customerName;
        this.userId = userId;
        this.user = user;
        this.countryId = countryId;
        this.countryName = countryName;
    }
  
    public Appointments(int appointmentId, String title, String description, String location, String type, String start, String end, String createDate, 
                       String createdby, String lastUpdated, String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;    
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdby;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }
    
    public Appointments(int appointmentId, String title, String type, String start, String end, int customerId, String user, int userId) {
        this.appointmentId = appointmentId;    
        this.title = title;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.user = user;
        this.userId = userId;
    }

    public Appointments(String start, String end, int userId) {
        this.start = start;
        this.end = end;
        this.userId = userId;
    }

    public Appointments(int appointmentId, String title, String description, String location, String type, String startD, String start, String end, int customerId, String customerName, int userId, int contactId, String contactName) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startD = startD;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.customerName = customerName;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    public Appointments(String tAppointmentId, String tTitle, String tType, String format, String format0, Customers tCustomer, String tUser, String tUserId) {
     }

    public Appointments(int appointmentId, String title, String description, String location, String type, String start, String end, int customerId, int contactId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getStartD() {
        return startD;
    }

    public void setStartD(String startD) {
        this.startD = startD;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
    
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
		
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
}

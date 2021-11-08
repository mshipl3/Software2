/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 * Customers
 * @author shipleym
 * Definitions to be used by the system for the Customers table
 */
public class Customers {
    
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private String createDate;
    private String createdBy;
    private String lastUpdated;
    private String lastUpdatedBy;
    private int divisionId;
    private String division;    
    private int countryId;
    private String country;    
    
    public Customers(){
        
    }

    public Customers(int customerId, String customerName, String address, String postalCode, String phone, String createDate, String createdBy, String lastUpdated, 
                    String lastUpdatedBy, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    public Customers(int customerId, String customerName, String address, String postalCode, String phone, int divisionId, String division, int countryId, String country) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
        this.country = country;
    }
    
    public Customers (int customerId, String customerName){
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public Customers(String customerName) {
        this.customerName = customerName;
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
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String setPhone() {
        return this.phone;
    }

    public String setCreateDate() {
        return this.createDate;
    }
    
    public String getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String setCreatedBy() {
        return this.createdBy;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String setLastUpdated() {
        return this.lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public String setLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public int getDivisionID(){
        return divisionId;
    }
    
    public int setDivisionID(){
        return this.divisionId;
    }
    
    public String getDivision(){
        return division;
    }
    
    public String setDivision(){
        return this.division;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    @Override
    public String toString() {
        return customerName;
    }
    
    
    
}

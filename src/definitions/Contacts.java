/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 * @author SHIPLEYM
 * Contacts
 * Definitions to be used by the system for the contacts table
 */
public class Contacts {

    private int contactId;
    private String contactName;
    private String email;
    
    public Contacts(int contactId, String contactName, String email){
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    public Contacts(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;
    }
    
    public int getContactID(){
        return contactId;
    }
    
    public void setContactID(int contactId){
        this.contactId = contactId;
    }
    
    public String getContactName(){
        return contactName;
    }
    
    public void setContactName(String contactName){
        this.contactName = contactName;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    @Override
    public String toString() {
        return contactName;
    }
}

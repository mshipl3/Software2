/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

/**
 * FirstLevelDivisions
 * @author SHIPLEYM
 * Definitions to be used by the system for the First_Level_Divisions table
 */
public class FirstLevelDivisions {

    private int divisionId;
    private String division;
    private String createDate;
    private String createdBy;
    private String lastUpdated;
    private String lastUpdatedBy;
    private int countryId;
    
    
    public FirstLevelDivisions(){
    }

    public FirstLevelDivisions(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }
    
    public FirstLevelDivisions(int divisionId, String division, String createDate, String createdBy, String lastUpdated, 
                               String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    public FirstLevelDivisions(int divisionId, String division){
        this.divisionId = divisionId;
        this.division = division;
    }
    
    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
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

    public int getDivisionID(){
        return divisionId;
    }
    
    public void setDivisionID(int divisionId){
        this.divisionId = divisionId;
    }

    @Override
    public String toString() {
        return division;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    
}

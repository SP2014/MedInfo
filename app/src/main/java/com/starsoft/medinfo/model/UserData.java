package com.starsoft.medinfo.model;

/**
 * Created by Aashish on 10/4/2016.
 */

public class UserData {
    private String presId;

    public String getPresId() {
        return presId;
    }

    public void setPresId(String presId) {
        this.presId = presId;
    }

    private String userId;
    private String prescriptionDate;
    private String diseaseName;
    private String prescriptionDescription;
    private String doctorName;

    public UserData(){}

    public UserData(String prescriptionDate, String diseaseName, String prescriptionDescription, String doctorName) {
        this.prescriptionDate = prescriptionDate;
        this.diseaseName = diseaseName;
        this.prescriptionDescription = prescriptionDescription;
        this.doctorName = doctorName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getPrescriptionDescription() {
        return prescriptionDescription;
    }

    public void setPrescriptionDescription(String prescriptionDescription) {
        this.prescriptionDescription = prescriptionDescription;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}

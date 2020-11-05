package com.adrinofast.sa4;

import java.io.Serializable;

public class Program implements Serializable {

    private String Id;
    private String documentid;

    private String CollegeName;
    private String Department;
    private String Duration;

    private String Engineering;

    private String Faculty;
    private String Level;

    private String PossibleCareer;
    private String  PrimaryCampus;
    private String StartTerm;
    private String imageURL;

    private String  typeCollegeUni;

    //Gettres and Setters


    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public String getTypeCollegeUni() {
        return typeCollegeUni;
    }

    public void setTypeCollegeUni(String typeCollegeUni) {
        this.typeCollegeUni = typeCollegeUni;
    }

    public String getId() {
        return Id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getProgramId() {
        return Id;
    }

    public void setProgramId(String programId) {
        Id = programId;
    }

    public String getCollegeName() {
        return CollegeName;
    }

    public void setCollegeName(String collegeName) {
        CollegeName = collegeName;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getEngineering() {
        return Engineering;
    }

    public void setEngineering(String engineering) {
        Engineering = engineering;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String faculty) {
        Faculty = faculty;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getPossibleCareer() {
        return PossibleCareer;
    }

    public void setPossibleCareer(String possibleCareer) {
        PossibleCareer = possibleCareer;
    }

    public String getPrimaryCampus() {
        return PrimaryCampus;
    }

    public void setPrimaryCampus(String primaryCampus) {
        PrimaryCampus = primaryCampus;
    }

    public String getStartTerm() {
        return StartTerm;
    }

    public void setStartTerm(String startTerm) {
        StartTerm = startTerm;
    }

    @Override
    public String toString() {
        return "Program{" +
                "Id='" + Id + '\'' +
                ", documentid='" + documentid + '\'' +
                ", CollegeName='" + CollegeName + '\'' +
                ", Department='" + Department + '\'' +
                ", Duration='" + Duration + '\'' +
                ", Engineering='" + Engineering + '\'' +
                ", Faculty='" + Faculty + '\'' +
                ", Level='" + Level + '\'' +
                ", PossibleCareer='" + PossibleCareer + '\'' +
                ", PrimaryCampus='" + PrimaryCampus + '\'' +
                ", StartTerm='" + StartTerm + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", typeCollegeUni='" + typeCollegeUni + '\'' +
                '}';
    }
}

package com.frontbackend.thymeleaf.bootstrap.rules;

public class FileLevelErrors {

  String fileName;
  int TotalNoOfRecords;
  int noOfErrorRecords;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public int getTotalNoOfRecords() {
    return TotalNoOfRecords;
  }

  public void setTotalNoOfRecords(int totalNoOfRecords) {
    TotalNoOfRecords = totalNoOfRecords;
  }

  public int getNoOfErrorRecords() {
    return noOfErrorRecords;
  }

  public void setNoOfErrorRecords(int noOfErrorRecords) {
    this.noOfErrorRecords = noOfErrorRecords;
  }
}

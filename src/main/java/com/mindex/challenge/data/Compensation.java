package com.mindex.challenge.data;

public class Compensation {
    private int salary;
    private String effectiveDate;

    public Compensation(){
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int sal){
        salary = sal;
    }

    public String getEffectiveDate(){
        return effectiveDate;
    }

    public void setEffectiveDate(String date){
        effectiveDate = date;
    }
}

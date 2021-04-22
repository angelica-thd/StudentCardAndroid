package com.example.qrcodetry1;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    //put json object here as well

    private final String greekFname;
    private final String greekLname;
    private final String latinFname;
    private final String latinLname;
    private final String address;
    private final String zipCode;
    private final String city;
    private final String prefecture;
    private final String institution;
    private final String school;
    private final String department;
    private final String academicAddress;
    private final String academicZipCode;
    private final String academicPrefecture;
    private final String academicCity;
    private final String studentshipType;
    private final String studentNumber;
    private final String studentAMKA;
    private final String entryDate;
    private final String academicPhoto;

    public String getGreekFname() {
        return greekFname;
    }

    public String getGreekLname() {
        return greekLname;
    }

    public String getLatinFname() {
        return latinFname;
    }

    public String getLatinLname() {
        return latinLname;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public String getInstitution() {
        return institution;
    }

    public String getSchool() {
        return school;
    }

    public String getDepartment() {
        return department;
    }

    public String getAcademicAddress() {
        return academicAddress;
    }

    public String getAcademicZipCode() {
        return academicZipCode;
    }

    public String getAcademicPrefecture() {
        return academicPrefecture;
    }

    public String getAcademicCity() {
        return academicCity;
    }

    public String getStudentshipType() {
        return studentshipType;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getStudentAMKA() {
        return studentAMKA;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getAcademicPhoto() {
        return academicPhoto;
    }



    private Student(Builder builder){
        this.greekFname = builder.greekFname;
        this.greekLname = builder.greekLname;
        this.latinFname = builder.latinFname;
        this.latinLname = builder.latinLname;
        this.address = builder.address;
        this.zipCode = builder.zipCode;
        this.city = builder.city;
        this.prefecture = builder.prefecture;
        this.institution = builder.institution;
        this.school = builder.school;
        this.department = builder.department;
        this.academicAddress = builder.academicAddress;
        this.academicZipCode = builder.academicZipCode;
        this.academicPrefecture = builder.academicPrefecture;
        this.academicCity = builder.academicCity;
        this.studentshipType = builder.studentshipType;
        this.studentNumber = builder.studentNumber;
        this.studentAMKA = builder.studentAMKA;
        this.entryDate = builder.entryDate;
        this.academicPhoto = builder.academicPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public void sendStudent(Student student, Context originContext, Context targetContext){
        originContext.startActivity(new Intent(originContext, targetContext.getClass()).putExtra("student",student));
    }

    public static class Builder {
        private final String greekFname;
        private final String greekLname;
        private final String latinFname;
        private final String latinLname;
        private final String address;
        private final String zipCode;
        private final String city;
        private final String prefecture;
        private final String institution;
        private final String school;
        private final String department;
        private final String academicAddress;
        private final String academicZipCode;
        private final String academicPrefecture;
        private final String academicCity;
        private final String studentshipType;
        private final String studentNumber;
        private final String studentAMKA;
        private final String entryDate;
        private final String academicPhoto;

        public Builder(String greekFname, String greekLname, String latinFname, String latinLname, String address, String zipCode, String city, String prefecture, String institution, String school, String department, String academicAddress, String academicZipCode, String academicPrefecture, String academicCity, String studentshipType, String studentNumber, String studentAMKA, String entryDate, String academicPhoto) {
            this.greekFname = greekFname;
            this.greekLname = greekLname;
            this.latinFname = latinFname;
            this.latinLname = latinLname;
            this.address = address;
            this.zipCode = zipCode;
            this.city = city;
            this.prefecture = prefecture;
            this.institution = institution;
            this.school = school;
            this.department = department;
            this.academicAddress = academicAddress;
            this.academicZipCode = academicZipCode;
            this.academicPrefecture = academicPrefecture;
            this.academicCity = academicCity;
            this.studentshipType = studentshipType;
            this.studentNumber = studentNumber;
            this.studentAMKA = studentAMKA;
            this.entryDate = entryDate;
            this.academicPhoto = academicPhoto;
        }

        public Student build(){
            //Here we create the actual bank account object, which is always in a fully initialised state when it's returned.
            Student student = new Student(this);  //Since the builder is in the BankAccount class, we can invoke its private constructor.
            return student;
        }


    }

    public Student(Parcel source) {
        greekFname = source.readString();
        greekLname = source.readString();
        latinFname = source.readString();
        latinLname = source.readString();
        address = source.readString();
        zipCode = source.readString();
        city = source.readString();
        prefecture = source.readString();
        institution = source.readString();
        school = source.readString();
        department = source.readString();
        academicAddress = source.readString();
        academicZipCode = source.readString();
        academicPrefecture = source.readString();
        academicCity = source.readString();
        studentshipType = source.readString();
        studentAMKA = source.readString();
        studentNumber = source.readString();
        entryDate = source.readString();
        academicPhoto = source.readString();

    }



    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }

        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }
    };
}


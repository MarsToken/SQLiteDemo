package com.example.sqldemo.sql.bean;

/**
 * Created by hp on 2019/10/15.
 */
public class Person {
    public int personId;
    public String personName;
    public int countryId;

    public Person(int personId, String personName, int countryId) {
        this.personId = personId;
        this.personName = personName;
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", personName='" + personName + '\'' +
                ", countryId='" + countryId + '\'' +
                '}';
    }
}

package com.example.sqldemo.sql.bean;

/**
 * Created by hp on 2019/10/15.
 */
public class Country {
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public int countryId;
    public String countryName;

    @Override
    public String toString() {
        return "Country{" +
                "countryId='" + countryId + '\'' +
                ", countryName='" + countryName + '\'' +
                '}';
    }
}

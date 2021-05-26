package com.example.rss;

public class Currrency {

    private String name;
    private String country_code;

    public Currrency(String name,String country_code)
    {
        this.name = name;
        this.country_code = country_code;
    }

    public Currrency()
    {

    }



    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    // Text show in Spinner
    @Override
    public String toString()  {
        return this.getName() + " - (" + this.getCountry_code()+")";
    }
}

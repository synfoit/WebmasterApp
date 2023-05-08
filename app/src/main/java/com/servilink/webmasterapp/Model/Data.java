package com.servilink.webmasterapp.Model;

public class Data {
     String date;
     String interval;

     public  Data( String date,String interval,String listOfTag){
         this.date=date;
         this.interval=interval;
         this.listOfTag=listOfTag;
     }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getListOfTag() {
        return listOfTag;
    }

    public void setListOfTag(String listOfTag) {
        this.listOfTag = listOfTag;
    }

    String listOfTag;
}

package com.example.reheimer2;

public class SingleEvent {
    /* Java class to construct the event object.
    * Takes date, context and hour of the event
    * and creates an object. */

    String date, eventContext,hour,key;
    double id;

    public SingleEvent(){

    }

    public SingleEvent(double id, String date, String eventContext, String hour){
        this.id = id;
        this.date=date;
        this.eventContext = eventContext;
        this.hour=hour;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventContext() {
        return eventContext;
    }

    public void setEventContext(String eventContext) {
        this.eventContext = eventContext;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}

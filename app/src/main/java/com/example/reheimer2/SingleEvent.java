package com.example.reheimer2;

public class SingleEvent {
    /* Java class to construct the event object.
    * Takes date, context and hour of the event
    * and creates an object. */

    String date, eventContext,hour;

    public SingleEvent(){

    }

    public SingleEvent(String date, String eventContext, String hour){
        this.date=date;
        this.eventContext = eventContext;
        this.hour=hour;
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

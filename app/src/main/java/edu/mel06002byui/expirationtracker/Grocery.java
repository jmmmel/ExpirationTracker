package edu.mel06002byui.expirationtracker;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by James on 2/17/2015.
 */
class Grocery implements Comparable<Grocery>{

    private Calendar expireDate;
    private int quantity;
    private String name;
    // needed for primary key of the database
    private int id;

    /**
     *
     */
    public Grocery() {
        id = -1;
        name = "Unknown";
        quantity = 0;
        expireDate = new GregorianCalendar();
    }

    /**
     *
     * @param tempID // primary key
     * @param tempName // temporary name
     * @param tempQuantity temporary quantity
     */
    public Grocery(int tempID, String tempName, int tempQuantity){
        id = tempID;
        quantity = tempQuantity;
        name = tempName;
        expireDate = new GregorianCalendar();
    }

    /**
     *
     * @param name // variable for constructor
     * @param quantity // variable for constructor
     * @param expireMonth // variable for constructor
     * @param expireDay // variable for constructor
     * @param expireYear // variable for constructor
     */
    public Grocery(String name, int quantity, int expireMonth, int expireDay, int expireYear){
        id = -1; // -1 means that this object hasn't been sent to the DB
        this.name = name;
        this.quantity = quantity;
        expireDate = new GregorianCalendar(expireYear,expireMonth,expireDay);
    }
    /**
     * Returns the name of the grocery
     *
     * @return The name of the grocery
     */
    public String getName() {
        return name;
    }
    /**
     *  returns the quantity of a grocery item
     *  needed for Database
     * @return
     */
    public int getQuantity() { return quantity; }

    /**
     * gets id
     * @return
     */
    public int getID() { return id; }

    /**
     * set Id
     * @param id
     */
    public void setId(int id) { this.id = id; }

    /**
     * set quantity
     * @param newQuantity
     */
    public void setQuantity(int newQuantity){ quantity = newQuantity; }
    /**
     *
     * @param newName name to be assigned
     */
    public void setName(String newName){name = newName;}


    /**
     * Gets the Expiration Date
     *
     * @return Expiration date as a Calendar object
     */
    public Calendar getExpireDate() {
        return expireDate;
    }

    /**
     * Sets the Expiration Date
     *
     * @param eDate This is the expiration date
     */
    public void setExpireDate(Calendar eDate) {
        expireDate = eDate;
    }

    /**
     * This will remove the specified number of items
     *
     * @param removeNumber how much is removed
     */
    public void removeValue(int removeNumber) {
        quantity = quantity - removeNumber;
    }

    /**
     * This will add specified number
     *
     * @param addNumber how much is added
     */
    public void addQuantity(int addNumber) {
        quantity = quantity + addNumber;
    }

    /**
     *
     * @param another The grocery object to compare to
     * @return -1 for less than 0 for the same 1 for greater than
     */
    @Override
    public int compareTo(Grocery another) {
        return this.getExpireDate().compareTo(another.getExpireDate());
    }

    /**
     * ***
     * public Date remainingTime(){ return startDate - expireDate; }
     *
     */
    @Override
    public String toString(){
        return name + "\nExpire Date: " + dateAsString()
                + "\nDays Left: " + daysBetween(expireDate,Calendar.getInstance())
                + "\nQuantity: " + quantity;

    }

    @Override
    public boolean equals(Object compGrocery){
        return compGrocery.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode(){

        return expireDate.hashCode()*name.hashCode();
    }

    private int daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return (int)TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    public String dateAsString(){
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("yyyy-MM-dd");
        return formatter.format(expireDate.getTime());
    }

    /**
     * Used to set the date with a string
     * @param newDate string in format MM/dd/yyyy
     */
    public void setDateWithString(String newDate){
        String[] tokens = newDate.split("-");
        if(tokens.length != 3) {
            Log.d("SetDateWithString", "Incorrect Format");
            return;
        }
        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1])-1;
        int day = Integer.parseInt(tokens[2]);
        expireDate = new GregorianCalendar(year,month,day);
    }

}

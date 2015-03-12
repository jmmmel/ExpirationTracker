package edu.mel06002byui.expirationtracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by James on 2/17/2015.
 */
class Grocery implements Comparable<Grocery>{

    private Calendar purchaseDate;
    private Calendar expireDate;
    private int quantity;
    private String name;

    /**
     *
     */
    public Grocery() {
        quantity = 0;
        purchaseDate = new GregorianCalendar();
        expireDate = new GregorianCalendar();
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
     * @return returns the purchase date as a Calendar Object
     */
    public Calendar getPurchaseDate() {
        return purchaseDate;
    }

    /**
     *
     * @param newName name to be assigned
     */
    public void setName(String newName){name = newName;}

    /**
     * Sets the Purchase Date
     *
     * @param pDate This is the purchase date
     */
    public void setPurchaseDate(Calendar pDate) {
        purchaseDate = pDate;
    }

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
    public void addValue(int addNumber) {
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
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("MM/dd/yyyy");
        return name + "\nPurchase Date: " + formatter.format(purchaseDate.getTime())
                + "\nExpire Date: " + formatter.format(expireDate.getTime())
                + "\nDays Left: " + daysBetween(expireDate,Calendar.getInstance());
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
}

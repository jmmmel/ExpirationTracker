package edu.mel06002byui.expirationtracker;

import java.util.Calendar;

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
        
        return null;
    }

    @Override
    public boolean equals(Object compGrocery){
        return compGrocery.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode(){

        return expireDate.hashCode()*name.hashCode();
    }
}

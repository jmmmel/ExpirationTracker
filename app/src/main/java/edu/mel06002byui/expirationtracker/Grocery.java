package edu.mel06002byui.expirationtracker;

import java.util.Calendar;

/**
 * Created by James on 2/17/2015.
 */
class Grocery implements Comparable<Grocery>{

    Calendar purchaseDate;
    Calendar expireDate;
    int quantity;
    private String name;
    private Object grocery;

    /**
     *
     */
    public Grocery() {

    }

    /**
     * Returns the name of the grocery
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public Calendar getPurchaseDate() {
        return purchaseDate;
    }

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
     * @return
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

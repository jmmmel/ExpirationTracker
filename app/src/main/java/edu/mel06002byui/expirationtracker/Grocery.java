package edu.mel06002byui.expirationtracker;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by James on 2/17/2015.
 */
class Grocery implements Comparable<Grocery> {

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
     * @param tempID       // primary key
     * @param tempName     // temporary name
     * @param tempQuantity temporary quantity
     */
    public Grocery(int tempID, String tempName, int tempQuantity) {
        id = tempID;
        quantity = tempQuantity;
        name = tempName;
        expireDate = new GregorianCalendar();
    }

    /**
     * @param name        // variable for constructor
     * @param quantity    // variable for constructor
     * @param expireMonth // variable for constructor
     * @param expireDay   // variable for constructor
     * @param expireYear  // variable for constructor
     */
    public Grocery(String name, int quantity, int expireMonth, int expireDay, int expireYear) {
        id = -1; // -1 means that this object hasn't been sent to the DB
        this.name = name;
        this.quantity = quantity;
        expireDate = new GregorianCalendar(expireYear, expireMonth, expireDay);
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
     * returns the quantity of a grocery item
     * needed for Database
     *
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * gets id
     *
     * @return
     */
    public int getID() {
        return id;
    }

    /**
     * set Id
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * set quantity
     *
     * @param newQuantity
     */
    public void setQuantity(int newQuantity) {
        quantity = newQuantity;
    }

    /**
     * @param newName name to be assigned
     */
    public void setName(String newName) {
        name = newName;
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
    public void addQuantity(int addNumber) {
        quantity = quantity + addNumber;
    }

    /**
     * @param another The grocery object to compare to
     * @return -1 for less than 0 for the same 1 for greater than
     */
    @Override
    public int compareTo(Grocery another) {
        int compare = this.getExpireDate().compareTo(another.getExpireDate());
        if (compare == 0)
            compare = this.getName().compareTo(another.getName());
        if (compare == 0)
            compare = ((Integer) this.getID()).compareTo((Integer) another.getID());
        return compare;
    }

    /**
     * ***
     * public Date remainingTime(){ return startDate - expireDate; }
     */
    @Override
    public String toString() {
        return name + "\nExpire Date: " + getDateAsMonthDayYear()
                + "\nDays Left: " + daysBetween(expireDate, todayTrimmed())
                + "\nQuantity: " + quantity;

    }

    @Override
    public boolean equals(Object compGrocery) {
        Grocery compareGrocery = (Grocery) compGrocery;
        boolean isSame = false;
        if (compareGrocery.getName().equals(this.getName())
                && compareGrocery.dateAsString().equals(this.dateAsString()))
            isSame = true;
        return isSame;
    }

    @Override
    public int hashCode() {
        String allCapsName = name.toUpperCase();
        return expireDate.hashCode() * allCapsName.hashCode() + id;
    }

    /**
     * Used to get the days to expiration
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private int daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return (int) TimeUnit.MILLISECONDS.toDays(start - end);
    }

    /**
     * Gives the date as a string for display or storage
     *
     * @return string representing date in format YYYY-MM-DD
     */
    public String dateAsString() {
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("yyyy-MM-dd");
        return formatter.format(expireDate.getTime());
    }

    /**
     * format the date
     *
     * @return string that will be formatted as Month day, year
     */
    public String getDateAsMonthDayYear() {

        Integer monthAsInt = expireDate.get(Calendar.MONTH);
        Integer dayAsInt = expireDate.get(Calendar.DAY_OF_MONTH);
        Integer yearAsInt = expireDate.get(Calendar.YEAR);
        String dateForListView = "";


        switch (monthAsInt) {
            case 0:
                dateForListView += "January ";
                break;
            case 1:
                dateForListView += "February ";
                break;
            case 2:
                dateForListView += "March ";
                break;
            case 3:
                dateForListView += "April ";
                break;
            case 4:
                dateForListView += "May ";
                break;
            case 5:
                dateForListView += "June ";
                break;
            case 6:
                dateForListView += "July ";
                break;
            case 7:
                dateForListView += "August ";
                break;
            case 8:
                dateForListView += "September ";
                break;
            case 9:
                dateForListView += "October ";
                break;
            case 10:
                dateForListView += "November ";
                break;
            case 11:
                dateForListView += "December ";
                break;
        }

        dateForListView += dayAsInt + ", " + yearAsInt;
        Log.i("calendar string", dateForListView);

        return dateForListView;
    }

    /**
     * Used to set the date with a string
     *
     * @param newDate string in format YYYY-MM-DD
     */
    public void setDateWithString(String newDate) {
        String[] tokens = newDate.split("-");
        if (tokens.length != 3) {
            Log.d("SetDateWithString", "Incorrect Format");
            return;
        }
        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]) - 1;
        int day = Integer.parseInt(tokens[2]);

        expireDate = new GregorianCalendar(year, month, day);
        expireDate.set(Calendar.HOUR_OF_DAY, 0);
        expireDate.set(Calendar.MINUTE, 0);
        expireDate.set(Calendar.SECOND, 0);
        expireDate.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Gives a calendar object with no hours minutes and seconds of today
     *
     * @return
     */
    private Calendar todayTrimmed() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}

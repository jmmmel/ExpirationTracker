package edu.mel06002byui.expirationtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Roman on 3/10/2015.
 */
public class GrocerySQLiteHelper extends SQLiteOpenHelper {
    // tag created  for logging
    private static final String TAG_GROCERY_DB = "groceryDB";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "GroceriesDB";

    /*******************************************************************
     * constants for table and column names
     *******************************************************************/
    private static final String TABLE_GROCERIES = "groceries";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_EXPIRATION_DATE = "expirationDate";
    private static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_QUANTITY};

    /**
     * default constructor
     * @param context
     */
    public GrocerySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * create a new database and create a table called groceries
     * groceries table will contain name, quantity and expiration date
     * @param db database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create groceries table
        String Create_Grocery_Table = "CREATE TABLE groceries ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "quantity TEXT, " +
                "expirationDate TEXT)";

        //Log
        Log.d("onCreate in DB", db.toString());

        // create groceries table
        db.execSQL(Create_Grocery_Table);
    }

    /**
     *
     * @param db // database that will be upgraded
     * @param oldVersion current version of Database
     * @param newVersion new version of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF exists groceries");

        // create groceries table
        this.onCreate(db);
    }

    /**
     *  add a grocery to the Database
     * @param grocery item to be added to the Database
     * @return The id of the grocery item that was added to Database
     */
    public long addGroceryToDatabase(Grocery grocery){
        long idToReturn = 0;
        // log
        Log.d("addGroceryToDatabase", grocery.toString());

        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // create contentValues to add to key column
        ContentValues values = new ContentValues();
        // get name and put it in values
        values.put(KEY_NAME, grocery.getName());

        // get quantity and put in values
        values.put(KEY_QUANTITY, grocery.getQuantity());

        //get expiration date and put it in values
        values.put(KEY_EXPIRATION_DATE, grocery.dateAsString());
        Log.d("ValuesToBeInserted",values.toString());
        Log.d("Inserting Table " + idToReturn,"*******************");
        // insert into table
        idToReturn = db.insert(TABLE_GROCERIES, null, values);
        Log.d("Inserting Table " + idToReturn,"*******************");
        db.close();
        return idToReturn;
    }

    public void checkItemIfExists(){}

    /**
     * gets a single grocery based of its id
     * @param id // id is the primary key
     * @return grocery that is in the Database
     */
    public Grocery getGroceryByID(int id){
        Grocery grocery = new Grocery();
        try {
            // get reference to readable DB
            SQLiteDatabase db = this.getReadableDatabase();

            // build query
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GROCERIES + " WHERE id=" + id,null);
            // if a result is returned get the first result
            if (cursor != null)
                cursor.moveToFirst();

            if (cursor != null) {

                grocery.setId(Integer.parseInt(cursor.getString(0)));
                grocery.setName(cursor.getString(1));
                grocery.setQuantity(Integer.parseInt(cursor.getString(2)));
                grocery.setDateWithString(cursor.getString(3));
                Log.d("getGrocerycursor 3",cursor.getString(3));
                cursor.close();
            }
            Log.d("getGroceryByID ( "+ id +" )", grocery.toString());
        } catch(NullPointerException e){
            Log.e("getGrocery", e.toString());
            Log.d("getGroceryByID ( "+ id +" )", grocery.toString());
        }
        return grocery;
    }

    /**
     * will create a tree set of groceries that will be returned
     * @return setOfGroceries
     */
    public Set<Grocery> getAllGroceries(){
        Set<Grocery> setOfGroceries = new TreeSet<>();

        // build query string
        String query = "SELECT * FROM " + TABLE_GROCERIES;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query,null);

        //go through each row and build a grocery item and add
        // it to the set of books
        Grocery grocery;
        if (cursor.moveToFirst()){

            // loop through the cursor and create a grocery object and add it to
            // the set of groceries
            do{
                // create Grocery object
                grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(0)));
                grocery.setName(cursor.getString(1));
                grocery.setQuantity(Integer.parseInt(cursor.getString(2)));
                grocery.setDateWithString(cursor.getString(3));
                Log.d(TAG_GROCERY_DB,cursor.getString(3));
                // add grocery object to set
                Log.d("getAllGroceriesaddtoset", grocery.toString());
                setOfGroceries.add(grocery);
                Log.d("getAllGroceries set", setOfGroceries.toString());
            } while (cursor.moveToNext());
        }

        cursor.close();

        // log
        Log.d("getAllGroceries set", setOfGroceries.toString());

        return setOfGroceries;

    }

    /**
     *
     * @param grocery object that will be updated
     * @return id of updated grocery
     */
    public int updateGroceryItem(Grocery grocery){
        Log.d("Grocery to be updated", grocery.toString());
        // get reference to writable db
        SQLiteDatabase db = this.getWritableDatabase();

        // create contentValues to add key column/value
        ContentValues values = new ContentValues();
        values.put("name", grocery.getName());
        values.put("quantity", grocery.getQuantity());
        values.put("expirationDate", grocery.dateAsString());
        int i = db.update(TABLE_GROCERIES, values, KEY_ID + " = ?"
                , new String[] {String.valueOf(grocery.getID())});

        //close db
        db.close();
        return i;
    }

    /**
     * takes the grocery to be deleted
     * @param grocery to be deleted
     */
    public void deleteItemFromDB(Grocery grocery){

        SQLiteDatabase db = this.getWritableDatabase();

        // delete item from db
        db.delete(TABLE_GROCERIES, KEY_ID + " = ?"
                ,new String[]{ String.valueOf(grocery.getID())});
        // close db
        db.close();

        Log.d("deleteItem", grocery.toString());
    }

    /**
     *
     * @return if items are to expire return true
     */
    public boolean expiringItems(){
        // return true is items are going to expire within the next week

        Calendar today = new GregorianCalendar();
        Calendar weekFromToday = new GregorianCalendar();
        // add seven days
        weekFromToday.add(weekFromToday.DATE, 7);

        // get set of Groceries from Database
        Set<Grocery> grocerySet = new TreeSet<>();
        grocerySet = getAllGroceries();

        // move set of groceries into an arraylist
        ArrayList<Grocery> groceryArray = new ArrayList<>();
        groceryArray.addAll(grocerySet);

        // calendar object will be set to a date from the database
        Calendar tempCalendar = new GregorianCalendar();
        Log.d("Size of Array","" + groceryArray.size());

        // walk through the groceryArray and return true if there are groceries that are going to
        // expire
        for (int i = 0; i < groceryArray.size(); i++) {
            String tempDate = groceryArray.get(i).dateAsString();
            tempCalendar = setDateWithString(tempDate);

            Log.d("tempCalendar", "" + tempCalendar.getTimeInMillis());
            Log.d("today", "" + today.getTimeInMillis());
            Log.d("weekFromToday", "" + weekFromToday.getTimeInMillis());
            if ((tempCalendar.getTimeInMillis() >= today.getTimeInMillis())
                    && (tempCalendar.getTimeInMillis() < weekFromToday.getTimeInMillis())){

                return true;
            }
        }

        return false;
    }

    /**
     * Used to set the date with a string
     * @param newDate string in format MM/dd/yyyy
     */
    public Calendar setDateWithString(String newDate){
        String[] tokens = newDate.split("-");
        if(tokens.length != 3) {
            Log.d("SetDateWithString", "Incorrect Format");
            return null;
        }
        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1])-1;
        int day = Integer.parseInt(tokens[2]);
        return new GregorianCalendar(year,month,day);
    }

    /**
     * Drops the groceries table and then recreates the groceries table so that it is empty
     * @param db database that will be cleared
     */
    public void clearDatabase(SQLiteDatabase db){

        // query to drop groceries table
        String DROP_TABLE ="DROP TABLE IF EXISTS " + TABLE_GROCERIES;
        db.execSQL(DROP_TABLE);

        // SQL statement to create groceries table
        String Create_Grocery_Table = "CREATE TABLE groceries ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "quantity TEXT, " +
                "expirationDate TEXT)";

        //Log
        Log.d("onCreate in DB", db.toString());

        // create groceries table
        db.execSQL(Create_Grocery_Table);
    }
}

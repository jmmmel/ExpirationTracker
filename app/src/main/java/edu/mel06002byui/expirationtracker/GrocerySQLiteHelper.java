package edu.mel06002byui.expirationtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
     *
     * @param db // database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create groceries table
        String Create_Grocery_Table = "CREATE TABLE groceries ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name TEXT, " +
                "quantity TEXT, ";

        // create groceries table
        db.execSQL(Create_Grocery_Table);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF exists groceries");

        // create groceries table
        this.onCreate(db);
    }

    /**
     *
     * @param grocery
     */
    public void addGroceryToDatabase(Grocery grocery){
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
        //values.put(KEY_EXPIRATION_DATE, grocery.dateAsString());

        // insert into table
        db.insert(TABLE_GROCERIES, null, values);

        db.close();
    }

    /**
     * gets a single grocery based of its id
     * @param id // id is the primary key
     * @return grocery
     */
    public Grocery getGroceryByID(int id){
        Grocery grocery = null;
        try {
            // get reference to readable DB
            SQLiteDatabase db = this.getReadableDatabase();

            // build query
            Cursor cursor = db.query(TABLE_GROCERIES, COLUMNS, " id = ?"
                    , new String[]{String.valueOf(id)}, null, null, null, null);
            // if a result is returned get the first result
            if (cursor != null)
                cursor.moveToFirst();

            if (cursor != null) {
                grocery = new Grocery(Integer.parseInt(cursor.getString(0))
                        , cursor.getString(1), Integer.parseInt(cursor.getString(2)));
                grocery.setDateWithString(cursor.getString(3));

                cursor.close();
            }
            Log.d("getGroceryByID ( "+ id +" )", grocery.toString());
        } catch(NullPointerException e){
            Log.e("getGrocery", grocery.toString());
        }
        return grocery;
    }

    /**
     * based on name it will create and return a grocery object
     * @param name it will create a query based off on the name and search for the name
     *             in the Database
     * @return
     */
    public Grocery getGroceryByName(String name){

        SQLiteDatabase db = this.getReadableDatabase();

        // build cursor
        Cursor cursor = db.query(TABLE_GROCERIES, COLUMNS, " name = ?"
        , new String[]{ String.valueOf(name)}
        ,null // group by
        ,null // having
        ,null // order by
        ,null); // limit

        Grocery grocery = new Grocery();
        if (cursor != null){
            cursor.moveToFirst();

            grocery.setId(Integer.parseInt(cursor.getString(0)));
            grocery.setName(cursor.getString(1));
            grocery.setQuantity(Integer.parseInt(cursor.getString(2)));

        }
        Log.d("getGroceryByName(" +grocery.getID()+")", grocery.toString());
        cursor.close();
        return grocery;
    }

    /**
     * will create a set of groceries that will be returned
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
        Grocery grocery = null;
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

                // add grocery object to set
                setOfGroceries.add(grocery);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // log
        Log.d("getAllGroceries", setOfGroceries.toString());

        return setOfGroceries;

    }

    /**
     *
     * @param grocery
     * @return
     */
    public int updateGroceryItem(Grocery grocery){
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
     *
     * @param grocery
     */
    public void deleteItem(Grocery grocery){

        SQLiteDatabase db = this.getWritableDatabase();

        // delete item from db
        db.delete(TABLE_GROCERIES, KEY_ID + " = ?"
                ,new String[]{ String.valueOf(grocery.getID())});
        // close db
        db.close();

        Log.d("deleteItem", grocery.toString());
    }
}

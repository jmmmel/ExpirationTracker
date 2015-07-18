package edu.mel06002byui.expirationtracker;

import android.os.Environment;

/**
 * provides the state of the external storage. Returns boolean variables that
 * represent the ability to read and write to the storage.
 * Created by Roman on 7/18/2015.
 */
public class externalStorage {
    private String state = Environment.getExternalStorageState();
    private Boolean okToWrite = isExternalStorageWritable();
    private Boolean okToRead = isExternalStorageReadable();

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    public Boolean getOkToRead() {
        return okToRead;
    }

    /**
     *
     * @return
     */
    public Boolean getOkToWrite() {

        return okToWrite;
    }

    /**
     *
     * @return
     */
    public String getState() {

        return state;
    }
}

package com.example.mgkan.hackathon_lost_pets.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mgkan.hackathon_lost_pets.Model.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikrudie on 7/23/16.
 */

/*
All SQL queries are made from this class
 */

//    REMINDER: NICKNAMES ARE STORED W/O APOSTRASCES OR QUOTATION MARKS

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static final String DATABASE_NAME = "LOST_PETS_DB";
    private static final int DATABASE_VERSION = 2;

    private static DBHelper DB;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static DBHelper getInstance(Context context) {
        if (DB == null) {
            DB = new DBHelper(context);
        }
        return DB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      // TODO: do... stuff... here?
      db.execSQL(SC.CREATE_TABLE_PETS);
      db.execSQL(SC.CREATE_TABLE_TIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        this.onCreate(db);
    }

    public void dropAllTables(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + SC.TABLE_PETS);
      db.execSQL("DROP TABLE IF EXISTS " + SC.TABLE_TIME);
    }

    public long getSavedTime() {
        String sql = "SELECT * FROM " + SC.TABLE_TIME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            return cursor.getLong(0);
        }
        db.close();
        return 0;
    }

    public void setSavedTime(long time) {
        String sql = "DELETE FROM " + SC.TABLE_TIME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        sql =  "INSERT INTO " + SC.TABLE_TIME + " (" + SC.SAVED_TIME + ") Values (" + time + ");";
        db.execSQL(sql);
        db.close();
    }

    public void flushTablePets() {
        String sql = "DELETE FROM " + SC.TABLE_PETS +";";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    // This method returns the character ID of the inserted row after inserting character details
    public void insertPetIntoDb(Pet pet) {
        String id = cleanTextForDb(pet.getAnimalId());
        String type = cleanTextForDb(pet.getAnimalType());
        String date = cleanTextForDb(pet.getDate());
        String dateType = cleanTextForDb(pet.getDateType());
        String color = cleanTextForDb(pet.getColor());
        String image = cleanTextForDb(pet.getImage());
        String city = cleanTextForDb(pet.getCity());
        String name = cleanTextForDb(pet.getName());
        String gender = cleanTextForDb(pet.getAnimalGender());
        String breed = cleanTextForDb(pet.getAnimalBreed());
        String link = cleanTextForDb(pet.getLink());
        int zip = pet.getZip();
        String address = cleanTextForDb(pet.getAddress());
        String memo = cleanTextForDb(pet.getMemo());
        String location = cleanTextForDb(pet.getCurrentLocation());
        int dayInt = pet.getDayInt();

        String sql = "INSERT INTO " + SC.TABLE_PETS + " (" + SC.ID + ", " + SC.TYPE + ", " + SC.DATE + ", "
                + SC.DATE_TYPE + ", " + SC.COLOR + ", " + SC.IMAGE + ", " + SC.CITY + ", " + SC.NAME + ", "
                + SC.GENDER + ", " + SC.BREED + ", " + SC.LINK + ", " + SC.ZIP + ", " + SC.ADDRESS + ", "
                + SC.MEMO + ", " + SC.LOCATION + ", " + SC.DAY_INT + ") VALUES ('" + id + "', '" + type + "', '"
                + date + "', '" + dateType + "', '" + color + "', '"  + image + "', '" + city + "', '"
                + name + "', '" + gender + "', '" + breed + "', '" + link + "', '" + zip + "', '"
                + address + "', '" + memo + "', '" + location + "', '" + dayInt + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }


    public String cleanTextForDb(String string) {
        if (string != null) {
            string = string.replace("'", "''");
            string = string.replace("\"", "\\\"");
        }
        return string;
    }

    public List<Pet> getPetListFromDb(String dogCat) {
        String sql = "SELECT * FROM " + SC.TABLE_PETS +" WHERE " + SC.TYPE + " = '" + dogCat +"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        List<Pet> pets = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(SC.ID));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(SC.TYPE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(SC.DATE));
            String dateType = cursor.getString(cursor.getColumnIndexOrThrow(SC.DATE_TYPE));
            String color = cursor.getString(cursor.getColumnIndexOrThrow(SC.COLOR));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(SC.IMAGE));
            String city = cursor.getString(cursor.getColumnIndexOrThrow(SC.CITY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SC.NAME));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(SC.GENDER));
            String breed = cursor.getString(cursor.getColumnIndexOrThrow(SC.BREED));
            String link = cursor.getString(cursor.getColumnIndexOrThrow(SC.LINK));
            int zip = cursor.getInt(cursor.getColumnIndexOrThrow(SC.ZIP));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(SC.ADDRESS));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(SC.MEMO));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(SC.LOCATION));
            pets.add(new Pet(id, type, date, dateType, color, image, city, name, gender, breed, link, zip
            , address, memo, location));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return pets;
    }

    public List<Pet> searchWithinDb(String animalType, String query) {
        String sql = "";
        if (query.toUpperCase().contains("FEMALE")) {
            // if query is male, search for gender not marked female
            String gender = "Female";
            sql = "SELECT * FROM " + SC.TABLE_PETS + " WHERE " + SC.TYPE + " = '" + animalType
                    + "' AND " + SC.GENDER + " LIKE '%" + gender + "%';";
        } else if (query.toUpperCase().contains("MALE")) {
            // if query is marked female, search for gender marked female
            String gender = "Female";
            sql = "SELECT * FROM " + SC.TABLE_PETS + " WHERE " + SC.TYPE + " = '" + animalType
                    + "' AND " + SC.GENDER + " NOT LIKE '%" + gender + "%';";
        } else {
            sql = "SELECT * FROM " + SC.TABLE_PETS + " WHERE " + SC.TYPE + " = '" + animalType
                    + "' AND (" + SC.COLOR + " LIKE '%" + query
                    + "%' " + " OR " + SC.CITY + " LIKE '%" + query + "%' " + " OR " + SC.NAME
                    + " LIKE '%" + query + "%' " + " OR " + SC.GENDER + " LIKE '" + query + "' "
                    + " OR " + SC.BREED + " LIKE '%" + query + "%' " + " OR " + SC.ZIP
                    + " LIKE '%" + query + "%' " + " OR " + SC.MEMO + " LIKE '%" + query + "%' "
                    + " OR " + SC.LOCATION + " LIKE '%" + query + "%');";

        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Pet> pets = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(SC.ID));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(SC.TYPE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(SC.DATE));
            String dateType = cursor.getString(cursor.getColumnIndexOrThrow(SC.DATE_TYPE));
            String color = cursor.getString(cursor.getColumnIndexOrThrow(SC.COLOR));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(SC.IMAGE));
            String city = cursor.getString(cursor.getColumnIndexOrThrow(SC.CITY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SC.NAME));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(SC.GENDER));
            String breed = cursor.getString(cursor.getColumnIndexOrThrow(SC.BREED));
            String link = cursor.getString(cursor.getColumnIndexOrThrow(SC.LINK));
            int zip = cursor.getInt(cursor.getColumnIndexOrThrow(SC.ZIP));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(SC.ADDRESS));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(SC.MEMO));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(SC.LOCATION));
            pets.add(new Pet(id, type, date, dateType, color, image, city, name, gender, breed, link, zip
                    , address, memo, location));
            cursor.moveToNext();
        }
        db.close();
        return pets;
    }


//    SELECT * FROM table_pets WHERE memo LIKE '%domesticated bunny%' OR city LIKE '%sho%' LIMIT 10;



}

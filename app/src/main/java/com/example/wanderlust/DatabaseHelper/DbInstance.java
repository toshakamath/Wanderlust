package com.example.wanderlust.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.wanderlust.Doa.BlogObject;

public class DbInstance extends SQLiteOpenHelper {

    private static final String TAG = "DbInstance";

    private static final String DATABASE_NAME = "wanderlust";
    private static final String TABLE_BLOG = "blog_table";
    private static final String TABLE_BLOG_PICS = "blog_pics_table";

    private static final String COL_1 = "blog_id";
    private static final String COL_2 = "user_id";
    private static final String COL_3 = "blog_title";
    private static final String COL_4 = "blog_location";
    private static final String COL_5 = "blog_text";
    private static final String COL_6 = "blog_pics";
    private static final String COL_7 = "blog_lat";
    private static final String COL_8 = "blog_long";
    private static final String COL_9 = "blog_likes";
    private static final String COL_10 = "blog_reviews";
    private static final String COl_11 = "blog_pic_id";

    private Context dbContext;
    private static DbInstance dbInstance = null;


    private static final String CREATE_TABLE_BLOG = "CREATE TABLE " + TABLE_BLOG + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 +" INTEGER, "
            + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " TEXT, " + COL_7 + " FLOAT, " + COL_8 + " FLOAT, " + COL_9 + " INTEGER, " + COL_10 + " TEXT)";


    private  static final String CREATE_TABLE_BLOG_PICS = "CREATE TABLE " + TABLE_BLOG_PICS + " (" + COl_11 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_1 +" INTEGER, " + COL_6 + " BLOB)";

    public DbInstance(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.dbContext = context;
    }

    public static synchronized DbInstance getDBInstance(Context context) {
        if(dbInstance == null) {
            dbInstance = new DbInstance(context.getApplicationContext());
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BLOG);
        sqLiteDatabase.execSQL(CREATE_TABLE_BLOG_PICS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOG);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOG_PICS);
        onCreate(sqLiteDatabase);
    }

    public long addBlogToTable(BlogObject blogObject) {
        Log.i(TAG, "addBlogToTable : called");
        SQLiteDatabase db = this.getWritableDatabase();
        long ID = 0;

        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, Integer.parseInt(blogObject.getUserId()));
            contentValues.put(COL_3, blogObject.getBlogTitle());
            contentValues.put(COL_4, blogObject.getBlogLocation());
            contentValues.put(COL_5, blogObject.getBlogText());
            contentValues.put(COL_7, blogObject.getBlogLat());
            contentValues.put(COL_8, blogObject.getBlogLong());
            contentValues.put(COL_9, blogObject.getBlogLikes());
            contentValues.put(COL_10, blogObject.getBlogReviews());

            ID = db.insertOrThrow(TABLE_BLOG, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG, "error while entering data into table - " + TABLE_BLOG);
        } finally {
            db.endTransaction();
        }

        return ID;
    }

    public void addBlogPicsToTable(BlogObject blogObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            for (byte[] img : blogObject.getBlogPics()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_1, Integer.parseInt(blogObject.getBlogId()));
                contentValues.put(COL_6, img);

                db.insertOrThrow(TABLE_BLOG_PICS, null, contentValues);
            }
        } catch (Exception e) {
            Log.e(TAG, "error while entering data into table - " + TABLE_BLOG_PICS);
            Log.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}

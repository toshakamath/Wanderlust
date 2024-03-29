package com.example.wanderlust.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.wanderlust.Doa.BlogObject;

import java.util.ArrayList;
import java.util.Vector;

public class DbInstance extends SQLiteOpenHelper {

    private static final String TAG = "DbInstance";

    private static final String DATABASE_NAME = "wanderlust";
    private static final String TABLE_BLOG = "blog_table";
    private static final String TABLE_BLOG_PICS = "blog_pics_table";
    private static final String TABLE_USERS = "users_table";

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

    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PASSWORD = "password";
    public static final String USERS_COLUMN_USERID = "userid";

    private Context dbContext;
    private static DbInstance dbInstance = null;


    private static final String CREATE_TABLE_BLOG = "CREATE TABLE " + TABLE_BLOG + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 +" INTEGER, "
            + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " TEXT, " + COL_7 + " DOUBLE, " + COL_8 + " DOUBLE, " + COL_9 + " INTEGER, " + COL_10 + " TEXT)";


    private  static final String CREATE_TABLE_BLOG_PICS = "CREATE TABLE " + TABLE_BLOG_PICS + " (" + COl_11 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_1 +" INTEGER, " + COL_6 + " BLOB)";

    private  static final String CREATE_TABLE_USER_DETAILS = "CREATE TABLE "+ TABLE_USERS +" ("+ USERS_COLUMN_USERID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+ USERS_COLUMN_NAME +" text, "+ USERS_COLUMN_EMAIL+" text UNIQUE, " +USERS_COLUMN_PASSWORD +" text )";

//    private static final String GET_ALL_BLOG_DATA ="SELECT "+COL_3+" FROM "+TABLE_BLOG+" WHERE "+COL_1+" = 1";
//    private static final String GET_ALL_BLOG_PICS ="SELECT * FROM "+TABLE_BLOG_PICS;

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
        sqLiteDatabase.execSQL(CREATE_TABLE_USER_DETAILS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOG);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOG_PICS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(sqLiteDatabase);
    }

    public long insertNewUser (String name, String email, String password) {
        Log.i(TAG, "add user - register : called");
        SQLiteDatabase db = this.getWritableDatabase();
        long ID = 0;

        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERS_COLUMN_NAME, name);
            contentValues.put(USERS_COLUMN_EMAIL, email);
            contentValues.put(USERS_COLUMN_PASSWORD, password);
            ID = db.insert(TABLE_USERS, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG, "error while entering data into table - " + TABLE_USERS);
        } finally {
            db.endTransaction();
        }
        Log.i("checkitout", ID+"");
        return ID;
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
        Log.i("tag", "======>"+blogObject.getBlogPics());
        try {
            for (byte[] img : blogObject.getBlogPics()) {
                Log.i("tag", "======>2 "+img);
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


    public BlogObject getOneBlogData (int blogId) {
        ArrayList<byte[]> pics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_BLOG + " where "+COL_1+"="+blogId, null );
        res.moveToFirst();
        if(res.getCount() == 1){
            String _blogId =res.getString(res.getColumnIndexOrThrow(COL_1));
            String userId = res.getString(res.getColumnIndexOrThrow(COL_2));
            String blogTitle = res.getString(res.getColumnIndexOrThrow(COL_3));
            String blogLocation = res.getString(res.getColumnIndexOrThrow(COL_4));
            String blogText = res.getString(res.getColumnIndexOrThrow(COL_5));
            Double blogLat = res.getDouble(res.getColumnIndexOrThrow(COL_7));
            Double blogLong = res.getDouble(res.getColumnIndexOrThrow(COL_8));
            int blogLikes = res.getInt(res.getColumnIndexOrThrow(COL_9));
            String blogReviews = res.getString(res.getColumnIndexOrThrow(COL_10));
            BlogObject bo = new BlogObject(_blogId,userId,blogTitle,blogLocation,blogText,pics,blogLat,blogLong,blogLikes,blogReviews);
            return bo;
        }else{
            return null;
        }
    }

    public ArrayList<BlogObject> getAllBlogData () {
        Log.i(TAG, "get all blog data : called");
        ArrayList<BlogObject> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String GET_ALL_BLOG_DATA ="SELECT * FROM "+TABLE_BLOG;
//        String GET_ALL_BLOG_PICS ="SELECT * FROM "+TABLE_BLOG_PICS+" WHERE "+COL_1+" = ";

        Log.i("QUERY: ", GET_ALL_BLOG_DATA);
//        Log.i("QUERY: ", GET_ALL_BLOG_PICS);
        try {
            Log.i("tag", "inside try");
            Cursor res = db.rawQuery(GET_ALL_BLOG_DATA, null);
            Log.i("tag", "cursor count: "+res.getCount());
            res.moveToFirst();
            if(res.getCount()>0){

                while (res.isAfterLast() == false) {
                    String _blogId = res.getString(res.getColumnIndexOrThrow(COL_1));

//                    String GET_ALL_BLOG_PICS ="SELECT * FROM "+TABLE_BLOG_PICS;
                    String GET_ALL_BLOG_PICS ="SELECT "+COL_6+" FROM " + TABLE_BLOG_PICS + " WHERE " + COL_1 + " = '" +_blogId+"'";
                    Log.i("tag: ", GET_ALL_BLOG_PICS);

                    Cursor res1 = db.rawQuery(GET_ALL_BLOG_PICS, null);
                    Log.i("tag", "cursor count:::: "+res1.getCount());
                    res1.moveToFirst();

                    ArrayList<byte[]> pics = new ArrayList<>();
                    while (res1.isAfterLast() == false){
                        Log.i("tag", "coming hereee");
                        byte [] blogPic = res1.getBlob(res1.getColumnIndexOrThrow(COL_6));
                        pics.add(blogPic);
                        Log.i("tag", "not coming hereee");
                        res1.moveToNext();
                    }
                    res1.close();

                    String userId = res.getString(res.getColumnIndexOrThrow(COL_2));
                    String blogTitle = res.getString(res.getColumnIndexOrThrow(COL_3));
                    String blogLocation = res.getString(res.getColumnIndexOrThrow(COL_4));
                    String blogText = res.getString(res.getColumnIndexOrThrow(COL_5));
                    Double blogLat = res.getDouble(res.getColumnIndexOrThrow(COL_7));
                    Double blogLong = res.getDouble(res.getColumnIndexOrThrow(COL_8));
                    int blogLikes = res.getInt(res.getColumnIndexOrThrow(COL_9));
                    String blogReviews = res.getString(res.getColumnIndexOrThrow(COL_10));
                    BlogObject bo = new BlogObject(_blogId, userId, blogTitle, blogLocation, blogText, pics, blogLat, blogLong, blogLikes, blogReviews);
                    data.add(bo);
                    res.moveToNext();
                }
                res.close();
                return data;
            }else {
                return null;
            }
        } catch (Exception e){
            Log.e(TAG, "error while getting data from table - " + TABLE_BLOG);
        } finally {
        }
        return data;
    }

    public String verifyUser (String email, String password) {
        Log.i(TAG, "verify user - login : called");
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        final String VERIFY_LOGIN ="SELECT "+USERS_COLUMN_EMAIL+", "+USERS_COLUMN_PASSWORD+", "+USERS_COLUMN_NAME+" FROM "+ TABLE_USERS+ " WHERE "+ USERS_COLUMN_EMAIL+" = '"+email+"' AND "+ USERS_COLUMN_PASSWORD+ " = '"+password+"'";
        Log.i(TAG, VERIFY_LOGIN);
        long ID = 0;
        String name="";

        try {
            Cursor cursor = db.rawQuery(VERIFY_LOGIN, null);
            if(cursor.getCount() == 1){
                cursor.moveToPosition(0);
                name = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_NAME));
                return name;
            }
        } catch (Exception e){
            Log.e(TAG, "error while verifying data in table - " + TABLE_USERS);
        } finally {
            db.endTransaction();
        }
        return name;
    }
}

package com.big.simplecash.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by big on 2019/6/13.
 */

public class MyOpenHelper extends DaoMaster.DevOpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(new GreenDaoContext(context), name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (newVersion > oldVersion) {
                switch (newVersion) {
                    case 2:
                        String new_column = "alter table " + "\"ORDER\"" + " add " + "discount" + " float";
                        db.execSQL(new_column);
                        break;
                    case 3:
                        String name = "alter table " + "\"ORDER\"" + " add " + "name" + " text";
                        db.execSQL(name);
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        clear(db);
//        onCreate(db);
    }

    private void clear(SQLiteDatabase db) {
        String sql = "DROP TABLE " + "IF EXISTS " + "\"MATERIAL_INFO\"";
        db.execSQL(sql);
        sql = "DROP TABLE " + "IF EXISTS " + "\"ORDER\"";
        db.execSQL(sql);
    }
}

package com.org.workout.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.channels.ticker

class DBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "workout.db"
        var table = "workouts"
        var title = "title"
        var date = "date"
        var place = "place"
        var startTime = "start_time"
        var endTime = "end_time"
        var group = "group_opt"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + table + " (" +
                    "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    title + " TEXT," +
                    date + " TEXT," +
                    place + " TEXT," +
                    startTime + " TEXT," +
                    endTime + " TEXT," +
                    group + " INTEGER)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $table"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun addItem(data: Item): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(title, data.title)
        values.put(date, data.date)
        values.put(place, data.place)
        values.put(startTime, data.startTime)
        values.put(endTime, data.endTime)
        values.put(group, data.group)

        return db.insert(table, null, values);
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteItem(id: Long): Int {
        val db = writableDatabase
        val selection = "id" + " LIKE ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(table, selection, selectionArgs)
    }
    fun updateItem(id: Long, item : Item): Int {
        val cv = ContentValues()
        cv.put(title, item.title)
        cv.put(date, item.date)
        cv.put(place, item.place)
        cv.put(startTime, item.startTime)
        cv.put(endTime, item.endTime)
        cv.put(group, item.group)

        val whereclause = "id=?"
        val whereargs = arrayOf(id.toString())
        return this.writableDatabase.update(table, cv, whereclause, whereargs)
    }
    fun readItem(id: Long): ArrayList<Item> {
        val workouts = ArrayList<Item>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from $table WHERE id='$id'", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(title))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(date))
                val place = cursor.getString(cursor.getColumnIndexOrThrow(place))
                val startTime = cursor.getString(cursor.getColumnIndexOrThrow(startTime))
                val endTime = cursor.getString(cursor.getColumnIndexOrThrow(endTime))
                val group = cursor.getInt(cursor.getColumnIndexOrThrow(group))

                workouts.add(Item(id, title, date, place, startTime, endTime, group))
                cursor.moveToNext()
            }
        }
        return workouts
    }

    fun readAll(): ArrayList<Item> {
        val workouts = ArrayList<Item>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from $table", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(title))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(date))
                val place = cursor.getString(cursor.getColumnIndexOrThrow(place))
                val startTime = cursor.getString(cursor.getColumnIndexOrThrow(startTime))
                val endTime = cursor.getString(cursor.getColumnIndexOrThrow(endTime))
                val group = cursor.getInt(cursor.getColumnIndexOrThrow(group))

                workouts.add(Item(id, title, date, place, startTime, endTime, group))
                cursor.moveToNext()
            }
        }
        return workouts
    }
}
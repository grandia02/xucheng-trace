package com.xucheng.trace
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
class TraceDb(context: Context): SQLiteOpenHelper(context,"trace.db",null,1) {
 override fun onCreate(db: SQLiteDatabase) { db.execSQL("CREATE TABLE observations (id INTEGER PRIMARY KEY AUTOINCREMENT, time_ms INTEGER NOT NULL, lat REAL NOT NULL, lon REAL NOT NULL, accuracy REAL NOT NULL, provider TEXT NOT NULL)") }
 override fun onUpgrade(db: SQLiteDatabase, oldVersion:Int,newVersion:Int) {}
 fun add(location:Location) { writableDatabase.execSQL("INSERT INTO observations(time_ms,lat,lon,accuracy,provider) VALUES(?,?,?,?,?)",arrayOf(location.time,location.latitude,location.longitude,location.accuracy,location.provider ?: "unknown")) }
 fun summary():String { readableDatabase.rawQuery("SELECT time_ms,lat,lon,accuracy FROM observations ORDER BY id DESC LIMIT 30",null).use { c -> val b=StringBuilder(); while(c.moveToNext()) { b.append(java.text.DateFormat.getDateTimeInstance().format(java.util.Date(c.getLong(0)))).append("  ").append(c.getDouble(1)).append(", ").append(c.getDouble(2)).append(" ±").append(c.getFloat(3)).append("m\n") }; return if(b.isEmpty()) "尚无定位记录" else b.toString() } }
}
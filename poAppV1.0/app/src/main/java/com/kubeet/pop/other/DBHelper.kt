package com.kubeet.pop.other

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DatabaseHandler.DB_NAME, null, DatabaseHandler.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($USER INTEGER PRIMARY KEY, $NAME TEXT,$PASSWORD TEXT,$STATUS TEXT,$TYPE TEXT);"
        val CREATE_TABLE_OPERADORES = "CREATE TABLE $TABLE_NAME_OPERADORES ($ID INTEGER PRIMARY KEY, $JSON TEXT);"
        val CREATE_TABLE_LUGARES = "CREATE TABLE $TABLE_NAME_LUGARES ($ID INTEGER PRIMARY KEY, $JSON TEXT);"

        db.execSQL(CREATE_TABLE)
        db.execSQL(CREATE_TABLE_OPERADORES)
        db.execSQL(CREATE_TABLE_LUGARES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        val DROP_TABLE_OPERADORES = "DROP TABLE IF EXISTS $TABLE_NAME_OPERADORES"
        val DROP_TABLE_LUGARES = "DROP TABLE IF EXISTS $TABLE_NAME_LUGARES"
        db.execSQL(DROP_TABLE)
        db.execSQL(DROP_TABLE_LUGARES)
        db.execSQL(DROP_TABLE_OPERADORES)
        onCreate(db)
    }

    companion object {
        private val DB_VERSION = 2
        private val DB_NAME = "AlphaDriver"
        private val TABLE_NAME = "Licencias"
        private val TABLE_NAME_OPERADORES = "Operadores"
        private val TABLE_NAME_LUGARES = "Lugares"
        private val USER = "Id"
        private val NAME = "Name"
        private val PASSWORD = "Password"
        private val STATUS = "Status"
        private val TYPE = "TipoUser"

        private val ID = "Id"
        private val JSON = "Json"

    }
}
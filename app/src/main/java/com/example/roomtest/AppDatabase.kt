package com.example.roomtest

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student :: class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    public abstract fun studentDao() : StudentDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            val tempInstance = INSTANCE
            if(tempInstance != null){
                Log.d("Return Exist Database" , "Rrturn Exist Database")
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                Log.d("Created new database" , "Created new database")
                return instance
            }

        }

    }

}
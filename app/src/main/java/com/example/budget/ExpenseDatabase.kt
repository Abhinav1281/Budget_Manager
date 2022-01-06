package com.example.budget

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budget.utils.subscribeOnBackground
import java.util.*

@Database(entities = [Expense::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase(){

    abstract fun expDao():ExpenseDAO
    companion object
    {
        private var instance:ExpenseDatabase?=null

        @Synchronized
        fun getInstance(ctx:Context):ExpenseDatabase
        {
            if(instance==null)
                instance= Room.databaseBuilder(ctx.applicationContext,ExpenseDatabase::class.java,
                    "exp_database").fallbackToDestructiveMigration().addCallback(roomCallback).build()

            return instance!!
        }

        private val roomCallback=object :Callback()
        {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populdateDatabase(instance!!)
            }
        }

        private fun populdateDatabase(db: ExpenseDatabase?) {
            //Not Populating with random values
            }
        }
    }

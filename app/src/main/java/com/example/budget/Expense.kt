package com.example.budget

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Expense")
data class Expense(val title:String,val category:String, val Date:Date, @PrimaryKey(autoGenerate = false) val id:Int)

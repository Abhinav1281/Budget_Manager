package com.example.budget

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Expense_Table")
data class Expense(val Amount:Double,
                   val category:String,
                   val Day:Int,
                   val Month:Int,
                   val Year:Int,
                   @PrimaryKey(autoGenerate = false) val id:Int?=null)

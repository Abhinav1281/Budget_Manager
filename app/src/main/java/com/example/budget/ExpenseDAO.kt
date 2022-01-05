package com.example.budget

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDAO {

    @Insert
    fun insert(exp:Expense)

    @Update
    fun update(exp:Expense)

    @Delete
    fun delete(exp:Expense)

    @Query("delete from Expense_Table")
    fun deleteAllExpenses()

    @Query("select * from Expense_Table")
    fun getAllExpenses():LiveData<List<Expense>>

    @Query("select sum(amount) from Expense_Table where category=:category and day=:day and month=:month and year=:year")
    fun getTodayCategoryExpense(category:String,day:Int,month:Int,year:Int):LiveData<Double>

    @Query("select sum(amount) from Expense_Table where day=:day and month=:month and year=:year")
    fun getTotalTodayExpense(day:Int,month:Int,year:Int):LiveData<Double>

    @Query("select sum(amount) from Expense_Table where category=:category and month=:month and year=:year")
    fun getMonthCategoryExpense(category:String,month:Int,year:Int):LiveData<Double>

    @Query("select sum(amount) from Expense_Table where month=:month and year=:year")
    fun getTotalMonthExpense(month:Int,year:Int):LiveData<Double>

    @Query("select sum(amount) from Expense_Table where category=:category and year=:year")
    fun getYearCategoryExpense(category:String,year:Int):LiveData<Double>

    @Query("select sum(amount) from Expense_Table where year=:year")
    fun getTotalYearExpense(year:Int):LiveData<Double>
}
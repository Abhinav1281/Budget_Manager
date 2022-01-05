package com.example.budget.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.budget.Expense
import com.example.budget.ExpenseRepository

class ExpenseViewModel(app:Application):AndroidViewModel(app) {

    private val repository=ExpenseRepository(app)
    private val allExp=repository.getAllExpenses()

    fun insert(exp:Expense)
    {
        repository.insert(exp)
    }

    fun update(exp:Expense)
    {
        repository.update(exp)
    }

    fun delete(exp:Expense)
    {
        repository.delete(exp)
    }

    fun deleteAllExpenses()
    {
        repository.deleteAllExpenses()
    }

    fun getAllExpenses():LiveData<List<Expense>>
    {
        return allExp
    }

    fun getTodayCategoryExpense(category:String,day:Int,month:Int,year:Int):LiveData<Double>
    {
        return repository.getTodayCategoryExpense(category, day, month, year)
    }

    fun getTotalTodayCategoryExpense(day:Int,month:Int,year:Int):LiveData<Double>
    {
        return repository.getTotalTodayCategoryExpense( day, month, year)
    }

    fun getMonthCategoryExpense(category:String,month:Int,year:Int):LiveData<Double>
    {
        return repository.getMonthCategoryExpense(category, month, year)
    }

    fun getTotalMonthCategoryExpense(month:Int,year:Int):LiveData<Double>
    {
        return repository.getTotalMonthCategoryExpense(month, year)
    }

    fun getYearCategoryExpense(category:String,year:Int):LiveData<Double>
    {
        return repository.getYearCategoryExpense(category, year)
    }

    fun getTotalYearCategoryExpense(year:Int):LiveData<Double>
    {
        return repository.getTotalYearCategoryExpense(year)
    }
}
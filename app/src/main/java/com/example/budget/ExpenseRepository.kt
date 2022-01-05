package com.example.budget

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.budget.utils.subscribeOnBackground

class ExpenseRepository(app:Application) {

    private var expDao:ExpenseDAO
    private var allExp:LiveData<List<Expense>>
    private val database=ExpenseDatabase.getInstance(app)

    init {
        expDao=database.expDao()
        allExp=expDao.getAllExpenses()
    }

    fun insert(exp:Expense)
    {
        subscribeOnBackground {
            expDao.insert(exp)
        }
    }

    fun update(exp:Expense)
    {
        subscribeOnBackground {
            expDao.update(exp)
        }
    }

    fun delete(exp:Expense)
    {
        subscribeOnBackground {
            expDao.delete(exp)
        }
    }

    fun deleteAllExpenses()
    {
        subscribeOnBackground {
            expDao.deleteAllExpenses()
        }
    }

    fun getAllExpenses():LiveData<List<Expense>>
    {
        return allExp
    }

    fun getTodayCategoryExpense(category:String,day:Int,month:Int,year:Int):LiveData<Double>
    {
        return expDao.getTodayCategoryExpense(category, day, month, year)
    }

    fun getTotalTodayCategoryExpense(day:Int,month:Int,year:Int):LiveData<Double>
    {
        return expDao.getTotalTodayExpense( day, month, year)
    }

    fun getMonthCategoryExpense(category:String,month:Int,year:Int):LiveData<Double>
    {
        return expDao.getMonthCategoryExpense(category, month, year)
    }

    fun getTotalMonthCategoryExpense(month:Int,year:Int):LiveData<Double>
    {
        return expDao.getTotalMonthExpense(month, year)
    }

    fun getYearCategoryExpense(category:String,year:Int):LiveData<Double>
    {
        return expDao.getYearCategoryExpense(category, year)
    }

    fun getTotalYearCategoryExpense(year:Int):LiveData<Double>
    {
        return expDao.getTotalYearExpense(year)
    }
}
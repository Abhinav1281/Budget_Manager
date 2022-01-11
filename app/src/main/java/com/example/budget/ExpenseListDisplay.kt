package com.example.budget

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.utils.ExpenseViewModel
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_expense_list_dispay.*
import java.util.*


class ExpenseListDisplay : AppCompatActivity() {

    private var day=-1
    private var month=-1
    private var year=-1
    private lateinit var vm:ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list_dispay)
        supportActionBar?.hide()



        day= intent.getIntExtra("DAY",-1)
        month= intent.getIntExtra("MONTH",-1)
        year=intent.getIntExtra("YEAR",-1)

        vm=ViewModelProviders.of(this)[ExpenseViewModel::class.java]
        setUpRecyclerView()
        setUpListener()
        getListItems()

        Snackbar.make(list_view_tag,"This is under development",Snackbar.LENGTH_SHORT).setAnchorView(add_expense).show()
    }


    private fun getListItems() {
        val c=Calendar.getInstance()
        if(day==-1)
            if(month==-1) {
                day_view.text=
                    if(year==c.get(Calendar.YEAR))
                        "Current Year"
                    else
                        year.toString()
                vm.getYearCategorySumExpense(year).observe(this,
                    {
                        if (it != null) {
                            adapter.submitList(it)
                            more_count.text=
                                if(it.isNotEmpty())
                                    "${it.size} entries"
                                else
                                    "No expenses"
                        }

                    })
                vm.getTotalYearCategoryExpense(year).observe(this,
                    {
                        if(it!=null)
                            Total_expense.text=getString(R.string.total_expenses_list_string)+" $it"
                        else
                            Total_expense.text=getString(R.string.total_expenses_list_string)+" 0"
                    })
            }
            else {
                val monthList= listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec")
                day_view.text=
                    if(month==c.get(Calendar.MONTH)&&year==c.get(Calendar.YEAR))
                        "Current Month"
                    else
                        "${monthList[month]}, $year"
                vm.getMonthCategorySumExpense(month, year).observe(this,
                    {
                        if (it != null) {
                            adapter.submitList(it)
                            more_count.text=
                                if(it.isNotEmpty())
                                    "${it.size} entries"
                                else
                                    "No expenses"
                        }

                    })
                vm.getTotalMonthCategoryExpense(month,year).observe(this,
                    {
                        if(it!=null)
                            Total_expense.text=getString(R.string.total_expenses_list_string)+" $it"
                        else
                            Total_expense.text=getString(R.string.total_expenses_list_string)+" 0"
                    })
            }
        else {
            day_view.text=
                if(day==c.get(Calendar.DAY_OF_MONTH)&&month==c.get(Calendar.MONTH)&&year==c.get(Calendar.YEAR))
                    "Today"
                else
                    "$day.${month+1}.$year"
            vm.getTodayCategorySumExpense(day, month, year).observe(this,
                {
                    if (it != null) {
                        adapter.submitList(it)
                        more_count.text=
                            if(it.isNotEmpty())
                                "${it.size} entries"
                            else
                                "No expenses"
                    }
                })
            vm.getTotalTodayCategoryExpense(day, month, year).observe(this,
                {
                    if(it!=null)
                        Total_expense.text=getString(R.string.total_expenses_list_string)+" $it"
                    else
                        Total_expense.text=getString(R.string.total_expenses_list_string)+" 0"
                })
        }

    }




    private fun setUpListener() {
        //date_picker_layout
        setUpDatePicker()

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(1,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        )
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item=adapter.getItemAt((viewHolder.adapterPosition))
                vm.delete(item)
            }
        }).attachToRecyclerView(ui_recyclerView)

        //bottomAppBar
        setUpBottomAppBar()

    }

    private fun setUpBottomAppBar() {
        bottomAppBar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        bottomAppBar.setNavigationOnClickListener {
            startActivity(Intent(this,ReportActivity::class.java))
        }
    }

    private fun setUpDatePicker() {
        date_check.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                date_picker_list_view.setDisplayDaysOfMonth(true)
                date_picker_list_view.setDisplayDaysOfMonth(true)
                month_check.isChecked=true
            }
            else
                date_picker_list_view.setDisplayDaysOfMonth(false)
        }
        month_check.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
                date_picker_list_view.setDisplayMonths(true)
            else
            {
                date_picker_list_view.setDisplayMonths(false)
                date_picker_list_view.setDisplayDaysOfMonth(false)
                date_check.isChecked=false
            }
        }
        date_picker_list_cancel.setOnClickListener {
            supportActionBar?.title=""
            calendar_view.visibility = View.GONE
            list_view_tag.alpha=1.0F
            ui_recyclerView.alpha=1.0F
        }
        date_final_list_btn.setOnClickListener {
            val date=date_picker_list_view.date
            val c=Calendar.getInstance()
            c.time=date
            day = if(!date_check.isChecked)
                -1
            else
                c.get(Calendar.DAY_OF_MONTH)
            month = if(!month_check.isChecked)
                -1
            else
                c.get(Calendar.MONTH)
            year=c.get(Calendar.YEAR)
            getListItems()

            supportActionBar?.title=""
            calendar_view.visibility = View.GONE
            list_view_tag.alpha=1.0F
            ui_recyclerView.alpha=1.0F
        }
    }

    private fun setUpRecyclerView() {
        ui_recyclerView.layoutManager= LinearLayoutManager(this)
        ui_recyclerView.setHasFixedSize(false)
        ui_recyclerView.isNestedScrollingEnabled=true

        adapter=ExpenseAdapter{}
        ui_recyclerView.adapter=adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.report_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.Calendar_call->
            {
                if(calendar_view.visibility==View.GONE) {
                    supportActionBar?.title="Select Date"
                    calendar_view.visibility = View.VISIBLE
                    list_view_tag.alpha=0.0F
                    ui_recyclerView.alpha=0.0F
                }
                else {
                    supportActionBar?.title=""
                    calendar_view.visibility = View.GONE
                    list_view_tag.alpha=1.0F
                    ui_recyclerView.alpha=1.0F
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this,ReportActivity::class.java))
    }
}
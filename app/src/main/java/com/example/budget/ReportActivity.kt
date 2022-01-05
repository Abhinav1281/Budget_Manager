package com.example.budget

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProviders
import app.futured.donut.DonutSection
import com.example.budget.utils.ExpenseViewModel
import kotlinx.android.synthetic.main.activity_report.*
import java.util.*
import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener


class ReportActivity : AppCompatActivity() {

    private lateinit var vm:ExpenseViewModel
    private lateinit var catlist:List<String>
    private var cal: Calendar =Calendar.getInstance()
    private lateinit var progressList:List<com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar>
    private var curCat= mutableListOf(-1,-1,-1)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this,R.color.black))
        supportActionBar?.title=""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_money_bank)
        vm= ViewModelProviders.of(this)[ExpenseViewModel::class.java]
//        setUpProgressBar()
        setUpDonutView()
        setUpListeners()
        donut_view.visibility=View.INVISIBLE

        //init
        catlist= arrayListOf("Food","Shopping","Game","General","Phone","Sports","Pets","Travel")
        progressList= arrayListOf(food_progress,shopping_progress,game_progress,
            general_progress,phone_progress,sports_progress,pets_progress,travel_progress)

        changeViewToToday()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpListeners() {
        day_select.setOnClickListener {
            setUpDonutView()
            changeViewToToday()
        }
        month_select.setOnClickListener {
            setUpDonutView()
            changeViewToMonth()
        }
        year_select.setOnClickListener {
            setUpDonutView()
            changeViewToYear()
        }
    }

    private fun changeViewToYear() {
        //resetting calendar reference
        cal= Calendar.getInstance()

        //forListView
        curCat= mutableListOf(-1,-1,cal.get(Calendar.YEAR))

        //for total expense
        vm.getTotalYearCategoryExpense(cal.get(Calendar.YEAR))
            .observe(this,
                {
                    if(it!=null) {
                        setMaxValues(it.toFloat())
                        donut_view.visibility=View.VISIBLE
                    }
                    else
                    {
                        donut_view.visibility=View.INVISIBLE
                        setMaxValues(0.0f)
                        total_spent.text=getText(R.string.nothing_to_show_report_text)
                    }
                })

        //button visibility modification
        day_select.alpha=0.5f
        month_select.alpha=0.5f
        year_select.alpha=1.0f

        //individual category fetch
        for (s in catlist) {
            vm.getYearCategoryExpense(s, cal.get(Calendar.YEAR))
                .observe(this,
                    {
                        if (it != null) {
                            addValuesProgress(it.toFloat(), s)
                        } else {
                            addValuesProgress(0.0f, s)
                            removeView(s)
                        }
                    })
        }

    }

    private fun changeViewToToday() {
        //resetting calendar reference
        cal= Calendar.getInstance()

        //forListView
        curCat= mutableListOf(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR))

        //for total exp value
        vm.getTotalTodayCategoryExpense(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR))
            .observe(this,
                {
                    if(it!=null) {
                        setMaxValues(it.toFloat())
                        donut_view.visibility=View.VISIBLE
                    }
                    else
                    {
                        donut_view.visibility=View.INVISIBLE
                        setMaxValues(0.0f)
                        total_spent.text=getText(R.string.nothing_to_show_report_text)
                    }
                })

        //button visibility modifications
        day_select.alpha=1.0f
        month_select.alpha=0.5f
        year_select.alpha=0.5f

        //individual category values
        for (s in catlist) {
            vm.getTodayCategoryExpense(s,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.YEAR))
                .observe(this,
                    {
                        if (it != null) {
                            addValuesProgress(it.toFloat(), s)
                        } else {
                            addValuesProgress(0.0f, s)
                            removeView(s)
                        } })
        }

    }

    private fun changeViewToMonth() {
        //resetting calendar reference
        cal= Calendar.getInstance()

        //forListView
        curCat= mutableListOf(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR))

        //for total expense
        vm.getTotalMonthCategoryExpense(cal.get(Calendar.MONTH),cal.get(Calendar.YEAR))
            .observe(this,
                {
                    if(it!=null) {
                        setMaxValues(it.toFloat())
                        donut_view.visibility=View.VISIBLE
                    }
                    else
                    {
                        donut_view.visibility=View.INVISIBLE
                        setMaxValues(0.0f)
                        total_spent.text=getText(R.string.nothing_to_show_report_text)
                    }
                })

        //button visibility modification
        day_select.alpha=0.5f
        month_select.alpha=1.0f
        year_select.alpha=0.5f


        //individual category expense
        for (s in catlist) {
            vm.getMonthCategoryExpense(s, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
                .observe(this,
                    {
                        if (it != null) {
                            addValuesProgress(it.toFloat(), s)
                        } else {
                            addValuesProgress(0.0f, s)
                            removeView(s)
                        }
                    })
        }

    }

    private fun removeView(s: String) {
        progressList[catlist.indexOf(s)].visibility=View.GONE
    }


    private fun setMaxValues(max: Float) {
        total_spent.text=getString(R.string.total_spent_value)+max.toString()
        for (bars in progressList){
            bars.max = max
        }
    }



    private fun addValuesProgress(progress: Float, s: String) {
        donut_view.setAmount(s, progress)
        progressList[catlist.indexOf(s)].visibility=View.VISIBLE
        progressList[catlist.indexOf(s)].progress=progress
        Log.i("Progress Visible",catlist.indexOf(s).toString())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpDonutView() {
        val foodAmount=DonutSection(
            "Food",
            getColor(R.color.Red),
            0f
        )
        val shoppingAmount=DonutSection(
            "Shopping",
            getColor(R.color.Pink),
            0f
        )
        val travelAmount=DonutSection(
            "Travel",
            getColor(R.color.Blue),
            0f
        )
        val phoneAmount=DonutSection(
            "Phone",
            getColor(R.color.Green),
            0f
        )
        val generalAmount=DonutSection(
            "General",
            getColor(R.color.Yellow),
            0f
        )
        val petAmount=DonutSection(
            "Pets",
            getColor(R.color.Orange),
            0f
        )
        val gameAmount=DonutSection(
            "Game",
            getColor(R.color.Purple),
            0f
        )
        val sportsAmount=DonutSection(
            "Sports",
            getColor(R.color.Teal),
            0f
        )
        donut_view.submitData(listOf(foodAmount,shoppingAmount,travelAmount,phoneAmount,
        gameAmount,generalAmount,petAmount,sportsAmount))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.report_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home->onBackPressed()
            R.id.Calendar_call->datePickDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun datePickDialog() {
        val datePickerDialog = DatePickerDialog(this,
            { _, year, monthOfYear, dayOfMonth ->
                setViewNewDate(dayOfMonth,monthOfYear,year) },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setViewNewDate(dayOfMonth: Int, month: Int, year: Int) {
        setUpDonutView()
        //forListView
        curCat= mutableListOf(dayOfMonth,month,year)

        //for total exp value
        vm.getTotalTodayCategoryExpense(dayOfMonth,month,year)
            .observe(this,
                {
                    if(it!=null) {
                        setMaxValues(it.toFloat())
                        donut_view.visibility=View.VISIBLE
                    }
                    else
                    {
                        donut_view.visibility=View.INVISIBLE
                        setMaxValues(0.0f)
                        total_spent.text=getText(R.string.nothing_to_show_report_text)
                    }
                })

        //button visibility modifications
        day_select.alpha=0.5f
        month_select.alpha=0.5f
        year_select.alpha=0.5f

        //individual category values
        for (s in catlist) {
            vm.getTodayCategoryExpense(s,
                dayOfMonth,month,year)
                .observe(this,
                    {
                        if (it != null) {
                            Log.i("available",s)
                            addValuesProgress(it.toFloat(), s)
                        } else {
                            addValuesProgress(0.0f, s)
                            removeView(s)
                        } })
        }
    }

}
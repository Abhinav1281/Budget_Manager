package com.example.budget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.budget.utils.ExpenseViewModel
import kotlinx.android.synthetic.main.activity_add_expense.*
import java.security.AccessController.getContext
import java.util.*

class Add_Expense : AppCompatActivity() {

    private lateinit var btn_list:List<Button>
    var date: Date? =null
    private lateinit var btn_cat_list:List<ImageButton>
    private var category: String ="General"
    private lateinit var cat_list:List<String>
    private lateinit var vm:ExpenseViewModel
    val list= mutableListOf<Double>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        supportActionBar?.title= "Enter Amount"
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.black))


        //btn_array_init
        btn_list= arrayListOf(btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,
            btn_6,btn_7,btn_8,btn_9,btn_plus,btn_minus,btn_divide,btn_multiply,
            btn_clear,btn_dot,btn_equal)
        btn_cat_list= arrayListOf(btn_food,btn_shopping,btn_game,btn_general,
            btn_mobile,btn_sports,btn_pet,btn_travel)
        cat_list= arrayListOf("Food","Shopping","Game","General","Phone","Sports","Pets","Travel")

        eqn_scroll.post {
            eqn_scroll.fullScroll(View.FOCUS_RIGHT)
        }
        eqn_holder.isSelected=true
        vm=ViewModelProviders.of(this)[ExpenseViewModel::class.java]
        setUpListeners()
        onCatBtnPress(btn_general)
    }

    private fun setUpListeners()
    {
        for (btn in btn_list)
        {
            btn.setOnClickListener {
                if(eqn_holder.text==getString( R.string.def_calc_text))
                    eqn_holder.text=""
                onCalcBtnPress(btn)
            }
        }

        for(btn in btn_cat_list)
        {
            btn.setOnClickListener {
                onCatBtnPress(btn)
            }
        }
        btn_backspace.setOnClickListener {
            onCalcBtnPress(btn_backspace)
        }
        btn_submit.setOnClickListener {
            onCalcBtnPress(btn_submit)
        }
        open_calendar.setOnClickListener {
            if(date_layout.visibility==View.GONE)
            {
                date_layout.visibility=View.VISIBLE
                calc_layout.alpha= 0.0F
                category_layout.alpha=0.0F
                date_layout.alpha=1.0f
            }
            else
            {
                date=date_picker.date
                date_layout.visibility=View.GONE
                calc_layout.alpha= 1.0F
                category_layout.alpha=1.0F
                date_layout.alpha=0.0f
            }
        }
        date_final_btn.setOnClickListener {
            date=date_picker.date
            date_layout.visibility=View.GONE
            calc_layout.alpha= 1.0F
            category_layout.alpha=1.0F
            date_layout.alpha=0.0f
            open_calendar.setColorFilter(ContextCompat.getColor(this,R.color.white))
        }

        date_picker_cancel.setOnClickListener {
            date_layout.visibility=View.GONE
            calc_layout.alpha= 1.0F
            category_layout.alpha=1.0F
            date_layout.alpha=0.0f
        }
    }

    private fun onCatBtnPress(btn:ImageButton) {
        for (b in btn_cat_list)
            if (btn.id == b.id){
                b.setBackgroundResource(R.drawable.button_bg_selected)
                category= cat_list[btn_cat_list.indexOf(btn)]
                btn.alpha=0.6f
            }else {
                b.setBackgroundResource(0)
                b.alpha=1.0f
            }
//        Toast.makeText(this,category,Toast.LENGTH_SHORT).show()
    }

    private fun onCalcBtnPress(item: View)
    {
        if(list.isNotEmpty()) {
            eqn_holder.text = getText(R.string.def_calc_text)
            list.clear()
        }
        when(item)
        {
            //numerical buttons
            btn_0->addNumber('0')
            btn_1->addNumber('1')
            btn_2->addNumber('2')
            btn_3->addNumber('3')
            btn_4->addNumber('4')
            btn_5->addNumber('5')
            btn_6->addNumber('6')
            btn_7->addNumber('7')
            btn_8->addNumber('8')
            btn_9->addNumber('9')
            btn_dot->
            {
                if(!eqn_holder.text.toString().contains('.'))
                    addNumber('.')
            }

            //function buttons
            btn_clear->eqn_holder.text=getString(R.string.def_calc_text)
            btn_plus->addNumber('+')
            btn_minus->addNumber('-')
            btn_multiply->addNumber('*')
            btn_divide->addNumber('/')
            btn_equal->calculateValue()
            btn_backspace->eqn_holder.text=eqn_holder.text.toString().dropLast(1)

            btn_submit->submitExpense()
        }

        if(eqn_holder.text.toString().isEmpty())
            eqn_holder.text=getText(R.string.def_calc_text)
    }

    private fun addNumber(c:Char)
    {
        if(eqn_holder.text==getString(R.string.def_calc_text))
            if(c == '.')
                eqn_holder.text= "0."
            else
                eqn_holder.text=c.toString()
        else
            eqn_holder.text=eqn_holder.text.toString()+c.toString()
    }

    private fun submitExpense() {
        calculateValue()
        if (date == null) {
            Toast.makeText(this, "Please Select a Date!", Toast.LENGTH_SHORT).show()
            open_calendar.setColorFilter(ContextCompat.getColor(this,R.color.Red))
        }
        else if(eqn_holder.text.toString()==getString(R.string.def_calc_text))
            Toast.makeText(this,"Please enter some amount",Toast.LENGTH_SHORT).show()
        else
        {
            val cal = Calendar.getInstance()
            cal.time = date
            var amount = eqn_holder.text.toString().toDouble()
            amount=String.format("%.2f", amount).toDouble()
            vm.insert(Expense(amount, category, cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)))
            Toast.makeText(this,"Expense Added!",Toast.LENGTH_SHORT).show()
            eqn_holder.text=getString(R.string.def_calc_text)
        }

    }

    private fun calculateValue() {
        var eqn=""
        val li=eqn_holder.text.toString().last()
        eqn = if(li=='+'||li=='-'||li=='*'||li=='/')
            eqn_holder.text.toString().dropLast(1)+"!"
        else
            eqn_holder.text.toString()+"!"
        val symbol_list= mutableListOf<Char>()
        var cur_amount:String=""
        for(i in eqn)
        {
            if(i=='+'||i=='-'||i=='*'||i=='/'||i=='!')
            {
                list += cur_amount.toDouble()
                cur_amount=""
                if(i!='!')
                    symbol_list.add(i)
                continue
            }
            cur_amount+=i
        }
        Log.i("calc_list", "$list...$symbol_list")

        for(symbol in symbol_list)
        {
            var value:Double=0.0
            when(symbol)
            {
                '+'->value= list.removeAt(0)+list.removeAt(0)
                '-'->value=list.removeAt(0)-list.removeAt(0)
                '*'->value=list.removeAt(0)*list.removeAt(0)
                '/'->value=list.removeAt(0)/list.removeAt(0)
            }
            Log.i("calc_list", "$list...$symbol_list")
            list.add(0, String.format("%.2f", value).toDouble())
        }
        eqn_holder.text=list.last().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_amount_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.btn_report->
            {
                startActivity(Intent(this,ReportActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
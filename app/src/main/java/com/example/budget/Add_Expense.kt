package com.example.budget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_add_expense.*

class Add_Expense : AppCompatActivity() {

    private lateinit var btn_list:List<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        supportActionBar?.title="Enter Amount"

        btn_list= arrayListOf(btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,
            btn_6,btn_7,btn_8,btn_9,btn_plus,btn_minus,btn_divide,btn_multiply,
            btn_clear,btn_dot,btn_equal)

        setUpListeners()
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
        btn_backspace.setOnClickListener {
            onCalcBtnPress(btn_backspace)
        }
        btn_submit.setOnClickListener {
            onCalcBtnPress(btn_submit)
        }
    }

    private fun onCalcBtnPress(item: View)
    {
        when(item)
        {
            //numerical buttons
            btn_0->eqn_holder.text=eqn_holder.text.toString()+"0"
            btn_1->eqn_holder.text=eqn_holder.text.toString()+"1"
            btn_2->eqn_holder.text=eqn_holder.text.toString()+"2"
            btn_3->eqn_holder.text=eqn_holder.text.toString()+"3"
            btn_4->eqn_holder.text=eqn_holder.text.toString()+"4"
            btn_5->eqn_holder.text=eqn_holder.text.toString()+"5"
            btn_6->eqn_holder.text=eqn_holder.text.toString()+"6"
            btn_7->eqn_holder.text=eqn_holder.text.toString()+"7"
            btn_8->eqn_holder.text=eqn_holder.text.toString()+"8"
            btn_9->eqn_holder.text=eqn_holder.text.toString()+"9"
            btn_dot->eqn_holder.text=eqn_holder.text.toString()+"."

            //function buttons
            btn_clear->eqn_holder.text=getString(R.string.def_calc_text)
            btn_plus->eqn_holder.text=eqn_holder.text.toString()+"+"
            btn_minus->eqn_holder.text=eqn_holder.text.toString()+"-"
            btn_multiply->eqn_holder.text=eqn_holder.text.toString()+"*"
            btn_divide->eqn_holder.text=eqn_holder.text.toString()+"/"
            btn_equal->calculateValue()
            btn_backspace->eqn_holder.text=eqn_holder.text.toString().dropLast(1)
        }

        if(eqn_holder.text.toString().isEmpty())
            eqn_holder.text=getText(R.string.def_calc_text)
    }

    private fun calculateValue() {
        val eqn=eqn_holder.text.toString()+"!"
        val list= mutableListOf<Double>()
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
            list.add(0,value)
        }
        eqn_holder.text=list.last().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_amount_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
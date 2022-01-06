package com.example.budget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

private lateinit var ctx:Context
class ExpenseAdapter(private val onItemClickListener:(Expense)->Unit):ListAdapter<Expense, ExpenseAdapter.ExpenseHolder>(
    diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {
        ctx=parent.context
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.expense_list_item,parent,false)
        return ExpenseHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        val iconlist= listOf(R.drawable.ic_food,R.drawable.ic_shopping,R.drawable.ic_travel,R.drawable.ic_mobile
        ,R.drawable.ic_home,R.drawable.ic_pets,R.drawable.ic_game,R.drawable.ic_sports)
        val catList= arrayListOf("Food","Shopping","Travel","Phone","General","Pets","Game","Sports")
        with(getItem(position))
        {
            holder.title.text=Amount.toString()
            holder.date.text="$Day.$Month.$Year"
            holder.icon.setImageResource(iconlist[catList.indexOf(category)])
        }
    }
    fun getItemAt(position: Int): Expense =getItem(position)
    inner class ExpenseHolder(v: View): RecyclerView.ViewHolder(v)
    {
        val title: TextView =itemView.findViewById(R.id.list_title)
        val icon:ImageView=itemView.findViewById(R.id.list_icon)
        val date:TextView=itemView.findViewById(R.id.list_date)
        init {
            itemView.setOnClickListener {
                if(adapterPosition!= DiffUtil.DiffResult.NO_POSITION)
                    onItemClickListener(getItem(adapterPosition))
            }
        }
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<Expense>()
{
    override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id==newItem.id

    override fun areContentsTheSame(oldItem: Expense, newItem: Expense)=
        oldItem.Amount==newItem.Amount
                && oldItem.category==newItem.category
                && oldItem.Day==newItem.Day
                && oldItem.Month==newItem.Month
                && oldItem.Year==newItem.Year
}
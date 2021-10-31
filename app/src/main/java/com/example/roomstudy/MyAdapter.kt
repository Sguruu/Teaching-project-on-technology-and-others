package com.example.roomstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val list: MutableList<DataMyAdapter>,
    private val customClickAdapter: CustomClickAdapter? = null,
) : RecyclerView.Adapter<MyAdapter.ViewHolderMyAdapter>() {
    class ViewHolderMyAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nameTV: TextView? = null
        var lastNameTV: TextView? = null
        var ageTV: TextView? = null
        var clearButton: Button? = null
        var editButton: Button? = null

        init {
            nameTV = itemView.findViewById(R.id.rec_ele_name)
            lastNameTV = itemView.findViewById(R.id.rec_ele_lastName)
            ageTV = itemView.findViewById(R.id.rec_ele_age)
            clearButton = itemView.findViewById(R.id.rec_ele_buttonClear)
            editButton = itemView.findViewById(R.id.rec_ele_buttonEdit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMyAdapter {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerviewel_ement, parent, false)
        return ViewHolderMyAdapter(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderMyAdapter, position: Int) {
        holder.nameTV?.text = list[position].name
        holder.lastNameTV?.text = list[position].lastName
        holder.ageTV?.text = list[position].age.toString()

        /*
        0 - нажатие на очистить
        1 - нажатие на редактировать
        Следует использовать именно holder.adapterPosition, т.к. position не обновляется при
        обновлении списка
         */
        holder.clearButton?.setOnClickListener {
            customClickAdapter?.customClick(0, holder.adapterPosition, list[holder.adapterPosition])
        }
        holder.editButton?.setOnClickListener {
            customClickAdapter?.customClick(1, holder.adapterPosition, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

data class DataMyAdapter(
    val id: Long?,
    val name: String?,
    val lastName: String?,
    val age: Int?,
)
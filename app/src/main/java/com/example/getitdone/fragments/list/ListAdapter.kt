package com.example.getitdone.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.getitdone.R
import com.example.getitdone.data.models.Priority
import com.example.getitdone.data.models.ToDoData
import kotlinx.android.synthetic.main.fragment_add.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var dataList = emptyList<ToDoData>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: TextView = itemView.findViewById(R.id.title_txt)
        val descriptionTxt: TextView = itemView.findViewById(R.id.description_txt)
        val priorityIndicator: CardView = itemView.findViewById(R.id.priority_indicator)
        val rowBackground : View = itemView.findViewById(R.id.row_background)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = dataList[position]
        holder.titleTxt.text = current.title
        holder.descriptionTxt.text = current.description

        holder.rowBackground.setOnClickListener {

            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(current)
            // as we are passing data so we will not use below line
            //holder.itemView.findNavController().navigate(R.id.action_listFragment_to_updateFragment)
            holder.itemView.findNavController().navigate(action)

        }

        when (current.priority) {
            Priority.HIGH -> {
                holder.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.red
                    )
                )
            }
            Priority.MEDIUM -> {
                holder.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.yellow
                    )
                )
            }
            Priority.LOW -> {
                holder.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
            }
        }
    }

    fun setData(toDoData: List<ToDoData>) {
        this.dataList = toDoData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataList.size
}
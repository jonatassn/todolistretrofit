package br.edu.ifpr.jonatas.tasktrab.ui

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifpr.jonatas.tasktrab.R
import br.edu.ifpr.jonatas.tasktrab.entities.Tasky
import kotlinx.android.synthetic.main.task_view_expanded.view.*
import kotlinx.android.synthetic.main.task_view_shrinked.view.*

class TaskyAdapter( //constructor attributes
                    private var taskies : MutableList<Tasky>,
                    private var listener: TaskyAdapterListener
    ) //implements
        : RecyclerView.Adapter<TaskyAdapter.ViewHolder>()
{
    var editingTask : Tasky? = null
    var newOne : Boolean = false
    var editing : Boolean = false

    fun addTasky(tasky: Tasky) : Int {
        if(editingTask == null) {
            taskies.add(0, tasky)
            notifyItemInserted(0)
            editingTask = tasky
            editing = false
            newOne = true
        }
        return 0
    }



    override fun getItemViewType(position: Int): Int {
        return if(editingTask != null && position == taskies.indexOf(editingTask!!)) {
            R.layout.task_view_expanded
        }
        else {
            R.layout.task_view_shrinked
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))

    override fun getItemCount() = taskies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tasky = taskies[position]
        holder.fillUi(tasky)
    }



    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun fillUi(tasky: Tasky) {
            if(itemViewType == R.layout.task_view_shrinked) {
                itemView.txtTitleShrinked.text = tasky.title
                itemView.setOnClickListener {
                    if(!newOne) {
                        editingTask = tasky
                        editing = true
                        //Log.d("task", "editingTask-task: title - "+ tasky.title+" desc - "+tasky.description)
                        notifyItemChanged(taskies.indexOf(tasky))
                    }
                }
                itemView.setOnLongClickListener {
                    tasky.done = !tasky.done
                    notifyItemChanged(taskies.indexOf(tasky))
                    listener.onTaskyEdited(tasky)
                    true
                }
                var card = itemView as CardView
                if (tasky.done) {
                    card.setCardBackgroundColor(Color.GREEN)
                    itemView.txtTitleShrinked.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    itemView.btShare.visibility = View.VISIBLE
                }
                else {
                    card.setCardBackgroundColor(0)
                    itemView.txtTitleShrinked.paintFlags = 0
                    itemView.btShare.visibility = View.INVISIBLE
                }

                itemView.btShare.setOnClickListener {
                    listener.onTaskyShared(tasky)
                }

            }
            else {
                itemView.txtTitle.text.clear()
                itemView.txtDescription.text.clear()
                itemView.txtTitle.setText(tasky.title)
                itemView.txtDescription.setText(tasky.description)

                itemView.btDelete.setOnClickListener {
                    with(this@TaskyAdapter) {
                        val position = taskies.indexOf(tasky)
                        taskies.removeAt(position)
                        notifyItemRemoved(position)
                        editingTask = null
                        listener.onTaskyRemoved(tasky)
                        newOne = false
                    }
                }

                itemView.btSave.setOnClickListener {
                    tasky.title = itemView.txtTitle.text.toString()
                    tasky.description = itemView.txtDescription.text.toString()
                    val position = taskies.indexOf(tasky)
                    if(tasky.title == "") {

                        //taskies.removeAt(position)
                        //notifyItemRemoved(position)
                        with(this@TaskyAdapter) {
                            //editingTask = null
                            listener.onTaskySaved(tasky)
                        }
                    }else {
                        if(editing) {
                            with(this@TaskyAdapter) {
                                editingTask = null
                                notifyItemChanged(position)
                                listener.onTaskyEdited(tasky)
                            }
                        }else {
                            with(this@TaskyAdapter) {
                                editingTask = null
                                editing = false
                                notifyItemChanged(position)
                                listener.onTaskySaved(tasky)
                            }
                        }
                        newOne = false
                    }
                }

            }
        }
    }
}
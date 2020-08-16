package br.edu.ifpr.jonatas.tasktrab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringDef
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import br.edu.ifpr.jonatas.tasktrab.db.AppDatabase
import br.edu.ifpr.jonatas.tasktrab.db.dao.TaskyDao
import br.edu.ifpr.jonatas.tasktrab.entities.Tasky
import br.edu.ifpr.jonatas.tasktrab.ui.TaskyAdapter
import br.edu.ifpr.jonatas.tasktrab.ui.TaskyAdapterListener

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), TaskyAdapterListener {
    lateinit var taskyDao : TaskyDao
    lateinit var adapter : TaskyAdapter
    var taskyEditing : Tasky? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java, "tasky.db"
        ).allowMainThreadQueries().build()

        taskyDao = db.taskyDao()

        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            val tasky = Tasky("", "", false)
            adapter.addTasky(tasky)
            rvTasks.scrollToPosition(0)
        }

        loadData()





    }

    private fun loadData() {
        val taskies = taskyDao.getAll()

        adapter = TaskyAdapter(taskies.toMutableList(), this)

        rvTasks.adapter = adapter
        rvTasks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
    }

    override fun onTaskyRemoved(tasky: Tasky) {
        taskyDao.delete(tasky)
    }

    override fun onTaskySaved(tasky: Tasky) {

        if(tasky.title == "") {
            Toast.makeText(this, R.string.empty_tasky_title_error_message, Toast.LENGTH_SHORT).show()
        }
        else {
            taskyDao.insert(tasky)
        }
    }

    override fun onTaskyEdited(tasky: Tasky) {
        taskyDao.update(tasky)
    }

    override fun onTaskyShared(tasky: Tasky) {
        val msg: String = "${getString(R.string.share_message)} ${tasky.title}"


        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, msg)
        startActivity(shareIntent)

    }
}

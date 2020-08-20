package br.edu.ifpr.jonatas.tasktrab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import br.edu.ifpr.jonatas.tasktrab.api.TaskiesService
import br.edu.ifpr.jonatas.tasktrab.db.AppDatabase
import br.edu.ifpr.jonatas.tasktrab.db.dao.TaskyDao
import br.edu.ifpr.jonatas.tasktrab.entities.Tasky
import br.edu.ifpr.jonatas.tasktrab.ui.TaskyAdapter
import br.edu.ifpr.jonatas.tasktrab.ui.TaskyAdapterListener

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), TaskyAdapterListener {
    lateinit var taskyDao : TaskyDao
    lateinit var adapter : TaskyAdapter
    lateinit var retrofit: Retrofit
    lateinit var service : TaskiesService
    var taskyEditing : Tasky? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java, "tasky.db"
        ).allowMainThreadQueries().build()

        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            val tasky = Tasky("", "", false)
            adapter.addTasky(tasky)
            rvTasks.scrollToPosition(0)
        }
        taskyDao = db.taskyDao()
        retrofitSetup()
        //roomSetup()

    }

    private fun retrofitSetup() {
        retrofit = Retrofit.Builder()
            .baseUrl("http://10.1.1.110:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(TaskiesService::class.java)

        service.getAll().enqueue(object : Callback<List<Tasky>>{
            override fun onFailure(call: Call<List<Tasky>>, t: Throwable) {
                Log.e("ERRO", "ERRO ", t)
            }

            override fun onResponse(call: Call<List<Tasky>>, response: Response<List<Tasky>>) {
                val taskies = response.body()
                if(taskies != null)
                    loadData(taskies)


            }
        })
    }

    private fun roomSetup() {
        val taskies = taskyDao.getAll()
        loadData(taskies)
    }

    private fun loadData(taskies : List<Tasky>) {

        adapter = TaskyAdapter(taskies.toMutableList(), this)

        rvTasks.adapter = adapter
        rvTasks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
    }

    override fun onTaskyRemoved(tasky: Tasky) {
        //taskyDao.delete(tasky)
        service.delete(tasky.id.toLong()).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
            }
        })
    }

    override fun onTaskySaved(tasky: Tasky) {

        if(tasky.title == "") {
            Toast.makeText(this, R.string.empty_tasky_title_error_message, Toast.LENGTH_SHORT).show()
        }
        else {
            //taskyDao.insert(tasky)
            service.insert(tasky).enqueue(object : Callback<Tasky> {
                override fun onFailure(call: Call<Tasky>, t: Throwable) {
                }
                override fun onResponse(call: Call<Tasky>, response: Response<Tasky>) {
                    tasky.id = response.body()!!.id
                }

            })
        }
    }

    override fun onTaskyEdited(tasky: Tasky) {
        //taskyDao.update(tasky)
        service.update(tasky.id.toLong(), tasky).enqueue(object : Callback<Tasky> {
            override fun onFailure(call: Call<Tasky>, t: Throwable) {
            }

            override fun onResponse(call: Call<Tasky>, response: Response<Tasky>) {

            }

        })
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

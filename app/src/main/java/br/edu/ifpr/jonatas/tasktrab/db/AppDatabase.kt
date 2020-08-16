package br.edu.ifpr.jonatas.tasktrab.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.ifpr.jonatas.tasktrab.db.dao.TaskyDao
import br.edu.ifpr.jonatas.tasktrab.entities.Tasky

@Database(entities = arrayOf(Tasky::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskyDao() : TaskyDao
}
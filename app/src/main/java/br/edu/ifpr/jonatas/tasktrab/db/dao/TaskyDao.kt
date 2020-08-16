package br.edu.ifpr.jonatas.tasktrab.db.dao

import androidx.room.*
import br.edu.ifpr.jonatas.tasktrab.entities.Tasky

@Dao
interface TaskyDao {
    @Query("select * from tasks order by title")
    fun getAll() : List<Tasky>

    @Query("select * from tasks where id = :id limit 1 ")
    fun findById(id: Int) : Tasky?

    @Insert
    fun insert(tasky: Tasky) : Long

    @Update
    fun update(tasky: Tasky)

    @Delete
    fun delete(tasky: Tasky)
}

package br.edu.ifpr.jonatas.tasktrab.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Tasky(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description : String,
    @ColumnInfo(name = "done")
    var done : Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
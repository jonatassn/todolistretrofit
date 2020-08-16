package br.edu.ifpr.jonatas.tasktrab.ui

import br.edu.ifpr.jonatas.tasktrab.entities.Tasky

interface TaskyAdapterListener {
    fun onTaskyRemoved(tasky: Tasky)
    fun onTaskySaved(tasky: Tasky)
    fun onTaskyEdited(tasky: Tasky)
    fun onTaskyShared(tasky: Tasky)
}
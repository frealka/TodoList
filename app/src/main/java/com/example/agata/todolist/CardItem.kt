package com.example.agata.todolist

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CardItem(title: String, content: String, deadline: String, taskPriority: String) {
    val title = title
    val content = content
    val deadline : LocalDate = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    val taskPriority: String = taskPriority


}
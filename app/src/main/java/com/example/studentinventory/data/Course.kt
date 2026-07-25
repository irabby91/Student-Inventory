package com.example.studentinventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val courseName: String,
    val courseCode: String,
    val credits: Int,
    val courseTeacher: String
)



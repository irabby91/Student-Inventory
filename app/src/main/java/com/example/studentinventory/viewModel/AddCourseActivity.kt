package com.example.studentinventory.viewModel

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.studentinventory.R
import com.example.studentinventory.data.Course
import com.example.studentinventory.data.InventoryDatabase
import com.example.studentinventory.repo.CourseRepository
import kotlinx.coroutines.launch

class AddCourseActivity : AppCompatActivity() {

    private lateinit var repository: CourseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_course)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom + ime.bottom)
            insets
        }

        val database = InventoryDatabase.getDatabase(this)
        repository = CourseRepository(database.courseDao())

        findViewById<CardView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<android.view.View>(R.id.btnSave).setOnClickListener {
            saveCourse()
        }
    }

    private fun saveCourse() {
        val name = findViewById<EditText>(R.id.etCourseName).text.toString()
        val code = findViewById<EditText>(R.id.etCourseCode).text.toString()
        val instructor = findViewById<EditText>(R.id.etInstructor).text.toString()
        val creditsStr = findViewById<EditText>(R.id.etCredits).text.toString()
        val schedule = findViewById<EditText>(R.id.etSchedule).text.toString()
        val room = findViewById<EditText>(R.id.etRoom).text.toString()

        if (name.isBlank() || code.isBlank() || instructor.isBlank() || creditsStr.isBlank()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val credits = creditsStr.toIntOrNull() ?: 0

        val course = Course(
            courseName = name,
            courseCode = code,
            courseTeacher = instructor,
            credits = credits,
            courseTime = schedule,
            courseRoom = room
        )

        lifecycleScope.launch {
            repository.insertCourse(course)
            runOnUiThread {
                Toast.makeText(this@AddCourseActivity, "Course Saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditCourseActivity : AppCompatActivity() {

    private lateinit var repository: CourseRepository
    private var courseId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_course)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom + ime.bottom)
            insets
        }

        courseId = intent.getIntExtra("COURSE_ID", -1)
        if (courseId == -1) {
            Toast.makeText(this, "Error loading course", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val database = InventoryDatabase.getDatabase(this)
        repository = CourseRepository(database.courseDao())

        loadCourseData()

        findViewById<CardView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<android.view.View>(R.id.btnUpdate).setOnClickListener {
            updateCourse()
        }
    }

    private fun loadCourseData() {
        lifecycleScope.launch {
            val course = repository.getCourseStream(courseId).first()
            if (course != null) {
                findViewById<EditText>(R.id.etCourseName).setText(course.courseName)
                findViewById<EditText>(R.id.etCourseCode).setText(course.courseCode)
                findViewById<EditText>(R.id.etInstructor).setText(course.courseTeacher)
                findViewById<EditText>(R.id.etCredits).setText(course.credits.toString())
                findViewById<EditText>(R.id.etSchedule).setText(course.courseTime)
                findViewById<EditText>(R.id.etRoom).setText(course.courseRoom)
            }
        }
    }

    private fun updateCourse() {
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

        val updatedCourse = Course(
            id = courseId,
            courseName = name,
            courseCode = code,
            courseTeacher = instructor,
            credits = credits,
            courseTime = schedule,
            courseRoom = room
        )

        lifecycleScope.launch {
            repository.updateCourse(updatedCourse)
            runOnUiThread {
                Toast.makeText(this@EditCourseActivity, "Course Updated!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

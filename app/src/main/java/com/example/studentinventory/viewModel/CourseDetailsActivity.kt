package com.example.studentinventory.viewModel

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.studentinventory.R
import com.example.studentinventory.data.InventoryDatabase
import com.example.studentinventory.repo.CourseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CourseDetailsActivity : AppCompatActivity() {

    private lateinit var repository: CourseRepository
    private var courseId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_course_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom + ime.bottom)
            insets
        }

        courseId = intent.getIntExtra("COURSE_ID", -1)
        if (courseId == -1) {
            Toast.makeText(this, "Error loading course details", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val database = InventoryDatabase.getDatabase(this)
        repository = CourseRepository(database.courseDao())

        findViewById<CardView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        loadCourseDetails()
    }

    private fun loadCourseDetails() {
        lifecycleScope.launch {
            val course = repository.getCourseStream(courseId).first()
            if (course != null) {
                findViewById<TextView>(R.id.tvDetailCode).text = course.courseCode
                findViewById<TextView>(R.id.tvDetailName).text = course.courseName
                findViewById<TextView>(R.id.tvDetailInstructor).text = course.courseTeacher
                findViewById<TextView>(R.id.tvDetailCredits).text = "${course.credits} Credits"
                findViewById<TextView>(R.id.tvDetailSchedule).text = course.courseTime
                findViewById<TextView>(R.id.tvDetailRoom).text = course.courseRoom
            }
        }
    }
}

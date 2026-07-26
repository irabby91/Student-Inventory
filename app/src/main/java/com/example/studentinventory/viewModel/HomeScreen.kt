package com.example.studentinventory.viewModel

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentinventory.R
import com.example.studentinventory.adapter.CourseAdapter
import com.example.studentinventory.data.InventoryDatabase
import com.example.studentinventory.repo.CourseRepository
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@kotlinx.coroutines.ExperimentalCoroutinesApi
class HomeScreen : AppCompatActivity() {

    private lateinit var repository: CourseRepository
    private val searchQuery = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        val database = InventoryDatabase.getDatabase(this)
        repository = CourseRepository(database.courseDao())

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom + ime.bottom)
                insets
            }
        }

        setupRecyclerView()
        setupSearch()

        findViewById<android.view.View>(R.id.fabAddCourse)?.setOnClickListener {
            val intent = Intent(this, AddCourseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val rvCourses = findViewById<RecyclerView>(R.id.rvCourses)
        rvCourses.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            searchQuery.flatMapLatest { query ->
                if (query.isEmpty()) {
                    repository.getAllCoursesStream()
                } else {
                    repository.searchCourses(query)
                }
            }.collect { courses ->
                rvCourses.adapter = CourseAdapter(
                    courses,
                    onEditClick = { course ->
                        val intent = Intent(this@HomeScreen, EditCourseActivity::class.java)
                        intent.putExtra("COURSE_ID", course.id)
                        startActivity(intent)
                    },
                    onDeleteClick = { courseToDelete ->
                        lifecycleScope.launch {
                            repository.deleteCourse(courseToDelete)
                        }
                    },
                    onDetailsClick = { course ->
                        val intent = Intent(this@HomeScreen, CourseDetailsActivity::class.java)
                        intent.putExtra("COURSE_ID", course.id)
                        startActivity(intent)
                    }
                )
                
                // Update stats
                findViewById<TextView>(R.id.tvCourseCount)?.text = courses.size.toString()
                findViewById<TextView>(R.id.tvCreditCount)?.text = courses.sumOf { it.credits }.toString()
            }
        }
    }

    private fun setupSearch() {
        val etSearch = findViewById<EditText>(R.id.etSearch)
        etSearch?.addTextChangedListener { text ->
            searchQuery.value = text?.toString() ?: ""
        }
    }
}

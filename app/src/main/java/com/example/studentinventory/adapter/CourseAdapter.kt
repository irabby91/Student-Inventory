package com.example.studentinventory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentinventory.R
import com.example.studentinventory.data.Course

class CourseAdapter(
    private var courses: List<Course>,
    private val onEditClick: (Course) -> Unit,
    private val onDeleteClick: (Course) -> Unit,
    private val onDetailsClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCode: TextView = view.findViewById(R.id.tvCode)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCredit: TextView = view.findViewById(R.id.tvCredit)
        val tvTeacher: TextView = view.findViewById(R.id.tvTeacher)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvRoom: TextView = view.findViewById(R.id.tvRoom)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
        val btnDetails: Button = view.findViewById(R.id.btnDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.tvCode.text = course.courseCode
        holder.tvName.text = course.courseName
        holder.tvCredit.text = "${course.credits} Cr"
        holder.tvTeacher.text = course.courseTeacher
        holder.tvTime.text = course.courseTime
        holder.tvRoom.text = course.courseRoom
        
        holder.btnEdit.setOnClickListener {
            onEditClick(course)
        }
        
        holder.btnDelete.setOnClickListener {
            onDeleteClick(course)
        }

        holder.btnDetails.setOnClickListener {
            onDetailsClick(course)
        }
    }

    override fun getItemCount() = courses.size
    
    fun updateList(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }
}

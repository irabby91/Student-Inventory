package com.example.studentinventory.repo

import com.example.studentinventory.data.Course
import com.example.studentinventory.data.CourseDao
import kotlinx.coroutines.flow.Flow

class CourseRepository(private val courseDao: CourseDao) {
    fun getAllCoursesStream(): Flow<List<Course>> = courseDao.getAllCourses()

    fun getCourseStream(id: Int): Flow<Course?> = courseDao.getCourse(id)

    suspend fun insertCourse(course: Course) = courseDao.insert(course)

    suspend fun deleteCourse(course: Course) = courseDao.delete(course)

    suspend fun updateCourse(course: Course) = courseDao.update(course)
}

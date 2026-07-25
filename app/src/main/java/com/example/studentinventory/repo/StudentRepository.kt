package com.example.studentinventory.repo

import com.example.studentinventory.data.Student
import com.example.studentinventory.data.StudentDao
import kotlinx.coroutines.flow.Flow

class StudentRepository(private val studentDao: StudentDao) {
    fun getAllStudentsStream(): Flow<List<Student>> = studentDao.getAllStudents()

    fun getStudentStream(id: Int): Flow<Student?> = studentDao.getStudent(id)

    suspend fun insertStudent(student: Student) = studentDao.insert(student)

    suspend fun deleteStudent(student: Student) = studentDao.delete(student)

    suspend fun updateStudent(student: Student) = studentDao.update(student)
}

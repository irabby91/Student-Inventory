package com.example.studentinventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(course: Course)

    @Update
    suspend fun update(course: Course)

    @Delete
    suspend fun delete(course: Course)

    @Query("SELECT * from courses WHERE id = :id")
    fun getCourse(id: Int): Flow<Course?>

    @Query("SELECT * from courses ORDER BY courseName ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Query("""
        SELECT * FROM courses
        WHERE courseName LIKE '%' || :searchQuery || '%'
        ORDER BY courseName ASC
    """)
    fun searchCourses(searchQuery: String): Flow<List<Course>>
}

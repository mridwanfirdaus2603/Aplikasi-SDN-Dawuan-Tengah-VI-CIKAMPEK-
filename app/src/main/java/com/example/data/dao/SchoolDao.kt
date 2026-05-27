package com.example.data.dao

import androidx.room.*
import com.example.data.model.AttendanceRecord
import com.example.data.model.ExamScore
import com.example.data.model.PPDBRegistration
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    // PPDB Queries
    @Query("SELECT * FROM ppdb_registrations ORDER BY distanceKm ASC, timestamp DESC")
    fun getAllRegistrations(): Flow<List<PPDBRegistration>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: PPDBRegistration): Long

    @Update
    suspend fun updateRegistration(registration: PPDBRegistration)

    @Delete
    suspend fun deleteRegistration(registration: PPDBRegistration)

    @Query("DELETE FROM ppdb_registrations")
    suspend fun deleteAllRegistrations()

    // Attendance Queries
    @Query("SELECT * FROM attendance_records ORDER BY timestamp DESC")
    fun getAllAttendance(): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(record: AttendanceRecord): Long

    // Exam Score Queries
    @Query("SELECT * FROM exam_scores ORDER BY timestamp DESC")
    fun getAllExamScores(): Flow<List<ExamScore>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamScore(score: ExamScore): Long
}

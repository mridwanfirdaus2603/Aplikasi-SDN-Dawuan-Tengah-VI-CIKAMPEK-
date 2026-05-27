package com.example.data.repository

import com.example.data.dao.SchoolDao
import com.example.data.model.AttendanceRecord
import com.example.data.model.ExamScore
import com.example.data.model.PPDBRegistration
import kotlinx.coroutines.flow.Flow

class SchoolRepository(private val schoolDao: SchoolDao) {
    val registrations: Flow<List<PPDBRegistration>> = schoolDao.getAllRegistrations()
    val attendance: Flow<List<AttendanceRecord>> = schoolDao.getAllAttendance()
    val examScores: Flow<List<ExamScore>> = schoolDao.getAllExamScores()

    suspend fun insertRegistration(registration: PPDBRegistration): Long {
        return schoolDao.insertRegistration(registration)
    }

    suspend fun updateRegistration(registration: PPDBRegistration) {
        schoolDao.updateRegistration(registration)
    }

    suspend fun deleteRegistration(registration: PPDBRegistration) {
        schoolDao.deleteRegistration(registration)
    }

    suspend fun resetRegistrations() {
        schoolDao.deleteAllRegistrations()
    }

    suspend fun insertAttendance(record: AttendanceRecord): Long {
        return schoolDao.insertAttendance(record)
    }

    suspend fun insertExamScore(score: ExamScore): Long {
        return schoolDao.insertExamScore(score)
    }
}

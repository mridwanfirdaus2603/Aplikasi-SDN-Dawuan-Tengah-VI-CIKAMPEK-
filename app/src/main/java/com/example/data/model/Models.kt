package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "ppdb_registrations")
data class PPDBRegistration(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nama: String,
    val nik: String,
    val nisn: String,
    val asalSekolah: String,
    val jalur: String, // Zonasi, Afirmasi, Prestasi, Perpindahan
    val alamat: String,
    val status: String = "Pending", // Pending, Verified, Rejected
    val AIFeedback: String = "",
    val score: Int = 0, // Placement exam or priority points
    val distanceKm: Double = 0.0, // calculated distance for Zonasi
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val status: String, // Present, Sick, Absent, Permission
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "exam_scores")
data class ExamScore(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val examName: String,
    val score: Int,
    val maxScore: Int,
    val cheatingFlags: Int,
    val status: String, // Passed, Failed
    val timestamp: Long = System.currentTimeMillis()
)

data class LessonMaterial(
    val id: String,
    val curriculum: String, // Merdeka, K13
    val subject: String,
    val level: String, // Kelas I - VI
    val title: String,
    val brief: String,
    val contents: String,
    val iconName: String
)

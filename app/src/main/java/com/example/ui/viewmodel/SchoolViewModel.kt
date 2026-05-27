package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiService
import com.example.data.database.SchoolDatabase
import com.example.data.model.AttendanceRecord
import com.example.data.model.ExamScore
import com.example.data.model.LessonMaterial
import com.example.data.model.PPDBRegistration
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class SchoolViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SchoolRepository
    val registrations: StateFlow<List<PPDBRegistration>>
    val attendance: StateFlow<List<AttendanceRecord>>
    val examScores: StateFlow<List<ExamScore>>

    // --- State: PPDB Form ---
    var namaInput by mutableStateOf("")
    var nikInput by mutableStateOf("")
    var nisnInput by mutableStateOf("")
    var asalSekolahInput by mutableStateOf("")
    var alamatInput by mutableStateOf("")
    var selectedJalur by mutableStateOf("Zonasi") // Zonasi, Afirmasi, Prestasi, Perpindahan
    var documentDetailsInput by mutableStateOf("")
    var isVerifyingDoc by mutableStateOf(false)
    var aiReportResult by mutableStateOf("")
    var verifiedRegistrationSuccess by mutableStateOf<PPDBRegistration?>(null)

    // --- State: Active Portal Student (Standard portal session) ---
    var currentStudentName by mutableStateOf("Budi Setiawan")
    var currentStudentClass by mutableStateOf("Kelas VI")
    var currentStudentNISN by mutableStateOf("0123456789")

    // --- State: Google Authentication ---
    var googleAccountName by mutableStateOf<String?>(null)
    var googleAccountEmail by mutableStateOf<String?>(null)
    var googleAccountPhotoUrl by mutableStateOf<String?>(null)
    var isGoogleAuthenticated by mutableStateOf(false)

    fun onGoogleSignInSuccess(name: String?, email: String?, photoUrl: String?) {
        googleAccountName = name
        googleAccountEmail = email
        googleAccountPhotoUrl = photoUrl
        isGoogleAuthenticated = true
        if (name != null) {
            currentStudentName = name
            currentStudentClass = "Siswa Google Auth"
            currentStudentNISN = "G-${Math.abs(email?.hashCode() ?: 0).toString().take(10)}"
        }
    }

    fun onGoogleSignOut() {
        googleAccountName = null
        googleAccountEmail = null
        googleAccountPhotoUrl = null
        isGoogleAuthenticated = false
        currentStudentName = "Budi Setiawan"
        currentStudentClass = "Kelas VI"
        currentStudentNISN = "0123456789"
    }

    // --- State: E-Learning ---
    var selectedCurriculum by mutableStateOf("Merdeka") // Merdeka, K13
    var selectedSubject by mutableStateOf("IPAS") // IPAS, Matematika, Bahasa Indonesia, PJOK
    var activeLessonMaterial by mutableStateOf<LessonMaterial?>(null)
    var isGeneratingLesson by mutableStateOf(false)
    var generatedLessonContent by mutableStateOf("")
    var miniQuizQueryAnswers by mutableStateOf(mutableMapOf<Int, Int>()) // Q_ID -> Option Index

    // --- State: CBT Exam System ---
    var isExamActive by mutableStateOf(false)
    var currentExamName by mutableStateOf("Evaluasi Tengah Semester")
    var currentQuestionIndex by mutableIntStateOf(0)
    var examTimerSeconds by mutableIntStateOf(120) // 2-minute exam for mock
    var isExamFinished by mutableStateOf(false)
    var finalExamScore by mutableIntStateOf(0)
    var flagCheatedEvent by mutableStateOf(false)
    var cheatingCount by mutableIntStateOf(0)
    var cheatLogs = mutableListOf<String>()

    // CBT Questions List
    val examQuestions = listOf(
        CBTQuestion(
            id = 1,
            question = "Menurut konsep Kurikulum Merdeka, apakah pilar utama dalam Profil Pelajar Pancasila?",
            options = listOf("Kreatif & Mandiri", "Kemampuan Menghafal", "Kepatuhan Total", "Keterampilan Berhitung"),
            answerIndex = 0
        ),
        CBTQuestion(
            id = 2,
            question = "Candi Jiwa merupakan salah satu situs sejarah peninggalan Kerajaan Tarumanegara yang berada di daerah...",
            options = listOf("Cikampek", "Dawuan Tengah", "Batu Jaya Karawang", "Telukjambe"),
            answerIndex = 2
        ),
        CBTQuestion(
            id = 3,
            question = "Apa fungsi utama dari pembuluh darah arteri dalam tubuh manusia?",
            options = listOf("Membawa darah kaya O2 dari paru ke jantung", "Mengalirkan darah bersih dari jantung ke seluruh tubuh", "Mengangkut CO2 ke paru-paru", "Menghasilkan sel darah merah baru"),
            answerIndex = 1
        ),
        CBTQuestion(
            id = 4,
            question = "Nilai ketukan dari notasi balok yang bulat utuh bernilai ... ketukan.",
            options = listOf("1 Ketuk", "2 Ketuk", "3 Ketuk", "4 Ketuk"),
            answerIndex = 3
        ),
        CBTQuestion(
            id = 5,
            question = "Manakah perilaku hemat energi yang paling efektif diterapkan sehari-hari?",
            options = listOf("Menghidupkan AC sepanjang hari", "Membiarkan dispenser menyala saat bepergian", "Mematikan lampu dan mencabut steker elektronik setelah digunakan", "Membeli perangkat elektronik berdaya besar"),
            answerIndex = 2
        )
    )
    val selectedCBTAnswers = mutableStateOf(IntArray(examQuestions.size) { -1 })

    init {
        val database = SchoolDatabase.getDatabase(application)
        repository = SchoolRepository(database.schoolDao())
        registrations = repository.registrations.stateInViewModel(emptyList<PPDBRegistration>())
        attendance = repository.attendance.stateInViewModel(emptyList<AttendanceRecord>())
        examScores = repository.examScores.stateInViewModel(emptyList<ExamScore>())

        seedInitialDataIfEmpty()
    }

    private fun <T> kotlinx.coroutines.flow.Flow<T>.stateInViewModel(initial: T): StateFlow<T> =
        this.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initial
        )

    private fun seedInitialDataIfEmpty() {
        viewModelScope.launch {
            // Seed PPDB
            registrations.collect { list ->
                if (list.isEmpty()) {
                    val initialList = listOf(
                        PPDBRegistration(nama = "Ahmad Rifai", nik = "3215011234560001", nisn = "0112345678", asalSekolah = "TK Tunas Bangsa", jalur = "Zonasi", alamat = "Dawuan Tengah RT 03/05", distanceKm = 0.8, status = "Verified", score = 85),
                        PPDBRegistration(nama = "Siti Aminah", nik = "3215011234560002", nisn = "0112345679", asalSekolah = "TK Bina Lestari", jalur = "Zonasi", alamat = "Dawuan Barat Blok M", distanceKm = 1.2, status = "Verified", score = 78),
                        PPDBRegistration(nama = "Rian Hidayat", nik = "3215011234560003", nisn = "0112345680", asalSekolah = "RA Al-Kautsar", jalur = "Zonasi", alamat = "Cikampek Kota Selatan", distanceKm = 2.4, status = "Verified", score = 91),
                        PPDBRegistration(nama = "Neng Kholilah", nik = "3215011234560004", nisn = "0112345681", asalSekolah = "TK Dawuan Sentosa", jalur = "Afirmasi", alamat = "Telagasari Karawang", distanceKm = 4.8, status = "Verified", score = 65, AIFeedback = "KIP Terverifikasi Aktif"),
                        PPDBRegistration(nama = "Dinda Rahmawati", nik = "3215011234560005", nisn = "0112345682", asalSekolah = "SPS Mawar Dawuan", jalur = "Prestasi", alamat = "Klari Indah Karawang", distanceKm = 8.5, status = "Verified", score = 96, AIFeedback = "Juara 1 Lomba Matematika Kab. Karawang"),
                        PPDBRegistration(nama = "Wawan Kurniawan", nik = "3215011234560006", nisn = "0112345683", asalSekolah = "TK Pertiwi Cikampek", jalur = "Zonasi", alamat = "Dawuan Timur No. 12", distanceKm = 3.6, status = "Pending", score = 70),
                    )
                    initialList.forEach { repository.insertRegistration(it) }
                }
            }
        }

        viewModelScope.launch {
            // Seed Attendance
            attendance.collect { list ->
                if (list.isEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
                    val baseTime = System.currentTimeMillis()
                    for (i in 1..10) {
                        val dateString = sdf.format(Date(baseTime - (i * 24 * 60 * 60 * 1000L)))
                        val status = if (i == 3) "Sick" else if (i == 7) "Permission" else "Present"
                        repository.insertAttendance(AttendanceRecord(date = dateString, status = status))
                    }
                }
            }
        }

        viewModelScope.launch {
            // Seed Exam Scores
            examScores.collect { list ->
                if (list.isEmpty()) {
                    repository.insertExamScore(ExamScore(examName = "UTS Matematika", score = 90, maxScore = 100, cheatingFlags = 0, status = "Passed"))
                    repository.insertExamScore(ExamScore(examName = "Ujian Harian IPA", score = 85, maxScore = 100, cheatingFlags = 1, status = "Passed"))
                }
            }
        }
    }

    // --- Actions: PPDB ---
    fun submitPPDBRegistration() {
        if (namaInput.isEmpty() || nikInput.isEmpty() || nisnInput.isEmpty()) return

        isVerifyingDoc = true
        aiReportResult = ""
        verifiedRegistrationSuccess = null

        viewModelScope.launch {
            // Calculate a random realistic distance for zonasi based on address
            val dist = if (selectedJalur == "Zonasi") {
                val base = if (alamatInput.lowercase().contains("dawuan")) 0.5 else 2.0
                base + Random.nextDouble(0.1, 4.0)
            } else {
                Random.nextDouble(2.0, 15.0)
            }
            val formattedDist = String.format(Locale.US, "%.2f", dist).toDouble()

            // Call real Gemini API if key is there, else retrieve fallback simulator
            val result = GeminiService.verifyDocumentWithAI(
                nama = namaInput,
                nik = nikInput,
                nisn = nisnInput,
                jalur = selectedJalur,
                docType = "Kartu Keluarga / Akta Kelahiran",
                details = "Pendaftar mandiri mengaku beralamat di: $alamatInput. Catatan: $documentDetailsInput"
            )

            val isRejected = result.contains("[REJECTED]") || (nikInput.length != 16 && !result.contains("Simulasi AI")) || (nisnInput.length != 10 && !result.contains("Simulasi AI"))
            val finalStatus = if (isRejected) "Rejected" else "Verified"

            val reg = PPDBRegistration(
                nama = namaInput,
                nik = nikInput,
                nisn = nisnInput,
                asalSekolah = asalSekolahInput,
                jalur = selectedJalur,
                alamat = alamatInput,
                status = finalStatus,
                AIFeedback = result,
                distanceKm = formattedDist,
                score = if (selectedJalur == "Prestasi") Random.nextInt(85, 100) else Random.nextInt(60, 85)
            )

            repository.insertRegistration(reg)
            aiReportResult = result
            verifiedRegistrationSuccess = reg
            isVerifyingDoc = false

            // Clear registration fields
            namaInput = ""
            nikInput = ""
            nisnInput = ""
            asalSekolahInput = ""
            alamatInput = ""
            documentDetailsInput = ""
        }
    }

    fun deleteRegistrationRecord(reg: PPDBRegistration) {
        viewModelScope.launch {
            repository.deleteRegistration(reg)
        }
    }

    fun resetAllRegistrations() {
        viewModelScope.launch {
            repository.resetRegistrations()
            // Immediately seed default values
            val initialList = listOf(
                PPDBRegistration(nama = "Ahmad Rifai", nik = "3215011234560001", nisn = "0112345678", asalSekolah = "TK Tunas Bangsa", jalur = "Zonasi", alamat = "Dawuan Tengah RT 03/05", distanceKm = 0.8, status = "Verified", score = 85),
                PPDBRegistration(nama = "Siti Aminah", nik = "3215011234560002", nisn = "0112345679", asalSekolah = "TK Bina Lestari", jalur = "Zonasi", alamat = "Dawuan Barat Blok M", distanceKm = 1.2, status = "Verified", score = 78),
                PPDBRegistration(nama = "Rian Hidayat", nik = "3215011234560003", nisn = "0112345680", asalSekolah = "RA Al-Kautsar", jalur = "Zonasi", alamat = "Cikampek Kota Selatan", distanceKm = 2.4, status = "Verified", score = 91),
                PPDBRegistration(nama = "Neng Kholilah", nik = "3215011234560004", nisn = "0112345681", asalSekolah = "TK Dawuan Sentosa", jalur = "Afirmasi", alamat = "Telagasari Karawang", distanceKm = 4.8, status = "Verified", score = 65, AIFeedback = "KIP Terverifikasi Aktif"),
                PPDBRegistration(nama = "Dinda Rahmawati", nik = "3215011234560005", nisn = "0112345682", asalSekolah = "SPS Mawar Dawuan", jalur = "Prestasi", alamat = "Klari Indah Karawang", distanceKm = 8.5, status = "Verified", score = 96, AIFeedback = "Juara 1 Lomba Matematika Kab. Karawang"),
            )
            initialList.forEach { repository.insertRegistration(it) }
        }
    }

    // --- Actions: Portal & Attendance ---
    fun checkInPresent() {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val todayStr = sdf.format(Date())
        viewModelScope.launch {
            repository.insertAttendance(AttendanceRecord(date = todayStr, status = "Present"))
        }
    }

    fun checkInSick() {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val todayStr = sdf.format(Date())
        viewModelScope.launch {
            repository.insertAttendance(AttendanceRecord(date = todayStr, status = "Sick"))
        }
    }

    // --- Actions: E-Learning ---
    fun selectLesson(material: LessonMaterial) {
        activeLessonMaterial = material
        generatedLessonContent = ""
        isGeneratingLesson = true
        miniQuizQueryAnswers.clear()

        viewModelScope.launch {
            val content = GeminiService.getAICurriculumGuidance(
                curriculum = material.curriculum,
                subject = material.subject,
                grade = material.level,
                topic = material.title
            )
            generatedLessonContent = content
            isGeneratingLesson = false
        }
    }

    // --- Actions: Exam & Proctoring ---
    fun startCBTExam() {
        isExamActive = true
        currentQuestionIndex = 0
        examTimerSeconds = 120
        isExamFinished = false
        finalExamScore = 0
        cheatingCount = 0
        cheatLogs.clear()
        selectedCBTAnswers.value = IntArray(examQuestions.size) { -1 }
    }

    fun submitCBTExam() {
        if (!isExamActive) return
        isExamActive = false
        isExamFinished = true

        // Calculate score
        var correct = 0
        examQuestions.forEachIndexed { i, q ->
            if (selectedCBTAnswers.value[i] == q.answerIndex) {
                correct++
            }
        }
        finalExamScore = (correct * 100) / examQuestions.size

        // Record score to Room Db
        val examStatus = if (finalExamScore >= 70 && cheatingCount < 3) "Passed" else "Failed"
        viewModelScope.launch {
            repository.insertExamScore(
                ExamScore(
                    examName = currentExamName,
                    score = finalExamScore,
                    maxScore = 100,
                    cheatingFlags = cheatingCount,
                    status = examStatus
                )
            )
        }
    }

    fun logCheatingAttempt(violationReason: String) {
        if (!isExamActive) return
        cheatingCount++
        val timestampString = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        cheatLogs.add("[$$timestampString] Gagal Fokus: $violationReason")
        flagCheatedEvent = true

        if (cheatingCount >= 5) {
            // Auto terminate exam if cheating exceeds threshold
            submitCBTExam()
        }
    }
}

data class CBTQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val answerIndex: Int
)

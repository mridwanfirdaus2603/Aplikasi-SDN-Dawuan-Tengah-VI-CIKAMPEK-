package com.example.api

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Checks if the Gemini API Key is configured.
     */
    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotEmpty() && key != "MY_GEMINI_API_KEY" && key != "placeholder"
    }

    /**
     * Generates content from a prompt.
     */
    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        if (!isApiKeyConfigured()) {
            Log.w(TAG, "API Key is not configured. Falling back to structured default replies.")
            return@withContext getMockResponseForPrompt(prompt)
        }

        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            val url = "$BASE_URL?key=$apiKey"

            // Construct requests dynamically using JSONObject
            val root = JSONObject()
            val contentsArray = JSONArray()
            val textPart = JSONObject()
            textPart.put("parts", JSONArray().apply {
                put(JSONObject().apply {
                    put("text", prompt)
                })
            })
            contentsArray.put(textPart)
            root.put("contents", contentsArray)

            // Setup system instructions if provided
            if (systemInstruction != null) {
                root.put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemInstruction)
                        })
                    })
                })
            }

            // Temperature adjustment
            root.put("generationConfig", JSONObject().apply {
                put("temperature", 0.7)
            })

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = root.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                if (!response.isSuccessful || bodyString == null) {
                    val errMsg = "HTTP Error ${response.code}: ${bodyString ?: "Empty body"}"
                    Log.e(TAG, errMsg)
                    return@withContext "Error: Failed to fetch API ($errMsg)"
                }

                val jsonResponse = JSONObject(bodyString)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    if (contentObj != null) {
                        val parts = contentObj.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text", "No text found in response")
                        }
                    }
                }
                return@withContext "Response parsed, but text content was empty."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during API call: ${e.message}", e)
            return@withContext "Error connection failed: ${e.localizedMessage}. Please verify internet access and your API key configuration."
        }
    }

    /**
     * High fidelity document verification system using Gemini API
     */
    suspend fun verifyDocumentWithAI(
        nama: String,
        nik: String,
        nisn: String,
        jalur: String,
        docType: String,
        details: String
    ): String {
        val prompt = """
            You are the Indonesian Ministry of Education (Kementerian Pendidikan dan Kebudayaan) AI PPDB Document Auditor.
            Verify the following student registration and document details for admissibility:
            ---
            Student Information:
            - Name: $nama
            - NIK: $nik
            - NISN: $nisn
            - Admission Pathway (Jalur PPDB): $jalur
            - Document Type to Verify: $docType
            - Document Self-Declaration details: $details
            ---
            Rules for verification:
            1. Name must be valid, proper casing, and match standard Indonesian name patterns.
            2. NIK must be 16 digits. (NIK: $nik has length: ${nik.length}).
            3. NISN must be 10 digits. (NISN: $nisn has length: ${nisn.length}).
            4. If pathway is ZONASI: Ensure that the self-declared residency (KK/Kartu Keluarga) details are in the Dawuan Tengah / Cikampek / Karawang district to qualify for placement.
            5. If pathway is AFIRMASI: Ensure that a valid PIP (Program Indonesia Pintar) or KIP (Kartu Indonesia Pintar) card number or PKH document is claimed.
            6. If pathway is PRESTASI: Check for any certificate or academic ranking mentioned.
            
            Provide a complete, detailed, Indonesian-written report in markdown style format, structured as follows:
            ### KEPUTUSAN: [VERIFIED] or [ACCEPTED WITH CONDITIONS] or [REJECTED]
            - **Skor Validasi**: [Integer 1 to 100 representing accuracy/trust score]
            
            ### ANALISIS DOKUMEN:
            (Write a short paragraph analyzing the consistency of NIK, NISN, and document types)
            
            ### TEMUAN & REKOMENDASI:
            (Detailed bullets pointing out issues or next steps. If rejected, explain clearly what is missing, e.g., missing Digits on NIK or NUPTK or KK zone mismatch)
        """.trimIndent()

        val systemInstruction = "You are an official Ministry of Education PPDB Document verification bot. Respond exclusively in Indonesian language. Be extremely authoritative and detailed."
        return generateContent(prompt, systemInstruction)
    }

    /**
     * Retrieves lesson guidance.
     */
    suspend fun getAICurriculumGuidance(
        curriculum: String,
        subject: String,
        grade: String,
        topic: String
    ): String {
        val prompt = """
            Create an interactive learning module summary aligned with the official guidelines of "Kementerian Pendidikan Dasar dan Menengah RI".
            Context:
            - Curriculum: $curriculum Layout (e.g. Kurikulum Merdeka CP/ATP or Kurikulum 13 KI/KD)
            - Subject (Mata Pelajaran): $subject
            - Class (Kelas): $grade
            - Target Topic: $topic

            Provide a comprehensive, engaging lesson breakdown written in Indonesian.
            Format it beautifully:
            # 📖 Pembelajaran Terpadu: $topic
            
            ## 🎯 Tujuan Pembelajaran (Capaian Pembelajaran):
            - Bullet points outlining what the student will achieve.
            
            ## 🌟 Konsep Inti (Core Concept):
            Write a clear, simple, yet rich explanation of the concept tailored for elementary level (SDN) children. Use analogies.
            
            ## 🧩 Aktivitas Interaktif (Profil Pelajar Pancasila):
            Describe 2 fun activities students or parents can do at home.
            
            ## 📝 Tes Pemahaman Singkat (Mini Quiz):
            Provide 3 multiple-choice questions with answer key at the very bottom (hidden or in separate text).
        """.trimIndent()

        val systemInstruction = "You are an Elite Elementary School Educator for SDN Dawuan Tengah VI, expert in Kurikulum Merdeka. Speak warmly, encouragingly, and professionally."
        return generateContent(prompt, systemInstruction)
    }

    /**
     * Fallback mock response when no API key is specified, maintaining exceptional high-fidelity simulation.
     */
    private fun getMockResponseForPrompt(prompt: String): String {
        return if (prompt.contains("Document Type")) {
            """
            ### KEPUTUSAN: [VERIFIED] (Simulasi AI)
            - **Skor Validasi**: 95/100
            
            ### ANALISIS DOKUMEN:
            Berkas terdeteksi lengkap dan sesuai dengan standar PPDB Elektronik **Kementerian Pendidikan Dasar dan Menengah RI**. Data kependudukan terverifikasi sinkron dengan nomor NIK dan NISN yang diinputkan.
            
            ### TEMUAN & REKOMENDASI:
            - **NIK**: Terdeteksi valid (16-Digit) terdaftar di database kependudukan wilayah Jawa Barat.
            - **NISN**: Terdeteksi aktif (10-Digit) terdaftar di sistem DAPODIK aktif.
            - **Zonasi Fisik**: Jarak alamat yang didaftarkan ke SDN Dawuan Tengah VI terhitung aman dalam radius zonasi utama (< 2.5 km).
            
            *Catatan Pengembang: Kunci API Gemini tidak terkonfigurasi di Secrets Panel. Tampilan di atas adalah representasi cerdas dokumen PPDB. Anda dapat mengonfigurasi GEMINI_API_KEY di AI Studio untuk mengaktifkan AI asli.*
            """.trimIndent()
        } else {
            """
            # 📖 Pembelajaran Terpadu (Simulasi AI)
            
            ## 🎯 Tujuan Pembelajaran (Capaian Pembelajaran):
            - Memahami konsep dasar topik pelajaran secara terapan dan menyenangkan.
            - Meningkatkan nalar kritis siswa sesuai dimensi Profil Pelajar Pancasila.
            
            ## 🌟 Konsep Inti (Core Concept):
            Kita belajar bahwa dunia di sekitar kita penuh dengan keajaiban! Pelajaran ini disusun menggunakan kurikulum interaktif agar adik-adik di SDN Dawuan Tengah VI dapat memahami materi secara langsung dengan contoh sehari-hari yang mudah diingat.
            
            ## 🧩 Aktivitas Interaktif:
            1. **Eksplorasi Lapangan Nyata**: Amati dan catat 3 contoh materi ini di sekitar rumahmu!
            2. **Diskusi Menyenangkan**: Diskusikan temuanmu bersama orang tua atau guru besok pagi.
            
            ## 📝 Tes Pemahaman Singkat:
            1. Apa yang dimaksud dengan konsep utama pelajaran ini? (Jawaban: Opsi terdekat)
            
            ---
            *Saran: Untuk mengaktifkan materi pelajaran kustom langsung lewat kecerdasan buatan Gemini AI secara real-time, silakan tautkan GEMINI_API_KEY Anda di Panel Secrets AI Studio.*
            """.trimIndent()
        }
    }
}

package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LessonMaterial
import com.example.ui.components.NeoBadge
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun ELearningScreen(viewModel: SchoolViewModel) {
    // Local pre-seeded Curricula Lessons
    val merdekaLessons = listOf(
        LessonMaterial("1", "Merdeka", "IPAS", "Kelas V", "Fotosintesis & Rantai Makanan", "Bagaimana tumbuhan memproduksi oksigen?", "Topik ini mengkaji proses tumbuhan berklorofil mensintesis glukosa dengan bantuan sinar matahari dan karbon dioksida.", "Spa"),
        LessonMaterial("2", "Merdeka", "Bahasa Indonesia", "Kelas VI", "Menemukan Ide Pokok Narasi", "Teknik membaca memindai untuk mencari substansi teks.", "Mengajarkan siswa menyaring informasi penting dari artikel bertopik budaya Karawang secara efisien.", "Book"),
        LessonMaterial("3", "Merdeka", "Matematika", "Kelas VI", "Volume Bangun Ruang Kubus", "Belajar menghitung kapasitas kubus dan balok.", "Materi perhitungan matematika volume bangun ruang dengan contoh benda nyata di sekitar ruang kelas Dawuan Tengah.", "Calculate"),
        LessonMaterial("4", "Merdeka", "IPS", "Kelas IV", "Keanekaragaman Budaya Karawang", "Seni, sejarah, candi jiwa, dan budaya lokal Karawang.", "Mencakup pelestarian seni tari jaipong, peninggalan Candi Jiwa Batujaya, serta kearifan agraris Karawang.", "Map")
    )

    val k13Lessons = listOf(
        LessonMaterial("5", "K13", "Matematika", "Kelas V", "Pecahan Campuran & Persen", "Penjumlahan bilangan campuran pecahan biasa.", "Sesuai dengan Kompetensi Dasar (KD) Matematika Kelas V Sekolah Dasar kurikulum kualitatif standard 2013.", "Calculate"),
        LessonMaterial("6", "K13", "IPA", "Kelas VI", "Alat Pencernaan Manusia", "Mempelajari organ lambung, usus dan pencernaan.", "Pembelajaran sistem organ pencernaan lambung, empedu, pankreas serta gangguan kesehatan terkait.", "Favorite"),
        LessonMaterial("7", "K13", "IPS", "Kelas VI", "Koperasi bagi Kesejahteraan Rakyat", "Pilar perkoperasian Bung Hatta.", "Menganalisis prinsip dasar ekonomi Pancasila lewat lembaga koperasi nasional berasaskan kekeluargaan.", "Store")
    )

    val currentLessonMaterials = if (viewModel.selectedCurriculum == "Merdeka") merdekaLessons else k13Lessons

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Parent Curriculum Banner ---
        item {
            NeoCard(backgroundColor = NeoYellow) {
                Text(
                    text = "📖 INTERACTIVE E-LEARNING KELAS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "AI Merdeka & K13 Syllabus Explorer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pilih topik kurikulum nasional pilihanmu dan panggil tutor kecerdasan buatan Gemini AI untuk menghasilkan modul penjelasan kontekstual sdn dalam hitungan detik!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeoBlack
                )
            }
        }

        // --- Curriculum Toggle Tabs ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Merdeka", "K13").forEach { cur ->
                    val isSel = viewModel.selectedCurriculum == cur
                    val label = if (cur == "Merdeka") "KURIKULUM MERDEKA IKM" else "KURIKULUM 2013 (K13)"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSel) NeoPink else Color.White,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = if (isSel) 3.dp else 1.5.dp,
                                color = NeoBlack,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { viewModel.selectedCurriculum = cur }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                    }
                }
            }
        }

        // --- Available Lesson Topics ---
        item {
            Text(
                text = "📚 MATERI AJAR SILABUS:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = NeoBlack
            )
        }

        items(currentLessonMaterials) { material ->
            val isCurrentActive = viewModel.activeLessonMaterial?.id == material.id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isCurrentActive) NeoCyan else Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = if (isCurrentActive) 3.dp else 1.5.dp,
                        color = NeoBlack,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { viewModel.selectLesson(material) }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = RoundedCornerShape(6.dp))
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (material.iconName) {
                                "Calculate" -> Icons.Default.Calculate
                                "Favorite" -> Icons.Default.Favorite
                                "Map" -> Icons.Default.Map
                                "Book" -> Icons.Default.Book
                                else -> Icons.Default.MenuBook
                            },
                            contentDescription = "Lesson Icon",
                            tint = NeoBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            NeoBadge(text = material.subject, backgroundColor = NeoYellow)
                            Text(text = material.level, fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.DarkGray)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = material.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open",
                    tint = NeoBlack
                )
            }
        }

        // --- Active Learning Material AI Console ---
        item {
            if (viewModel.activeLessonMaterial != null) {
                val actMat = viewModel.activeLessonMaterial!!
                NeoCard(backgroundColor = Color.White) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✍️ TOPIK AKTIF: ${actMat.title}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                        IconButton(onClick = { viewModel.activeLessonMaterial = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NeoBlack)
                        }
                    }

                    Text(
                        text = "Deskripsi: ${actMat.brief}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (viewModel.isGeneratingLesson) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = NeoBlack, strokeWidth = 5.dp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Guru AI sedang menyusun materi interaktif...", fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    } else if (viewModel.generatedLessonContent.isNotEmpty()) {
                        // Display AI Lesson Board
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NeoBg, shape = RoundedCornerShape(6.dp))
                                .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = viewModel.generatedLessonContent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeoBlack,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Mini Interactive Evaluation Quiz inside standard classroom!
                        Text(
                            text = "⚡ EVALUASI MANDIRI KELAS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Selesaikan mini evaluasi dari guru AI berdasarkan rangkuman membaca materi di atas.",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Mock mini questionnaire
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MiniQuizItem(
                                questionNumber = 1,
                                question = "Manakah faktor mutlak untuk terjadinya fotosintesis pada tumbuhan hijau?",
                                options = listOf("Oksigen & Angin", "Air, Karbohidrat & Garam", "Klorofil, Cahaya Matahari & Air CO2"),
                                selectedIndex = viewModel.miniQuizQueryAnswers[1],
                                onSelect = { viewModel.miniQuizQueryAnswers[1] = it }
                            )

                            MiniQuizItem(
                                questionNumber = 2,
                                question = "Keanekaragaman budaya apa yang paling ikonik di pesisir dekat Karawang?",
                                options = listOf("Kesenian Tari Candi Batujaya", "Suku Dayak Kalbar", "Suku Asmat"),
                                selectedIndex = viewModel.miniQuizQueryAnswers[2],
                                onSelect = { viewModel.miniQuizQueryAnswers[2] = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val q1Ans = viewModel.miniQuizQueryAnswers[1]
                        val q2Ans = viewModel.miniQuizQueryAnswers[2]

                        if (q1Ans != null && q2Ans != null) {
                            val isQ1Correct = q1Ans == 2
                            val isQ2Correct = q2Ans == 0
                            val score = (if (isQ1Correct) 50 else 0) + (if (isQ2Correct) 50 else 0)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (score == 100) NeoGreen else NeoYellow, shape = RoundedCornerShape(6.dp))
                                    .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Hasil Evaluasi: $score / 100",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = NeoBlack
                                    )
                                    Text(
                                        text = if (score == 100) "🎉 SEMPURNA! Selamat nilai pembelajarmu luar biasa!" else "💡 Bagus sekali, ayo baca lagi konsep di atas untuk perbaikan!",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeoBlack
                                    )
                                }
                            }
                        }
                    } else {
                        // Call to action button
                        NeoButton(
                            onClick = { viewModel.selectLesson(actMat) },
                            backgroundColor = NeoGreen,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.SmartToy, contentDescription = "AI Generate", tint = NeoBlack)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("BENTUK RANGKUMAN MATERI DENGAN AI GURU", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Select a Lesson",
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pilih salah satu materi ajar menteri di atas untuk memulai.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun MiniQuizItem(
    questionNumber: Int,
    question: String,
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(6.dp))
            .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = "Pertanyaan $questionNumber: $question",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = NeoBlack
            )
            Spacer(modifier = Modifier.height(8.dp))

            options.forEachIndexed { idx, opt ->
                val isSel = idx == selectedIndex
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .background(if (isSel) NeoCyan else Color.White, shape = RoundedCornerShape(4.dp))
                        .border(
                            width = if (isSel) 1.5.dp else 1.dp,
                            color = if (isSel) NeoBlack else Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { onSelect(idx) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(8.dp))
                            .background(if (isSel) NeoBlack else Color.White, shape = RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = opt, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeoBlack)
                }
            }
        }
    }
}

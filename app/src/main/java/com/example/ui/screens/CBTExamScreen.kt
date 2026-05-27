package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeoBadge
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel
import kotlinx.coroutines.delay

@Composable
fun CBTExamScreen(viewModel: SchoolViewModel) {
    // Timer Countdown logic
    LaunchedEffect(key1 = viewModel.isExamActive) {
        if (viewModel.isExamActive) {
            while (viewModel.examTimerSeconds > 0 && viewModel.isExamActive) {
                delay(1000)
                viewModel.examTimerSeconds--
                if (viewModel.examTimerSeconds == 0) {
                    viewModel.submitCBTExam()
                }
            }
        }
    }

    // Active lifecycle focus loss detection using LocalWindowInfo!
    val windowInfo = LocalWindowInfo.current
    LaunchedEffect(key1 = windowInfo.isWindowFocused) {
        if (viewModel.isExamActive && !windowInfo.isWindowFocused) {
            // Student minimized or went out of application window!
            viewModel.logCheatingAttempt("Kehilangan Fokus Sistem (Aplikasi diminimalisir / beralih tab)")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Banner ---
        item {
            NeoCard(backgroundColor = NeoPink) {
                Text(
                    text = "💻 CBT EXAM PORTAL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Computer-Based Test Anti-Cheat & Fraud",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sistem evaluasi ujian komputasi digital cerdas. Mengawasi gerak layar, pemantul fokus, serta kamera pendeteksi kecurangan proctoring secara aktif.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeoBlack
                )
            }
        }

        // --- Active Exam Warning popup ---
        item {
            AnimatedVisibility(visible = viewModel.flagCheatedEvent) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeoRed, shape = RoundedCornerShape(8.dp))
                        .border(width = 3.dp, color = NeoBlack, shape = RoundedCornerShape(8.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = "Cheat Alert", tint = NeoBlack)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TERDETEKSI PELANGGARAN CBT!",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Anda beralih layar atau terdeteksi split screen! Pelanggaran saat ini: ${viewModel.cheatingCount} / 5 kali. Jika terulang 5 kali, ujian akan dibatalkan/auto-submit oleh sistem.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeoBlack
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(Color.White, shape = RoundedCornerShape(4.dp))
                                .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(4.dp))
                                .clickable { viewModel.flagCheatedEvent = false }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("SAYA MENGERTI, LANJUTKAN", fontSize = 10.sp, fontWeight = FontWeight.Black, color = NeoBlack)
                        }
                    }
                }
            }
        }

        if (!viewModel.isExamActive && !viewModel.isExamFinished) {
            // --- Welcome Regulations Screens ---
            item {
                NeoCard(backgroundColor = Color.White) {
                    Text(
                        text = "📜 PROSEDUR PENGERJAAN CBT",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = NeoBlack
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CBTInstructionRow("1", "Dilarang meminimalisir aplikasi atau membagi layar (split screen) selama ujian berlangsung.")
                    CBTInstructionRow("2", "Sistem AI mengawasi kelayajaran tab dan memantau status fokus window pengerjaan.")
                    CBTInstructionRow("3", "Pastikan kuota baterai dan jaringan seluler lancar sebelum mengklik mulai.")

                    Spacer(modifier = Modifier.height(14.dp))

                    // Identity Mock camera scan panel
                    Text(
                        text = "👤 CHANTING PROCTORING VERIFICATION",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = NeoBlack
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(NeoBg, shape = RoundedCornerShape(8.dp))
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FilterCenterFocus,
                                contentDescription = "Scan",
                                modifier = Modifier.size(36.dp),
                                tint = NeoBlack
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "FOTO PROFIL COCOK: 98%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoGreen
                            )
                            Text(
                                text = "Budi Setiawan terverifikasi sebagai peserta ujian.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    NeoButton(
                        onClick = { viewModel.startCBTExam() },
                        backgroundColor = NeoYellow,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start", tint = NeoBlack)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("MULAI EVALUASI UJIAN CBT SEKARANG", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 12.sp)
                    }
                }
            }
        } else if (viewModel.isExamActive) {
            // --- Main Exam Questionnaire Dashboard ---
            val activeQuestion = viewModel.examQuestions[viewModel.currentQuestionIndex]

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Timer Badge
                    val timerMin = viewModel.examTimerSeconds / 60
                    val timerSec = viewModel.examTimerSeconds % 60
                    val formattedTimer = String.format("%02d:%02d", timerMin, timerSec)

                    Box(
                        modifier = Modifier
                            .background(NeoYellow, shape = RoundedCornerShape(4.dp))
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "⏱️ SISA WAKTU: $formattedTimer",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                    }

                    // Cheating counter Badge
                    Box(
                        modifier = Modifier
                            .background(if (viewModel.cheatingCount > 1) NeoRed else NeoGreen, shape = RoundedCornerShape(4.dp))
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "⚠️ CURANG: ${viewModel.cheatingCount}/5",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                    }
                }
            }

            // PROCTORING LIVE FEEDBACK BANNER
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeoGreen, shape = RoundedCornerShape(6.dp))
                        .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Videocam, contentDescription = "Proctor Camera", tint = NeoBlack, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE PROCTOR: KAMERA INTEGRASI AKTIF & NIK TERKONFIRMASI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = NeoBlack
                    )
                }
            }

            // Question Content Card
            item {
                NeoCard(backgroundColor = Color.White) {
                    Text(
                        text = "SOAL NOMOR ${viewModel.currentQuestionIndex + 1} dari ${viewModel.examQuestions.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = activeQuestion.question,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = NeoBlack,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Option Buttons list
                    activeQuestion.options.forEachIndexed { optIdx, option ->
                        val isChecked = viewModel.selectedCBTAnswers.value[viewModel.currentQuestionIndex] == optIdx
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(if (isChecked) NeoCyan else Color.White, shape = RoundedCornerShape(6.dp))
                                .border(
                                    width = if (isChecked) 2.dp else 1.5.dp,
                                    color = if (isChecked) NeoBlack else Color.LightGray,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { viewModel.selectedCBTAnswers.value[viewModel.currentQuestionIndex] = optIdx }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(9.dp))
                                    .background(if (isChecked) NeoBlack else Color.White, shape = RoundedCornerShape(9.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = option,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )
                        }
                    }
                }
            }

            // Question Navigation Controls
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isFirst = viewModel.currentQuestionIndex == 0
                    val isLast = viewModel.currentQuestionIndex == viewModel.examQuestions.size - 1

                    NeoButton(
                        onClick = { if (!isFirst) viewModel.currentQuestionIndex-- },
                        backgroundColor = if (isFirst) Color.LightGray else NeoWhite,
                        borderColor = if (isFirst) Color.Gray else NeoBlack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("KEMBALI", fontWeight = FontWeight.Black, color = if (isFirst) Color.Gray else NeoBlack, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (!isLast) {
                        NeoButton(
                            onClick = { viewModel.currentQuestionIndex++ },
                            backgroundColor = NeoWhite,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("LANJUT", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
                        }
                    } else {
                        NeoButton(
                            onClick = { viewModel.submitCBTExam() },
                            backgroundColor = NeoGreen,
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Text("SIAP KIRIM", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Anti-Fraud demonstration testing helper
            item {
                NeoCard(backgroundColor = Color.White) {
                    Text(
                        text = "🧪 DEV TESTING SHIELDS (ANTI-CHEAT TESTER)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = NeoBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Karena emulator streaming tidak bisa digeser/berpindah layar layaknya pengguna, gunakan tombol di bawah ini untuk mensimulasikan kegagalan proctoring sistem secara paksa.",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NeoButton(
                        onClick = { viewModel.logCheatingAttempt("Kehilangan Fokus Sistem (Pencet Simulator)") },
                        backgroundColor = NeoYellow,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.BugReport, contentDescription = "Trigger Cheat", tint = NeoBlack)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SIMULASIKAN BERALIH WINDOW / SPLIT SCREEN", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
                    }
                }
            }
        } else {
            // --- Exam Summary & Results Dashboard ---
            item {
                NeoCard(backgroundColor = Color.White) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = if (viewModel.finalExamScore >= 70 && viewModel.cheatingCount < 3) Icons.Default.TaskAlt else Icons.Default.Cancel,
                                contentDescription = "Result",
                                modifier = Modifier.size(64.dp),
                                tint = if (viewModel.finalExamScore >= 70 && viewModel.cheatingCount < 3) NeoGreen else NeoRed
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "UJIAN CBT SELESAI!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )
                            Text(
                                text = "Hasil pengerjaan Anda telah diunggah & diverifikasi oleh sistem CBT kependidikan.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            NeoCard(backgroundColor = NeoYellow) {
                                Text("NILAI AKHIR", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${viewModel.finalExamScore} / 100", fontSize = 24.sp, fontWeight = FontWeight.Black)
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            NeoCard(backgroundColor = if (viewModel.cheatingCount < 3) NeoGreen else NeoRed) {
                                Text("PELANGGARAN", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${viewModel.cheatingCount} Bendera", fontSize = 20.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Decisions banner
                    val passed = viewModel.finalExamScore >= 70 && viewModel.cheatingCount < 3
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (passed) NeoGreen else NeoRed, shape = RoundedCornerShape(6.dp))
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (passed) "KETERANGAN: LULUS EVALUASI KELAS (PRESTASI AMAN)" else "KETERANGAN: GAGAL / CURANG (DISKUALIFIKASI)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                    }

                    // Cheat Log lists if any
                    if (viewModel.cheatLogs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "📜 LOG PELANGGARAN PROCTORING:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NeoBg, shape = RoundedCornerShape(6.dp))
                                .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                viewModel.cheatLogs.forEach { log ->
                                    Text(text = log, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Reset test button
                    NeoButton(
                        onClick = { viewModel.isExamFinished = false },
                        backgroundColor = NeoWhite,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ULANG UJIAN CBT BISA", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
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
fun CBTInstructionRow(num: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(NeoCyan, shape = RoundedCornerShape(10.dp))
                .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = num, fontSize = 10.sp, fontWeight = FontWeight.Black, color = NeoBlack)
        }
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NeoBlack,
            modifier = Modifier.weight(1f)
        )
    }
}

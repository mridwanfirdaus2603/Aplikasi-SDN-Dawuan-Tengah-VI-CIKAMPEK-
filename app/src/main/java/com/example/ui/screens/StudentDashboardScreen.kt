package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeoBadge
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun StudentDashboardScreen(viewModel: SchoolViewModel) {
    val attendanceList by viewModel.attendance.collectAsState()
    val examScoresList by viewModel.examScores.collectAsState()

    // Aggregate Attendance metrics
    val totalRecords = attendanceList.size
    val presentRecords = attendanceList.count { it.status == "Present" }
    val sickRecords = attendanceList.count { it.status == "Sick" }
    val permRecords = attendanceList.count { it.status == "Permission" }
    val absentRecords = attendanceList.count { it.status == "Absent" }

    val attendancePercentage = if (totalRecords > 0) {
        (presentRecords.toFloat() / totalRecords.toFloat()) * 100
    } else {
        100f
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Student Profile Card ---
        item {
            NeoCard(backgroundColor = NeoYellow) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(NeoCyan, shape = RoundedCornerShape(28.dp))
                            .border(width = 2.5.dp, color = NeoBlack, shape = RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Avatar Siswa",
                            tint = NeoBlack,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column {
                        Text(
                            text = viewModel.currentStudentName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                        Text(
                            text = "${viewModel.currentStudentClass} • NISN: ${viewModel.currentStudentNISN}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        // --- Attendance Status Card ---
        item {
            NeoCard(backgroundColor = Color.White) {
                Text(
                    text = "📅 MONITORING PRESENSI SISWA",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Presentase kehadiran dihitung secara cerdas berbasis catatan harian di sistem database dapodik lokal.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // % Widget Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(NeoGreen, shape = RoundedCornerShape(6.dp))
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format("%.0f%%", attendancePercentage),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )
                            Text(
                                text = "Kehadiran",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )
                        }
                    }

                    // Count Box
                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .background(NeoBg, shape = RoundedCornerShape(6.dp))
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            AttendanceBadgeCount("HADIR", "$presentRecords Hari", NeoGreen)
                            Spacer(modifier = Modifier.height(4.dp))
                            AttendanceBadgeCount("SAKIT/IJIN", "${sickRecords + permRecords} Hari", NeoYellow)
                            Spacer(modifier = Modifier.height(4.dp))
                            AttendanceBadgeCount("ALFA", "$absentRecords Hari", NeoRed)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive check-in button
                Text(
                    text = "📝 ABSENSI CEPAT MANDIRI HARI INI",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NeoButton(
                        onClick = { viewModel.checkInPresent() },
                        backgroundColor = NeoGreen,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Present", tint = NeoBlack)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAYA HADIR", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
                    }

                    NeoButton(
                        onClick = { viewModel.checkInSick() },
                        backgroundColor = NeoYellow,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Sick, contentDescription = "Sick", tint = NeoBlack)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAYA SAKIT", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
                    }
                }
            }
        }

        // --- Academic Performance (Dynamic Report Layout) ---
        item {
            NeoCard(backgroundColor = Color.White) {
                Text(
                    text = "🎓 RAPOR NILAI BERKELANJUTAN",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aktivitas belajar asesmen sumatif harian kurikulum merdeka.",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Standard Subjects list
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SubjectGradeRow("Matematika", 88, NeoYellow)
                    SubjectGradeRow("IPAS (Sains)", 92, NeoGreen)
                    SubjectGradeRow("Bahasa Indonesia", 85, NeoCyan)
                    SubjectGradeRow("Pendidikan Jasmani & Kesehatan (PJOK)", 95, NeoPink)
                }
            }
        }

        // --- CBT Exam History logs (From Local Room Database!) ---
        item {
            NeoCard(backgroundColor = Color.White) {
                Text(
                    text = "✍️ RIWAYAT CBT EXAM ONLINE (ROOM DB)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(6.dp))

                if (examScoresList.isEmpty()) {
                    Text(
                        text = "Belum ada riwayat pengerjaan CBT di database lokal.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        examScoresList.forEach { exam ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(NeoBg, shape = RoundedCornerShape(6.dp))
                                    .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = exam.examName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = NeoBlack
                                    )
                                    Text(
                                        text = "Cheating flags: ${exam.cheatingFlags} • Status: ${exam.status}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (exam.cheatingFlags > 2) NeoRed else Color.DarkGray
                                    )
                                }

                                NeoBadge(
                                    text = "${exam.score}/${exam.maxScore}",
                                    backgroundColor = if (exam.score >= 70) NeoGreen else NeoRed
                                )
                            }
                        }
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
fun AttendanceBadgeCount(label: String, count: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(color, shape = RoundedCornerShape(4.dp)))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Black, color = NeoBlack)
        }
        Text(text = count, fontSize = 11.sp, fontWeight = FontWeight.Black, color = NeoBlack)
    }
}

@Composable
fun SubjectGradeRow(subject: String, score: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = subject, fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeoBlack)
            Text(text = "$score/100", fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeoBlack)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Color.White, shape = RoundedCornerShape(2.dp))
                .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(score.toFloat() / 100f)
                    .background(color, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}

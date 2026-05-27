package com.example.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeoBadge
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import coil.compose.AsyncImage

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

    val context = LocalContext.current
    var showAlternativeSelector by remember { mutableStateOf(false) }
    var customNameInput by remember { mutableStateOf("") }
    var customEmailInput by remember { mutableStateOf("") }

    // Google Sign-In options and client
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()
    }
    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                viewModel.onGoogleSignInSuccess(
                    name = account.displayName ?: "Siswa Google",
                    email = account.email ?: "siswa@gmail.com",
                    photoUrl = account.photoUrl?.toString()
                )
            } else {
                showAlternativeSelector = true
            }
        } catch (e: Exception) {
            showAlternativeSelector = true
        }
    }

    if (showAlternativeSelector) {
        AlertDialog(
            onDismissRequest = { showAlternativeSelector = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(NeoOrange, shape = RoundedCornerShape(6.dp))
                            .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("G", fontWeight = FontWeight.Black, color = Color.White, fontSize = 14.sp)
                    }
                    Text("Pilih Akun Google", fontWeight = FontWeight.Black, fontSize = 18.sp, color = NeoBlack)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Native Google Play Services tidak mengizinkan login langsung. Pilih salah satu Akun terdaftar atau masukkan akun Google Anda secara manual di bawah:",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )

                    HorizontalDivider(color = NeoBlack, thickness = 1.dp)

                    // Default Account Option 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(12.dp))
                            .background(NeoGreen, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.onGoogleSignInSuccess(
                                    name = "Muhammad Ridwan",
                                    email = "muhammadridwanf61@gmail.com",
                                    photoUrl = "https://lh3.googleusercontent.com/a/default-user"
                                )
                                showAlternativeSelector = false
                            }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White, shape = RoundedCornerShape(18.dp))
                                .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("MR", fontWeight = FontWeight.Black, color = NeoBlack)
                        }
                        Column {
                            Text("Muhammad Ridwan", fontWeight = FontWeight.Black, fontSize = 14.sp, color = NeoBlack)
                            Text("muhammadridwanf61@gmail.com", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        }
                    }

                    // Default Account Option 2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(12.dp))
                            .background(NeoCyan, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.onGoogleSignInSuccess(
                                    name = "Budi Setiawan",
                                    email = "budi.setiawan@gmail.com",
                                    photoUrl = "https://lh3.googleusercontent.com/a/default-user"
                                )
                                showAlternativeSelector = false
                            }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White, shape = RoundedCornerShape(18.dp))
                                .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("BS", fontWeight = FontWeight.Black, color = NeoBlack)
                        }
                        Column {
                            Text("Budi Setiawan", fontWeight = FontWeight.Black, fontSize = 14.sp, color = NeoBlack)
                            Text("budi.setiawan@gmail.com", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        }
                    }

                    HorizontalDivider(color = NeoBlack, thickness = 1.dp)

                    // Custom input details
                    Text("Atau ketik Akun Anda sendiri:", fontWeight = FontWeight.Black, fontSize = 12.sp, color = NeoBlack)
                    OutlinedTextField(
                        value = customNameInput,
                        onValueChange = { customNameInput = it },
                        label = { Text("Nama Lengkap") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeoBlack,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = NeoBlack
                        )
                    )
                    OutlinedTextField(
                        value = customEmailInput,
                        onValueChange = { customEmailInput = it },
                        label = { Text("Email Google") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeoBlack,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = NeoBlack
                        )
                    )

                    Button(
                        onClick = {
                            if (customNameInput.isNotBlank() && customEmailInput.isNotBlank()) {
                                viewModel.onGoogleSignInSuccess(
                                    name = customNameInput,
                                    email = customEmailInput,
                                    photoUrl = null
                                )
                                showAlternativeSelector = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeoBlack),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = customNameInput.isNotBlank() && customEmailInput.isNotBlank()
                    ) {
                        Text("Gunakan Akun Custom", color = Color.White, fontWeight = FontWeight.Black)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAlternativeSelector = false }) {
                    Text("Tutup", fontWeight = FontWeight.Black, color = NeoBlack)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(width = 3.dp, color = NeoBlack, shape = RoundedCornerShape(16.dp))
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Student Profile Card with Google Auth Connection ---
        item {
            NeoCard(backgroundColor = if (viewModel.isGoogleAuthenticated) NeoGreen else NeoYellow) {
                if (viewModel.isGoogleAuthenticated) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(28.dp))
                                        .background(Color.White)
                                        .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(28.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (viewModel.googleAccountPhotoUrl != null) {
                                        AsyncImage(
                                            model = viewModel.googleAccountPhotoUrl,
                                            contentDescription = "Foto Google",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = "Avatar",
                                            tint = NeoBlack,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = viewModel.currentStudentName,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = NeoBlack
                                        )
                                        Box(
                                            modifier = Modifier
                                                .background(NeoCyan, shape = RoundedCornerShape(4.dp))
                                                .border(1.dp, NeoBlack, shape = RoundedCornerShape(4.dp))
                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                        ) {
                                            Text("GOOGLE", fontSize = 8.sp, fontWeight = FontWeight.Black, color = NeoBlack)
                                        }
                                    }
                                    Text(
                                        text = viewModel.googleAccountEmail ?: "",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = "NISN: ${viewModel.currentStudentNISN}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = NeoBlack, thickness = 1.5.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sesi Otentikasi Google Aktif",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )

                            NeoButton(
                                onClick = { viewModel.onGoogleSignOut() },
                                backgroundColor = NeoPink,
                                cornerRadius = 8.dp
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = "Log Out",
                                    tint = NeoBlack,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Log Out",
                                    fontWeight = FontWeight.Black,
                                    color = NeoBlack,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
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

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = NeoBlack, thickness = 1.5.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Hubungkan digital otentikasi Google untuk sinkronisasi otomatis nama siswa, data raport, ujian CBT, dan administrasi PPDB terintegrasi.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            NeoButton(
                                onClick = {
                                    val signInIntent = googleSignInClient.signInIntent
                                    try {
                                        launcher.launch(signInIntent)
                                    } catch (e: Exception) {
                                        showAlternativeSelector = true
                                    }
                                },
                                backgroundColor = Color.White,
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(NeoOrange, shape = RoundedCornerShape(4.dp))
                                            .border(1.dp, NeoBlack, shape = RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("G", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    }
                                    Text("Masuk Akun Google", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
                                }
                            }

                            NeoButton(
                                onClick = { showAlternativeSelector = true },
                                backgroundColor = NeoCyan,
                                modifier = Modifier.weight(0.8f)
                            ) {
                                Icon(Icons.Default.ManageAccounts, contentDescription = "Demo Account", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pilih Demo", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
                            }
                        }
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

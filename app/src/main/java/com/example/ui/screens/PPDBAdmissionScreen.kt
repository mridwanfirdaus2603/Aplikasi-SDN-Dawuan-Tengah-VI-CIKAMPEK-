package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PPDBRegistration
import com.example.ui.components.NeoBadge
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun PPDBAdmissionScreen(viewModel: SchoolViewModel) {
    val registrations by viewModel.registrations.collectAsState()
    var selectedAnalyticsPathway by remember { mutableStateOf("Zonasi") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Block ---
        item {
            NeoCard(backgroundColor = NeoOrange) {
                Text(
                    text = "🗳️ PPDB ELEKTRONIK 2026",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Penerimaan Peserta Didik Baru & AI Document Auditor",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sistem cerdas penyeleksian berkas KK/Akte dengan AI (Kementerian Pendidikan Dasar dan Menengah RI) yang terintegrasi analisis penempatan terpusat.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeoBlack
                )
            }
        }

        // --- PPDB FORM ZONE ---
        item {
            NeoCard(backgroundColor = Color.White) {
                Text(
                    text = "✍️ FORMULIR PENDAFTARAN SISWA BARU",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Nama
                Text("Nama Lengkap Calon Siswa", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeoBlack)
                OutlinedTextField(
                    value = viewModel.namaInput,
                    onValueChange = { viewModel.namaInput = it },
                    placeholder = { Text("Contoh: Budi Prasetyo") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeoBlack,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = NeoBlack
                    )
                )

                // NIK
                Text("NIK (KTP/KIA) (16 Digit)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeoBlack)
                OutlinedTextField(
                    value = viewModel.nikInput,
                    onValueChange = { if (it.length <= 16) viewModel.nikInput = it },
                    placeholder = { Text("Contoh: 321501xxxxxxxxxx") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeoBlack,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                // NISN
                Text("NISN (10 Digit, Opsional Paud/TK)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeoBlack)
                OutlinedTextField(
                    value = viewModel.nisnInput,
                    onValueChange = { if (it.length <= 10) viewModel.nisnInput = it },
                    placeholder = { Text("Contoh: 0115324512") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeoBlack,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                // Asal Sekolah
                Text("Asal Sekolah TK / RA / SPS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeoBlack)
                OutlinedTextField(
                    value = viewModel.asalSekolahInput,
                    onValueChange = { viewModel.asalSekolahInput = it },
                    placeholder = { Text("Contoh: SPS Tunas Harapan Dawuan") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeoBlack,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                // Alamat
                Text("Detail Alamat Tempat Tinggal (KK)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeoBlack)
                OutlinedTextField(
                    value = viewModel.alamatInput,
                    onValueChange = { viewModel.alamatInput = it },
                    placeholder = { Text("Contoh: Perum Dawuan Indah Blok C No. 5") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeoBlack,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Jalur Dropdown
                Text("Jalur Seleksi Keppendik", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeoBlack)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Zonasi", "Afirmasi", "Prestasi", "Perpindahan").forEach { pathway ->
                        val isSel = viewModel.selectedJalur == pathway
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isSel) NeoYellow else Color.White,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .border(
                                    width = if (isSel) 3.dp else 1.5.dp,
                                    color = NeoBlack,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable { viewModel.selectedJalur = pathway }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pathway,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // KK Verification helper details
                Text("Kelengkapan Mandiri KK / Akta", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeoBlack)
                OutlinedTextField(
                    value = viewModel.documentDetailsInput,
                    onValueChange = { viewModel.documentDetailsInput = it },
                    placeholder = { Text("Sebutkan nomor KK atau detail pendukung, misal: PIP Aktif: 45112...") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeoBlack,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Button Verify
                if (viewModel.isVerifyingDoc) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = NeoBlack, strokeWidth = 5.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Menghubungi AI Auditor Kemendikbud...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )
                        }
                    }
                } else {
                    NeoButton(
                        onClick = { viewModel.submitPPDBRegistration() },
                        backgroundColor = NeoYellow,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Verified, contentDescription = "AI Audit", tint = NeoBlack)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VERIFIKASI BERKAS DENGAN AI", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 13.sp)
                    }
                }
            }
        }

        // --- AI Verification Console Output ---
        item {
            AnimatedVisibility(visible = viewModel.aiReportResult.isNotEmpty()) {
                NeoCard(
                    backgroundColor = NeoGreen,
                    cornerRadius = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🤖 HASIL AUDIT DOKUMEN PPDB AI",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                        IconButton(onClick = { viewModel.aiReportResult = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = NeoBlack)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(4.dp))
                            .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(4.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = viewModel.aiReportResult,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeoBlack,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // --- REAL TIME STUDENT PLACEMENT ANALYTICS BOARD ---
        item {
            NeoCard(backgroundColor = Color.White) {
                Text(
                    text = "📊 ANALISIS PENEMPATAN REAL-TIME",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Daftar pemeringkatan berdasarkan Jalur Seleksi. Perubahan pendaftar langsung merubah susunan kursi pendaftaran secara otomatis.",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Pathway Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Zonasi", "Afirmasi", "Prestasi", "Perpindahan").forEach { path ->
                        val isSel = selectedAnalyticsPathway == path
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isSel) NeoCyan else Color.White,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .border(
                                    width = if (isSel) 2.dp else 1.dp,
                                    color = NeoBlack,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable { selectedAnalyticsPathway = path }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = path,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Get Filtered & Sorted Registrations
                val filteredList = registrations.filter { it.jalur == selectedAnalyticsPathway }
                    .sortedWith(
                        if (selectedAnalyticsPathway == "Zonasi") {
                            compareBy<PPDBRegistration> { it.distanceKm }.thenBy { it.timestamp }
                        } else if (selectedAnalyticsPathway == "Prestasi") {
                            compareByDescending<PPDBRegistration> { it.score }.thenBy { it.timestamp }
                        } else {
                            compareBy<PPDBRegistration> { it.distanceKm }.thenBy { it.timestamp }
                        }
                    )

                // Quota metrics (e.g. Quota max is 4 seats per pathway for demo)
                val maxSeats = 4
                val filledSeats = filteredList.filter { it.status == "Verified" }.take(maxSeats).size
                val percentage = (filledSeats.toFloat() / maxSeats.toFloat()).coerceIn(0f, 1f)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kuota Terisi: $filledSeats / $maxSeats Kursi",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = NeoBlack
                    )
                    Text(
                        text = "${(percentage * 100).toInt()}% Penuh",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = NeoBlack
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Quota Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color.White, shape = RoundedCornerShape(4.dp))
                        .border(width = 2.dp, color = NeoBlack, shape = RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(percentage)
                            .background(NeoPink, shape = RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp))
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Leaderboard List
                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada pendaftar di jalur ini.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredList.forEachIndexed { index, reg ->
                            val isWithinQuota = index < maxSeats && reg.status == "Verified"

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isWithinQuota) Color(0xFFEDFDF5) else Color(0xFFFEF2F2),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .border(
                                        width = if (isWithinQuota) 2.5.dp else 1.dp,
                                        color = if (isWithinQuota) NeoBlack else Color.Gray,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Rank counter
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(
                                                if (isWithinQuota) NeoYellow else Color.White,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .border(width = 1.5.dp, color = NeoBlack, shape = RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = NeoBlack
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = reg.nama,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = NeoBlack
                                        )
                                        Text(
                                            text = "Asal: ${reg.asalSekolah} • NIK: ${reg.nik.take(4)}...",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray
                                        )
                                    }
                                }

                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    if (selectedAnalyticsPathway == "Zonasi") {
                                        Text(
                                            text = "${reg.distanceKm} km",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = NeoBlack
                                        )
                                        Text(
                                            text = "Jarak",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray
                                        )
                                    } else {
                                        Text(
                                            text = "${reg.score} Pts",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = NeoBlack
                                        )
                                        Text(
                                            text = "Skor",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (isWithinQuota) NeoGreen else NeoRed,
                                                shape = RoundedCornerShape(2.dp)
                                            )
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (isWithinQuota) "LULUS" else "TERDEPAK",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black,
                                            color = NeoBlack
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- DEV CONTROLS RESET ---
        item {
            NeoCard(backgroundColor = Color.White) {
                Text(
                    text = "🛠️ SISTEM DEVELOPER CONSOLE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                NeoButton(
                    onClick = { viewModel.resetAllRegistrations() },
                    backgroundColor = NeoPink,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset", tint = NeoBlack)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RESET DATABASE PENDAFTAR SISWA BARU", fontWeight = FontWeight.Black, color = NeoBlack, fontSize = 11.sp)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

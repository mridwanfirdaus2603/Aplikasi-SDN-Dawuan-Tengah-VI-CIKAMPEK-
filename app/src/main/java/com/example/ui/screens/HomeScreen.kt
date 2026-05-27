package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeoBadge
import com.example.ui.components.NeoCard
import com.example.ui.theme.*

@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- High-Contrast Hero Header ---
        item {
            NeoCard(
                backgroundColor = NeoYellow,
                shadowColor = NeoBlack,
                cornerRadius = 24.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .border(width = 4.dp, color = NeoBlack, shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Logo Sekolah",
                            modifier = Modifier.size(36.dp),
                            tint = NeoBlack
                        )
                    }

                    Column {
                        Text(
                            text = "PORTAL RESMI SMART SCHOOL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "SDN DAWUAN TENGAH VI",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack,
                            lineHeight = 28.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Membentuk generasi pembelajar yang unggul, berbudaya, kreatif, berkebinekaan global, dan menanamkan nilai-nilai luhur Profil Pelajar Pancasila di era digital.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = NeoBlack
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NeoBadge(text = "AKREDITASI A", backgroundColor = NeoCyan)
                    NeoBadge(text = "KURIKULUM MERDEKA", backgroundColor = NeoGreen)
                }
            }
        }

        // --- Quick Facts Grid ---
        item {
            Text(
                text = "⚡ INFORMASI SEKOLAH",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = NeoBlack,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    NeoCard(backgroundColor = NeoPink) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Siswa",
                            tint = NeoBlack,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "480+",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                        Text(
                            text = "Siswa Aktif",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeoBlack
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    NeoCard(backgroundColor = NeoOrange) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "Prestasi",
                            tint = NeoBlack,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "25+",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack
                        )
                        Text(
                            text = "Prestasi Kab.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeoBlack
                        )
                    }
                }
            }
        }

        // --- School Location & Contact (Indonesian Context) ---
        item {
            NeoCard(backgroundColor = Color.White) {
                Text(
                    text = "📍 LOKASI SEKOLAH",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeoBlack,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Jl. Jend. Lorong Dawuan Tengah No. 45, Kecamatan Cikampek, Kabupaten Karawang, Jawa Barat - 41373.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .background(NeoCyan, shape = RoundedCornerShape(24.dp))
                        .border(width = 4.dp, color = NeoBlack, shape = RoundedCornerShape(24.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map Finder",
                            tint = NeoBlack,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "MAPS: 0.8 km dari Stasiun Cikampek",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = NeoBlack,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // --- Announcements News (Brutalist Style Rows) ---
        item {
            Text(
                text = "📢 PENGUMUMAN TERBARU",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = NeoBlack,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AnnouncementRow(
                    tagText = "PPDB 2026/2027",
                    tagBg = NeoGreen,
                    title = "Pendaftaran Peserta Didik Baru Telah Dibuka Resmi",
                    date = "Hari Ini"
                )
                AnnouncementRow(
                    tagText = "KURIKULUM",
                    tagBg = NeoPink,
                    title = "Uji Coba Sistem CBT Anti-Fraud Berbasis Kecerdasan Buatan (AI) di SDN Dawuan VI",
                    date = "Kemarin"
                )
                AnnouncementRow(
                    tagText = "KEGIATAN",
                    tagBg = NeoYellow,
                    title = "Pembelajaran Berbasis Proyek (P5) Pengolahan Sampah Organik Dawuan Tengah",
                    date = "3 hari yang lalu"
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun AnnouncementRow(
    tagText: String,
    tagBg: Color,
    title: String,
    date: String
) {
    NeoCard(backgroundColor = Color.White) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            NeoBadge(text = tagText, backgroundColor = tagBg)
            Text(
                text = date,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = NeoBlack
        )
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.neoShadow
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeoBg
import com.example.ui.theme.NeoBlack
import com.example.ui.theme.NeoYellow
import com.example.ui.theme.NeoCyan
import com.example.ui.theme.NeoGreen
import com.example.ui.theme.NeoOrange
import com.example.ui.theme.NeoPink
import com.example.ui.theme.NeoPurple
import com.example.ui.viewmodel.SchoolViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: SchoolViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: SchoolViewModel) {
    var activeTab by remember { mutableStateOf("HOME") } // HOME, PPDB, PORTAL, ELEARNING, CBT

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "SDN DT VI",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black,
                                color = NeoBlack,
                                letterSpacing = (-0.5).sp
                            )
                            Row {
                                Box(
                                    modifier = Modifier
                                        .background(NeoYellow)
                                        .border(width = 2.dp, color = NeoBlack)
                                        .neoShadow(shadowColor = NeoBlack, offsetX = 2.dp, offsetY = 2.dp, cornerRadius = 0.dp)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "DAWUAN TENGAH SIX",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        color = NeoBlack
                                    )
                                }
                            }
                        }

                        // Artistic Flair Action AI Avatar Tool on the Right
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .neoShadow(shadowColor = NeoBlack, offsetX = 3.dp, offsetY = 3.dp, cornerRadius = 22.dp)
                                .background(NeoCyan, shape = RoundedCornerShape(22.dp))
                                .border(width = 3.dp, color = NeoBlack, shape = RoundedCornerShape(22.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "AI Core",
                                tint = NeoBlack,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .background(Color.White)
                    .border(width = 4.dp, color = NeoBlack),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            // Elegant Neo-Brutalist High-Contrast Floating Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neoShadow(shadowColor = NeoBlack, offsetX = 4.dp, offsetY = 4.dp, cornerRadius = 32.dp)
                        .background(Color.White, shape = RoundedCornerShape(32.dp))
                        .border(width = 4.dp, color = NeoBlack, shape = RoundedCornerShape(32.dp))
                        .height(72.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BottomNavItem(
                        label = "Beranda",
                        icon = Icons.Default.Home,
                        activeColor = NeoYellow,
                        isActive = activeTab == "HOME",
                        onClick = { activeTab = "HOME" }
                    )
                    BottomNavItem(
                        label = "PPDB",
                        icon = Icons.Default.HowToReg,
                        activeColor = NeoOrange,
                        isActive = activeTab == "PPDB",
                        onClick = { activeTab = "PPDB" }
                    )
                    BottomNavItem(
                        label = "Siswa",
                        icon = Icons.Default.Dashboard,
                        activeColor = NeoCyan,
                        isActive = activeTab == "PORTAL",
                        onClick = { activeTab = "PORTAL" }
                    )
                    BottomNavItem(
                        label = "Kelas",
                        icon = Icons.Default.AutoStories,
                        activeColor = NeoGreen,
                        isActive = activeTab == "ELEARNING",
                        onClick = { activeTab = "ELEARNING" }
                    )
                    BottomNavItem(
                        label = "CBT",
                        icon = Icons.Default.Computer,
                        activeColor = NeoPink,
                        isActive = activeTab == "CBT",
                        onClick = { activeTab = "CBT" }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NeoBg)
                .padding(innerPadding)
        ) {
            when (activeTab) {
                "HOME" -> HomeScreen()
                "PPDB" -> PPDBAdmissionScreen(viewModel = viewModel)
                "PORTAL" -> StudentDashboardScreen(viewModel = viewModel)
                "ELEARNING" -> ELearningScreen(viewModel = viewModel)
                "CBT" -> CBTExamScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector,
    activeColor: Color,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .run {
                        if (isActive) {
                            this.neoShadow(shadowColor = NeoBlack, offsetX = 2.dp, offsetY = 2.dp, cornerRadius = 24.dp)
                                .background(color = activeColor, shape = RoundedCornerShape(24.dp))
                                .border(width = 2.5.dp, color = NeoBlack, shape = RoundedCornerShape(24.dp))
                        } else {
                            this.background(color = Color.Transparent)
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = NeoBlack,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isActive) FontWeight.Black else FontWeight.Bold,
                color = NeoBlack
            )
        }
    }
}

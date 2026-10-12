package com.example.instasaverhd.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.instasaverhd.data.repository.UpdateInfo
import com.example.instasaverhd.ui.theme.DarkSurfaceGlass
import com.example.instasaverhd.ui.theme.InstaPink
import com.example.instasaverhd.ui.theme.InstagramGradient

@Composable
fun UpdateDialog(
    updateInfo: UpdateInfo,
    isDownloading: Boolean,
    updateProgress: Int = 0,
    onUpdateClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissClick) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DarkSurfaceGlass,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(InstagramGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SystemUpdate,
                        contentDescription = "Update Icon",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Update Available 🚀",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Version v${updateInfo.latestVersion} is ready to install!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InstaPink,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (updateInfo.releaseNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = updateInfo.releaseNotes,
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            maxLines = 4
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isDownloading) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            progress = { updateProgress / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = InstaPink,
                            trackColor = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (updateProgress > 0) "Downloading update: $updateProgress%" else "Starting download...",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = onDismissClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Later", color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = onUpdateClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = InstaPink),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Update Now", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

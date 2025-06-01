package com.example.attendify.ui.student.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.attendify.ui.theme.BorderColor
import com.example.attendify.ui.theme.CardColour
import com.example.attendify.ui.theme.CharcoalBlue
import com.example.attendify.ui.theme.PrimaryColor
import com.example.attendify.ui.theme.PrimaryVariant
import com.example.attendify.ui.theme.SurfaceColor
import com.example.attendify.ui.theme.TextOnPrimary


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceDetailsDialog(
    combinedMap: Map<String, Int>,
    onDismiss: () -> Unit
) {
    val dateFormatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateFormatterOutput = DateTimeFormatter.ofPattern("MMM dd, yyyy")


    val totalDays = combinedMap.size
    val presentDays = combinedMap.values.count { it == 1 }
    val attendancePercentage = if (totalDays > 0) (presentDays * 100) / totalDays else 0



        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = PrimaryVariant,
                            contentColor = TextOnPrimary
                        )
                    ) {
                        Text("Close", fontWeight = FontWeight.SemiBold)
                    }


                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = PrimaryColor
                    )
                    Text(
                        "Attendance Details",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 250.dp, max = 450.dp)
                ) {
                    // Statistics Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceColor.copy(0.7f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Attendance Summary",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = CharcoalBlue
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$presentDays",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    Text(
                                        text = "Present",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF388E3C)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "   ${totalDays - presentDays}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD32F2F)
                                    )
                                    Text(
                                        text = "      Absent",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFF44336)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "    $attendancePercentage%",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = CharcoalBlue
                                    )
                                    Text(
                                        text = " Rate",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CharcoalBlue, textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.fillMaxHeight(0.06f))
                    // Header Row
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardColour
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Date",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                color = BorderColor
                            )
                            Text(
                                text = "Status",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                color = BorderColor
                            )
                        }
                    }

                    // Scrollable attendance list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        combinedMap.toSortedMap(compareByDescending { it }).forEach { (dateStr, status) ->
                            val formattedDate = try {
                                val parsedDate = LocalDate.parse(dateStr, dateFormatterInput)
                                parsedDate.format(dateFormatterOutput)
                            } catch (e: Exception) {
                                dateStr
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (status == 1) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = formattedDate,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (status == 1) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                            contentDescription = null,
                                            tint = if (status == 1) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = if (status == 1) "Present" else "Absent",
                                            color = if (status == 1) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                                            fontWeight = FontWeight.Medium,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .widthIn(min = 350.dp, max = 450.dp)
                .clip(RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            containerColor = Color(0xFFFAF8FF),
            titleContentColor = Color(0xFF4A148C),
            textContentColor = Color(0xFF1B5E20)
        )

}



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendify.ui.verification.VerificationViewModel
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CombinedUnverifiedStudentSummary(
    viewModel: VerificationViewModel,
    modifier: Modifier = Modifier
) {
    val counts by viewModel.unverifiedCounts.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CombinedHeader(totalUnverified = counts.sumOf { it.count })

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(Constants.BRANCHES) { branch ->  // <- always renders all branches
                val branchCounts = counts.filter { it.branch == branch }
                CombinedBranchTile(
                    branch = branch,
                    counts = branchCounts
                )
            }
        }
    }
}

@Composable
private fun CombinedHeader(totalUnverified: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(0.95f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Unverified Students",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (totalUnverified > 0) {
            Text(
                text = "Total: $totalUnverified",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Text(
                text = "All Verified ✓",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
private fun CombinedBranchTile(
    branch: String,
    counts: List<com.example.attendify.data.model.UnverifiedCount>
) {

    val totalCount = counts.sumOf { it.count }
    val hasUnverified = totalCount > 0

    Card(
        modifier = Modifier.padding(4.dp)
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasUnverified)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.05f)
            else
                MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly){
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5F)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header with branch name and total count
                Row(
                    modifier = Modifier.fillMaxWidth().offset(y=(10).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = branch,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )


                }
                // Semester breakdown - simplified like ultra compact
                if (hasUnverified && counts.isNotEmpty()) {
                    // Single row with semester details
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 42.dp)
                            .padding(vertical = 16.dp),
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 8.dp
                    ) {
                        counts
                            .sortedBy { it.semester.toIntOrNull() ?: 0 }
                            .forEach { count ->

                                Text(
                                    text = "S${count.semester[0]}: ${count.count}",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                    }

                } else if (!hasUnverified) {
                    Row(
                        modifier = Modifier.fillMaxWidth().heightIn(min = 42.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "All students verified",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }
            if (hasUnverified) {
                Text(
                    text = totalCount.toString(),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF4CAF50)
                )
            }
        }


    }
}

// Preview section with mock data
@Preview(showBackground = true, name = "Combined Compact Tiles")
@Composable
fun CombinedCompactTilesPreview() {
    MaterialTheme {
        CombinedUnverifiedStudentSummaryContent(
            counts = listOf(
                com.example.attendify.data.model.UnverifiedCount("CSE", "1", 5),
                com.example.attendify.data.model.UnverifiedCount("CSE", "3", 12),
                com.example.attendify.data.model.UnverifiedCount("ME", "2", 3),
                com.example.attendify.data.model.UnverifiedCount("ME", "4", 7),
                com.example.attendify.data.model.UnverifiedCount("ME", "1", 15),
                com.example.attendify.data.model.UnverifiedCount("EE", "6", 4),
            )
        )
    }
}

// Preview-friendly content composable
@Composable
private fun CombinedUnverifiedStudentSummaryContent(
    counts: List<com.example.attendify.data.model.UnverifiedCount>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CombinedHeader(totalUnverified = counts.sumOf { it.count })

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(300.dp) // Fixed height for preview
        ) {
            items(Constants.BRANCHES) { branch ->
                val branchCounts = counts.filter { it.branch == branch }
                CombinedBranchTile(
                    branch = branch,
                    counts = branchCounts
                )
            }
        }
    }
}

// Mock Constants object for preview
object Constants {
    val BRANCHES = listOf(
        "CSE",
        "ME",
        "EE",
        "CE"
    )
}
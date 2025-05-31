import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.PrimaryColor
import com.example.attendify.ui.theme.PrimaryVariant

@Composable

fun VerifyFloatingButton(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Glow behind FAB
        Canvas(
            modifier = Modifier
                .size(72.dp) // slightly bigger than FAB to simulate bleed
                .align(Alignment.BottomEnd)
        ) {
            val gradient = Brush.radialGradient(
                colors = listOf(
                    PrimaryColor.copy(alpha = 0.6f), // Orange with alpha
                    Color.Transparent
                ),
                center = Offset(size.width / 2, size.height / 2),
                radius = size.minDimension / 2
            )
            drawCircle(
                brush = gradient,
                radius = size.minDimension / 2,
                center = Offset(size.width / 2, size.height / 2),
            )
        }

        // The FAB itself with border
        Box(
            modifier = Modifier
                .size(56.dp)
                .border(2.dp, PrimaryVariant, CircleShape) // solid orange border
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavRoutes.VerificationPage.route)
                },
                containerColor = Color.LightGray,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Verify")
            }
        }
    }
}

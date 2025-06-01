import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.attendify.navigation.NavRoutes
import com.example.attendify.ui.theme.PrimaryVariant



@Composable
fun VerifyFloatingButton(navController: NavController) {
    Box(
        modifier = Modifier.offset(y=(-10).dp)
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .border(width = 2.dp, color = Color.White, shape = CircleShape)
                .padding(2.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavRoutes.VerificationPage.route)
                },
                containerColor = PrimaryVariant,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Text(
                    text = "Verify", // or use "Verify" if text fits, else use an icon
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

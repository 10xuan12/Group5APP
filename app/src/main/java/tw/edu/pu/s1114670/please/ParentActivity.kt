package tw.edu.pu.s1114670.please

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tw.edu.pu.s1114670.please.ui.theme.PleaseTheme

class ParentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PleaseTheme {
                ParentScreen()
            }
        }
    }
}

@Composable
fun ParentScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(85.dp)
    ) {
        Text(
            text = "家長專區",
            fontSize = 48.sp, // Adjusted size for better readability
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(100.dp) // Adjusted spacing for better separation
        ) {
            InteractionRow(
                imageResId = R.drawable.student,
                buttonText = "與孩子互動",
                onClick = {context.startActivity(Intent(context, ParenttalktostudentActivity::class.java)) }
            )

            InteractionRow(
                imageResId = R.drawable.teacher,
                buttonText = "與老師互動",
                onClick = {context.startActivity(Intent(context, ParenttalktoteacherActivity::class.java)) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Transparent)
                    .clickable {
                        val intent = Intent(context, IdentitychoiceActivity::class.java)
                        context.startActivity(intent)
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParentScreenPreview() {
    PleaseTheme {
        ParentScreen()
    }
}



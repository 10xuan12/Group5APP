package tw.edu.pu.s1114670.please

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class IdentitychoiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IdentityChoiceScreen()
        }
    }
}

@Composable
fun IdentityChoiceScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "身份選擇",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold
        )

        // 學生選項
        IdentityOption(
            imageId = R.drawable.student,
            buttonText = "學生",
            onClick = { context.startActivity(Intent(context, StudentActivity::class.java)) }
        )

        // 家長選項
        IdentityOption(
            imageId = R.drawable.parents,
            buttonText = "家長",
            onClick = { context.startActivity(Intent(context, ParentActivity::class.java)) }
        )

        // 老師選項
        IdentityOption(
            imageId = R.drawable.teacher,
            buttonText = "老師",
            onClick = { context.startActivity(Intent(context, TeacherActivity::class.java)) }
        )
    }

}

@Composable
fun IdentityOption(imageId: Int, buttonText: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .size(175.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onClick,
            modifier = Modifier.size(170.dp, 176.dp)
        ) {
            Text(text = buttonText, fontSize = 50.sp)
        }
    }
}
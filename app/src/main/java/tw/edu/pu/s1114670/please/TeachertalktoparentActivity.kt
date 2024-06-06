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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TeachertalktoparentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 獲取傳遞的使用者資料
        val userName = intent.getStringExtra("userName") ?: ""
        val userAccount = intent.getStringExtra("userAccount") ?: ""
        val userPassword = intent.getStringExtra("userPassword") ?: ""
        val currentUser = Person(userName, userAccount, userPassword)

        setContent {
            ParentTeacherInteractionScreen(currentUser)
        }
    }
    @Composable
    fun ParentTeacherInteractionScreen(
        currentUser: Person
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0))
                .padding(16.dp)
        ) {
            ParentMessageSection()
            Spacer(modifier = Modifier.height(16.dp))

            TeacherMessageSection(currentUser = currentUser)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    @Composable
    fun ParentMessageSection() {
        val db = Firebase.firestore
        val usersCollectionRef = db.collection("users")
        val query = usersCollectionRef
            .whereEqualTo("userID", "家長")

        // 創建用於存儲消息的狀態
        var parentMessages by remember { mutableStateOf<List<String>>(emptyList()) }
        var loading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            query.get()
                .addOnSuccessListener { documents ->
                    val messages = documents.mapNotNull {
                        it.getString("message").also { message ->
                            println("Fetched message: $message") // 調試信息
                        }
                    }
                    parentMessages = messages
                    loading = false
                    println("Total messages fetched: ${messages.size}") // 調試信息
                }
                .addOnFailureListener { exception ->
                    // 處理失敗的情況
                    errorMessage = "Error getting documents: ${exception.message}"
                    loading = false
                    println("Error getting documents: ${exception.message}") // 調試信息
                }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFFFF59D))
                    .padding(16.dp)
            ) {
                Text(text = "小明家長的留言", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "老師您好，小明今天需在中午飯後吃過敏藥",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    @Composable
    fun TeacherMessageSection(currentUser: Person) {
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFFFF59D))
                    .padding(16.dp)
            ) {
                Text(
                    text = "給小明家長的話",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 在這裡添加老師的留言內容或文本框
                SendMessage(currentUser)
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
                            val intent = Intent(context, TeacherActivity::class.java)
                            context.startActivity(intent)
                        }
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        // 獲取傳遞的使用者資料
        val userName = intent.getStringExtra("userName") ?: ""
        val userAccount = intent.getStringExtra("userAccount") ?: ""
        val userPassword = intent.getStringExtra("userPassword") ?: ""
        ParentTeacherInteractionScreen(
            currentUser = Person(userName, userAccount, userPassword))
    }
}
// 傳訊息功能
@Composable
fun SendMessage(currentUser: Person) {
    var userMsg by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    val db = Firebase.firestore

    Column {
        TextField(
            value = userMsg,
            onValueChange = { newText ->
                userMsg = newText
            },
            label = { Text("新增訊息") },
            placeholder = { Text("請輸入訊息") }
        )

        Button(onClick = {
            val message = Person(currentUser.userName, message = userMsg)
            db.collection("users")
                .document(currentUser.userName) // 使用者名稱作為文件ID
                .collection("Messages") // 新增訊息子集合
                .add(message) // 添加新訊息
                .addOnSuccessListener { _ ->
                    msg = "傳送成功"
                }
                .addOnFailureListener { e ->
                    msg = "傳送失敗：$e"
                }
        }) {
            Text("傳送")
        }
        Text(text = msg)
    }
}
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ParenttalktoteacherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParentTeacherInteractionScreen()
        }
    }

    @Composable
    fun ParentTeacherInteractionScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0))
                .padding(16.dp)
        ) {
            ParentMessageSection()
            Spacer(modifier = Modifier.height(16.dp))
            TeacherMessageSection()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @Composable
    fun ParentMessageSection() {
        val currentUser = Person("Teacher", "")
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
                    text = "給老師的話",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                SendMessage2(currentUser)
            }
        }
    }

    @Composable
    fun TeacherMessageSection() {
        val db = Firebase.firestore
        val usersCollectionRef = db.collection("users")
        val query = usersCollectionRef.whereEqualTo("userID", "老師")

        var teacherMessages by remember { mutableStateOf<List<String>>(emptyList()) }
        var loading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            query.get()
                .addOnSuccessListener { documents ->
                    val messages = documents.mapNotNull {
                        it.getString("message2").also { message ->
                            println("Fetched message: $message")
                        }
                    }
                    teacherMessages = messages
                    loading = false
                    println("Total messages fetched: ${messages.size}")
                }
                .addOnFailureListener { exception ->
                    errorMessage = "Error getting documents: ${exception.message}"
                    loading = false
                    println("Error getting documents: ${exception.message}")
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
                Text(
                    text = "老師的留言",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "今天小明有吃藥喔",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))
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
                                val intent = Intent(context, ParentActivity::class.java)
                                context.startActivity(intent)
                            }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }

    @Composable
    fun SendMessage2(currentUser: Person) {
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
                val message = hashMapOf(
                    "userName" to currentUser.userName,
                    "message" to userMsg
                )
                db.collection("users")
                    .document(currentUser.userName)
                    .collection("Messages")
                    .add(message)
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
}
package com.example.trysample.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.example.trysample.Helper
import com.example.trysample.R
import com.example.trysample.mydata.Message
import com.example.trysample.myviewmodels.ScanCropViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBot(scanCropViewModel: ScanCropViewModel) {


    val question = remember { mutableStateOf("") }
    val chatHistory = scanCropViewModel.chatHistory.collectAsState()
    val scope = rememberCoroutineScope()
    val userSelectedImage = remember {
        mutableStateOf("")
    }

    val accessToken by scanCropViewModel.accessToken.collectAsState()

    LaunchedEffect(accessToken) {
        if (accessToken != null) {
            Log.d("ShowChats", "AccessToken updated: $accessToken")
            scanCropViewModel.fetchConversation(question.value)
        }
    }


    val isChatBotThinking = scanCropViewModel.isBotThinking.collectAsState()


    val userSelectedImageBase64 = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            userSelectedImage.value = it.toString()
            val base64String = Helper.convertImageUriToBase64(context, it)
            userSelectedImageBase64.value = base64String
        }
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (imageUploadingArea, chatArea, questionInputArea) = createRefs()

        // 1. Uploading Section at Top

        Box(
            modifier = Modifier
                .constrainAs(imageUploadingArea) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .height(180.dp)
                .padding(top = 8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {

            if (userSelectedImage.value.isNotEmpty()) {
                AsyncImage(
                    model = userSelectedImage.value,
                    contentDescription = null,
                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(180.dp)
                        .fillMaxSize()
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            } else {
                AsyncImage(
                    model = R.drawable.upload_image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            }


        }

        // 2. Chat Area in the Middle (takes remaining space)
        ChatScreen(
            messages = chatHistory.value,
            modifier = Modifier
                .constrainAs(chatArea) {
                    top.linkTo(imageUploadingArea.bottom, margin = 16.dp)
                    bottom.linkTo(questionInputArea.top, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }

        )

        // 3. Input at Bottom
        Row(
            modifier = Modifier
                .constrainAs(questionInputArea) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = question.value,
                onValueChange = { question.value = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                ),
                placeholder = { Text("अपनी फसल के बारे में पूछो...") },
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp,Color.LightGray , RoundedCornerShape(22.dp))
                    .padding(4.dp)
                    .clip(RoundedCornerShape(22.dp)),
            )

            if (!isChatBotThinking.value) {
                OutlinedButton(
                    onClick = {
                        Log.d("Access_Token","Btn clicked ")
                        scanCropViewModel.fetchIdentification(userSelectedImageBase64.value)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send" , tint = Color.White)
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.size(26.dp))
            }

        }
    }
}

@Composable
fun ChatScreen(messages: List<Message>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        items(messages) {
            MessageItem(message = it)
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    val isUser = message.type.uppercase() == "QUESTION"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isUser) Color(0xFFDCF8C6) else Color(0xFFE5E5EA),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.content,
                color = Color.Black
            )
        }
    }
}


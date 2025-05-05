package com.gleidsonlm.businesscard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.gleidsonlm.businesscard.ui.theme.BusinessCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BusinessCardTheme {
                Surface {
                    BusinessCardApp()
                }
            }
        }
    }
}

@Composable
private fun BusinessCardApp() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BusinessCardFace(
            image = painterResource(R.drawable.avatar),
            fullName = stringResource(R.string.full_name),
            title = stringResource(R.string.title),
        )
    }
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BusinessCardLink(
            icon = Icons.Rounded.Call,
            linkText = stringResource(R.string.link_text_call),
            linkUrl = stringResource(R.string.link_url_call),
        )
        BusinessCardLink(
            icon = Icons.Rounded.Email,
            linkText = stringResource(R.string.link_text_email),
            linkUrl = stringResource(R.string.link_url_email),
        )
        BusinessCardLink(
            icon = Icons.Rounded.AccountCircle,
            linkText = stringResource(R.string.link_text_in),
            linkUrl = stringResource(R.string.link_url_in),
        )
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
private fun BusinessCardFace(
    image: Painter,
    fullName: String,
    title: String,
    modifier: Modifier = Modifier
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = image,
            contentDescription = null,
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = fullName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = title,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun BusinessCardLink(
    icon: ImageVector,
    linkText: String,
    linkUrl: String,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    Row(
        modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            icon,
            contentDescription = null,
        )
        Spacer(modifier.padding(4.dp))
        Button(
            onClick = { openLink(context, linkUrl) },
            modifier = modifier.defaultMinSize(128.dp)
        ) {
            Text(
                text = linkText,
                modifier = modifier.clickable { openLink(context, linkUrl) }
            )
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
private fun openLink(context: Context, linkUrl: String) {
    try {
        val intentUrl = Intent(Intent.ACTION_VIEW, linkUrl.toUri())
        context.startActivity(intentUrl)
    } catch (e: Exception) {
        Log.e("LinkButton", "Error opening URL: $linkUrl", e)
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    BusinessCardTheme {
        Column(
            modifier = Modifier.fillMaxHeight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BusinessCardFace(
                image = painterResource(R.drawable.avatar),
                fullName = stringResource(R.string.full_name),
                title = stringResource(R.string.title),
            )
            Column (
                modifier = Modifier.padding(0.dp)
            ) {
                BusinessCardLink(
                    icon = Icons.Rounded.Call,
                    linkText = stringResource(R.string.link_text_call),
                    linkUrl = stringResource(R.string.link_url_call),
                )
                BusinessCardLink(
                    icon = Icons.Default.Email,
                    linkText = stringResource(R.string.link_text_email),
                    linkUrl = stringResource(R.string.link_url_email),
                )
                BusinessCardLink(
                    icon = Icons.Default.Home,
                    linkText = stringResource(R.string.link_text_in),
                    linkUrl = stringResource(R.string.link_url_in),
                )
            }
        }
    }
}
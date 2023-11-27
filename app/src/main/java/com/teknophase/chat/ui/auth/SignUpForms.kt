package com.teknophase.chat.ui.auth

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teknophase.chat.R
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.theme.ChatTheme

@Composable
fun SignUpForm1() {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Column {
        AppTextField(
            title = "Country Code",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .width(150.dp),
            placeholder = "+1",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_earth),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_arrow_down),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        )

//        DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
//            DropdownMenuItem(modifier = Modifier.width(280.dp),text = {Text(text = "i.toString()")}, onClick = { /*TODO*/ })
//            DropdownMenuItem(text = {Text(text = "i.toString()")}, onClick = { /*TODO*/ })
//        }


        AppTextField(
            title = "Mobile Number",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .padding(top = 24.dp)
                .align(CenterHorizontally),
            placeholder = "9876 543 210",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_dialpad),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Composable
fun SignUpForm2() {
    Column {
        AppTextField(
            title = "Enter OTP",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .padding(top = 24.dp)
                .align(CenterHorizontally),
            placeholder = "1234",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_numbers),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Composable
fun SignUpForm3() {
    Column {
        AppTextField(
            title = "Username",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .align(CenterHorizontally),
            placeholder = "Type Something...",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        )

        AppTextField(
            title = "Password",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .padding(top = 24.dp)
                .align(CenterHorizontally),
            placeholder = "Type Something...",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_visible),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            visualTransformation = PasswordVisualTransformation()
        )

        AppTextField(
            title = "Repeat Password",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .padding(top = 24.dp)
                .align(CenterHorizontally),
            placeholder = "Type Something...",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_visible),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

@Composable
fun SignUpForm4() {
    Column {
        Text(
            text = "Setup your Profile",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(bottom = 24.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Image(
            painter = painterResource(id = R.drawable.setup_profile_empty),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .align(CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        AppTextField(
            title = "Display Name",
            value = "",
            onValueChange = {},
            modifier = Modifier
                .padding(vertical = 24.dp)
                .align(CenterHorizontally),
            placeholder = "Type Something...",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        )

        Text(
            text = "Skip",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            modifier = Modifier
                .align(CenterHorizontally)
                .clickable {

                }
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignUpFormsPreview() {
    ChatTheme {
        Column(Modifier.background(MaterialTheme.colorScheme.background)) {
//            SignUpForm1()
//            Spacer(modifier = Modifier.height(50.dp))
//            Divider()
//            SignUpForm2()
//            Spacer(modifier = Modifier.height(50.dp))
//            Divider()
//            SignUpForm3()
//            Spacer(modifier = Modifier.height(50.dp))
            Divider()
            SignUpForm4()
        }
    }
}
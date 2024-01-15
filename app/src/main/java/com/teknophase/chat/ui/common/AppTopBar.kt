package com.teknophase.chat.ui.common

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.teknophase.chat.navigation.AppNavRoutes
import com.teknophase.chat.providers.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onNavigate: ((String) -> Unit),
    optionsMenu: @Composable (() -> Unit)? = null
) {
    var showOptionMenu by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
            IconButton(onClick = { showOptionMenu = !showOptionMenu }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = showOptionMenu,
                onDismissRequest = { showOptionMenu = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                DropdownMenuItem(text = { Text("Logout", fontSize = 16.sp) }, onClick = {
                    // Handle the logout action
                    // TODO: Make menu dynamic
                    AuthState.logout(context)
                    onNavigate(AppNavRoutes.LOGIN.route)
                })
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    )
}
package com.teknophase.chat.ui.auth

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.teknophase.chat.R
import com.teknophase.chat.util.getFormattedPhoneNumber
import com.teknophase.chat.navigation.AppNavRoutes
import com.teknophase.chat.ui.common.AppTextField
import com.teknophase.chat.ui.constants.CountryCodes
import com.teknophase.chat.ui.constants.FULL_WEIGHT
import com.teknophase.chat.ui.constants.padding_large
import com.teknophase.chat.ui.constants.padding_medium
import com.teknophase.chat.ui.constants.size_120
import com.teknophase.chat.ui.constants.size_150
import com.teknophase.chat.ui.constants.size_200
import com.teknophase.chat.ui.constants.size_250
import com.teknophase.chat.ui.constants.size_50
import com.teknophase.chat.ui.constants.text_normal
import com.teknophase.chat.ui.constants.text_small
import com.teknophase.chat.ui.theme.ChatTheme
import com.teknophase.chat.viewmodel.RegisterViewModel

@Composable
fun SignUpForm1(
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val state = registerViewModel.registerState.collectAsState()
    val localFocusManager = LocalFocusManager.current
    Box {
        var filteredCountries by remember {
            mutableStateOf(CountryCodes.values())
        }
        var search by remember {
            mutableStateOf(state.value.countryCode ?: "")
        }
        var selectedCode: CountryCodes
        var phoneNumber = remember { mutableStateOf("") }
        val pattern = Regex("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?\$")


        Box(modifier = Modifier) {
            if (isExpanded) LazyColumn(
                modifier = Modifier
                    .offset(y = (-50 * (if (filteredCountries.size > 4) 4 else filteredCountries.size)).dp)
                    .width(size_250)
                    .heightIn(max = size_200)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                items(filteredCountries.size) { index ->
                    val country = filteredCountries[index]
                    DropdownMenuItem(
                        modifier = Modifier.height(size_50),
                        onClick = {
                            search = country.code
                            registerViewModel.changeCountryCode(country.code)
                            isExpanded = false
                            localFocusManager.clearFocus()
                            selectedCode = country
                        },
                        text = {
                            Row(horizontalArrangement = Arrangement.SpaceBetween)
                            {
                                Text(
                                    text = "${country.emoji} ${country.countryName}",
                                    modifier = Modifier.weight(
                                        FULL_WEIGHT
                                    )
                                )
                                Text(
                                    text = "${country.code}",
                                    modifier = Modifier,
                                    textAlign = TextAlign.Right
                                )
                            }

                        }
                    )
                }
            }
        }

        Column {

            AppTextField(
                title = stringResource(R.string.country_code),
                value = search,
//                readOnly = true,
                onValueChange = {
                    search = it
                    isExpanded = true
                    val filtered: List<CountryCodes> = CountryCodes.values().filter { country ->
                        country.code.contains(it)
                    }
                    filteredCountries = filtered.toTypedArray()
                },
                singleLine = true,
                modifier = Modifier
                    .width(size_150)
                    .onFocusChanged { isExpanded = it.isFocused },
                placeholder = stringResource(R.string.country_code_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_earth),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = padding_medium)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_down),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = padding_medium)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = search != state.value.countryCode.toString(),
                errorText = stringResource(R.string.invalid_country_code)
            )


            AppTextField(
                title = stringResource(R.string.mobile_number),
                value = state.value.mobileNumber.toString(),
                onValueChange = {
                    registerViewModel.changeMobileNumber(it)
                    phoneNumber.value = getFormattedPhoneNumber(search,it)
                },
                modifier = Modifier
                    .padding(top = padding_large)
                    .align(CenterHorizontally),
                placeholder = stringResource(R.string.mobile_number_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_dialpad),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                isError = !pattern.matches(phoneNumber.value),
                errorText = stringResource(R.string.invalid_phone_number)
            )
        }
    }
}

@Composable
fun SignUpForm2(registerViewModel: RegisterViewModel = hiltViewModel()) {
    val state = registerViewModel.registerState.collectAsState()
    Column {
        AppTextField(
            title = stringResource(R.string.enter_otp),
            value = state.value.otp.toString(),
            onValueChange = { registerViewModel.changeOTP(it) },
            modifier = Modifier
                .padding(top = padding_large)
                .align(CenterHorizontally),
            placeholder = stringResource(R.string.otp_placeholder),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_numbers),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )
    }
}

@Composable
fun SignUpForm3(registerViewModel: RegisterViewModel = hiltViewModel()) {
    val state = registerViewModel.registerState.collectAsState()
    Column {
        AppTextField(
            title = stringResource(id = R.string.title_username),
            value = state.value.username.toString(),
            onValueChange = { registerViewModel.changeUsername(it) },
            modifier = Modifier
                .align(CenterHorizontally),
            placeholder = stringResource(id = R.string.type_something),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = padding_medium)
                )
            },
            singleLine = true,

            )

        AppTextField(
            title = stringResource(id = R.string.title_password),
            value = state.value.password.toString(),
            onValueChange = { registerViewModel.changePassword(it) },
            modifier = Modifier
                .padding(top = padding_large)
                .align(CenterHorizontally),
            placeholder = stringResource(id = R.string.type_something),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_visible),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            visualTransformation = PasswordVisualTransformation()
        )

        AppTextField(
            title = stringResource(R.string.title_repeat_password),
            value = state.value.repeatPassword.toString(),
            onValueChange = { registerViewModel.changeRepeatPassword(it) },
            modifier = Modifier
                .padding(top = padding_large)
                .align(CenterHorizontally),
            placeholder = stringResource(id = R.string.type_something),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_password),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_visible),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

@Composable
fun SignUpForm4(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val state = registerViewModel.registerState.collectAsState()
    Column {
        Text(
            text = stringResource(R.string.setup_your_profile),
            fontSize = text_normal,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(bottom = padding_large),
            color = MaterialTheme.colorScheme.onSurface
        )

        Image(
            painter = painterResource(id = R.drawable.setup_profile_empty),
            contentDescription = null,
            modifier = Modifier
                .size(size_120)
                .clip(CircleShape)
                .align(CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        AppTextField(
            title = stringResource(R.string.title_display_name),
            value = state.value.displayName.toString(),
            onValueChange = { registerViewModel.changeDisplayName(it) },
            modifier = Modifier
                .padding(vertical = padding_large)
                .align(CenterHorizontally),
            placeholder = stringResource(id = R.string.type_something),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_username),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = padding_medium)
                )
            }
        )

        Text(
            text = stringResource(R.string.skip),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = text_small,
            modifier = Modifier
                .align(CenterHorizontally)
                .clickable {
                    onNavigate(AppNavRoutes.BOTTOM_NAVIGATION.route)
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
            SignUpForm4 {}
        }
    }
}
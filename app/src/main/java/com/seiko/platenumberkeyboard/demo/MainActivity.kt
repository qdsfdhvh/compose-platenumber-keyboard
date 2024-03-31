package com.seiko.platenumberkeyboard.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seiko.platenumber.BACK
import com.seiko.platenumber.DEL
import com.seiko.platenumber.KeyVisibleRegistry
import com.seiko.platenumber.KeyboardEngine
import com.seiko.platenumber.MORE
import com.seiko.platenumber.OK
import com.seiko.platenumber.PlateNumberKeyboard
import com.seiko.platenumber.PlateNumberTextField
import com.seiko.platenumber.PlateNumberType
import com.seiko.platenumberkeyboard.demo.theme.PlateNumberKeyboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlateNumberKeyboardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()) {
                        val focusManager = LocalFocusManager.current
                        val softwareKeyboardController = LocalSoftwareKeyboardController.current

                        var plateNumberType by remember { mutableStateOf(PlateNumberType.CIVIL) }
                        var plateNumber by remember { mutableStateOf(TextFieldValue()) }
                        var isKeyboardShown by remember { mutableStateOf(false) }

                        PlateNumberTextField(
                            value = plateNumber,
                            plateNumberType = plateNumberType,
                            isFocused = isKeyboardShown,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .onFocusChanged {
                                    isKeyboardShown = it.hasFocus
                                    if (isKeyboardShown) {
                                        softwareKeyboardController?.hide()
                                    }
                                },
                        )

                        KeyboardArea(
                            isKeyboardShown,
                            onDismissRequest = { focusManager.clearFocus() },
                        ) {
                            var isShowMore by remember { mutableStateOf(false) }
                            val layouts by remember {
                                derivedStateOf {
                                    KeyboardEngine.getLayout(
                                        selectIndex = plateNumber.text.length,
                                        showMoreLayout = isShowMore,
                                        numberType = plateNumberType,
                                    )
                                }
                            }
                            val keyVisibleRegistry by remember {
                                derivedStateOf {
                                    KeyVisibleRegistry.PlateNumber(
                                        numberType = plateNumberType,
                                        selectIndex = plateNumber.text.length,
                                    )
                                }
                            }
                            PlateNumberKeyboard(
                                keyboard = layouts,
                                onItemKeyClicked = { key ->
                                    when (key) {
                                        MORE -> {
                                            isShowMore = true
                                        }

                                        BACK -> {
                                            isShowMore = false
                                        }

                                        OK -> {
                                            focusManager.clearFocus()
                                        }

                                        DEL -> {
                                            if (plateNumber.text.isNotEmpty()) {
                                                plateNumber = TextFieldValue(plateNumber.text.dropLast(1))
                                            }
                                        }

                                        else -> {
                                            plateNumber = TextFieldValue(plateNumber.text + key)
                                        }
                                    }
                                },
                                keyVisibleRegistry = keyVisibleRegistry,
                                contentPadding = PaddingValues(8.dp),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}

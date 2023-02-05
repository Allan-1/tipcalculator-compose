package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipcalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipcalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun TipCalculatorScreen(){
    var amountInput by remember {
        mutableStateOf("")
    }
    var tipInput by remember {
        mutableStateOf("")
    }
    var roundUp by remember {
        mutableStateOf(false)
    }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercentage = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercentage, roundUp)
    val focusManager = LocalFocusManager.current
    Column(Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = stringResource(id = R.string.app_name), fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(value = amountInput, onValueChange = {amountInput = it},
            textLabel = R.string.cost_of_service,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(
            FocusDirection.Down)}))
        EditNumberField(textLabel = R.string.tip_percentage, value = tipInput,
            onValueChange = {tipInput = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()})
        )
        RoundTheTipRow(roundUp = roundUp, onCheckedChange = {
            roundUp = it
        })
        Text(text = stringResource(id = R.string.tip_amount, tip), fontSize = 20.sp,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EditNumberField(@StringRes textLabel: Int, value: String, onValueChange: (String) -> Unit,
                    modifier: Modifier= Modifier, keyboardOptions: KeyboardOptions,
                    keyboardActions: KeyboardActions){
    TextField(
        value = value,
        label = { Text(text = stringResource(id = textLabel), modifier = modifier.fillMaxWidth())},
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true)
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun RoundTheTipRow(modifier: Modifier = Modifier, roundUp: Boolean,
                   onCheckedChange: (Boolean) -> Unit){
    Row(modifier = modifier
        .fillMaxWidth()
        .size(48.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(id = R.string.round_tip))
        Switch(checked = roundUp, onCheckedChange = onCheckedChange,
            colors =SwitchDefaults.colors(
                uncheckedThumbColor = Color.White
            ),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

private fun calculateTip(amount: Double, tipPercentage: Double = 15.0, roundUp: Boolean): String{
    var tip = (tipPercentage/100) * amount
    if (roundUp){
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipcalculatorTheme {
       TipCalculatorScreen()
    }
}
package com.example.calculatorapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _result = MutableStateFlow<String>("")
    val result = _result.asStateFlow()

    fun setResult(result: String){
        _result.value = result
    }

    fun add(n1: Double, n2: Double) {
        _result.value = (n1 + n2).toString()
    }

    fun minus(n1: Double, n2: Double) {
        _result.value = (n1 - n2).toString()
    }

    fun multiply(n1: Double, n2: Double) {
        _result.value = (n1 * n2).toString()
    }

    fun divide(n1: Double, n2: Double) {
        _result.value = (n1 / n2).toString()
    }

    fun percent(n1: Double, n2: Double) {
        _result.value = ((n1 * n2)/100).toString()
    }
}
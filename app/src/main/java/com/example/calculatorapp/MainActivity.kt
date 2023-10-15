package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.calculatorapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import com.example.calculatorapp.Operations.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var operation = ""
    private var n1 = 0.0
    private var n2 = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()

    }

    private fun listeners() {
        with(binding) {
            tvAC.setOnClickListener(this@MainActivity)
            tvDivision.setOnClickListener(this@MainActivity)
            tvPlusMinus.setOnClickListener(this@MainActivity)
            tvEquals.setOnClickListener(this@MainActivity)
            tvMinus.setOnClickListener(this@MainActivity)
            tvMultiply.setOnClickListener(this@MainActivity)
            tvPlus.setOnClickListener(this@MainActivity)
            tvDot.setOnClickListener(this@MainActivity)
            tvPercent.setOnClickListener(this@MainActivity)
            tv1.setOnClickListener(this@MainActivity)
            tv2.setOnClickListener(this@MainActivity)
            tv3.setOnClickListener(this@MainActivity)
            tv4.setOnClickListener(this@MainActivity)
            tv5.setOnClickListener(this@MainActivity)
            tv6.setOnClickListener(this@MainActivity)
            tv7.setOnClickListener(this@MainActivity)
            tv8.setOnClickListener(this@MainActivity)
            tv9.setOnClickListener(this@MainActivity)
            tv0.setOnClickListener(this@MainActivity)
        }
    }

    /*
    This calculate function is called in case if
    operation is used multiple times before clicking equals button
    */
    private fun calculate(result: String, operation: String) {
        var processEnded = false
        when (operation) {
            "+" -> {
                viewModel.add(n1, n2)
                processEnded = true
            }
            "-" -> {
                viewModel.minus(n1, n2)
                processEnded = true
            }
            "÷" -> {
                viewModel.divide(n1, n2)
                processEnded = true
            }
            "×" -> {
                viewModel.multiply(n1, n2)
                processEnded = true
            }
            "=" -> {
                processEnded = true
            }
        }
        if (processEnded) {
            showResult(result, operation)
            processEnded = false
        }
    }


    //This showResult function is called after equals button is clicked
    private fun showResult(result: String, operation: String = "") {

        var res = ""
        if (operation != "=") {
            viewModel.setResult(result)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect() {
                    res = it
                }
            }
        }

        with(binding) {
            if (res.endsWith(".0")) {
                tvResult.text = res.dropLast(2)
            } else {
                tvResult.text = res
            }
        }
    }

    override fun onClick(clickedView: View?) {

        var result = binding.tvResult.text.toString()
        var previousOperator = ""

        fun checkNum1(previousOperation: String = "", operation: String, operator: String, result: String){
            when (operation) {
                PLUS.operator -> {
                    if (operation == operator || previousOperation != "") {
                        n1 += result.toDouble()
                    } else {
                        checkNum1(operation, operation, operator, result)
                    }
                }
                MINUS.operator -> {
                    if (operation == operator || previousOperation != "") {
                        n1 -= result.toDouble()
                    } else {
                        checkNum1(operation, operation, operator, result)
                    }
                }
                MULTIPLY.operator -> {
                    if (operation == operator || previousOperation != "") {
                        n1 *= result.toDouble()
                        println(n1)
                    } else {
                        checkNum1(operation, previousOperation, operator, result)
                    }
                }
                DIVIDE.operator -> {
                    if (operation == operator || previousOperation != "") {
                        n1 /= result.toDouble()
                    } else {
                        checkNum1(operation, operation, operator, result)
                    }
                }
                EQUAL.operator -> {
                    if (previousOperation != operation) {
                        checkNum1(operation, operation, operator, result)
                    }
                }
                "" -> {
                    n1 = result.toDouble()
                }
            }
        }

        with(binding) {
            when (clickedView) {
                tvAC -> {
                    result = ""
                    operation = ""
                    showResult(result, operation)
                }
                tvDivision -> {
                    if (result.isNotEmpty()) {
                        checkNum1(previousOperator, operation, DIVIDE.operator, result)
                        operation = DIVIDE.operator
                        previousOperator = DIVIDE.operator
                        n2 = tvResult.text.toString().toDouble()
                        calculate(result = "", operation)
                    } else {
                    }
                }
                tvMinus -> {
                    if (result.isNotEmpty()) {
                        checkNum1(previousOperator, operation, MINUS.operator, result)
                        operation = MINUS.operator
                        previousOperator = MINUS.operator
                        n2 = tvResult.text.toString().toDouble()
                        calculate(result = "", operation)
                    } else {
                    }
                }
                tvPlus -> {
                    if (result.isNotEmpty()) {
                        checkNum1(previousOperator, operation, PLUS.operator, result)
                        operation = PLUS.operator
                        previousOperator = PLUS.operator
                        n2 = tvResult.text.toString().toDouble()
                        calculate(result = "", operation)
                    } else { }
                }
                tvMultiply -> {
                    if (result.isNotEmpty()) {
                        checkNum1(previousOperator, operation, MULTIPLY.operator, result)
                        operation = MULTIPLY.operator
                        previousOperator = MULTIPLY.operator
                        n2 = tvResult.text.toString().toDouble()
                        calculate(result = "", operation)
                    } else {
                    }
                }
                tvPercent -> {}
                tvEquals -> {
                    if (result.isNotEmpty()) {
                        n2 = tvResult.text.toString().toDouble()
                        when (operation) {
                            PLUS.operator -> {
                                viewModel.add(n1, n2)
                                operation = "="
                                n1 += n2
                                showResult(result, operation)
                            }
                            MINUS.operator -> {
                                viewModel.minus(n1, n2)
                                operation = "="
                                n1 -= n2
                                showResult(result, operation)
                            }
                            MULTIPLY.operator -> {
                                viewModel.multiply(n1, n2)
                                operation = "="
                                n1 *= n2
                                showResult(result, operation)
                            }
                            DIVIDE.operator -> {
                                viewModel.divide(n1, n2)
                                operation = "="
                                n1 /= n2
                                showResult(result, operation)
                            }
                            else -> {}
                        }
                        showResult(result, operation)
                    } else {

                    }
                }
                tvDot -> {
                    if (
                        result.isNotEmpty()
                        && !result.contains(".")
                    ) {
                        result += "."
                        showResult(result, operation)
                    } else {
                    }
                }
                tv0 -> {
                    if (result != "0") {
                        result += "0"
                        showResult(result, operation)
                    } else {
                    }
                }
                tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9 -> {
                    if (clickedView is TextView) {
                        if (operation == "=") {
                            result = ""
                            operation = ""
                        }
                        result += clickedView.text.toString()

                        showResult(result, operation)
                    } else {
                    }
                }
                else -> {}
            }
        }
    }
}
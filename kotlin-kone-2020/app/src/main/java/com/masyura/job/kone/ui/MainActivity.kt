package com.masyura.job.kone.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.epoxy.EpoxyRecyclerView
import com.masyura.job.kone.*

class MainActivity : AppCompatActivity() {

    private val LOG_TAG by lazy { javaClass.simpleName }

    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private val viewModelFactory by lazy {
        MainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<EpoxyRecyclerView>(R.id.main_recycler_view)

        recyclerView.apply {
            withModels(
                buildModels = {
                    hint {
                        id("hint_user_input")
                        text(getString(R.string.hint_user_input))
                    }
                    hint {
                        id("hint_app_output")
                        text(getString(R.string.hint_app_output))
                    }
                    itemInput {
                        id("item_input_side_a")
                        prompt(getString(R.string.input_prompt_side_a))
                        textWatcher(object : TextWatcher {
                            override fun onTextChanged(
                                text: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                viewModel.onSideATextChanged(text)
                            }

                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                            }

                            override fun afterTextChanged(p0: Editable?) {
                            }
                        })
                    }
                    itemInput {
                        id("item_input_side_b")
                        prompt(getString(R.string.input_prompt_side_b))
                        textWatcher(object : TextWatcher {
                            override fun onTextChanged(
                                text: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                viewModel.onSideBTextChanged(text)
                            }

                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                            }

                            override fun afterTextChanged(p0: Editable?) {
                            }
                        })
                    }
                    actionButton {
                        id("action")
                        text(getString(R.string.action_calculate))
                        isActionEnabled(viewModel.canCalculate())
                        onClick { view: View ->
                            hideInputKeyboard(view)
                            viewModel.onActionButtonClicked()
                        }
                    }

                    viewModel.outputMetrics.let { metrics ->
                        if(metrics.any()) {
                            metrics.forEachIndexed { index, metric ->
                                itemOutput {
                                    id("item_output_$index")
                                    title(getString(metric.titleResId))
                                    value(getString(metric.valueFormatResIs, metric.value))
                                }
                            }
                        }
                    }
                }
            )
        }

        viewModel.canCalculateLiveData.observe(this, androidx.lifecycle.Observer { event ->
            event.getContentIfNotHandled()?.let {
                recyclerView.requestModelBuild()
            }
        })

        viewModel.showOutputLiveData.observe(this, androidx.lifecycle.Observer { event ->
            event.getContentIfNotHandled()?.let {
                recyclerView.requestModelBuild()
            }
        })

    }

    private fun hideInputKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


package com.example.expensetracker.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentGraphBinding
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class GraphFragment : DaggerFragment(), OnChartValueSelectedListener {
    @Inject
    lateinit var expenseViewModelFactory: ExpenseViewModelFactory

    private val viewModel: ExpenseViewModel by lazy {
        ViewModelProvider(
            (requireActivity().viewModelStore),
            expenseViewModelFactory
        )[ExpenseViewModel::class.java]
    }

    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private val entries: ArrayList<PieEntry> = ArrayList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.categoryAndAmount.value?.let {


            for (i in it) {
                entries.add(PieEntry(i.categoryAmount.toFloat(), i.category))
            }

        }
        setPieChart()
    }


    private fun setPieChart() {
        val pieChart: PieChart? = binding?.pieChart

        val colors: ArrayList<Int> = ArrayList()
        for (color in ColorTemplate.MATERIAL_COLORS)
            colors.add(color)
        for (color in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(color)
        val dataset: PieDataSet = PieDataSet(entries, "Expenses and Category")
        dataset.colors = colors

        val data: PieData = PieData(dataset)
        data.setDrawValues(false)
        data.setValueFormatter(PercentFormatter(pieChart))


        pieChart?.data = data
        pieChart?.setDrawEntryLabels(false)
        pieChart?.isDrawHoleEnabled = true
        pieChart?.setCenterTextSize(24f)
        pieChart?.centerText = "Expenses by \n Category"
        pieChart?.description = null

        //legend attributes
        val l: Legend? = pieChart?.legend
        l?.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l?.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l?.orientation = Legend.LegendOrientation.VERTICAL
        l?.setDrawInside(false)
        l?.isEnabled = true


        pieChart?.setOnChartValueSelectedListener(this)
        pieChart?.invalidate()

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.d("here", "Im here")
        if (e == null || h == null) {
            Toast.makeText(requireContext(), "empty", Toast.LENGTH_LONG)
            return
        }
        Log.d(
            "VAL SELECTED",
            "Value: " + e.y + ", index: " + h.x
                    + ", DataSet index: " + h.dataSetIndex
        )
        val textView: TextView? = binding?.pieValue
        textView?.text = getString(R.string.pie_chart_text, e.y.toInt(),entries[h.x.toInt()].label.toString())



    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }

}
package com.example.expensetracker.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Half.toFloat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentGraphBinding
import com.example.expensetracker.databinding.FragmentListDisplayBinding
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_graph.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import javax.inject.Inject

class GraphFragment : DaggerFragment(){
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setPieChart()
    }


    private fun setPieChart(){
        val listColors = listOf<Int>(
            R.color.grey,
            R.color.purple_200,
            R.color.teal_200,
            R.color.purple_500,
            R.color.purple_700
        )

        viewModel.categoryAndAmount.observe(viewLifecycleOwner) {

            binding?.run {
                val chart: PieChart? = binding?.pieChart
                if (chart == null)
                    Log.d("PieChart", "Empty returned")
                else {
                    Log.d("PieChart", "Chart not empty")
                }


                if (it != null) {
                    var j: Int = 2
                    var s = it.size
                    for (i in  it) {
                        Log.d("InChart", "${i.category}, ${i.categoryAmount}")
                        chart?.addPieSlice(
                                    PieModel(i.category, i.categoryAmount.toFloat(), listColors[j%5])
                        )
                        j++
                    }
                    chart?.startAnimation()
                }
            }
        }
    }

}
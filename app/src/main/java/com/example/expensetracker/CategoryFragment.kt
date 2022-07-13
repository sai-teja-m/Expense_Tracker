package com.example.expensetracker


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentCategoryBinding
import com.example.expensetracker.databinding.FragmentListDisplayBinding
import com.example.expensetracker.domain.usecase.*
import com.example.expensetracker.viewmodels.ExpenseViewModel
import com.example.expensetracker.viewmodels.ExpenseViewModelFactory
import kotlinx.android.synthetic.main.fragment_list_display.*
import javax.inject.Inject


class CategoryFragment : Fragment() {



    @Inject
    lateinit var expenseViewModelFactory: ExpenseViewModelFactory

    private val viewModel: ExpenseViewModel by activityViewModels {
        expenseViewModelFactory
    }

    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter(::onClick) }

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.run{


            categoryRecycler.layoutManager = LinearLayoutManager(context)

            categoryRecycler.adapter = categoryAdapter

            viewModel.categoryList.observe(viewLifecycleOwner, Observer { categoryAdapter.submitList(it)})
        }
    }

    private fun onClick(str:String){
        viewModel.selectCategory(str)
        findNavController().navigateUp()
    }

}
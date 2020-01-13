package eu.bwbw.decent.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory
import eu.bwbw.decent.databinding.FragmentToolsBinding
import kotlinx.android.synthetic.main.fragment_tools.*

class ToolsFragment : Fragment() {

    private lateinit var viewModel: ToolsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentToolsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tools,
            container,
            false
        )


        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(
            ToolsViewModel::class.java
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signOut.setOnClickListener {
            viewModel.signOut()
            val directions: NavDirections =
                ToolsFragmentDirections.actionToolsFragmentToWelcomeFragment()
            view.findNavController().navigate(directions)
        }

        withdrawMoney.setOnClickListener {
            viewModel.withDrawMoney()
            val infoSnackbar = Snackbar.make(
                it,
                R.string.withdraw_confirmation,
                Snackbar.LENGTH_LONG
            )
            infoSnackbar.show()
        }
    }
}

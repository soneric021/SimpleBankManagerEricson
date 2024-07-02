package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransferFundsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransferFundsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var transferFundsAccountEditText: EditText
    private lateinit var transferFundsAmountEditText: EditText
    private lateinit var transferFundsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(this, object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               findNavController().navigateUp()
            }

        });
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer_funds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transferFundsAccountEditText = view.findViewById(R.id.transferFundsAccountEditText)
        transferFundsAmountEditText = view.findViewById(R.id.transferFundsAmountEditText)
        transferFundsButton = view.findViewById(R.id.transferFundsButton)
        val balance = (activity as? MainActivity)?.balance ?: 100.0


        transferFundsButton.setOnClickListener {
            val account = transferFundsAccountEditText.text.toString()
            val amount = transferFundsAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
            var isValid = true
            if(!useRegex(account)){
                transferFundsAccountEditText.error = "Invalid account number"
                isValid = false
            }

            if(amount <= 0) {
                transferFundsAmountEditText.error = "Invalid amount"
                isValid = false
            }
            val formattedNumber = String.format(Locale.getDefault(), "%.2f", amount)
            if (amount > balance){
                Toast.makeText(requireContext(), "Not enough funds to transfer \$$formattedNumber", Toast.LENGTH_SHORT).show()
            }
            if (isValid && amount <= balance){
                Toast.makeText(requireContext(), "Transferred \$$formattedNumber to account $account", Toast.LENGTH_SHORT).show()
                (activity as? MainActivity)?.balance = balance - amount
                findNavController().navigateUp()
            }
        }
    }

    fun useRegex(input: String): Boolean {
        val regex = Regex(pattern = "(ca|sa)\\d\\d\\d\\d", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(input)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransferFundsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransferFundsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
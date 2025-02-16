package com.mobile.app.lincride.requestRide

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobile.app.lincride.databinding.FragmentConfirmRideBinding
import com.mobile.app.lincride.home.DialogCallback
import com.mobile.app.lincride.models.CustomPlacesModel
import com.mobile.app.lincride.utility.parcelable
import com.mobile.app.lincride.utility.toast
import com.mobile.app.lincride.utility.toggleVisibility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val OBJECT_CONFIRM_RIDE_SOURCE = "OBJECT_CONFIRM_RIDE_SOURCE"
private const val OBJECT_CONFIRM_RIDE_DESTINATION = "OBJECT_CONFIRM_RIDE_DESTINATION"
@AndroidEntryPoint
class ConfirmRideFragment: BottomSheetDialogFragment() {

    private val viewModel: ConfirmRideViewModel by viewModels()

    private var listener: DialogCallback? = null
    private lateinit var binding: FragmentConfirmRideBinding
    private val sourceLocation: CustomPlacesModel? by lazy { arguments?.parcelable(OBJECT_CONFIRM_RIDE_SOURCE) }
    private val destinationLocation: CustomPlacesModel? by lazy { arguments?.parcelable(OBJECT_CONFIRM_RIDE_DESTINATION) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is DialogCallback)
            listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmRideBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        if(sourceLocation != null && destinationLocation != null){
            viewModel.getFareAmount(sourceLocation!! to destinationLocation!!)

            binding.acceptBtn.setOnClickListener {
                listener?.onStartRide(viewModel.requestId)
                dialog?.dismiss()
            }
        }else{
            toast("The source or destination locations is not valid")
        }

        viewModel.error.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                toast(it)
            }
        }

        viewModel.estimate.observe(viewLifecycleOwner){
            binding.fareEstimateTxt.text = it.first
            binding.distanceTxt.text = it.second
            binding.trafficTxt.toggleVisibility(it.third)
            binding.surgeTxt.toggleVisibility(it.fourth)
        }

        binding.cancelBtn.setOnClickListener {
            listener?.onCancelRide()
            dialog?.dismiss()
        }
    }

    companion object{
        fun newInstance(source: CustomPlacesModel, destination: CustomPlacesModel) = ConfirmRideFragment().apply {
            arguments = Bundle().apply {
                putParcelable(OBJECT_CONFIRM_RIDE_SOURCE, source)
                putParcelable(OBJECT_CONFIRM_RIDE_DESTINATION, destination)
            }
        }
    }
}

package com.mobile.app.lincride.rideStarted

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobile.app.lincride.databinding.FragmentRideStartedBinding
import com.mobile.app.lincride.home.DialogCallback
import com.mobile.app.lincride.models.request.RideRequest
import com.mobile.app.lincride.utility.toast
import dagger.hilt.android.AndroidEntryPoint

private const val OBJECT_RIDE_STARTED = "OBJECT_RIDE_STARTED"
private const val OBJECT_RIDE_STARTED_REQUEST = "OBJECT_RIDE_STARTED_REQUEST"
@AndroidEntryPoint
class RideStartedDialog: BottomSheetDialogFragment() {

    private val viewModel: RideStartedViewModel by viewModels()

    private lateinit var binding: FragmentRideStartedBinding
    private var listener: DialogCallback? = null
    private val destination: String? by lazy { arguments?.getString(OBJECT_RIDE_STARTED) }
    private val requestId: Int? by lazy { arguments?.getInt(OBJECT_RIDE_STARTED_REQUEST) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is DialogCallback)
            listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRideStartedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        viewModel.requestRide(RideRequest(requestId = null))
        binding.destTxt.text = "Destination: ${destination}"
        binding.cancelBtn.setOnClickListener {
            listener?.onCancelRide()
            dialog?.dismiss()
        }

        viewModel.error.observe(viewLifecycleOwner){
            toast(it)
        }

        viewModel.response.observe(viewLifecycleOwner){
            binding.driverText.text = "Driver: ${it.driver?.name}"
            binding.carTxt.text = "Car: ${it.driver?.car}"
            binding.plateNumberTxt.text = "Plate Number: ${it.driver?.plateNumber}"
            listener?.onRideStarted(requestId = requestId, driverId = it.driver?.driverId)
        }
    }

    companion object{
        fun newInstance(destination: String, requestId: Int) = RideStartedDialog().apply {
            arguments = Bundle().apply {
                putString(OBJECT_RIDE_STARTED, destination)
                putInt(OBJECT_RIDE_STARTED_REQUEST, requestId)
            }
        }
    }
}

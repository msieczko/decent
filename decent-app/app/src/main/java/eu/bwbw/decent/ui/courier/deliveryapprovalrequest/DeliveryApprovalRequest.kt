package eu.bwbw.decent.ui.courier.deliveryapprovalrequest

import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.core.util.valueIterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.snackbar.Snackbar
import eu.bwbw.decent.R
import eu.bwbw.decent.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_delivery_approval_request.*
import java.io.IOException
import kotlin.math.roundToInt


class DeliveryApprovalRequest : Fragment() {

    private lateinit var viewModel: DeliveryApprovalRequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_delivery_approval_request, container, false)

        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance())
            .get(DeliveryApprovalRequestViewModel::class.java)

        viewModel.deliveryRegistered.observe(this, Observer {
            val infoSnackbar = Snackbar.make(
                root,
                if (it) R.string.delivery_success else R.string.delivery_failed,
                Snackbar.LENGTH_LONG
            )
            infoSnackbar.show()
        })

        val cameraView = root.findViewById<SurfaceView>(R.id.cameraView)

        val display: Display? = activity?.windowManager?.defaultDisplay
        val size = Point(400, 400)
        display?.getSize(size)

        val barcodeDetector = BarcodeDetector.Builder(context)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        val cameraSource = CameraSource.Builder(context, barcodeDetector)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize((size.x / 1.5).roundToInt(), (size.y / 1.5).roundToInt())
            .setRequestedFps(15.0f)
            .setAutoFocusEnabled(true)
            .build()

        barcodeDetector.setProcessor(
            object : Detector.Processor<Barcode> {
                override fun release() {
                }

                override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                    processDetections(detections, cameraSource, barcodeDetector)
                }
            }
        )

        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
            }

            override fun surfaceDestroyed(p0: SurfaceHolder?) {
                cameraSource.stop()
            }

            override fun surfaceCreated(p0: SurfaceHolder?) {
                try {
                    cameraSource.start(cameraView.holder)
                    p0?.setFixedSize(
                        cameraSource.previewSize.width,
                        cameraSource.previewSize.height
                    )
                } catch (e: IOException) {
                    println("error: $e")
                }
            }

        })

        return root
    }

    private fun processDetections(
        detections: Detector.Detections<Barcode>?,
        cameraSource: CameraSource,
        barcodeDetector: BarcodeDetector
    ) {
        val detectedValue = detections?.run {
            detectedItems.valueIterator()
                .asSequence()
                .map { it.displayValue }
                .toList()
                .firstOrNull()
        }

        if (detectedValue != null) {
            activity?.runOnUiThread {
                viewModel.deliverPackage(detectedValue)
                barcodeDetector.release()
                cameraSource.release()
                cameraView.visibility = View.GONE
            }
        }
    }


}

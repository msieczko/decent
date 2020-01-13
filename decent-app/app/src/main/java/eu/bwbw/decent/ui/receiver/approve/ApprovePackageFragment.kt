package eu.bwbw.decent.ui.receiver.approve

import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import eu.bwbw.decent.R
import eu.bwbw.decent.ui.receiver.approve.ApprovePackageFragmentArgs


class ApprovePackageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_approve_package, container, false)

        arguments?.let {
            val safeArgs =
                ApprovePackageFragmentArgs.fromBundle(it)
            val text = safeArgs.deliveryTitle

            val qrCodeImageView = root.findViewById<ImageView>(R.id.qrCodeImage)
            showQrCode(text, qrCodeImageView)
        }
        return root
    }

    private fun showQrCode(text: String, qrCodeImageView: ImageView) {
        val dimension = getQrCodeSize()

        val multiFormatWriter = MultiFormatWriter()
        try {

            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, dimension, dimension)
            val bitmap = createBitmap(bitMatrix)
            qrCodeImageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun getQrCodeSize(): Int {
        val display: Display? = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val dimension = (Math.min(size.x, size.y) / 1.3).toInt()
        return dimension
    }

    private fun createBitmap(matrix: BitMatrix): Bitmap? {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (matrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}

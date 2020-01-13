package eu.bwbw.decent.utils

import org.web3j.utils.Convert
import org.web3j.utils.Convert.fromWei
import org.web3j.utils.Convert.toWei
import java.math.BigDecimal
import java.math.MathContext

fun weiToString(wei: BigDecimal): String {
    val minMWei = toWei(BigDecimal(0.1), Convert.Unit.MWEI)
    val minSzabo = toWei(BigDecimal(0.1), Convert.Unit.SZABO)
    val minEther = toWei(BigDecimal(0.1), Convert.Unit.ETHER)
    val r = MathContext(3)

    when {
        wei < minMWei -> {
            return "$wei Wei"
        }
        wei < minSzabo -> {
            val rounded = fromWei(wei, Convert.Unit.MWEI).round(r).toPlainString()
            return "$rounded MWei"
        }
        wei < minEther -> {
            val rounded = fromWei(wei, Convert.Unit.SZABO).round(r).toPlainString()
            return "$rounded Szabo"
        }
        else -> {
            val rounded = fromWei(wei, Convert.Unit.ETHER).round(r).toPlainString()
            return "$rounded ETH"
        }
    }
}
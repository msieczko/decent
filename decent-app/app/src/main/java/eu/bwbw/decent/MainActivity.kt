package eu.bwbw.decent

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import eu.bwbw.decent.contracts.generated.Greeter
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    fun testWeb3() {
        val web3j = Web3j.build(
            HttpService(
                "https://rinkeby.infura.io/9749d9a7675445bbb291333162af6f95"
            )
        )  // FIXME: Enter your Infura token here;
        println("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().web3ClientVersion)

        // We then need to load our Ethereum wallet file
        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
        val credentials = Credentials.create(
            "1c1b8e4c1d8fa9eaf16e1abb16505fd2046cc4624784a065b7db5cd673b2aea4"
        )
        println("Credentials loaded")

        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
        println(
            "Sending 1 Wei ("
                    + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)"
        )
        val transferReceipt = Transfer.sendFunds(
            web3j, credentials,
            "0x19e03255f667bdfd50a32722df860b1eeaf4d635", // you can put any address here
            BigDecimal.ONE, Convert.Unit.WEI
        )  // 1 wei = 10^-18 Ether
            .send()
        println(("Transaction complete, view it at https://rinkeby.etherscan.io/tx/" + transferReceipt.transactionHash))

        // Now lets deploy a smart contract
        println("Deploying smart contract")
        val contractGasProvider = DefaultGasProvider()
        val contract = Greeter.deploy(
            web3j,
            credentials,
            contractGasProvider,
            "test"
        ).send()

        val contractAddress = contract.contractAddress
        println("Smart contract deployed to address $contractAddress")
        println("View contract at https://rinkeby.etherscan.io/address/$contractAddress")

        println("Value stored in remote smart contract: " + contract.greet().send())

        // Lets modify the value in our smart contract
        val transactionReceipt = contract.newGreeting("Well hello again").send()

        println("New value stored in remote smart contract: " + contract.greet().send())

        // Events enable us to log specific events happening during the execution of our smart
        // contract to the blockchain. Index events cannot be logged in their entirety.
        // For Strings and arrays, the hash of values is provided, not the original value.
        // For further information, refer to https://docs.web3j.io/filters.html#filters-and-events
        for (event in contract.getModifiedEvents(transactionReceipt)) {
            println(
                ("Modify event fired, previous value: " + event.oldGreeting
                        + ", new value: " + event.newGreeting)
            )
            println(
                ("Indexed event previous value: " + Numeric.toHexString(event.oldGreetingIdx)
                        + ", new value: " + Numeric.toHexString(event.newGreetingIdx))
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val thread = Thread(Runnable {
            try {
                testWeb3()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

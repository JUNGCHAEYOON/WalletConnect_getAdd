package dev.pinkroom.sample.walletconnectkit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dev.pinkroom.sample.walletconnectkit.databinding.ActivityMainBinding
import dev.pinkroom.walletconnectkit.WalletConnectKit
import dev.pinkroom.walletconnectkit.WalletConnectKitConfig
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding

    // addressTxt 가 현재 연결된 이더리움 주소
    var addressTxt : String = ""

    private val config by lazy {
        WalletConnectKitConfig(
            context = this,
            bridgeUrl = "wss://bridge.aktionariat.com:8887",
            appUrl = "walletconnectkit.com",
            appName = "WalletConnect Kit",
            appDescription = "This is the Swiss Army toolkit for WalletConnect!"
        )
    }
    private val walletConnectKit by lazy { WalletConnectKit.Builder(config).build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        walletConnectKit.address?.let {
            menuInflater.inflate(R.menu.toolbar_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.disconnectView -> onDisconnectClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        initLoginView()
//        initPerformTransactionView()
    }

    private fun initLoginView() = with(binding) {
        loginView.start(walletConnectKit, ::onConnected, ::onDisconnected)
    }

    private fun onConnected(address: String) = with(binding) {
        loginView.visibility = View.GONE
        connectedView.visibility = View.VISIBLE
        connectedAddressView.text = getString(R.string.connected_with, address)
        addressTxt = getString(R.string.connected_with, address)
        invalidateOptionsMenu()
    }

    private fun onDisconnected() = with(binding) {
        loginView.visibility = View.VISIBLE
        connectedView.visibility = View.GONE
        invalidateOptionsMenu()
    }
//    //송금기능 구현X
//    private fun initPerformTransactionView() = with(binding) {
//        performTransactionView.setOnClickListener {
//            val toAddress = toAddressView.text.toString()
//            val value = valueView.text.toString()
//            lifecycleScope.launch {
//                runCatching { walletConnectKit.performTransaction(toAddress, value) }
//                    .onSuccess { showMessage("Transaction done!") }
//                    .onFailure { showMessage(it.message ?: it.toString()) }
//            }
//        }
//    }

    private fun onDisconnectClicked() {
        walletConnectKit.removeSession()
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
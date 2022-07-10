package com.qnape.instaking.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.qnape.instaking.R
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PayPalButton
import com.qnape.instaking.databinding.ActivityCompleteSearviceBinding
import com.qnape.instaking.util.Constant
import com.qnape.instaking.util.Preferences
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import dev.shreyaspatil.easyupipayment.model.TransactionStatus


class CompleteSearviceActivity : AppCompatActivity(), PaymentStatusListener {
    var binding: ActivityCompleteSearviceBinding? = null
    var TYPE_TITLE: String = ""
    var SERVICE_ID: Int = 0
    var minimum = 20
    val TAG = "CompleteService"
    val YOUR_CLIENT_ID = ""
    var environment = false
    private var easyUpiPayment: EasyUpiPayment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteSearviceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val config = CheckoutConfig(
            application = application,
            clientId = YOUR_CLIENT_ID,
            environment = Environment.SANDBOX,
            returnUrl = String.format("%s://paypalpay","$packageName"),
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true,
                false
            )
        )
        PayPalCheckout.setConfig(config)

        TYPE_TITLE = intent.getStringExtra("type").toString()
        binding?.TitleTv?.text = TYPE_TITLE
        val myFollowersStrings = arrayOf("Select Service", "Instagram Followers")
        val myLikesStrings = arrayOf("Select Service", "Instagram Likes")
        val myViewsStrings = arrayOf("Select Service", "Instagram Views")
        val myReelViewsStrings = arrayOf("Select Service", "Instagram Reel Views")
        val myReelLikesStrings = arrayOf("Select Service", "Instagram Reel Likes")
        val myCommentsStrings = arrayOf("Select Service", "Instagram Comments")
        binding?.backBtn?.setOnClickListener {
            onBackPressed()
        }
        when (TYPE_TITLE) {
            "Followers" -> {
                setSpinner(binding?.planSpinner!!, myFollowersStrings)
                SERVICE_ID = Constant.FollowersServiceId
                minimum = 100
            }
            "Likes" -> {
                setSpinner(binding?.planSpinner!!, myLikesStrings)
                SERVICE_ID = Constant.LikesServiceId
                minimum = 500
            }
            "Views" -> {
                setSpinner(binding?.planSpinner!!, myViewsStrings)
                SERVICE_ID = Constant.ViewsServiceId
                minimum = 1000
            }
            "Reel Views" -> {
                setSpinner(binding?.planSpinner!!, myReelViewsStrings)
                SERVICE_ID = Constant.ReelViewsServiceId
                minimum = 1000
            }
            "Reel Likes" -> {
                setSpinner(binding?.planSpinner!!, myReelLikesStrings)
                SERVICE_ID = Constant.ReelLikesServiceId
                minimum = 500
            }
            "Comments" -> {
                setSpinner(binding?.planSpinner!!, myCommentsStrings)
                SERVICE_ID = Constant.CommentServiceId
                minimum = 20
            }
        }
        binding?.maxMinTv?.text = "Min. $minimum and Max. 100000"

        binding?.paymentBtn?.setOnClickListener {
            if (TextUtils.isEmpty(binding?.linked?.text)) {
                Toast.makeText(applicationContext, "Please enter Username!", Toast.LENGTH_LONG)
                    .show()
            } else if (binding?.planSpinner?.selectedItem.toString().startsWith("Select")) {
                Toast.makeText(applicationContext, "Please Select plan Type!", Toast.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding?.requiredEd?.text)) {
                Toast.makeText(applicationContext, "Please enter quantity!", Toast.LENGTH_LONG)
                    .show()
            } else if ((binding?.requiredEd?.text?.toString()?.toInt() ?: 0) < minimum) {
                Toast.makeText(
                    applicationContext,
                    "Please enter valid quantity!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                bottomDialog()
            }
        }

        binding?.requiredEd?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s)) {
                    binding?.textCountShow?.text = "${(6 - s.toString().length)}/6"
                    if (binding?.planSpinner?.selectedItem.toString().endsWith("Followers")) {
                        binding?.priceTV?.text =
                            "₹${(((Constant.FOLLOWERS * (s.toString().toInt())) / 100).toString())}"
                    } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Likes")) {
                        binding?.priceTV?.text =
                            "₹${(((Constant.LIKES * (s.toString().toInt())) / 100).toString())}"
                    } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Views")) {
                        binding?.priceTV?.text =
                            "₹${(((Constant.VIEWS * (s.toString().toInt())) / 100).toString())}"
                    } else if (binding?.planSpinner?.selectedItem.toString()
                            .endsWith("Reel Views")
                    ) {
                        binding?.priceTV?.text = "₹${
                            (((Constant.REEL_VIEWS * (s.toString().toInt())) / 100).toString())
                        }"
                    } else if (binding?.planSpinner?.selectedItem.toString()
                            .endsWith("Reel Likes")
                    ) {
                        binding?.priceTV?.text = "₹${
                            (((Constant.REEL_LIKES * (s.toString().toInt())) / 100).toString())
                        }"
                    } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Comments")) {
                        binding?.priceTV?.text =
                            "₹${(((Constant.COMMENTS * (s.toString().toInt())) / 100).toString())}"
                    }
                } else {
                    binding?.priceTV?.text = "₹0.0"
                }
            }
        })

    }

    private fun upiPaymentProcess() {
        if (binding?.planSpinner?.selectedItem.toString().endsWith("Followers")) {
            callpaymentMethod(Constant.FOLLOWERS)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Likes")) {
            callpaymentMethod(Constant.LIKES)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Views")) {
            callpaymentMethod(Constant.VIEWS)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Reel Views")) {
            callpaymentMethod(Constant.REEL_VIEWS)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Reel Likes")) {
            callpaymentMethod(Constant.REEL_LIKES)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Comments")) {
            callpaymentMethod(Constant.COMMENTS)
        }
    }

    fun paypalPayment(): Int {
        if (binding?.planSpinner?.selectedItem.toString().endsWith("Followers")) {
            return Constant.FOLLOWERS
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Likes")) {
            return (Constant.LIKES)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Views")) {
            return (Constant.VIEWS)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Reel Views")) {
            return (Constant.REEL_VIEWS)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Reel Likes")) {
            return (Constant.REEL_LIKES)
        } else if (binding?.planSpinner?.selectedItem.toString().endsWith("Comments")) {
            return (Constant.COMMENTS)
        }
        return  0
    }

    fun bottomDialog(){
        val mBottomSheetDialog = BottomSheetDialog(this@CompleteSearviceActivity)
        val sheetView = layoutInflater.inflate(R.layout.payment_dialog,null)
        mBottomSheetDialog.setContentView(sheetView!!)
        val paypal = sheetView.findViewById<CardView>(R.id.paypalBtn)
        val upi = sheetView.findViewById<CardView>(R.id.upiBtn)
        val payPalButton = sheetView.findViewById<PayPalButton>(R.id.payPalButton)
        upi.setOnClickListener {
            upiPaymentProcess()
        }
        val amount = "${((((paypalPayment() * (binding?.requiredEd?.text.toString().toInt())))) / 100)}.00"
        payPalButton.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = amount)
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    toast("Success")
                    val userId =
                        Preferences.getIntString(applicationContext, Preferences.PreferencesKey.UserID.name, 0)
                    if (userId != null) {
                        setData(
                            userId.toString(),
                            SERVICE_ID,
                            binding?.requiredEd?.text.toString(),
                            binding?.linked?.text.toString()
                        )
                        showCustomDialog()
                    }
                }
            },

            onCancel = OnCancel {
                Log.d("OnCancel", "Buyer canceled the PayPal experience.")
            },

            onError = OnError{errorInfo->
                Log.d("OnError", "Error: $errorInfo")
            }

        )

        paypal.setOnClickListener {
            payPalButton.performClick()
        }
    }

    private fun callpaymentMethod(service: Int) {
        val amount = "${((((service * (binding?.requiredEd?.text.toString().toInt())))) / 100)}.00"
//        pay(amount)
        val description = "Insta king Payment"
        val tId = System.currentTimeMillis()
        val tRId = System.currentTimeMillis()
        val payeeVpa = Constant.UPI
        val payeeName = "Insta King"
        val easyUpiPayment = EasyUpiPayment(this@CompleteSearviceActivity) {
            this.payeeVpa = payeeVpa
            this.payeeName = payeeName
            this.payeeMerchantCode = "4344"
            this.transactionId = "T$tId"
            this.transactionRefId = "TR_ID$tRId"
            this.description = description
            this.amount = amount
        }
        easyUpiPayment.startPayment()
        easyUpiPayment.setPaymentStatusListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        easyUpiPayment?.removePaymentStatusListener()
    }

    private fun setSpinner(mySpinner: Spinner, myStrings: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, myStrings)
        mySpinner.adapter = adapter
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
        }

    }

    private fun setData(userId: String, service: Int, quantity: String, link: String) {
        val queue = Volley.newRequestQueue(applicationContext)
        val url =
            "https://makepopularonsocialmedia.xyz/insta.php?client_id=$userId&service_id=$service&order_quantity=$quantity&order_url=$link"
        val sr: StringRequest =
            object : StringRequest(
                Request.Method.GET, url,
                Response.Listener { response ->
                    Log.e(
                        "HttpClient",
                        "success! response: $response"
                    )
                    Toast.makeText(
                        applicationContext,
                        "Payment Successful! please wait 2 to 5 hours for better result",
                        Toast.LENGTH_LONG
                    ).show()
                },
                Response.ErrorListener { error ->
                    Log.e(
                        "HttpClient",
                        "error: ${error.message}"
                    )
                }) {

            }
        queue.add(sr)
    }

    private fun showCustomDialog() {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(this@CompleteSearviceActivity)
                .inflate(R.layout.success_dialog, viewGroup, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@CompleteSearviceActivity)
        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()
        dialogView.findViewById<Button>(R.id.buttonOk).setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onTransactionCancelled() {
        Log.d(TAG, "onTransactionCancelled: ")
        Toast.makeText(applicationContext, "Payment Cancelled", Toast.LENGTH_LONG).show()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        Log.d(
            TAG,
            "onTransactionCompleted: ${transactionDetails?.transactionId}, ${transactionDetails?.transactionRefId}"
        )

        when (transactionDetails.transactionStatus) {
            TransactionStatus.SUCCESS -> onTransactionSuccess()
            TransactionStatus.FAILURE -> onTransactionFailed()
            TransactionStatus.SUBMITTED -> onTransactionSubmitted()
        }


    }

    private fun onTransactionSuccess() {
        // Payment Success
        toast("Success")
        val userId =
            Preferences.getIntString(applicationContext, Preferences.PreferencesKey.UserID.name, 0)
        if (userId != null) {
            setData(
                userId.toString(),
                SERVICE_ID,
                binding?.requiredEd?.text.toString(),
                binding?.linked?.text.toString()
            )
            showCustomDialog()
        }

    }

    private fun onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted")

    }

    private fun onTransactionFailed() {
        // Payment Failed
        toast("TransactionFailed")

    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
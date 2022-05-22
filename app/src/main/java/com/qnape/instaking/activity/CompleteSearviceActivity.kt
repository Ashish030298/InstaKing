package com.qnape.instaking.activity

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.qnape.instaking.databinding.ActivityCompleteSearviceBinding
import com.qnape.instaking.util.Constant
import com.qnape.instaking.util.Preferences
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import java.util.*


class CompleteSearviceActivity : AppCompatActivity(), PaymentStatusListener {
    var binding: ActivityCompleteSearviceBinding? = null
    var TYPE_TITLE: String = ""
    var SERVICE_ID: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteSearviceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        TYPE_TITLE = intent.getStringExtra("type").toString()
        binding?.TitleTv?.text = TYPE_TITLE
        val myFollowersStrings = arrayOf("Select Service","Instagram Followers")
        val myLikesStrings = arrayOf("Select Service","Instagram Likes")
        val myViewsStrings = arrayOf("Select Service","Instagram Views")
        val myReelViewsStrings = arrayOf("Select Service","Instagram Reel Views")
        val myReelLikesStrings = arrayOf("Select Service","Instagram Reel Likes")
        val myCommentsStrings = arrayOf("Select Service","Instagram Comments")
        binding?.backBtn?.setOnClickListener {
            onBackPressed()
        }
        when(TYPE_TITLE){
            "Followers"->{setSpinner(binding?.planSpinner!!, myFollowersStrings)
            SERVICE_ID = Constant.FollowersServiceId}
            "Likes"->{setSpinner(binding?.planSpinner!!, myLikesStrings)
            SERVICE_ID = Constant.LikesServiceId}
            "Views"->{setSpinner(binding?.planSpinner!!, myViewsStrings)
            SERVICE_ID = Constant.ViewsServiceId}
            "Reel Views"->{setSpinner(binding?.planSpinner!!, myReelViewsStrings)
            SERVICE_ID = Constant.ReelViewsServiceId}
            "Reel Likes"->{setSpinner(binding?.planSpinner!!, myReelLikesStrings)
            SERVICE_ID = Constant.REEL_LIKES}
            "Comments"->{setSpinner(binding?.planSpinner!!, myCommentsStrings)
            SERVICE_ID = Constant.CommentServiceId}
        }





        binding?.paymentBtn?.setOnClickListener {
            if (TextUtils.isEmpty(binding?.linked?.text)){
                Toast.makeText(applicationContext, "Please enter Instagram Link!", Toast.LENGTH_LONG).show()
            }else if (binding?.planSpinner?.selectedItem.toString().startsWith("Select")){
                Toast.makeText(applicationContext, "Please Select plan Type!", Toast.LENGTH_LONG).show()
            }else if (TextUtils.isEmpty(binding?.requiredEd?.text)){
                Toast.makeText(applicationContext, "Please enter quantity!", Toast.LENGTH_LONG).show()
            }else if ((binding?.requiredEd?.text?.length ?: 0) <= 2){
                Toast.makeText(applicationContext, "Please enter valid quantity!", Toast.LENGTH_LONG).show()
            }else{
                if (binding?.planSpinner?.selectedItem.toString().endsWith("Followers")) {
                    callpaymentMethod(Constant.FOLLOWERS)
                }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Likes")){
                    callpaymentMethod(Constant.LIKES)
                }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Views")){
                    callpaymentMethod(Constant.VIEWS)
                }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Reel Views")){
                    callpaymentMethod(Constant.REEL_VIEWS)
                }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Reel Likes")){
                    callpaymentMethod(Constant.REEL_LIKES)
                }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Comments")){
                    callpaymentMethod(Constant.comments)
                }
            }
        }

        binding?.requiredEd?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding?.textCountShow?.text = "${(6 - s.toString().length)}/6"
                if (s.toString().length >= 3){
                    if (binding?.planSpinner?.selectedItem.toString().endsWith("Followers")) {
                        binding?.priceTV?.text = "₹${(((Constant.FOLLOWERS *(s.toString().toInt()))/100).toString())}"
                    }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Likes")){
                        binding?.priceTV?.text = "₹${(((Constant.LIKES *(s.toString().toInt()))/100).toString())}"
                    }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Views")){
                        binding?.priceTV?.text = "₹${(((Constant.VIEWS *(s.toString().toInt()))/100).toString())}"
                    }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Reel Views")){
                        binding?.priceTV?.text = "₹${(((Constant.REEL_VIEWS *(s.toString().toInt()))/100).toString())}"
                    }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Reel Likes")){
                        binding?.priceTV?.text = "₹${(((Constant.REEL_LIKES *(s.toString().toInt()))/100).toString())}"
                    }else if (binding?.planSpinner?.selectedItem.toString().endsWith("Comments")){
                        binding?.priceTV?.text = "₹${(((Constant.comments *(s.toString().toInt()))/100).toString())}"
                    }
                }
            }
        })

    }

    private fun callpaymentMethod(service: Int) {
        val amount = "${((((service * (binding?.requiredEd?.text.toString().toInt()))))/100)}.00"
        val easyUpiPayment = EasyUpiPayment.Builder()
            .with(this) // on below line we are adding upi id.
            .setPayeeVpa(Constant.UPI)
            .setTransactionId(UUID.randomUUID().toString().substring(0,13))
            .setTransactionRefId(UUID.randomUUID().toString())
            .setDescription("InstaKing services")
            .setPayeeName("Insta King")
            .setAmount(amount)
            .build()
        easyUpiPayment.startPayment()
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        easyUpiPayment.setPaymentStatusListener(this)
    }

    private fun setSpinner(mySpinner: Spinner, myStrings: Array<String>) {
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, myStrings)
        mySpinner.adapter=adapter
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }

    }

    private fun setData(userId: String, service: Int, quantity: String, link: String) {
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "https://makepopularonsocialmedia.xyz/insta.php?client_id=$userId&service_id=$service&order_quantity=$quantity&order_url=$link"
        val sr: StringRequest =
            object : StringRequest(
                Request.Method.GET, url,
                Response.Listener { response ->
                    Log.e(
                        "HttpClient",
                        "success! response: $response"
                    )
                    Toast.makeText(applicationContext, "Payment Successful! please wait 2 to 5 hours for better result", Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error -> Log.e("HttpClient", "error: ${error.message}") }) {

            }
        queue.add(sr)
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        val transcDetails = """
            ${transactionDetails?.status.toString()}
            Transaction ID : ${transactionDetails?.transactionId}
            """.trimIndent()
        Log.d("TAG", "onTransactionCompleted: $transcDetails")
        val userId = Preferences.getSavedString(applicationContext, Preferences.PreferencesKey.UserID.name,"")
        if (userId != null) {
            setData(userId, SERVICE_ID, binding?.requiredEd?.text.toString(), binding?.linked?.text.toString())
        }
        //transactionDetailsTV.setVisibility(View.VISIBLE)
        // on below line we are setting details to our text view.
        // on below line we are setting details to our text view.
        //transactionDetailsTV.setText(transcDetails)
    }

    override fun onTransactionSuccess() {
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show()
    }

    override fun onTransactionSubmitted() {
        Log.e("TAG", "TRANSACTION SUBMIT");
    }

    override fun onTransactionFailed() {
        Toast.makeText(applicationContext, "Transaction Failed..!", Toast.LENGTH_LONG).show()
    }

    override fun onTransactionCancelled() {
        Toast.makeText(applicationContext, "Transaction Cancel..!", Toast.LENGTH_LONG).show()
    }

    override fun onAppNotFound() {
        Toast.makeText(applicationContext, "No app found for making transaction..!", Toast.LENGTH_LONG).show()
    }
}
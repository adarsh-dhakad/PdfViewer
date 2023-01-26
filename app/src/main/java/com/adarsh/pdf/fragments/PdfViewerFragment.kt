package com.adarsh.pdf.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.adarsh.pdf.databinding.FragmentPdfViewerBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.shockwave.pdfium.PdfPasswordException


class PdfViewerFragment : Fragment() {
   private lateinit var binding: FragmentPdfViewerBinding
    var uri: Uri? = null
    private var password:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentPdfViewerBinding.inflate(inflater,container,false)

        val bundle = this.arguments
        uri = if(bundle != null) {
            bundle.getString("data")?.toUri()
        }else{
            null
        }
        viewPdf()
        return binding.root
    }


    private val onLoadCompleteListener = OnLoadCompleteListener {
        binding.pdfView.elevation = 55f
        binding.progressBar.elevation = 0f
        binding.progressBar.visibility = View.GONE
        binding.textError.visibility = View.GONE
        binding.textSelectAgainText.visibility = View.GONE
    }
    private val passwordListener = (OnErrorListener { t ->
        if (t is PdfPasswordException || t is ClassNotFoundException) {
            val builder = AlertDialog.Builder(requireContext())
            //set title for alert dialog
            builder.setTitle("This file is protected")
            //set message for alert dialog
            builder.setMessage("password")
      //      builder.setIcon(android.R.drawable.ic_dialog_alert)
            val editText = EditText(requireContext()).apply {
               // setText(preFill)
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            val viewContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    setMargins(16, 0, 16, 0)
                }
                addView(editText, lp)
            }

            builder.setView(viewContainer)
            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                password = editText.text.toString()
                viewPdfWithPassWord()
            }
            //performing cancel action
            builder.setNegativeButton("Cancel"){dialogInterface , which ->
               // Toast.makeText(requireContext(),"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }

            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
            Toast.makeText(requireContext() , t.message,Toast.LENGTH_SHORT).show()
        } else {
            binding.textSelectAgainText.visibility = View.VISIBLE
            binding.textError.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.pdfView.elevation = 0f
            Toast.makeText(requireContext() , t.message,Toast.LENGTH_SHORT).show()
        }
    })

    private fun viewPdf(){
        binding.pdfView.elevation = 0f
        binding.textError.visibility = View.GONE
        binding.textSelectAgainText.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        binding.pdfView.fromUri(uri)
            //    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            // spacing between pages in dp. To define spacing color, set view background
            .spacing(0)
            .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//            .linkHandler(DefaultLinkHandler)
//            .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
            .pageSnap(false) // snap pages to screen boundaries
            .pageFling(false) // make a fling change only a single page like ViewPager
            .nightMode(false) // t
            .onLoad(onLoadCompleteListener)
            .onError(passwordListener)
            .load()
    }

    private fun viewPdfWithPassWord(){
        binding.pdfView.fromUri(uri)
            //    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            // spacing between pages in dp. To define spacing color, set view background
            .spacing(0)
            .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//            .linkHandler(DefaultLinkHandler)
//            .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
            .pageSnap(false) // snap pages to screen boundaries
            .pageFling(false) // make a fling change only a single page like ViewPager
            .nightMode(false) // t
            .onLoad(onLoadCompleteListener)
            .onError(passwordListener)
            .password(password)
            .load()
    }

}
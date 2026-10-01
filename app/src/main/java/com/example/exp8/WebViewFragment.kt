package com.example.exp8

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exp8.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupWebView()
        setupControls()
        observeViewModel()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (_binding != null) {
                    if (newProgress < 100) {
                        binding.pbLoading.visibility = View.VISIBLE
                        binding.pbLoading.progress = newProgress
                    } else {
                        binding.pbLoading.visibility = View.GONE
                    }
                }
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false // Keep inside WebView
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (_binding != null) {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                    updateNavButtons()
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (_binding != null) {
                    binding.pbLoading.visibility = View.GONE
                    updateNavButtons()
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (_binding != null && request?.isForMainFrame == true) {
                    binding.layoutError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupControls() {
        binding.btnWebBack.setOnClickListener {
            if (canGoBack()) {
                goBack()
            } else {
                Toast.makeText(requireContext(), "No previous web page", Toast.LENGTH_SHORT).show()
                (activity as? MainActivity)?.navigateToTab(R.id.nav_explore)
            }
        }

        binding.btnWebForward.setOnClickListener {
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            } else {
                Toast.makeText(requireContext(), "No forward web page", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnWebRefresh.setOnClickListener {
            reloadCurrentPage()
        }

        binding.btnRetry.setOnClickListener {
            reloadCurrentPage()
        }

        binding.chipGoogleSearch.setOnClickListener {
            viewModel.openInWeb("https://www.google.com")
        }

        binding.chipIncredibleIndia.setOnClickListener {
            viewModel.openInWeb("https://www.incredibleindia.org")
        }

        binding.chipWikipedia.setOnClickListener {
            viewModel.openInWeb("https://en.wikipedia.org/wiki/Culture_of_India")
        }

        binding.chipLocalHtml.setOnClickListener {
            viewModel.openInWeb("file:///android_asset/heritage.html")
        }
    }

    private fun observeViewModel() {
        viewModel.activeWebUrl.observe(viewLifecycleOwner) { url ->
            if (!url.isNullOrEmpty() && binding.webView.url != url) {
                binding.webView.loadUrl(url)
            }
        }
    }

    fun canGoBack(): Boolean {
        return _binding != null && binding.webView.canGoBack()
    }

    fun goBack() {
        if (_binding != null && binding.webView.canGoBack()) {
            binding.webView.goBack()
        }
    }

    fun reloadCurrentPage() {
        if (_binding != null) {
            binding.layoutError.visibility = View.GONE
            binding.webView.reload()
        }
    }

    private fun updateNavButtons() {
        if (_binding != null) {
            binding.btnWebBack.isEnabled = binding.webView.canGoBack()
            binding.btnWebForward.isEnabled = binding.webView.canGoForward()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
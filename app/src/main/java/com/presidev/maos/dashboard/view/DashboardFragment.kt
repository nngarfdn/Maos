package com.presidev.maos.dashboard.view

import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.presidev.maos.R
import com.presidev.maos.dashboard.model.Slider
import com.presidev.maos.search.view.SearchActivity
import com.presidev.maos.utils.AppUtils.showToast
import com.presidev.maos.utils.ShimmerHelper
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment() {

    private lateinit var  dashboardViewModel: DashboardViewModel
    private lateinit var sliderView: SliderView
    private lateinit var adapter: SliderAdapter

    companion object{
        private const val TAG = "DashboardFragment"
        private const val ADMIN_PHONE_NUMBER = "6287838804270"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shimmer = ShimmerHelper(shimmer_mitra, rv_mitra)
        btn_daftar_penyedia.visibility = View.INVISIBLE
        shimmer.show()

        dashboardViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel::class.java)
        dashboardViewModel.getResultMitra().observe(viewLifecycleOwner, { result ->
            shimmer.hide()
            Log.d(TAG, "onCreate: $result")
            val layoutManager = GridLayoutManager(context, 2)
            rv_mitra.layoutManager = layoutManager
            val adapter = DashboardMitraAdapter(result)
            rv_mitra.adapter = adapter
            btn_daftar_penyedia.visibility = View.VISIBLE
        })

        sliderView = view.findViewById(R.id.imageSlider)
        adapter = SliderAdapter()
        adapter.addItem(Slider("https://i.imgur.com/g6rZGhw.png"))
        adapter.addItem(Slider("https://i.imgur.com/HDcDGcn.png"))
        adapter.addItem(Slider("https://i.imgur.com/pS4Yr5R.png"))
        sliderView.setSliderAdapter(adapter)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()

        view.edt_search.setOnClickListener{
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        view.btn_daftar_penyedia.setOnClickListener {
            try {
                val whatsappIntent = Intent("android.intent.action.SEND")
                whatsappIntent.component = ComponentName("com.whatsapp", "com.whatsapp.ContactPicker")
                whatsappIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(ADMIN_PHONE_NUMBER) + "@s.whatsapp.net")
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, """
                Halo kak, saya ingin mendaftar sebagai penyedia buku di aplikasi *Maos*, apakah bisa kak ?
                """.trimIndent())
                startActivity(whatsappIntent)
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Error on sharing: $e")
                showToast(view.context, "Kamu belum punya aplikasi WhatsApp")
            }
        }
    }

    override fun onStart() {
        dashboardViewModel.loadResultMitra()
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        dashboardViewModel.loadResultMitra()
    }
}
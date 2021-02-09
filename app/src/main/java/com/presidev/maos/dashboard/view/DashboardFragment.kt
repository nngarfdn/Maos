package com.presidev.maos.dashboard.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.presidev.maos.R
import com.presidev.maos.dashboard.model.Slider
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var  dashboardViewModel: DashboardViewModel
    private lateinit var sliderView: SliderView
    private lateinit var adapter: SliderAdapter

    companion object{
        private const val TAG = "DashboardFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dashboardViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel::class.java)

        dashboardViewModel.getResultMitra().observe(viewLifecycleOwner, { result ->
            Log.d(TAG, "onCreate: $result")
//            imgLaporanKosong.visibility = View.INVISIBLE
            val layoutManager = GridLayoutManager(context, 2)
            rv_mitra.layoutManager = layoutManager
            val adapter = DashboardMitraAdapter(result)
            rv_mitra.adapter = adapter
        })

        sliderView = view.findViewById<SliderView>(R.id.imageSlider)

        adapter = SliderAdapter(context)

        adapter.addItem(Slider("https://lh3.googleusercontent.com/proxy/UUgs-tHSUQygir4eUBnHYOOztsXOidDYjsIyQHCtWrGMiulXUbwvwdeuOvUkr_SThHsTmT3gfK__6r28RjbFGbcK7JH7-fS5PpgQneBBVsKVbJJwUZgsSQ"))
        adapter.addItem(Slider("https://i.pinimg.com/originals/71/d6/71/71d671919e929493e33816336f625355.jpg"))
        adapter.addItem(Slider("https://i.pinimg.com/736x/70/b8/e2/70b8e25d04a4e79f4ea15893bfcc4f91.jpg"))
        sliderView.setSliderAdapter(adapter)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        sliderView.setIndicatorSelectedColor(Color.WHITE)
        sliderView.setIndicatorUnselectedColor(Color.GRAY)
        sliderView.setScrollTimeInSec(3)
        sliderView.setAutoCycle(true)
        sliderView.startAutoCycle()


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
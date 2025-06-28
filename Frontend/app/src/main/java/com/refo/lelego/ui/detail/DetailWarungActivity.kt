package com.refo.lelego.ui.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.refo.lelego.R
import com.refo.lelego.databinding.ActivityDetailWarungBinding

class DetailWarungActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailWarungBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWarungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyName = intent.getStringExtra("STORY_NAME")
        val storyDescription = intent.getStringExtra("STORY_DESCRIPTION")
        val storyImageUrl = intent.getStringExtra("STORY_IMAGE_URL")

//        binding.apply {
//            tvDetailName.text = storyName
//            tvDetailDescription.text = storyDescription
//            Glide.with(this@DetailStoryActivity)
//                .load(storyImageUrl)
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
//                .into(ivDetailPhoto)
//
//            ivDetailPhoto.scaleX = 0.5f
//            ivDetailPhoto.scaleY = 0.5f
//            ivDetailPhoto.alpha = 0f
//
//            val scaleX = ObjectAnimator.ofFloat(ivDetailPhoto, "scaleX", 0.5f, 1f)
//            val scaleY = ObjectAnimator.ofFloat(ivDetailPhoto, "scaleY", 0.5f, 1f)
//            val alpha = ObjectAnimator.ofFloat(ivDetailPhoto, "alpha", 0f, 1f)
//
//            AnimatorSet().apply {
//                playTogether(scaleX, scaleY, alpha)
//                duration = 300
//                start()
//            }
//        }
    }
}
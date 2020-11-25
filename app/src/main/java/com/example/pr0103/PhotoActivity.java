package com.example.pr0103;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.pr0103.UserStaticInfo.POSITION;
import static com.example.pr0103.UserStaticInfo.photos;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        int position = getIntent().getIntExtra(POSITION, 0);
        Init(position);
        setCurrentItem();
    }

    LayoutInflater inflater;
    TextView progressTextView;
    ViewPager photosViewPager;

    private void Init(int position) {
        progressTextView = findViewById(R.id.progressTextView);
        photosViewPager = findViewById(R.id.PhotosViewPager);
        inflater = LayoutInflater.from(this);
        photosViewPager.setAdapter(new PhotosAdapter());
        photosViewPager.setCurrentItem(position);
        photosViewPager.setPageTransformer(false, new ProgressPageTransformer());
    }

    public class PhotosAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = inflater.inflate(R.layout.item_full_photo,container, false);
            ImageView imageView = itemView.findViewById(R.id.image);
            imageView.setImageBitmap(photos.get(position));
            container.addView(itemView);
            return itemView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }
    }

    private class ProgressPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if(position == 0.0F){
                setCurrentItem();
            }
        }
    }

    private void setCurrentItem() {
        String text = photosViewPager.getCurrentItem()+1 + "/" + photos.size();
        progressTextView.setText(text);
    }
}

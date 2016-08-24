package kitchen.dev.icfbooks.esther;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"ArnhemPro-Black.ttf");
        TextView title = (TextView) findViewById(R.id.about_newsletter_title);
        title.setTypeface(typeface);

        ((LinearLayout)findViewById(R.id.about_author_blog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Intent.ACTION_VIEW , Uri.parse("http://www.leobigger.com") ));
            }
        });

        ((LinearLayout)findViewById(R.id.about_author_insta)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/leobigger");
                Intent instaIntent = new Intent(Intent.ACTION_VIEW, uri);

                instaIntent.setPackage("com.instagram.android");

                try {
                    startActivity(instaIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/leobigger")));
                }
            }
        });

        ((LinearLayout)findViewById(R.id.about_author_fb)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("fb:/profile/240588435984358");
                Intent instaIntent = new Intent(Intent.ACTION_VIEW, uri);

                instaIntent.setPackage("com.facebook.katana");

                try {
                    startActivity(instaIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.facebook.com/Bigger.Leo")));
                }
            }
        });

        ((LinearLayout)findViewById(R.id.about_store_link)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Intent.ACTION_VIEW , Uri.parse("https://icf-onlinestore.com") ));
            }
        });

        ((LinearLayout)findViewById(R.id.about_dk_link)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Intent.ACTION_VIEW , Uri.parse("https://dev.kitchen") ));
            }
        });
    }
}

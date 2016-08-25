package kitchen.dev.icfbooks.esther;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;

import kitchen.dev.icfbooks.esther.dal.ApiClient;
import kitchen.dev.icfbooks.esther.dal.ApiResultHandler;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final Context context = this;

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



        ((Button)findViewById(R.id.about_newsletter_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((TextView)findViewById(R.id.about_newsletter_name)).getText().toString();
                String mail = ((TextView)findViewById(R.id.about_newsletter_mail)).getText().toString();

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(mail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    builder.setPositiveButton(getString(R.string.button_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.setTitle(getString(R.string.about_newsletter_failure_title));
                    builder.setMessage(getString(R.string.about_newsletter_invalid));
                    builder.create().show();
                    return;
                }

                ApiClient.getInstance(context).subscribeNewsletter(name, mail, new ApiResultHandler<Object>() {
                    @Override
                    public void onResult(Object result) {
                        ((TextView)findViewById(R.id.about_newsletter_name)).setText("");
                        ((TextView)findViewById(R.id.about_newsletter_mail)).setText("");
                        builder.setPositiveButton(getString(R.string.button_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.setTitle(getString(R.string.about_newsletter_success_title));
                        builder.setMessage(getString(R.string.about_newsletter_success));
                        builder.create().show();
                    }
                    @Override
                    public void onError(Object error) {
                        builder.setPositiveButton(getString(R.string.button_OK), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.setTitle(getString(R.string.about_newsletter_failure_title));
                        builder.setMessage(getString(R.string.about_newsletter_failure));
                        builder.create().show();
                    }
                });

            }
        });
    }
}

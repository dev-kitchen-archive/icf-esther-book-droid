package kitchen.dev.icfbooks.esther;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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

        //Workaround to set black title
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>"+ getString(R.string.menu_about)+"</font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        setContentView(R.layout.activity_about);

        final Context context = this;

        Typeface typeface = Typeface.createFromAsset(getAssets(),"ArnhemPro-Black.ttf");
        TextView title = (TextView) findViewById(R.id.about_newsletter_title);
        title.setTypeface(typeface);

        ((LinearLayout)findViewById(R.id.about_author_blog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle(getString(R.string.about_author_follow));
              builder.setItems(new CharSequence[]{
                      "Blog (leobigger.com)",
                      "Instagram (@leobigger)",
                      "Facebook (fb.com/Bigger.Leo)",
              }, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      switch (i) {
                          case 0:
                              startActivity(new Intent( Intent.ACTION_VIEW , Uri.parse("http://www.leobigger.com") ));
                              break;
                          case 1:
                              Uri uri = Uri.parse("http://instagram.com/_u/leobigger");
                              Intent instaIntent = new Intent(Intent.ACTION_VIEW, uri);

                              instaIntent.setPackage("com.instagram.android");

                              try {
                                  startActivity(instaIntent);
                              } catch (ActivityNotFoundException e) {
                                  startActivity(new Intent(Intent.ACTION_VIEW,
                                          Uri.parse("http://instagram.com/leobigger")));
                              }
                              break;
                          case 2:
                              try {
                                  context.getPackageManager()
                                          .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
                                  startActivity(new Intent(Intent.ACTION_VIEW,
                                          Uri.parse("fb://page/240588435984358")));
                              } catch (Exception e) {
                                  startActivity(new Intent(Intent.ACTION_VIEW,
                                          Uri.parse("https://www.facebook.com/Bigger.Leo")));
                              }
                              break;
                      }
                  }
              });
                    builder.create().show();
            }
        });

        ((LinearLayout)findViewById(R.id.about_church_follow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(getString(R.string.about_church_follow));
                builder.setItems(new CharSequence[]{
                        "Web (icf.church)",
                        "Instagram (@icfmovement)",
                        "Facebook (fb.com/icfmovement)",
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                startActivity(new Intent( Intent.ACTION_VIEW , Uri.parse("http://www.icf.church") ));
                                break;
                            case 1:
                                Uri uri = Uri.parse("http://instagram.com/_u/icfmovement");
                                Intent instaIntent = new Intent(Intent.ACTION_VIEW, uri);

                                instaIntent.setPackage("com.instagram.android");

                                try {
                                    startActivity(instaIntent);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://instagram.com/icfmovement")));
                                }
                                break;
                            case 2:
                                try {
                                    context.getPackageManager()
                                            .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("fb://page/102703242193")));
                                } catch (Exception e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://www.facebook.com/icfmovement")));
                                }
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

        ((LinearLayout)findViewById(R.id.about_church_worship)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(getString(R.string.about_church_worship));
                builder.setItems(new CharSequence[]{
                        "Web (icf-worship.com)",
                        "Instagram (@icfworship)",
                        "Facebook (fb.com/ICFWorship)",
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                startActivity(new Intent( Intent.ACTION_VIEW , Uri.parse("http://www.icf-worship.com") ));
                                break;
                            case 1:
                                Uri uri = Uri.parse("http://instagram.com/_u/icfworship");
                                Intent instaIntent = new Intent(Intent.ACTION_VIEW, uri);

                                instaIntent.setPackage("com.instagram.android");

                                try {
                                    startActivity(instaIntent);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://instagram.com/icfworship")));
                                }
                                break;
                            case 2:
                                try {
                                    context.getPackageManager()
                                            .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("fb://page/570890826269611")));
                                } catch (Exception e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://www.facebook.com/ICFWorship")));
                                }
                                break;
                        }
                    }
                });
                builder.create().show();
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

        ((LinearLayout)findViewById(R.id.about_imprint)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Intent.ACTION_VIEW , Uri.parse("https://www.icf.church/impressum") ));
            }
        });

        ((LinearLayout)findViewById(R.id.about_press)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( Intent.ACTION_VIEW , Uri.parse("https://www.fontis-verlag.com") ));
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

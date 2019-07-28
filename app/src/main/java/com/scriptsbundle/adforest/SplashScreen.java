package com.scriptsbundle.adforest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.Base64;
import com.squareup.picasso.Picasso;

import com.scriptsbundle.adforest.helper.LocaleHelper;
import com.scriptsbundle.adforest.home.HomeActivity;
import com.scriptsbundle.adforest.signinorup.MainActivity;
import com.scriptsbundle.adforest.utills.Network.RestService;
import com.scriptsbundle.adforest.utills.SettingsMain;
import com.scriptsbundle.adforest.utills.UrlController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    Activity activity;
    SettingsMain setting;
    JSONObject jsonObjectSetting;

    boolean isRTL = false;

    public static JSONObject jsonObjectAppRating, jsonObjectAppShare;

    public static boolean gmap_has_countries = false, app_show_languages = false;
    public static JSONArray app_languages;
    public static String languagePopupTitle, languagePopupClose, gmap_countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = (float) 1; //0.85 small size, 1 normal size, 1,15 big etc

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

        activity = this;
        setting = new SettingsMain(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            setting.setUserLogin("0");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.scriptsbundle.adforest",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (SettingsMain.isConnectingToInternet(this)) {
            adforest_getSettings();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
            alert.setTitle(setting.getAlertDialogTitle("error"));
            alert.setCancelable(false);
            alert.setMessage(setting.getAlertDialogMessage("internetMessage"));
            alert.setPositiveButton(setting.getAlertOkText(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    dialog.dismiss();
                    SplashScreen.this.recreate();
                }
            });
            alert.show();
        }
    }

    public void adforest_getSettings() {
        RestService restService =
                UrlController.createService(RestService.class);
        Call<ResponseBody> myCall = restService.getSettings(UrlController.AddHeaders(this));
        myCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                try {
                    if (responseObj.isSuccessful()) {
                        Log.d("info settings Responce", "" + responseObj.toString());

                        JSONObject response = new JSONObject(responseObj.body().string());
                        Log.d("info settings object", "" + response.getJSONObject("data"));
                        if (response.getBoolean("success")) {
                            jsonObjectSetting = response.getJSONObject("data");

                            setting.setMainColor(jsonObjectSetting.getString("main_color"));

                            isRTL = jsonObjectSetting.getBoolean("is_rtl");
                            setting.setRTL(isRTL);
                            setting.setAlertDialogTitle("error", jsonObjectSetting.getJSONObject("internet_dialog").getString("title"));
                            setting.setAlertDialogMessage("internetMessage", jsonObjectSetting.getJSONObject("internet_dialog").getString("text"));
                            setting.setAlertOkText(jsonObjectSetting.getJSONObject("internet_dialog").getString("ok_btn"));
                            setting.setAlertCancelText(jsonObjectSetting.getJSONObject("internet_dialog").getString("cancel_btn"));

                            setting.setAlertDialogTitle("info", jsonObjectSetting.getJSONObject("alert_dialog").getString("title"));
                            setting.setAlertDialogMessage("confirmMessage", jsonObjectSetting.getJSONObject("alert_dialog").getString("message"));

                            setting.setAlertDialogMessage("waitMessage", jsonObjectSetting.getString("message"));

                            setting.setAlertDialogMessage("search", jsonObjectSetting.getJSONObject("search").getString("text"));
                            setting.setAlertDialogMessage("catId", jsonObjectSetting.getString("cat_input"));
                            setting.setAlertDialogMessage("location_type", jsonObjectSetting.getString("location_type"));
                            setting.setAlertDialogMessage("gmap_lang", jsonObjectSetting.getString("gmap_lang"));
                            setting.setGoogleButn(jsonObjectSetting.getJSONObject("registerBtn_show").getBoolean("google"));
                            setting.setfbButn(jsonObjectSetting.getJSONObject("registerBtn_show").getBoolean("facebook"));

                            JSONObject alertDialog = jsonObjectSetting.getJSONObject("dialog").getJSONObject("confirmation");
                            setting.setGenericAlertTitle(alertDialog.getString("title"));
                            setting.setGenericAlertMessage(alertDialog.getString("text"));
                            setting.setGenericAlertOkText(alertDialog.getString("btn_ok"));
                            setting.setGenericAlertCancelText(alertDialog.getString("btn_no"));

                            setting.isAppOpen(jsonObjectSetting.getBoolean("is_app_open"));
                            setting.checkOpen(jsonObjectSetting.getBoolean("is_app_open"));
                            setting.setGuestImage(jsonObjectSetting.getString("guest_image"));

                            JSONObject jsonObjectLocationPopup = jsonObjectSetting.getJSONObject("location_popup");
                            Log.d("info location_popup obj", "" + jsonObjectLocationPopup);
                            setting.setLocationSliderNumber(jsonObjectLocationPopup.getInt("slider_number"));
                            setting.setLocationSliderStep(jsonObjectLocationPopup.getInt("slider_step"));
                            setting.setLocationText(jsonObjectLocationPopup.getString("text"));
                            setting.setLocationBtnSubmit(jsonObjectLocationPopup.getString("btn_submit"));
                            setting.setLocationBtnClear(jsonObjectLocationPopup.getString("btn_clear"));

                            JSONObject jsonObjectLocationSettings = jsonObjectSetting.getJSONObject("gps_popup");
                            setting.setShowNearby(jsonObjectSetting.getBoolean("show_nearby"));
                            Log.d("info gps_popup obj", "" + jsonObjectLocationSettings);
                            setting.setGpsTitle(jsonObjectLocationSettings.getString("title"));
                            setting.setGpsText(jsonObjectLocationSettings.getString("text"));
                            setting.setGpsConfirm(jsonObjectLocationSettings.getString("btn_confirm"));
                            setting.setGpsCancel(jsonObjectLocationSettings.getString("btn_cancel"));

                            setting.setAdsPositionSorter(jsonObjectSetting.getBoolean("ads_position_sorter"));


                            setting.setNotificationTitle("");
                            setting.setNotificationMessage("");
                            setting.setNotificationTitle("");

                            if (setting.getAppOpen()) {
                                setting.setNoLoginMessage(jsonObjectSetting.getString("notLogin_msg"));
                            }

                            setting.setFeaturedScrollEnable(jsonObjectSetting.getBoolean("featured_scroll_enabled"));
                            if (setting.isFeaturedScrollEnable()) {
                                Log.d("info setting AutoScroll", jsonObjectSetting.getJSONObject("featured_scroll").toString());
                                setting.setFeaturedScroolDuration(jsonObjectSetting.getJSONObject("featured_scroll").getInt("duration"));
                                setting.setFeaturedScroolLoop(jsonObjectSetting.getJSONObject("featured_scroll").getInt("loop"));
                            }

                            jsonObjectAppRating = jsonObjectSetting.getJSONObject("app_rating");
                            jsonObjectAppShare = jsonObjectSetting.getJSONObject("app_share");

                            gmap_has_countries = jsonObjectSetting.getBoolean("gmap_has_countries");
                            if (gmap_has_countries) {
                                gmap_countries = jsonObjectSetting.getString("gmap_countries");
                            }
                            app_show_languages = jsonObjectSetting.getBoolean("app_show_languages");

                            if (app_show_languages) {
                                languagePopupTitle = jsonObjectSetting.getString("app_text_title");
                                languagePopupClose = jsonObjectSetting.getString("app_text_close");
                                app_languages = jsonObjectSetting.getJSONArray("app_languages");

                            }

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms

                                    if (setting.getUserLogin().equals("0")) {
                                        SplashScreen.this.finish();
                                        if (setting.isLanguageChanged()) {
                                            if (isRTL)
                                                updateViews("ur");
                                            else {
                                                updateViews("en");
                                            }
                                        }
                                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                    } else {
                                        SplashScreen.this.finish();
                                        if (setting.isLanguageChanged()) {
                                            if (isRTL)
                                                updateViews("ur");
                                            else {
                                                updateViews("en");
                                            }
                                        }

                                        setting.isAppOpen(false);
                                        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                    }

                                    if (app_show_languages && !setting.isLanguageChanged()) {
                                        if (setting.getLanguageRtl()) {
                                            updateViews("ur");
                                        } else {
                                            updateViews("en");
                                        }
                                    }
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(activity, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("info settings error", String.valueOf(t));
                Log.d("info settings error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void updateViews(String languageCode) {
        LocaleHelper.setLocale(this, languageCode);
    }
}

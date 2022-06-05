package com.example.medaitest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.medaitest.R;


public class Functions {

    private static final String TAG = "InFunction";
    private Context context;
    private AlertDialog.Builder dialogBuilder;
    private ProgressDialog mProgressDialog;
    private boolean tokenFlag;

    //Volley

    private JsonObjectRequest objRequest;
    private JSONObject jsonObject;
    private RequestQueue queue;
    private SharedPreferences preferences;

    public Functions(Context context) {
        this.context = context;
        dialogBuilder = new AlertDialog.Builder(context);
        jsonObject = new JSONObject();
        queue = Volley.newRequestQueue(context);
        preferences = context.getSharedPreferences(Constant.LOGIN_PREFERENCE,Context.MODE_PRIVATE);
        tokenFlag = true;
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void backPressMessage() {
        dialogBuilder.setTitle(context.getString(R.string.alert));
        dialogBuilder.setMessage(R.string.back_press_message);
        dialogBuilder.setPositiveButton(context.getString(R.string.close_message), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finish();
            }
        });

        dialogBuilder.setNegativeButton(context.getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogBuilder.setCancelable(false);
        dialogBuilder.setIcon(R.drawable.ic_alert);
        dialogBuilder.show();
    }

    public void exitMessage() {
        dialogBuilder.setTitle(context.getString(R.string.alert));
        dialogBuilder.setMessage(R.string.exit_message);
        dialogBuilder.setPositiveButton(context.getString(R.string.close_message), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finish();
            }
        });

        dialogBuilder.setNegativeButton(context.getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialogBuilder.setCancelable(false);
        dialogBuilder.setIcon(R.drawable.ic_alert);
        dialogBuilder.show();
    }

    public void singlePositiveMessage(String title, String message, String buttonMessage) {
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(buttonMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setCancelable(false);
        dialogBuilder.setIcon(R.drawable.ic_alert);
        dialogBuilder.show();
    }

    public void singlePositiveExit(String title, String message, String buttonMessage) {
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);

        dialogBuilder.setPositiveButton(buttonMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finish();
            }
        });

        dialogBuilder.setCancelable(false);
        dialogBuilder.setIcon(R.drawable.ic_alert);
        dialogBuilder.show();
    }

    public String encodeBitmap(Bitmap bitmap) {
        String encodedImage = "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] bytesOfImage = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(bytesOfImage, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap decodedBitmap(String imageString) {
        // decode base64 string
        byte[] bytes= Base64.decode(imageString,Base64.DEFAULT);
        // Initialize bitmap
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }

    public void doubleNegativeExit(String title, String message) {
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finish();
            }
        });
        dialogBuilder.setCancelable(false);
        dialogBuilder.setIcon(R.drawable.ic_alert);
        dialogBuilder.show();
    }

    public String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        return df.format(date);
    }

    public boolean isConnected() {

        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }

    public void connection() {
        if (!isConnected()) {
            dialogBuilder.setTitle(context.getString(R.string.internet))
                    .setMessage(context.getString(R.string.turn_on_data))
                    .setCancelable(false).
                    setNegativeButton(context.getString(R.string.mobile_data), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                        }
                    }).setPositiveButton(context.getString(R.string.wifi), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }).setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity)context).finish();
                }
            }).show();

        }
    }

    /*public void comingSoon(){
        singlePositiveMessage(context.getString(R.string.upcoming_feature),context.getString(R.string.coming_soon),context.getString(R.string.ok));
    }

    public void enterAnimation(){
        ((Activity)context).overridePendingTransition(R.anim.enter_up,R.anim.exit_right);
    }

    public void exitAnimation(){
        ((Activity)context).overridePendingTransition(R.anim.enter_left,R.anim.exit_down);
    }*/


//    public boolean verifyToken(String token) {
//        if (token.equals("")){
//            Log.e(TAG,"TokenLog0");
//            preferences.edit().putBoolean(Constant.TOKEN_FLAG,false).apply();
//            return false;
//        }
//        try {
//            jsonObject.put("token",token);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        //Log.e(TAG,"PathTracking: Verify Top");
//        //showProgressDialog(context.getString(R.string.loading));
//        //Log.e(TAG, jsonObject.toString());
//        objRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_VERIFY_TOKEN, jsonObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        preferences.edit().putBoolean(Constant.TOKEN_FLAG,true).apply();
//                        tokenFlag = true;
//                        Log.e(TAG,"TokenFlagVerSuc: "+tokenFlag);
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                tokenFlag = refreshToken();
//            }
//        });
//        queue.add(objRequest);
//        return tokenFlag;
//    }
//
//    public boolean refreshToken() {
//        String refresh = preferences.getString(Constant.REFRESH,"");
//        try {
//            jsonObject.put("refresh",refresh);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        //Log.e(TAG,"PathTracking: Refresh Top");
//        //showProgressDialog(context.getString(R.string.loading));
//        //Log.e(TAG, jsonObject.toString());
//        objRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_REFRESH_TOKEN, jsonObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //hideProgressDialog();
//                        //Log.e(TAG, "ResponseRefreshToken: "+response.toString());
//                        try {
//                            String token = response.getString("access");
//                            Log.e(TAG, "TokenFlagRefSucItself: " + token);
//                            if (!token.isEmpty()) {
//                                tokenFlag = true;
//                                preferences.edit().putString(Constant.TOKEN, token).apply();
//                                preferences.edit().putBoolean(Constant.TOKEN_FLAG,true).apply();
//                                Log.e(TAG,"TokenFlagRefSuc: "+tokenFlag);
//                            } else {
//                                preferences.edit().putBoolean(Constant.TOKEN_FLAG,false).apply();
//                                tokenFlag = false;
//                                Log.e(TAG,"TokenFlagRefSucElse: "+tokenFlag);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            tokenFlag = false;
//                            preferences.edit().putBoolean(Constant.TOKEN_FLAG,false).apply();
//                            Log.e(TAG,"TokenFlagRefSuc: "+tokenFlag);
//                            Log.e(TAG, "CatchRefreshToken: "+e.getMessage());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideProgressDialog();
//                //getTokenFromServer();
//                tokenFlag = false;
//                preferences.edit().putBoolean(Constant.TOKEN_FLAG,false).apply();
//                //Log.e(TAG, "ErrorResponseRefreshToken: "+error.networkResponse.headers);
//            }
//        });
//        queue.add(objRequest);
//        return tokenFlag;
//    }

//    public void getTokenFromServer(){
//        Log.e(TAG, "Generating Token!");
//        try {
//
//            jsonObject.put("username","1234567890");
//            jsonObject.put("password","yayneuralogicyay");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.e(TAG, jsonObject.toString());
//
//        objRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_USER_LOGIN, jsonObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //Log.e("OnResponseLogin", String.valueOf(response));
//                        //Toast.makeText(APITestActivity.this, String.valueOf(response), Toast.LENGTH_LONG).show();
//                        try {
//                            String token = response.getString("access");
//                            String refresh = response.getString("refresh");
//                            Log.e("OnResponseToken", String.valueOf(token));
//                            if(!token.isEmpty()) {
//                                preferences.edit().putString(Constant.TOKEN,token).apply();
//                                preferences.edit().putString(Constant.REFRESH,refresh).apply();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.e(TAG,"CatchException: "+e.toString());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                JSONObject errorObject = getVolleyError(error);
//                //int code = error.networkResponse.statusCode;
//                Toast.makeText(context, "Error: Token Is not Initialized. "+errorObject, Toast.LENGTH_LONG).show();
//                Log.e(TAG,"GetTokenError: "+error);
//            }
//        });
//        queue.add(objRequest);
//    }

    public JSONObject getVolleyError(VolleyError error){
        NetworkResponse response = error.networkResponse;
        JSONObject obj = new JSONObject();
        //Do not delete following codes
        //if (error instanceof ServerError && response != null) {
        if (response != null) {
            try {
                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                obj = new JSONObject(res);
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();

            }
        }
        return obj;
    }

}

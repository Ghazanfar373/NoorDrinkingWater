package com.pac.ideatech.noordrinkingwater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;

/**
 * Created by MianGhazanfar on 10/25/2016.
 */
public class Utils {
    public void showConfirmDialog(final Context ctx, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SessionManager manager=new SessionManager(ctx);
                manager.logout();
                ((Activity)ctx).finish();
                ctx.startActivity(new Intent(ctx,LoginActivity.class));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    public static void showSettingsAlert(String provider, Context ctx) {

        final String providestr = provider;
        final Context context = ctx;
        String type = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx, THEME_HOLO_LIGHT);
        alertDialog.setTitle("SETTINGS");
        if (provider.equalsIgnoreCase("GPS")) {
            alertDialog.setMessage((R.string.gps_alert));
            type = ctx.getString(R.string.action_settings);
            alertDialog.setPositiveButton((R.string.action_settings), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = null;
                    if (providestr.equalsIgnoreCase("GPS")) {
                        intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    } else if (providestr.equalsIgnoreCase("DATA")) {
                        //   intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        dialog.cancel();
                    }


                }
            });
            alertDialog.setNegativeButton((R.string.Cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else if (provider.equalsIgnoreCase("DATA")) {
            alertDialog.setMessage(R.string.data_alert);
            alertDialog.setNegativeButton(("Ok"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

        }


    }

    public static boolean isConnected(Context ctx) {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

//    public String getDirectionsUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//
//        String mode = "mode=DRIVING";
////+ "&" + mode;
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//        // Log.d(MapRoutes.class.getName(), url);
//
//        return url;
//    }

    class DownloadTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dialog=new ProgressDialog()
        }

        @Override
        protected String doInBackground(String... params) {
            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(params[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w("Direction data ", s);
            try {
               // parseJson(s);
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("JSON PARSING ERROR", e.toString());
            }
        }
    }

//    private void parseJson(String data) throws JSONException {
//        try {
//            if (data == null) ;
//            try {
//                final JSONObject json = new JSONObject(data);
//                Log.e("test", "json  :p==  " + json);
//                JSONArray routeArray = json.getJSONArray("routes");
//                JSONObject routes = routeArray.getJSONObject(0);
//                Log.e("test", "routes :==  " + routes);
//                JSONArray newTempARr = routes.getJSONArray("legs");
//                JSONObject newDisTimeOb = newTempARr.getJSONObject(0);
//                Log.e("test", "newDisTimeOb :==  " + newDisTimeOb);
//                JSONObject distOb = newDisTimeOb.getJSONObject("distance");
//                JSONObject timeob = newDisTimeOb.getJSONObject("duration");
//                // elapsing time duration
//                String elapsetime = timeob.getString("text");
//                String totaldistance = distOb.getString("text");
//                String tempDist[] = distOb.getString("text").toString()
//                        .split(" ");
//                double tempDoubleDist = Double.valueOf(tempDist[0].replace(",",
//                        ""));
//                String totalKm = String.valueOf(tempDoubleDist);
//                int dist = (int) tempDoubleDist;
//                JSONObject overviewPolylines = routes
//                        .getJSONObject("overview_polyline");
//                String encodedString = overviewPolylines.getString("points");
//                List<LatLng> list = decodePoly(encodedString);
////                PolylineOptions options = new PolylineOptions().width(5)
////                        .color(Color.BLUE).geodesic(true);
//                MapsActivity.routeOpts = new PolylineOptions().color(0x7F0000FF);
//                for (int z = 0; z < list.size(); z++) {
//                    LatLng point = list.get(z);
//                    MapsActivity.routeOpts.add(point);
//                }
//
//                MapsActivity.timedistanceText.setText(totaldistance + "| Time " + elapsetime);
//                MapsActivity.timedistanceText.setText(totaldistance + "| Time duration " + elapsetime);
//                MapsActivity.mMap.addPolyline(MapsActivity.routeOpts);
//                hidepDialog();
////MapsActivity.locationbtn.setText("Clear Map");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

//    public void hidepDialog() {
//        MapsActivity.pDialog.setVisibility(View.INVISIBLE);
//    }
//
//    private List<LatLng> decodePoly(String encoded) {
//
//        List<LatLng> poly = new ArrayList<LatLng>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//
//        return poly;
//    }

    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void getOptimumPath(String adddress) {
        Log.w("#######URL ADDRESS", adddress);
        DownloadTask task = new DownloadTask();
        task.execute(adddress);
    }

    public void makeExceptionReport(final String excepType, final String details, final String activityName) {
        // Tag used to cancel the request
        String tag_string_req = "req_exception";
        // showProgress(true);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebServerLinks.loginURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RES", "Volley Response: " + response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("There was Error", "Volley Error: " + error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("errorname", excepType);
                params.put("errordetails", details);
                params.put("activityname", activityName);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public void sendOrdersSortedList(final String sortedList, Context ctx) {
        // Tag used to cancel the request
        String tag_string_req = "req_exception";
      showDial("Updating to Server...",ctx);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebServerLinks.loginURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RES", "Volley Response: " + response);
hideDial();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("There was Error", "Volley Error: " + error.getMessage());
hideDial();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
               params.put("list", sortedList);
//                params.put("errordetails", details);
//                params.put("activityname",activityName);
                return params;

            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    ProgressDialog dialog;
    public void showDial(String msg, Context ctx) {
         dialog = new ProgressDialog(ctx);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void hideDial(){
        dialog.dismiss();
    }



}






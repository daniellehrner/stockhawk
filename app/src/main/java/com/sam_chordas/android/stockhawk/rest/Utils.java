package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.support.annotation.Nullable;
import android.util.Log;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

  private static String LOG_TAG = Utils.class.getSimpleName();

  public static boolean showPercent = true;

  public static ArrayList<ContentProviderOperation> quoteJsonToContentVals(String JSON){
    ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
    JSONObject jsonObject;
    JSONArray resultsArray;
    Log.i(LOG_TAG, "GET FB: " +JSON);
    try{
      jsonObject = new JSONObject(JSON);
      if (jsonObject.length() != 0){
        jsonObject = jsonObject.getJSONObject("query");
        int count = Integer.parseInt(jsonObject.getString("count"));
        if (count == 1){
          jsonObject = jsonObject.getJSONObject("results")
              .getJSONObject("quote");

          ContentProviderOperation cpo = buildBatchOperation(jsonObject);

          if (cpo == null) {
            throw new NullPointerException("test");
          }

          batchOperations.add(cpo);
        } else{
          resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");

          if (resultsArray != null && resultsArray.length() != 0){
            ContentProviderOperation cpo;
            for (int i = 0; i < resultsArray.length(); i++){
              jsonObject = resultsArray.getJSONObject(i);
              cpo = buildBatchOperation(jsonObject);

              if (cpo == null) {
                continue;
              }

              batchOperations.add(cpo);
            }
          }
        }
      }
    } catch (JSONException e){
      Log.e(LOG_TAG, "String to JSON failed: " + e);
    }
    catch (NullPointerException e) {
      Log.e(LOG_TAG, "Symbol no: " + e);
    }
    return batchOperations;
  }

  public static String truncateBidPrice(String bidPrice){
    bidPrice = String.format("%.2f", Float.parseFloat(bidPrice));
    return bidPrice;
  }

  public static String truncateChange(String change, boolean isPercentChange){
    String weight = change.substring(0,1);
    String ampersand = "";
    if (isPercentChange){
      ampersand = change.substring(change.length() - 1, change.length());
      change = change.substring(0, change.length() - 1);
    }
    change = change.substring(1, change.length());

    Double changeDouble;

    try {
      changeDouble = Double.parseDouble(change);
    }
    catch (NumberFormatException e) {
      Log.e(LOG_TAG, "Can't parse change: " + e.toString());
      return "0.00";
    }

    double round = (double) Math.round(changeDouble * 100) / 100;
    change = String.format("%.2f", round);
    StringBuilder changeBuilder = new StringBuilder(change);
    changeBuilder.insert(0, weight);
    changeBuilder.append(ampersand);
    change = changeBuilder.toString();
    return change;
  }

  @Nullable
  public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject){
    ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
            QuoteProvider.Quotes.CONTENT_URI);
    try {
      String change = jsonObject.getString("Change");
      String bid = jsonObject.getString("Bid");

      if (bid.equals("null")) {
        return null;
      }

      builder.withValue(QuoteColumns.SYMBOL, jsonObject.getString("symbol"));
      builder.withValue(QuoteColumns.BIDPRICE, truncateBidPrice(bid));
      builder.withValue(QuoteColumns.PERCENT_CHANGE, truncateChange(
          jsonObject.getString("ChangeinPercent"), true));
      builder.withValue(QuoteColumns.CHANGE, truncateChange(change, false));
      builder.withValue(QuoteColumns.ISCURRENT, 1);
      if (change.charAt(0) == '-'){
        builder.withValue(QuoteColumns.ISUP, 0);
      }else{
        builder.withValue(QuoteColumns.ISUP, 1);
      }

    } catch (JSONException e){
      e.printStackTrace();
    }
    return builder.build();
  }

  public static String getCalculatedDate(String dateFormat, int days) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat s = new SimpleDateFormat(dateFormat);
    cal.add(Calendar.DAY_OF_YEAR, days);
    return s.format(new Date(cal.getTimeInMillis()));
  }

  public static int roundToNextTen(int v, boolean directionUp) {
    int x;

    if (directionUp) {
      x = 10 - (v % 10);
      v += x;
    }
    else {
      x = (v % 10);
      v -= x;
    }

    return v;
  }
}

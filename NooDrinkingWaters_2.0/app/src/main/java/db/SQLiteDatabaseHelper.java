package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import model.CartProductModel;


/**
 * Created by Muhammad_Shahid on 25-Jul-16.
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    //database
    private static final String DATABASE_NAME = "NDWClient.db";
    //tables
    private static final String CART = "cart";
    //columns
    private static final String PRODUCT_ID = "ProductID";
    private static final String PRODUCT_NAME = "Product_Name";
    private static final String PRODUCT_QTY = "ProductQuantity";
    private static final String PRODUCT_TOTAL_AMOUNT = "TotalAmount";
    private static final String PRODUCT_PRICE = "ProductPrice";
    private static final String PRODUCT_IMAGE_URL = "ProductImageUrl";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table " + CART + "(" + PRODUCT_ID + " text, " + PRODUCT_NAME + " text," + PRODUCT_QTY + " interger,"
                + PRODUCT_TOTAL_AMOUNT + " interger," + PRODUCT_PRICE + " interger," + PRODUCT_IMAGE_URL + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CART);
        onCreate(db);
    }

    public void insertProductIntoCart(CartProductModel cartProductModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues S = new ContentValues();

        String productID = cartProductModel.getId();
        S.put(PRODUCT_NAME, cartProductModel.getName());
        S.put(PRODUCT_PRICE, cartProductModel.getPrice());
        S.put(PRODUCT_TOTAL_AMOUNT, cartProductModel.getTotalAmount());
        S.put(PRODUCT_IMAGE_URL, cartProductModel.getImage());

        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCT_QTY + " FROM " + CART + " WHERE " + PRODUCT_ID + "='" + productID + "'", null);
        if (itemsCursor.getCount() > 0) {
            itemsCursor.moveToFirst();
            S.put(PRODUCT_QTY, itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_QTY)) + cartProductModel.getQty());
            db.update(CART, S, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
        } else {
            S.put(PRODUCT_QTY, cartProductModel.getQty());
            S.put(PRODUCT_ID, productID);
            db.insert(CART, null, S);
        }

        db.close();
    }

    public int getTotalCartCounter() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCT_ID + " FROM " + CART, null);
        int count = itemsCursor.getCount();
        itemsCursor.close();
        db.close();
        return count;
    }

    public int geProductQty(String productID) {
        SQLiteDatabase db = getWritableDatabase();
        int count = 0;
        Cursor itemsCursor = db.rawQuery("SELECT  " + PRODUCT_QTY + " FROM " + CART +
                " WHERE " + PRODUCT_ID + " ='" + productID + "'", null);
        if (itemsCursor.getCount() > 0) {
            itemsCursor.moveToNext();
            count = itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_QTY));
        }
        itemsCursor.close();
        db.close();
        return count;
    }

    public ArrayList<CartProductModel> getCartProducts() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<CartProductModel> cartProductModelArrayList = new ArrayList<>();
        CartProductModel cartProductModel = null;
        Cursor itemsCursor = db.rawQuery("SELECT * FROM " + CART, null);

        while (itemsCursor.moveToNext()) {
            cartProductModel = new CartProductModel();
            cartProductModel.setId(itemsCursor.getString(itemsCursor.getColumnIndex(PRODUCT_ID)));
            cartProductModel.setName(itemsCursor.getString(itemsCursor.getColumnIndex(PRODUCT_NAME)));
            cartProductModel.setImage(itemsCursor.getString(itemsCursor.getColumnIndex(PRODUCT_IMAGE_URL)));
            cartProductModel.setTotalAmount(itemsCursor.getDouble(itemsCursor.getColumnIndex(PRODUCT_TOTAL_AMOUNT)));
            cartProductModel.setPrice(itemsCursor.getDouble(itemsCursor.getColumnIndex(PRODUCT_PRICE)));
            cartProductModel.setQty(itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_QTY)));
            cartProductModelArrayList.add(cartProductModel);
        }
        itemsCursor.close();
        db.close();
        return cartProductModelArrayList;
    }

    public boolean removeItemFromCart(String productID) {
        SQLiteDatabase db = getWritableDatabase();
        int effectedRowsCount = db.delete(CART, PRODUCT_ID + "= ?", new String[]{String.valueOf(productID)});
        db.close();
        return effectedRowsCount > 0;
    }

    public void clearCart() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CART, null, null);
        db.close();
    }

    public String getCartProductsForOrderRequest() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT * FROM " + CART, null);

        JSONObject dataObj = new JSONObject();
        JSONArray cartItemsArray = new JSONArray();
        JSONObject cartItemsObject;

        while (itemsCursor.moveToNext()) {
            try {
                cartItemsObject = new JSONObject();
                cartItemsObject.putOpt("ProductID", itemsCursor.getString(itemsCursor.getColumnIndex(PRODUCT_ID)));
                cartItemsObject.putOpt("Product_Name", itemsCursor.getString(itemsCursor.getColumnIndex(PRODUCT_NAME)));
                cartItemsObject.putOpt("ProductQuantity", itemsCursor.getInt(itemsCursor.getColumnIndex(PRODUCT_QTY)));
                cartItemsObject.putOpt("TotalAmount", String.valueOf(itemsCursor.getDouble(itemsCursor.getColumnIndex(PRODUCT_TOTAL_AMOUNT))));
                cartItemsArray.put(cartItemsObject);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            dataObj.put("cart", cartItemsArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        itemsCursor.close();
        db.close();

        return dataObj.toString();
    }
}

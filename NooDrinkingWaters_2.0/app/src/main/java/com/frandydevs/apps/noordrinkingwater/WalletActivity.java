package com.frandydevs.apps.noordrinkingwater;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONObject;
import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import adapters.WalletListAdapter;
import model.WalletListData;
import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_USER_ID;

public class WalletActivity extends ParentActivity {
    WalletListData[] walletListData;
    int imgID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Wallet");
        accessWalletServerRequest();

    }
    private void accessWalletServerRequest() {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("from", "app");
            params.put("module", "get_wallet");
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            String url = "process";
            WebRequestHandlerInstance.post(url, params, new LoopJRequestHandler(context, 5, null, this, getString(R.string.wait)));
        }
    }
    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        imgID=R.drawable.arrow_up;
        try {
            JSONArray array = jsonObject.getJSONArray("data");
            walletListData = new WalletListData[array.length()];
            for (int i=0; i<array.length();i++) {
                JSONObject arrayobject = new JSONObject(array.getString(i));
                if(arrayobject.getString("type").equals("D")) imgID=R.drawable.arrow_down;
                else imgID=R.drawable.arrow_up;
                walletListData[i]=new WalletListData(arrayobject.getString("description"),imgID,arrayobject.getString("day"),arrayobject.getString("time"),arrayobject.getString("amount"),arrayobject.getString("type"));
            }
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            WalletListAdapter adapter = new WalletListAdapter(walletListData);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
               // showMessageDialog("Thanks for Contacting Us.", "", "Welcome", "", "finishContactUs", "");
            } else {
                //showToast(jsonObject.getString("error_msg"), 1, 17);
                //Log.e("ERROR Result###########",jsonObject.getString("result"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//http://new.noorwaters.com/process
//        user_id=userid
//        from=app
//        module=get_wallet
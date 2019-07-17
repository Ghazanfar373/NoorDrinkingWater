package com.pac.ideatech.noordrinkingwater;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceFragment extends Fragment {
ListView list;
Button btncnfrm;
TextView tvname,tvcontact,tvordernumber,tvDate,tvaddress,tvBalancedue;
private SessionManager manager;

    public InvoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manager=new SessionManager(getActivity());
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_invoice, container, false);
        list=(ListView)view.findViewById(R.id.listviewinvoice);
        btncnfrm=view.findViewById(R.id.btn_cnfrm);



        tvordernumber=view.findViewById(R.id.ordernum);
        tvDate=view.findViewById(R.id.orderdate);
        tvname=view.findViewById(R.id.ordername);
        tvcontact=view.findViewById(R.id.ordercontact);
        tvaddress=view.findViewById(R.id.orderaddr);
        tvBalancedue=view.findViewById(R.id.balancedue);
        Random ordernm=new Random();
        int order_num=ordernm.nextInt(5000);

        tvordernumber.setText("Order# "+order_num);
        tvDate.setText(getdate());
        tvname.setText(manager.getUser_fName());
        tvcontact.setText(manager.getUser_Mobile());
        tvaddress.setText(Order.getUser_address());
        tvBalancedue.setText("Balance due: "+Order.getTotalPrice());
        // Getting adapter by passing xml data ArrayList
       ArrayAdapter adapter=new InvoiceListAdapter(getActivity(), Order.orderList);
        list.setAdapter(adapter);


        btncnfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Module pending for Noroz Bhai Approval!", Toast.LENGTH_SHORT).show();
           Log.w("cartitems",Order.getCartitemslist());
           Log.w("User id",manager.getUserId());
                Log.w("note:",Order.getNote());
                if(Utils.isConnected(getActivity())) {
               SendOrderJSON(manager.getUserId(),Order.getNote(),Order.getCartitemslist());

            }else Utils.showSettingsAlert("DATA",getActivity());
        }


        });
        return view;
    }
    private String getdate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
       return df.format(c);

    }
    private void SendOrderJSON(final String user_id, final String notee, final String items) {

        String tag_string_req = "req_place_order";
        final ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebServerLinks.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("######RES", "JSON Order Response: " + response);
                Log.w("######RES", response);
                // hideDialog();
                //  Toast.makeText(CheckoutActivity.this, "responce: "+response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("result").equals("true")){
                        //responceback=true;



                         showAlert();

//                        Bundle bundle=new Bundle();
//                        bundle.putString("updateVal","yes");
//                        CartItemsFragment fragment=new CartItemsFragment();
//                        fragment.setArguments(bundle);
//                        final android.app.FragmentManager manager=getFragmentManager();
//                        android.app.FragmentTransaction transaction=manager.beginTransaction();
//                        transaction.replace(R.id.container_checkout,fragment);
//                        transaction.commit();


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                dialog.hide();
//

            }

        }, new Response.ErrorListener()

        {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("There was Error", "json Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Network Error!", Toast.LENGTH_LONG).show();
                //hideDialog();

                dialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                try {
                    // params.put("module", "purchase_history");
                    params.put("module", "place_order");
                    params.put("from", "app");
                    params.put("user_id", user_id);
                    params.put("note", notee);
                    params.put("cart",items);

                }catch (Exception ef){
                    ef.printStackTrace();
                }
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
   builder
            .setTitle("Order Confirmed!")
            .setIcon(R.drawable.ic_check_circle_black_24dp)
            .setMessage("Your order submitted we will contact you within 24 hours.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent intent=new Intent(getActivity(),CheckoutActivity.class);
//                intent.putExtra("from","none");
//                getActivity().startActivityForResult(intent,2244);
                Order.num_orders=0;
                Order.setClearEnabled(true);
                Order.setResponceback(true);
                getActivity().finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

package com.pac.ideatech.noordrinkingwater;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineOrderFragment extends Fragment implements View.OnClickListener {
private TextView tv_title,tv_price,tv_price_total;
private Button btn_min,btn_max,btn_addCart;
private EditText ed_qntyLabel;
int quantity, postion;
private double total_amount;
private Order order;
private ImageView img_Thumbnail;
    public OnlineOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_online_order, container, false);
        try {
            order=new Order();
            img_Thumbnail=view.findViewById(R.id.img_thumbnail);

            btn_min = (Button) view.findViewById(R.id.btn_min);
            ed_qntyLabel = (EditText) view.findViewById(R.id.ed_quantity);
            tv_price= (TextView) view.findViewById(R.id.tv_price);
            btn_max = (Button) view.findViewById(R.id.btn_max);
            tv_price_total= (TextView) view.findViewById(R.id.tv_price_total);
            tv_title= (TextView) view.findViewById(R.id.title_pro);

            btn_max.setOnClickListener(this);
            btn_min.setOnClickListener(this);
            btn_addCart= (Button) view.findViewById(R.id.btn_add_cart);
            //getting product position
            postion = getArguments().getInt("key");
            tv_title.setText(Product.ITEMS.get(postion).getTitle());
            tv_price.setText(Product.ITEMS.get(postion).getPrice()+" AED");
            Picasso.with(getActivity()).load(Product.ITEMS.get(postion).getImgURL()).networkPolicy(NetworkPolicy.NO_CACHE).into(img_Thumbnail);
           btn_addCart.setOnClickListener(this);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view==btn_min){
            if(quantity>=1)
            quantity--;

        }
        if(view==btn_max){
            quantity++;

        }
        ed_qntyLabel.setText(String.valueOf(quantity));
        try {
            double unit_price = Double.parseDouble(Product.ITEMS.get(postion).getPrice());
            int qnty = Integer.parseInt(ed_qntyLabel.getText().toString());
            total_amount=unit_price * qnty;
            tv_price_total.setText("Total Price: " + total_amount + " AED");

        }catch (Exception ex){
            ex.printStackTrace();
        }
            if (view == btn_addCart) {
                if (TextUtils.isEmpty(ed_qntyLabel.getText().toString()) || ed_qntyLabel.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "Select Quantity", Toast.LENGTH_SHORT).show();
                } else {
                    order.setProduct_id(Product.ITEMS.get(postion).getProduct_id());
                    order.setProdut_title(Product.ITEMS.get(postion).getTitle());
                    order.setUnit_price(Product.ITEMS.get(postion).getPrice());
                    order.setQnty(ed_qntyLabel.getText().toString());
                    order.setTotal_amount(total_amount);
                    order.setImgURI(Product.ITEMS.get(postion).getImgURL());
                    Order.orderList.add(order);
                    Order.setTotalPrice(total_amount);
                    Order.setNum_orders(1);
                    ((MainActivity) getActivity()).updateHotCount(Order.getNum_orders());
                    Log.w("Order####", order.getProduct_id() + "/" + order.getQnty());
                    ShowDialog();
                }
            }

    }

    void ShowDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
        builder.setTitle("Items added to Cart ("+Order.orderList.size()+")");
        builder.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);
        builder.setMessage("Please proceede to approperiate action.");
        builder.setPositiveButton("Check Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getActivity(),CheckoutActivity.class);
                intent.putExtra("from","none");
                getActivity().startActivityForResult(intent,2244);
            }
        });

        builder.setNegativeButton("Add more to Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //perform any action
                ProductFragment fragment=new ProductFragment();
                android.app.FragmentManager manager=getFragmentManager();
                android.app.FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.frag_container,fragment);
                transaction.commit();
                //Toast.makeText(getActivity(), "No clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //creating alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

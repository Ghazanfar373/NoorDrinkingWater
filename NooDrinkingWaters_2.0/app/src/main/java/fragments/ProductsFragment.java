package fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.frandydevs.apps.noordrinkingwater.CartAndOrderProcessActivity;
import com.frandydevs.apps.noordrinkingwater.MainActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;
import db.SQLiteDatabaseHelper;
import model.CartProductModel;
import model.ProductModel;
import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.DEVICE_WIDTH;
import static app.AppConfig.PREF_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends ParentFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ViewPager vpProducts;
    ProductsPagerAdapter productsPagerAdapter;
    ArrayList<ProductModel> productModelArrayList;
    ProductModel productModel;
    CartProductModel cartProductModel;
    LinearLayout llAddToCart;
    ImageView ivMore, ivLess;
    TextView tvQty, tvPrice, tvProductName;
    int qty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_products, container, false);
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(context);
        vpProducts = returnView.findViewById(R.id.vpProducts);
        llAddToCart = returnView.findViewById(R.id.llAddToCart);
        ivLess = returnView.findViewById(R.id.ivLess);
        tvQty = returnView.findViewById(R.id.tvQty);
        ivMore = returnView.findViewById(R.id.ivMore);
        tvProductName = returnView.findViewById(R.id.tvProductName);
        tvPrice = returnView.findViewById(R.id.tvPrice);
        productModelArrayList = new ArrayList<>();
        ViewGroup.LayoutParams layoutParams = vpProducts.getLayoutParams();
        layoutParams.height = DEVICE_WIDTH - (DEVICE_WIDTH / 3);
        vpProducts.setLayoutParams(layoutParams);
        getProductsServerRequest();
        return returnView;
    }

    private void getProductsServerRequest() {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "get_products");
            params.put("from", "app");
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 14, this, null, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        int searchQueryMatchedModelPosition = 0;
        boolean searchQueryMatchedModelPositionFound = false;
        try {
            productModelArrayList.clear();
            JSONArray productsJArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < productsJArray.length(); i++) {
                JSONObject productJObject = productsJArray.getJSONObject(i);
                productModel = new ProductModel();
                productModel.setId(productJObject.getString("id"));
                productModel.setName(productJObject.getString("name"));
                if (!searchQueryMatchedModelPositionFound && productModel.getName().toLowerCase().contains(mListener.getProductsSearchQuery().toLowerCase())) {
                    searchQueryMatchedModelPosition = i;
                    searchQueryMatchedModelPositionFound = true;
                }
                mListener.setProductsSearchQuery("");
                productModel.setUnit(productJObject.getString("unit"));
                productModel.setPrice(productJObject.getString("price"));
                productModel.setImage(productJObject.getString("image"));
                productModelArrayList.add(productModel);
            }
            if (!productModelArrayList.isEmpty()) {
                vpProducts.setClipToPadding(false);
                vpProducts.setPadding(DEVICE_WIDTH / 6, 0, DEVICE_WIDTH / 6, 0);
                vpProducts.setPageMargin(DEVICE_WIDTH / 54);
                productsPagerAdapter = new ProductsPagerAdapter();
                vpProducts.setAdapter(productsPagerAdapter);
                vpProducts.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {

                    }

                    @Override
                    public void onPageSelected(int i) {
                        populateProductDetail();
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });
                ivLess.setOnClickListener(this);
                ivMore.setOnClickListener(this);
                llAddToCart.setOnClickListener(this);
                productModel = productModelArrayList.get(searchQueryMatchedModelPosition);
                vpProducts.setCurrentItem(searchQueryMatchedModelPosition);
                tvProductName.setText(productModel.getName());
                tvPrice.setText(productModel.getPrice() + " " + getString(R.string.aed_each));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast(getString(R.string.something_wrong), 1, 17);
        }
    }

    public class ProductsPagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ProductsPagerAdapter() {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return productModelArrayList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View parentLayout = inflater.inflate(R.layout.lv_products, view, false);

            assert parentLayout != null;
            ImageView imageView = parentLayout
                    .findViewById(R.id.ivProduct);
            TextView title = parentLayout
                    .findViewById(R.id.tvTitle);
            productModel = productModelArrayList.get(position);
            title.setText(productModel.getName());
            String imageUrl = productModel.getImage();
            if (imageUrl.equalsIgnoreCase("null") || imageUrl.equalsIgnoreCase("")) {
                Glide.with(context).load(R.drawable.ic_products).into(imageView);
            } else {
                Picasso.get().load(imageUrl).placeholder(R.drawable.ic_products).error(R.drawable.ic_products).into(imageView);
            }
            parentLayout.setTag(position);
            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = (int) v.getTag();
                    vpProducts.setCurrentItem(clickedPosition);
                }
            });
            view.addView(parentLayout, 0);
            return parentLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMore:
                if (productModelArrayList.isEmpty()) {
                    showToast("Plz select a product.", 1, 17);
                    return;
                }
                qty++;
                tvQty.setText(String.valueOf(qty));
                break;
            case R.id.ivLess:
                if (productModelArrayList.isEmpty()) {
                    showToast("Plz select a product.", 1, 17);
                    return;
                }
                if (qty > 0) {
                    qty--;
                    tvQty.setText(String.valueOf(qty));
                }
                break;
            case R.id.llAddToCart:
                if (tvQty.getText().toString().equalsIgnoreCase("0")) {
                    showToast("Please select quantity.", 1, 17);
                    return;
                }
                //add to cart
                productModel = productModelArrayList.get(vpProducts.getCurrentItem());
                cartProductModel = new CartProductModel();
                cartProductModel.setId(productModel.getId());
                cartProductModel.setName(productModel.getName());
                cartProductModel.setImage(productModel.getImage());
                cartProductModel.setQty(qty);
                String priceStr = productModel.getPrice();
                if (priceStr.equalsIgnoreCase("") || priceStr.equalsIgnoreCase("null")) {
                    priceStr = "0";
                }
                double price = Double.parseDouble(priceStr);
                double totalAmount = qty * price;
                cartProductModel.setPrice(price);
                cartProductModel.setTotalAmount(totalAmount);
                sqLiteDatabaseHelper.insertProductIntoCart(cartProductModel);
                mListener.updateCartCounter();
                qty = 0;
                tvQty.setText(String.valueOf(0));
                //show item added dialog
                showItemAddedToCartDialog(cartProductModel.getImage());
                break;
        }
    }

    private void populateProductDetail() {
        productModel = productModelArrayList.get(vpProducts.getCurrentItem());
        tvPrice.setText(productModel.getPrice() + " " + getString(R.string.aed_each));
        tvProductName.setText(productModel.getName());
//        qty = sqLiteDatabaseHelper.geProductQty(productModel.getId());
        qty = 0;
        tvQty.setText(String.valueOf(qty));
    }

    private void showItemAddedToCartDialog(String imageUrl) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog_FullScreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_item_added_to_cart);
        dialog.setCancelable(false);
        final Button btnContinueShopping = dialog.findViewById(R.id.btnContinueShopping);
        final Button btnGotToCart = dialog.findViewById(R.id.btnGotToCart);
        final ImageView ivProduct = dialog.findViewById(R.id.ivProduct);
        if (imageUrl.equalsIgnoreCase("null") || imageUrl.equalsIgnoreCase("")) {
            Picasso.get().load(R.drawable.ic_bottle_blue).into(ivProduct);
        } else {
            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_bottle_blue).error(R.drawable.ic_bottle_blue).into(ivProduct);
        }
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnGotToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent goToCartIntent = new Intent(context, CartAndOrderProcessActivity.class);
                ((MainActivity) context).startActivityForResult(goToCartIntent, ((MainActivity) context).CART_INTENT_REQUEST_CODE);
            }
        });
        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class CoverFlowAdapter extends BaseAdapter {

        private ArrayList<String> mData = new ArrayList<>(0);
        private Context mContext;

        public CoverFlowAdapter(Context context) {
            mContext = context;
        }

        public void setData(ArrayList<String> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int pos) {
            return "";
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.lv_products, null);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.image = (ImageView) rowView
                        .findViewById(R.id.ivProduct);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();

            holder.image.setImageResource(R.drawable.bg_sp_offers);

            return rowView;
        }


        class ViewHolder {
            public ImageView image;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void updateCartCounter();

        void setProductsSearchQuery(String query);

        String getProductsSearchQuery();
    }
}

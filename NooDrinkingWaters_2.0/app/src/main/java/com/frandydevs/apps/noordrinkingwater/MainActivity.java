package com.frandydevs.apps.noordrinkingwater;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import adapters.DrawerAdapter;
import app.AppConfig;
import fragments.BasicProfileFragment;
import fragments.ChangePasswordFragment;
import fragments.DashBoardFragment;
import fragments.MyOrdersFragment;
import fragments.MyOrdersParentFragment;
import fragments.NewOffersFragment;
import fragments.NotificationsFragment;
import fragments.ProductsFragment;
import fragments.ProfileAddressFragment;
import fragments.ProfileFragment;
import helper.CountDrawable;
import model.DrawerModel;

import static app.AppConfig.IMAGE_DIRECTORY;
import static app.AppConfig.IMAGE_PROFILE_NAME;
import static app.AppConfig.PREF_USER_EMAIL;
import static app.AppConfig.PREF_USER_NAME;
import static app.AppConfig.PREF_USER_PROFILE_IMAGE;

public class MainActivity extends ParentActivity implements View.OnClickListener,
        DashBoardFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        BasicProfileFragment.OnFragmentInteractionListener, ChangePasswordFragment.OnFragmentInteractionListener,
        ProfileAddressFragment.OnFragmentInteractionListener, MyOrdersFragment.OnFragmentInteractionListener,
        ProductsFragment.OnFragmentInteractionListener, NewOffersFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener, MyOrdersParentFragment.OnFragmentInteractionListener {

    FragmentManager fragmentManager;
    RecyclerView rvMenu;
    LinearLayoutManager linearLayoutManager;
    DrawerAdapter drawerAdapter;
    ArrayList<DrawerModel> drawerModelArrayList;
    DrawerModel drawerModel;
    String[] drawerTitles = {"Home", "Suggestion", "Complaint","Wallet", "Rate Us", "Logout"};
    int[] drawerLogos = {R.drawable.ic_home, R.drawable.ic_suggestion, R.drawable.ic_complain,R.drawable.baseline_account_balance_wallet_white_18dp, R.drawable.ic_rate_us,
            R.drawable.ic_logout};
    DrawerLayout drawer;
    long back_pressed;
    ImageView ivBG, ivProfile, ivCam;
    LinearLayout llBottomBar, llProducts, llNewOffers, llMyOrders, llProfile;
    TextView tvName, tvEmail;

    //for action bar cart badge
    LayerDrawable cartActionBarDrawable;
    CountDrawable cartCounterBadge;

    String productsSearchQuery = "";
    public int CART_INTENT_REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        rvMenu = findViewById(R.id.rvMenu);
        ivProfile = findViewById(R.id.ivProfile);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        llBottomBar = findViewById(R.id.llBottomBar);
        llProducts = findViewById(R.id.llProducts);
        llNewOffers = findViewById(R.id.llNewOffers);
        llMyOrders = findViewById(R.id.llMyOrders);
        llProfile = findViewById(R.id.llProfile);
        ivCam = findViewById(R.id.ivCam);

        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.llMainContainer, DashBoardFragment.newInstance("", ""), "DashBoardFragment")
                .commit();

        drawerModelArrayList = new ArrayList<>();
        for (int i = 0; i < drawerTitles.length; i++) {
            drawerModel = new DrawerModel();
            drawerModel.setTitle(drawerTitles[i]);
            drawerModel.setLogo(drawerLogos[i]);
            drawerModelArrayList.add(drawerModel);
        }
        drawerModelArrayList.get(0).setSelected(true);

        linearLayoutManager = new LinearLayoutManager(this);
        drawerAdapter = new DrawerAdapter(this, drawerModelArrayList);
        rvMenu.setLayoutManager(linearLayoutManager);
        rvMenu.setAdapter(drawerAdapter);

        Picasso.get().load(R.drawable.ic_add_another_picture).into(ivCam);
        ivCam.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvEmail.setOnClickListener(this);
        llProducts.setOnClickListener(this);
        llNewOffers.setOnClickListener(this);
        llMyOrders.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        updateDrawerUserInfo();
    }

    private void updateDrawerUserInfo() {
        try {
            try {
                String imageUrl = dataSaving.getPrefValue(context, PREF_USER_PROFILE_IMAGE);
                Glide.with(context).load(imageUrl).apply(new RequestOptions().error(R.drawable.ic_user_profile_image)
                        .placeholder(R.drawable.ic_user_profile_image)).into(ivProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvName.setText(String.valueOf(dataSaving.getPrefValue(context, PREF_USER_NAME)));
        tvEmail.setText(String.valueOf(dataSaving.getPrefValue(context, PREF_USER_EMAIL)));
        if (tvName.getText().toString().isEmpty()) {
            tvName.setVisibility(View.GONE);
        } else {
            tvName.setVisibility(View.VISIBLE);
        }
        if (tvEmail.getText().toString().isEmpty()) {
            tvEmail.setVisibility(View.GONE);
        } else {
            tvEmail.setVisibility(View.VISIBLE);
        }
    }

    public void performDrawerMenuClickAction(int clickedPosition) {
        if (clickedPosition == 0) {
            setItemSelected(clickedPosition);
        }
        switch (clickedPosition) {
            case 0:
                goToHome();
                break;
            case 1:
                Intent suggestionIntent = new Intent(context, SuggestionComplaintActivity.class);
                suggestionIntent.putExtra("viewType", "Suggestion");
                startActivity(suggestionIntent);
                break;
            case 2:
                Intent complainIntent = new Intent(context, SuggestionComplaintActivity.class);
                complainIntent.putExtra("viewType", "Complaint");
                startActivity(complainIntent);
                break;
            case 3:
                Intent walletIntent = new Intent(context, WalletActivity.class);
                walletIntent.putExtra("viewType", "Complaint");
                startActivity(walletIntent);
                break;
            case 4:
                rateApp();
                /*Intent rateUsIntent = new Intent(context, RateUsActivity.class);
                startActivity(rateUsIntent);*/
                break;
            case 5:
                drawer.closeDrawer(Gravity.START);
                showMessageDialog(getString(R.string.logout_message), "", getString(R.string.yes),
                        getString(R.string.no), "logout", "dismiss");
                break;
        }
    }

    public void goToHome() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        drawer.closeDrawer(Gravity.START);
        setTitle(getString(R.string.home));
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
            if (fragmentManager.getBackStackEntryCount() == 0) {
                setTitle(getString(R.string.home));
                llBottomBar.setVisibility(View.GONE);
            }
        } else if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                if (!dataSaving.getPrefValue(context, AppConfig.PREF_IS_REMEMBER_LOGIN).equalsIgnoreCase("true")) {
                    dataSaving.setPrefValue(context, AppConfig.PREF_IS_LOGIN, "false");
                }
                super.onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), R.string.exit_message, Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
            }
        }
    }

    public void setItemSelected(int clickedPosition) {
        for (int i = 0; i < drawerModelArrayList.size(); i++) {
            if (i != clickedPosition) {
                drawerModelArrayList.get(i).setSelected(false);
            }
        }
        drawerModelArrayList.get(clickedPosition).setSelected(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void updateCartCounter() {
        setCartCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        MenuItem menuItem = menu.findItem(R.id.actionCart);
        cartActionBarDrawable = (LayerDrawable) menuItem.getIcon();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCartCount();
        return super.onPrepareOptionsMenu(menu);
    }

    public void setCartCount() {
        if (cartActionBarDrawable != null) {
            String count = String.valueOf(sqLiteDatabaseHelper.getTotalCartCounter());
            // Reuse drawable if possible
            Drawable reuse = cartActionBarDrawable.findDrawableByLayerId(R.id.ic_group_count);
            if (reuse != null && reuse instanceof CountDrawable) {
                cartCounterBadge = (CountDrawable) reuse;
            } else {
                cartCounterBadge = new CountDrawable(context);
            }

            cartCounterBadge.setCount(count);
            cartActionBarDrawable.mutate();
            cartActionBarDrawable.setDrawableByLayerId(R.id.ic_group_count, cartCounterBadge);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartCount();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionCart:
                Intent cartIntent = new Intent(context, CartAndOrderProcessActivity.class);
                startActivityForResult(cartIntent, CART_INTENT_REQUEST_CODE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CART_INTENT_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            goToHome();
        }
    }

    @Override
    public void updateUserInfoInDrawer() {
        updateDrawerUserInfo();
    }

    @Override
    public void onDashBoardMenuClick(String menu) {
        switch (menu) {
            case "products":
                if (!(fragmentManager.findFragmentById(R.id.llMainContainer) instanceof ProductsFragment)) {
                    goToHome();
                    setTitle(getString(R.string.products));
                    llBottomBar.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).
                            replace(R.id.llMainContainer, ProductsFragment.newInstance("", ""), "ProductsFragment")
                            .addToBackStack("ProductsFragment").commit();
                    updateBottomBarMenu(0);
                }
                break;
            case "newOffers":
                if (!(fragmentManager.findFragmentById(R.id.llMainContainer) instanceof NewOffersFragment)) {
                    goToHome();
                    setTitle(getString(R.string.new_offers));
                    llBottomBar.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).
                            replace(R.id.llMainContainer, NewOffersFragment.newInstance("", ""), "NewOffersFragment")
                            .addToBackStack("NewOffersFragment").commit();
                    updateBottomBarMenu(1);
                }
                break;
            case "myOrders":
                if (!(fragmentManager.findFragmentById(R.id.llMainContainer) instanceof MyOrdersFragment)) {
                    goToHome();
                    setTitle(getString(R.string.my_orders));
                    llBottomBar.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).
                            replace(R.id.llMainContainer, MyOrdersFragment.newInstance("", ""), "MyOrdersFragment")
                            .addToBackStack("MyOrdersFragment").commit();
                    updateBottomBarMenu(2);
                }
                break;
            case "notifications":
                setTitle(getString(R.string.notifications));
                llBottomBar.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).
                        replace(R.id.llMainContainer, NotificationsFragment.newInstance("", ""), "NotificationsFragment")
                        .addToBackStack("NotificationsFragment").commit();
                updateBottomBarMenu(-1);
                break;
            case "myProfile":
                if (!(fragmentManager.findFragmentById(R.id.llMainContainer) instanceof ProfileFragment)) {
                    goToHome();
                    setTitle(getString(R.string.my_profile));
                    llBottomBar.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).
                            replace(R.id.llMainContainer, ProfileFragment.newInstance("", ""), "ProfileFragment")
                            .addToBackStack("ProfileFragment").commit();
                    updateBottomBarMenu(3);
                }
                break;
        }
    }

    public String getProductsSearchQuery() {
        return productsSearchQuery;
    }

    public void setProductsSearchQuery(String productsSearchQuery) {
        this.productsSearchQuery = productsSearchQuery;
    }

    private void updateBottomBarMenu(int position) {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout, llMenus;
        llMenus = (LinearLayout) llBottomBar.getChildAt(1);
        for (int i = 0; i < llMenus.getChildCount(); i++) {
            linearLayout = (LinearLayout) llMenus.getChildAt(i);
            imageView = (ImageView) linearLayout.getChildAt(0);
            textView = (TextView) linearLayout.getChildAt(1);
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorTintBottomBarNormal),
                    PorterDuff.Mode.MULTIPLY);
            textView.setTextColor(getResources().getColor(R.color.colorTintBottomBarNormal));
        }
        if (position != -1) {
            linearLayout = (LinearLayout) llMenus.getChildAt(position);
            imageView = (ImageView) linearLayout.getChildAt(0);
            textView = (TextView) linearLayout.getChildAt(1);
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorTintBottomBarSelected),
                    PorterDuff.Mode.MULTIPLY);
            textView.setTextColor(getResources().getColor(R.color.colorTintBottomBarSelected));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfile:
            case R.id.ivCam:
            case R.id.tvName:
            case R.id.tvEmail:
            case R.id.llProfile:
                drawer.closeDrawer(Gravity.START);
                onDashBoardMenuClick("myProfile");
                break;
            case R.id.llProducts:
                onDashBoardMenuClick("products");
                break;
            case R.id.llNewOffers:
                onDashBoardMenuClick("newOffers");
                break;
            case R.id.llMyOrders:
                onDashBoardMenuClick("myOrders");
                break;
        }
    }
}

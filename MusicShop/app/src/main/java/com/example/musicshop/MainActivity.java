package com.example.musicshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicshop.entity.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView quantityTextView;
    private TextView priceTextView;
    private EditText userNameEditText;
    private Spinner spinner;
    private int count = 0;
    private ArrayList<String> spinnerArrayList;
    private ArrayAdapter<String> spinnerAdapter;
    private HashMap<String, Double> goodsMap;
    private String goodsName;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация элементов UI
        quantityTextView = findViewById(R.id.countTextView);
        priceTextView = findViewById(R.id.priceTextView);
        spinner = findViewById(R.id.spinner);

        // Настройка Spinner
        setupSpinner();

        // Заполнение данных товарами и их ценами
        setupGoodsMap();
    }

    private void setupSpinner() {
        spinner.setOnItemSelectedListener(this);
        spinnerArrayList = new ArrayList<>();
        spinnerArrayList.add("guitar");
        spinnerArrayList.add("keyboard");
        spinnerArrayList.add("drums");

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void setupGoodsMap() {
        goodsMap = new HashMap<>();
        goodsMap.put("guitar", 400.0);
        goodsMap.put("keyboard", 20000.0);
        goodsMap.put("drums", 10000.0);
    }

    public void increaseQuantity(View view) {
        count++;
        updateQuantityAndPrice();
    }

    public void decreaseQuantity(View view) {
        if (count > 0) {
            count--;
            updateQuantityAndPrice();
        }
    }

    private void updateQuantityAndPrice() {
        quantityTextView.setText(String.valueOf(count));
        priceTextView.setText(String.format("%.2f $", count * price));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        goodsName = spinner.getSelectedItem().toString();
        price = goodsMap.get(goodsName);
        updateQuantityAndPrice();
        updateGoodsImage();
    }

    private void updateGoodsImage() {
        ImageView goodsImageView = findViewById(R.id.goodsImageView);
        switch (goodsName) {
            case "keyboard":
                goodsImageView.setImageResource(R.drawable.piano);
                break;
            case "drums":
                goodsImageView.setImageResource(R.drawable.drums);
                break;
            default:
                goodsImageView.setImageResource(R.drawable.guitar);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Ничего не делать
    }

    public void addToCart(View view) {
        Order order = new Order();
        userNameEditText = findViewById(R.id.editText);
        order.setUserName(userNameEditText.getText().toString());
        order.setGoodsName(goodsName);
        order.setQuantity(count);
        order.setOrderPrice(price * count);
        order.setPrice(price);
        Log.d("Order: {}", String.valueOf(order));

        Intent orderIntent = new Intent(MainActivity.this, OrderActivity.class);
        orderIntent.putExtra("userName", order.getUserName());
        orderIntent.putExtra("goodsName", order.getGoodsName());
        orderIntent.putExtra("quantity", order.getQuantity());
        orderIntent.putExtra("orderPrice", order.getOrderPrice());
        orderIntent.putExtra("price", order.getPrice());
        startActivity(orderIntent);
    }
}

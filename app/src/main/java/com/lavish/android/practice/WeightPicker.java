package com.lavish.android.practice;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.lavish.android.practice.databinding.WeightPickerDialogBinding;
import com.lavish.android.practice.models.Cart;
import com.lavish.android.practice.models.Product;

import androidx.appcompat.app.AlertDialog;

public class WeightPicker {

    private WeightPickerDialogBinding b;
    private Cart cart;
    private Product product;

    public void show(Context context, Cart cart, Product product, final OnWeightPickedListener listener){
        b = WeightPickerDialogBinding.inflate(
                LayoutInflater.from(context)
        );
        this.cart = cart;
        this.product = product;

        new AlertDialog.Builder(context)
                .setTitle("Pick Weight")
                .setView(b.getRoot())
                .setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Replace 0s & assign kg & g values from respective NumberPickers
//                        int kg = 0, g = 0;
                        int kg = b.numberPickerKg.getValue();
                        int g = b.numberPickerG.getValue();

                        // Add GuardCode to prevent user from selecting 0kg 0g. If so, then return
                        if(kg == 0 && g == 0) {
                            return;
                        }

                        changeInCart(kg + (g/1000f));
                        listener.onWeightPicked(kg, g);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onWeightPickerCancelled();
                    }
                })
                .show();

        setupNumberPickers();
    }

    private void changeInCart(float qty) {
        cart.updateWeightBasedProductQuantity(product, qty);
    }

    private void setupNumberPickers() {
        // Define this method to setup kg & g NumberPickers as per the given ranges
        //kg Range - 0kg to 10kg
        //g Range - 0g to 950g
        float quantity = product.minQty;
        b.numberPickerKg.setMinValue((int) (quantity / 1000));
        b.numberPickerKg.setMaxValue(10);
        b.numberPickerG.setMinValue(((int) (quantity % 1000)) / 50);
        b.numberPickerG.setMaxValue(19);

        b.numberPickerKg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + " " + "kg";
            }
        });

        b.numberPickerG.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return (value * 50) + " " + "g";
            }
        });

        updateFirstItemViewInNumberPicker(b.numberPickerKg);
        updateFirstItemViewInNumberPicker(b.numberPickerG);

    }

    private void updateFirstItemViewInNumberPicker(NumberPicker p) {
        View firstItem = p.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

    interface OnWeightPickedListener{
        void onWeightPicked(int kg, int g);
        void onWeightPickerCancelled();
    }

}

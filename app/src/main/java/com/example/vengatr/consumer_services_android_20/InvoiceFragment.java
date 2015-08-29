package com.example.vengatr.consumer_services_android_20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vengatr.consumer_services_android_20.model.Invoice;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetInvoice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by vengat.r on 8/27/2015.
 */
public class InvoiceFragment extends Fragment implements View.OnClickListener {

    private Button billButton;
    private Button cancelButton;
    private Button acceptButton;

    private EditText materialChargesEditText;
    private EditText labourChargesEditText;
    private EditText couponCodeEditText;


    private TextView materialChargesTextView;
    private TextView labourChargesTextView;
    private TextView couponCodeTextView;
    private TextView invoiceConfirmationTextView;
    private TextView totalChargesTextView;
    private TextView discountedLabourChargesTextView;
    private TextView discountedMaterialChargesTextView;
    private TextView discountedTotalChargesTextView;

    private TextView totalChargesVal,
            discountedLabourChargesVal,
            discountedMaterialChargesVal,
            discountedTotalChargesVal;

    private View fragmentView;
    private Activity context;
    private static final String TAG = "InvoiceFragment";

    private ProgressDialog progressDialog;

    private String materialChargesAmount, labourChargesAmount;
    private String couponCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        Log.i(TAG, "About to create the view of fragment");

        View view = inflater.inflate(R.layout.fragment_invoice_form, container, false);
        fragmentView = view;

        materialChargesEditText = (EditText) view.findViewById(R.id.material_charges);
        labourChargesEditText = (EditText) view.findViewById(R.id.labour_charges);
        couponCodeEditText = (EditText) view.findViewById(R.id.coupon_code);

        materialChargesTextView = (TextView) view.findViewById(R.id.material_charges_text);
        labourChargesTextView = (TextView) view.findViewById(R.id.labour_charges_text);
        couponCodeTextView = (TextView) view.findViewById(R.id.coupon_code_text);
        invoiceConfirmationTextView = (TextView) view.findViewById(R.id.invoice_confirmation);
        invoiceConfirmationTextView.setVisibility(View.GONE);
        totalChargesTextView = (TextView) view.findViewById(R.id.total_charges_text);
        totalChargesTextView.setVisibility(View.GONE);
        discountedLabourChargesTextView = (TextView) view.findViewById(R.id.discounted_labour_charges_text);
        discountedLabourChargesTextView.setVisibility(View.GONE);
        discountedMaterialChargesTextView = (TextView) view.findViewById(R.id.discounted_material_charges_text);
        discountedMaterialChargesTextView.setVisibility(View.GONE);
        discountedTotalChargesTextView = (TextView) view.findViewById(R.id.discounted_total_charges_text);
        discountedTotalChargesTextView.setVisibility(View.GONE);
        totalChargesVal = (TextView) view.findViewById(R.id.total_charges);
        totalChargesVal.setVisibility(View.GONE);
        discountedLabourChargesVal = (TextView) view.findViewById(R.id.discounted_labour_charges);
        discountedLabourChargesVal.setVisibility(View.GONE);
        discountedMaterialChargesVal = (TextView) view.findViewById(R.id.discounted_material_charges);
        discountedMaterialChargesVal.setVisibility(View.GONE);
        discountedTotalChargesVal = (TextView) view.findViewById(R.id.discounted_total_charges);
        discountedTotalChargesVal.setVisibility(View.GONE);

        billButton = (Button) view.findViewById(R.id.invoice_button);
        billButton.setOnClickListener(this);

        cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
        cancelButton.setVisibility(View.GONE);

        acceptButton = (Button) view.findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(this);
        acceptButton.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (context == null) context = activity;
        //if (!(activity instanceof ))
    }

    @Override
    public void onClick(View v) {
        couponCode = couponCodeEditText.getText().toString();
        materialChargesAmount = materialChargesEditText.getText().toString();
        labourChargesAmount = labourChargesEditText.getText().toString();

        if (materialChargesAmount == null || labourChargesAmount == null) return;

        if (v.getId() == R.id.invoice_button) {
            progressDialog.show();
            hideBillButton();
            disableEditTextFields();
            showAcceptButton();
            showCancelButton();
            showConfirmationText();
            progressDialog.dismiss();
        } else if (v.getId() == R.id.cancel_button) {
            progressDialog.show();
            enableEditTextFields();
            hideAcceptButton();
            hideCancelButton();
            showBillButton();
            hideConfirmationText();
            progressDialog.dismiss();
        } else if (v.getId() == R.id.accept_button) {
            progressDialog.show();

        }

    }

    public void enableEditTextFields() {
        materialChargesEditText.setVisibility(View.VISIBLE);
        materialChargesEditText.setEnabled(true);
        labourChargesEditText.setVisibility(View.VISIBLE);
        labourChargesEditText.setEnabled(true);
        couponCodeEditText.setVisibility(View.VISIBLE);
        couponCodeEditText.setEnabled(true);
    }

    public void disableEditTextFields() {
        materialChargesEditText.setVisibility(View.VISIBLE);
        materialChargesEditText.setEnabled(false);
        labourChargesEditText.setVisibility(View.VISIBLE);
        labourChargesEditText.setEnabled(false);
        couponCodeEditText.setVisibility(View.VISIBLE);
        couponCodeEditText.setEnabled(false);
    }

    public void showBillButton() {
        billButton.setVisibility(View.VISIBLE);
    }

    public void hideBillButton() {
        billButton.setVisibility(View.GONE);
    }

    public void showAcceptButton() {
        acceptButton.setVisibility(View.VISIBLE);
    }

    public void hideAcceptButton() {
        acceptButton.setVisibility(View.GONE);
    }

    public void showCancelButton() {
        cancelButton.setVisibility(View.VISIBLE);
    }

    public void hideCancelButton() {
        cancelButton.setVisibility(View.GONE);
    }

    public void showConfirmationText() {
        invoiceConfirmationTextView.setVisibility(View.VISIBLE);
    }

    public void hideConfirmationText() {
        invoiceConfirmationTextView.setVisibility(View.GONE);
    }

    public void showDiscountedTotalChargesOnly() {
        totalChargesTextView.setVisibility(View.VISIBLE);
        discountedLabourChargesTextView.setVisibility(View.GONE);
        discountedMaterialChargesTextView.setVisibility(View.GONE);
        discountedTotalChargesTextView.setVisibility(View.VISIBLE);
        totalChargesVal.setVisibility(View.VISIBLE);
        discountedLabourChargesVal.setVisibility(View.GONE);
        discountedMaterialChargesVal.setVisibility(View.GONE);
        discountedTotalChargesVal.setVisibility(View.VISIBLE);
    }

    public void showDiscountedTotalAndMaterialChargesOnly() {
        totalChargesTextView.setVisibility(View.VISIBLE);
        discountedLabourChargesTextView.setVisibility(View.GONE);
        discountedMaterialChargesTextView.setVisibility(View.VISIBLE);
        discountedTotalChargesTextView.setVisibility(View.VISIBLE);
        totalChargesVal.setVisibility(View.VISIBLE);
        discountedLabourChargesVal.setVisibility(View.GONE);
        discountedMaterialChargesVal.setVisibility(View.VISIBLE);
        discountedTotalChargesVal.setVisibility(View.VISIBLE);
    }

    public void showDiscountedTotalAndLabourChargesOnly() {
        totalChargesTextView.setVisibility(View.VISIBLE);
        discountedLabourChargesTextView.setVisibility(View.VISIBLE);
        discountedMaterialChargesTextView.setVisibility(View.GONE);
        discountedTotalChargesTextView.setVisibility(View.VISIBLE);
        totalChargesVal.setVisibility(View.VISIBLE);
        discountedLabourChargesVal.setVisibility(View.VISIBLE);
        discountedMaterialChargesVal.setVisibility(View.GONE);
        discountedTotalChargesVal.setVisibility(View.VISIBLE);
    }

    public void showDiscountedTotalMaterialAndLabourChargesOnly() {
        totalChargesTextView.setVisibility(View.VISIBLE);
        discountedLabourChargesTextView.setVisibility(View.VISIBLE);
        discountedMaterialChargesTextView.setVisibility(View.VISIBLE);
        discountedTotalChargesTextView.setVisibility(View.VISIBLE);
        totalChargesVal.setVisibility(View.VISIBLE);
        discountedLabourChargesVal.setVisibility(View.VISIBLE);
        discountedMaterialChargesVal.setVisibility(View.VISIBLE);
        discountedTotalChargesVal.setVisibility(View.VISIBLE);
    }

    private class DisplayInvoiceAsyncHttpTask extends AsyncTask<String, Void, Invoice> {

        @Override
        protected Invoice doInBackground(String... params) {
            Invoice inv = null;
            try {
                inv = new GetInvoice(context).getNewInvoice();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inv.setCouponCode(couponCode);
            inv.setMaterialCharges(new BigDecimal(materialChargesAmount));
            inv.setLabourCharges(new BigDecimal(labourChargesAmount));
            inv.setInvoiceDate(new Date());
            return null;
        }
    }

}

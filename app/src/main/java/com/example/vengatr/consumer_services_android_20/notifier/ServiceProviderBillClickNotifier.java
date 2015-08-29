package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.listener.ServiceProviderBillClickListener;

/**
 * Created by vengat.r on 8/29/2015.
 */
public class ServiceProviderBillClickNotifier {

    public ServiceProviderBillClickNotifier(ServiceProviderBillClickListener serviceProviderBillClickListener) {
        serviceProviderBillClickListener.showInvoiceFragment();
    }
}

package com.example.vengatr.consumer_services_android_20.dummy;

import com.example.vengatr.consumer_services_android_20.model.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vengat.r on 7/1/2015.
 */
public class JobListContent {

    /**
     * An array of sample (job) items.
     */
    public static List<Job> ITEMS = new ArrayList<Job>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Long, Job> JOB_ITEM_MAP =
            new HashMap<>();

    /*
    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "eBookFrenzy",
                "http://www.ebookfrenzy.com"));
        addItem(new DummyItem("2", "Google",
                "http://www.google.com"));
        addItem(new DummyItem("3", "Android",
                "http://www.android.com"));
    }
    */

    public void setJobs(List<Job> jobs) {
        for (Job job: jobs) {
            addItem(job);
        }
    }

    private static void addItem(Job item) {
        if (JOB_ITEM_MAP.get(item.getId()) != null) return;
        ITEMS.add(item);
        JOB_ITEM_MAP.put(item.getId(), item);
    }

}

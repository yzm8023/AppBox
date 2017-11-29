package com.smonline.virtual.client.badger;

import android.content.Intent;

import com.smonline.virtual.remote.BadgerInfo;

/**
 * @author Lody
 */
public interface IBadger {

    String getAction();

    BadgerInfo handleBadger(Intent intent);

}

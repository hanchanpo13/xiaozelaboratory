
package com.xiaozeze.staginglib.ui.dialog.globleDialog;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;

interface XZActivityLifecycleInterface {

    SparseArray<XZActivityLifecycleInterface> dialogPageMap = new SparseArray<>();

    void onCreate(Activity activity);

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

}

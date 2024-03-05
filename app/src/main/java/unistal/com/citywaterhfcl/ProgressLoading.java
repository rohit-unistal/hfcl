package unistal.com.citywaterhfcl;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressLoading {
    private final Context context;
    private ProgressDialog loadingDialog;

    public ProgressLoading(Context context) {
        this.context = context;
        this.loadingDialog = new ProgressDialog(context);
    }

    public void onShow() {
        loadingDialog = new ProgressDialog(context);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setTitle("Please wait...");
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
    }
    public void setMessage(){

        loadingDialog.setTitle("Sync Successfully");
    }

    public void dismiss() {
        loadingDialog.dismiss();
    }

    public boolean isShowing() {
        return loadingDialog.isShowing();
    }
}

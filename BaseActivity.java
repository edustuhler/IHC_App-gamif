package ihc.ihc_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import ihc.ihc_app.R;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    public void moveTo(Class c){
        moveTo(c, false);
    }

    public void moveTo(Class c, boolean finish) {
        startActivity(new Intent(this, c));
        if(finish) {
            finish();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}

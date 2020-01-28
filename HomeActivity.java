package mytestgallery.com.mytestgallery;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



public class HomeActivity extends AppCompatActivity {
    private static int IMG_RESULT = 1;
    String ImageDecode;
    ImageView imageViewLoad,compressedView;
    Button LoadImage;
    Intent intent;
    String[] FILE;

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (shouldAskPermissions()) {
            askPermissions();
        }

        imageViewLoad = (ImageView) findViewById(R.id.imageView1);
        compressedView = (ImageView) findViewById(R.id.compressedView);
        LoadImage = (Button)findViewById(R.id.button1);

        LoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, IMG_RESULT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();
                String[] FILE = { MediaStore.Images.Media.DATA };


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();

                imageViewLoad.setImageBitmap(BitmapFactory
                        .decodeFile(ImageDecode));
                /*compressedView.setImageBitmap(BitmapFactory
                        .decodeFile(ImageDecode));*/
                Context context = this;
                SiliCompressor sili = new SiliCompressor(context);
                compressedView.setImageBitmap(sili.getCompressBitmap(ImageDecode));


            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }




}

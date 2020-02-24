package com.alienware.scan2shop.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.scandit.datacapture.barcode.capture.BarcodeCapture;
import com.scandit.datacapture.barcode.capture.BarcodeCaptureListener;
import com.scandit.datacapture.barcode.capture.BarcodeCaptureSession;
import com.scandit.datacapture.barcode.capture.BarcodeCaptureSettings;
import com.scandit.datacapture.barcode.capture.SymbologySettings;
import com.scandit.datacapture.barcode.data.Barcode;
import com.scandit.datacapture.barcode.data.Symbology;
import com.scandit.datacapture.barcode.data.SymbologyDescription;
import com.scandit.datacapture.barcode.ui.overlay.BarcodeCaptureOverlay;
import com.scandit.datacapture.core.capture.DataCaptureContext;
import com.scandit.datacapture.core.data.FrameData;
import com.scandit.datacapture.core.source.Camera;
import com.scandit.datacapture.core.source.FrameSourceState;
import com.scandit.datacapture.core.ui.DataCaptureView;
import com.scandit.datacapture.core.ui.control.TorchSwitchControl;
import com.scandit.datacapture.core.ui.viewfinder.RectangularViewfinder;
import java.util.Arrays;
import java.util.HashSet;
/**
 * Created by henry cheruiyot on 3/1/2018.
 */

public class ScanActivity extends CameraPermissionActivity implements BarcodeCaptureListener {
    // Enter your Scandit License key here.
    // Your Scandit License key is available via your Scandit SDK web account.
    public static final String SCANDIT_LICENSE_KEY = "AQHeU5eBAy3OFL6nFgx0JBIu8gaEHSV623MQOh9k6/4cZLdwKmwqHXk5PMtuULKWpkCvyfV52+zAe+u9Q1FH/V0K7bnIVA/8Xh1jjYRC43OsT7hrbnIzhTk9JCDaTsyj3hNu6QMtYzMdQA1QMJPijl/PZ1w54sxDFWF/j7mTAHqCpcuoYIfquuXOhdo+RCaivj4SW+z2uWyqPtLxhFURBQfSM+HsOU8HxynU4Q1hJoHxZhwvOQMsOehj4w4LH6iY6AsP+bAzGizSQZXeAlT7Y7F10Mgrs+h/nUTNRhVM6FP6NMI8U11j1j+50TZRtkjnYttzJ1OxEOfeos9v1nku69s5tQUCjYc2UKYP+DIMXPXESWEkkURJZOR95YxwdlC+aiNa2uCLIvPzETzfW6JFSm1UcMOrLRqsWxffX/ESpqok+O1faxIMiL0hQi5/ANzA8TcP2HDhzxbOhwnWa5AqfACNGkcyBp1e1iED0GaiNVNVMagfpt0HU4keFAVQECPKgf18ONPrRmz/H77AsPf//HgF5np5Y6uofowLI5aWKhFhhK0m/h/lwIT50HPzJ7I6s7o5u6bcn6xNTDyLCt5lTUdRayFsRTN1pUJdJnHmtohB9OmkrSzIrPbgSyRv8NXBgI8aO+/y1jdfYcWjoIYLgZKNbSGC7A9tlRbH6Dm2dnWZBekKsoojpN61f3gEe4OIcbnY7RcQjaUPZHyk0Mf9r1ykKAo3lP1QOdNhlY1ome7zApRB1T4ZPJqxzdoFTrtTk4GYZxk0uI5cqV0MtbZikJxxtw5Dx/RkgwY03eKZDbq+Sh4/gliGr+C5mA==";
    private DataCaptureContext dataCaptureContext;
    private BarcodeCapture barcodeCapture;
    private Camera camera;
    private DataCaptureView dataCaptureView;
    private Toast mToast = null;
    private AlertDialog dialog;
    static final int RESULT_CODE=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize and start the barcode recognition.
        initializeAndStartBarcodeScanning();

    }

    private void initializeAndStartBarcodeScanning() {
        // Create data capture context using your license key.
        dataCaptureContext = DataCaptureContext.forLicenseKey(SCANDIT_LICENSE_KEY);


        // Use the default camera and set it as the frame source of the context.
        // The camera is off by default and must be turned on to start streaming frames to the data
        // capture context for recognition.
        // See resumeFrameSource and pauseFrameSource below.
        camera = Camera.getDefaultCamera();

        if (camera != null) {
            // Use the recommended camera settings for the BarcodeCapture mode.
            camera.applySettings(BarcodeCapture.createRecommendedCameraSettings());
            dataCaptureContext.setFrameSource(camera);
        } else {
            throw new IllegalStateException("Sample depends on a camera, which failed to initialize.");
        }

        // The barcode capturing process is configured through barcode capture settings
        // which are then applied to the barcode capture instance that manages barcode recognition.
        BarcodeCaptureSettings barcodeCaptureSettings = new BarcodeCaptureSettings();

        // The settings instance initially has all types of barcodes (symbologies) disabled.
        // For the purpose of this sample we enable a very generous set of symbologies.
        // In your own app ensure that you only enable the symbologies that your app requires as
        // every additional enabled symbology has an impact on processing times.
        HashSet<Symbology> symbologies = new HashSet<>();
        symbologies.add(Symbology.EAN13_UPCA);
        symbologies.add(Symbology.EAN8);
        symbologies.add(Symbology.UPCE);
        symbologies.add(Symbology.QR);


        barcodeCaptureSettings.enableSymbologies(symbologies);
      // Some linear/1d barcode symbologies allow you to encode variable-length data.
        // By default, the Scandit Data Capture SDK only scans barcodes in a certain length range.
        // If your application requires scanning of one of these symbologies, and the length is
        // falling outside the default range, you may need to adjust the "active symbol counts"
        // for this symbology. This is shown in the following few lines of code for one of the
        // variable-length symbologies.
        SymbologySettings symbologySettings =
                barcodeCaptureSettings.getSymbologySettings(Symbology.CODE39);

        HashSet<Short> activeSymbolCounts = new HashSet<>(
                Arrays.asList(new Short[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 }));

        symbologySettings.setActiveSymbolCounts(activeSymbolCounts);

        // Create new barcode capture mode with the settings from above.
        barcodeCapture = BarcodeCapture.forDataCaptureContext(dataCaptureContext, barcodeCaptureSettings);

        // Register self as a listener to get informed whenever a new barcode got recognized.
        barcodeCapture.addListener(this);

        // To visualize the on-going barcode capturing process on screen, setup a data capture view
        // that renders the camera preview. The view must be connected to the data capture context.
        dataCaptureView = DataCaptureView.newInstance(this, dataCaptureContext);

        // Add a barcode capture overlay to the data capture view to render the location of captured
        // barcodes on top of the video preview.
        // This is optional, but recommended for better visual feedback.
        dataCaptureView.addControl(new TorchSwitchControl(getApplicationContext()));
        BarcodeCaptureOverlay overlay = BarcodeCaptureOverlay.newInstance(barcodeCapture, dataCaptureView);
        overlay.setViewfinder(new RectangularViewfinder());
        setContentView(dataCaptureView);
    }
    @Override
    protected void onPause() {
        pauseFrameSource();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        barcodeCapture.removeListener(this);
        dataCaptureContext.removeMode(barcodeCapture);
        super.onDestroy();
    }

    private void pauseFrameSource() {
        // Switch camera off to stop streaming frames.
        // The camera is stopped asynchronously and will take some time to completely turn off.
        // Until it is completely stopped, it is still possible to receive further results, hence
        // it's a good idea to first disable barcode capture as well.
        barcodeCapture.setEnabled(false);
        camera.switchToDesiredState(FrameSourceState.OFF, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        requestCameraPermission();
    }

    @Override
    public void onCameraPermissionGranted() {
        resumeFrameSource();
    }

    private void resumeFrameSource() {
        dismissScannedCodesDialog();

        // Switch camera on to start streaming frames.
        // The camera is started asynchronously and will take some time to completely turn on.
        barcodeCapture.setEnabled(true);
        camera.switchToDesiredState(FrameSourceState.ON, null);
    }

    private void dismissScannedCodesDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void showResult(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.setCancelable(false)
                .setTitle(result)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                barcodeCapture.setEnabled(true);
                            }
                        })
                .create();
        dialog.show();
    }

    @Override
    public void onBarcodeScanned(
            @NonNull BarcodeCapture barcodeCapture,
            @NonNull BarcodeCaptureSession session,
            @NonNull FrameData frameData
    ) {
        if (session.getNewlyRecognizedBarcodes().isEmpty()) return;

        final Barcode barcode = session.getNewlyRecognizedBarcodes().get(0);

        // Stop recognizing barcodes for as long as we are displaying the result. There won't be any
        // new results until the capture mode is enabled again. Note that disabling the capture mode
        // does not stop the camera, the camera continues to stream frames until it is turned off.
        barcodeCapture.setEnabled(false);

        // If you are not disabling barcode capture here and want to continue scanning, consider
        // setting the codeDuplicateFilter when creating the barcode capture settings to around 500
        // or even -1 if you do not want codes to be scanned more than once.

        // Get the human readable name of the symbology and assemble the result to be shown.
        String symbology = SymbologyDescription.create(barcode.getSymbology()).getReadableName();
        final String result = "Scanned: " + barcode.getData() + " (" + symbology + ")";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.putExtra("barcodeData",barcode.getData());
                setResult(RESULT_CODE,intent);
                finish();
                //showResult(result);
            }
        });
    }

    @Override
    public void onSessionUpdated(@NonNull BarcodeCapture barcodeCapture,
                                 @NonNull BarcodeCaptureSession session, @NonNull FrameData data) {}

    @Override
    public void onObservationStarted(@NonNull BarcodeCapture barcodeCapture) {}

    @Override
    public void onObservationStopped(@NonNull BarcodeCapture barcodeCapture) {}
    @Override
    public void onBackPressed() {
        finish();
    }
}


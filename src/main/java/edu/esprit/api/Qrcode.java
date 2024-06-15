package edu.esprit.api;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Qrcode {


    public static BitMatrix generateQRCode(String data) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        return qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200, hints);
    }

    public static void displayQRCode(BitMatrix bitMatrix) throws IOException {
        File outputFile = new File("qrcode.png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", outputFile.toPath());
        System.out.println("QR Code saved to: " + outputFile.getAbsolutePath());
    }
}
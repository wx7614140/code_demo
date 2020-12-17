package org.febsteam.demos.totp.util;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author : wx
 * @version : 1
 * @date : 2020/12/17 10:44
 */
public class FebsQRGenerator {
    /**
     * 生成base64格式的QRcode字符串
     * @param content
     * @return
     */
    public static String geneQRCode(String content) {
        QrConfig config = new QrConfig();
        // 高纠错级别
        config.setErrorCorrection(ErrorCorrectionLevel.M);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(0);
        config.setHeight(200);
        config.setWidth(200);
        return QrCodeUtil.generateAsBase64(content,config,"png");
    }
    public static void geneQRCode(String content, OutputStream outputStream) {
        QrConfig config = new QrConfig();
        // 高纠错级别
        config.setErrorCorrection(ErrorCorrectionLevel.M);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(0);
        config.setHeight(200);
        config.setWidth(200);
        QrCodeUtil.generate(content,config,"png",outputStream);
    }
    public static String geneQRCode(String issuer, String accountName, GoogleAuthenticatorKey credentials) throws UnsupportedEncodingException {
        return geneQRCode(GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer,accountName,credentials));
    }
    public static void geneQRCode(String issuer, String accountName, GoogleAuthenticatorKey credentials,OutputStream outputStream) throws UnsupportedEncodingException {
        geneQRCode(GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer,accountName,credentials),outputStream);
    }
}

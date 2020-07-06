package com.longyingqing.qrcode.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

/**
 * 描述：当前类说说明
 * @author 刘梓江
 * @date 2020/7/6  14:08
 */
@SuppressWarnings("all")
public class QRCodeUtils {

    //字符编码
    private static final String CHARSET = "utf-8";
    //图片格式
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 180;
    // LOGO宽度
    private static final int LOGIN_WIDTH = 70;
    // LOGO高度
    private static final int LOGIN_HEIGHT = 70;

    /**
     * 生成二维码
     * @param content	    源内容
     * @param needCompress	是否要压缩
     * @return		        返回二维码图片
     * @throws Exception
     */
    public static BufferedImage createImage(String content, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        //容错级别为H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        //其他参数，如字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);

        //白边的宽度，可取0~4
        hints.put(EncodeHintType.MARGIN,0);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);

        //删除二维码周围的白边
        //bitMatrix = deleteWhite(bitMatrix);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        // 插入图片
        QRCodeUtils.insertImage(image, needCompress);
        return image;
    }

    /**
     * 在生成的二维码中插入图片
     * @param source
     * @param imgPath
     * @param needCompress
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, boolean needCompress) throws Exception {

        String currentFilePath = QRCodeUtils.class.getResource("/").getPath();
        File file = new File(new File(currentFilePath).getAbsolutePath(), "icon/logo.png");
        if (!file.exists()) {
            System.err.println("该文件不存在！");
            return;
        }
        Image src = ImageIO.read(file);
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > LOGIN_WIDTH) {
                width = LOGIN_WIDTH;
            }
            if (height > LOGIN_HEIGHT) {
                height = LOGIN_HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     *  删除生成的二维码周围的白边，根据审美决定是否删除
     * @param matrix BitMatrix对象
     * @return BitMatrix对象
     * */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
}

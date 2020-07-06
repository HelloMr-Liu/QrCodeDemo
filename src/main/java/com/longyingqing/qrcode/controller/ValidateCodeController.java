package com.longyingqing.qrcode.controller;

import com.longyingqing.qrcode.utils.QRCodeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：当前类说说明
 * @author 刘梓江
 * @date 2020/7/6  15:12
 */
@SuppressWarnings("all")
@RestController
public class ValidateCodeController {

    /**
     * 获取二进制图片二维码信息
     * @param response
     * @throws Exception
     */
    @RequestMapping("/imageQrCode")
    public void imageQrCode(HttpServletResponse response)throws Exception {
        //获取二维码信息
        BufferedImage qrCodeImage = QRCodeUtils.createImage("http://www.huijianzhu.com/#/example2/cityWork", true);
        //以JPEG格式向客户端发送
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(qrCodeImage, "PNG",os);
        os.flush();
        os.close();
    }


    /**
     * 获取以base64对应的二维码信息
     * @return
     * @throws Exception
     */
    @RequestMapping("/base64QrCode")
    public Object base64QrCode()throws Exception{
        //获取二维码信息
        BufferedImage qrCodeImage = QRCodeUtils.createImage("http://www.huijianzhu.com/#/example2/cityWork", true);
        //转base64
        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayOutputStream baoS = new ByteArrayOutputStream();//io流
        ImageIO.write(qrCodeImage, "png", baoS);      //写入流中
        byte[] bytes = baoS.toByteArray();//转换成字节
        String png_base64 =  encoder.encodeBuffer(bytes).trim();//转换成base64串
        //删除 \r\n
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");
        Map map = new HashMap<>();
        map.put("base64","data:image/png;base64,"+ png_base64);
        return map;
    }




}

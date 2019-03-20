package com.hndt.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 根据ip地址从第三方接口获取详细的地理位置
 *
 * @author Hystar
 * @date 2018/1/12
 */
public class AddressUtil {

    /**
     * 测试
     */
    public static void main(String[] args) {
        // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信
        String ip = IpUtil.getLocalIP();
        System.out.println(ip);
        String address = "";
        try {
            address = AddressUtil.getAddresses("ip=" + ip, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(address);
        // 输出结果为：广东省,广州市,越秀区
    }

    /**
     * 格式：AddressUtil.getAddresses("ip=" + ip, "utf-8")
     *
     * @param content        请求的参数 格式为：name=xxx&pwd=xxx
     * @param encodingString 服务器端请求编码。如GBK,UTF-8等
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getAddresses(String content, String encodingString) throws UnsupportedEncodingException {
        // 这里调用taobao的接口
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = getResult(urlStr, content, encodingString);
        if (returnStr != null) {
            // 处理返回的省市区信息
            System.out.println(returnStr);
            String[] temp = returnStr.split(",");
            // 无效IP，局域网测试
            if (temp.length < 3) {
                return "0";
            }
            String region = (temp[5].split(":"))[1].replaceAll("\"", "");
            // 省份
            region = decodeUnicode(region);

            String country = "";
            String area = "";
            // String region = "";
            String city = "";
            String county = "";
            String isp = "";
            for (int i = 0; i < temp.length; i++) {
                switch (i) {
                    case 1:
                        country = (temp[i].split(":"))[2].replaceAll("\"", "");
                        // 国家
                        country = decodeUnicode(country);
                        break;
                    case 3:
                        area = (temp[i].split(":"))[1].replaceAll("\"", "");
                        // 地区
                        area = decodeUnicode(area);
                        break;
                    case 5:
                        region = (temp[i].split(":"))[1].replaceAll("\"", "");
                        // 省份
                        region = decodeUnicode(region);
                        break;
                    case 7:
                        city = (temp[i].split(":"))[1].replaceAll("\"", "");
                        // 市区
                        city = decodeUnicode(city);
                        break;
                    case 9:
                        county = (temp[i].split(":"))[1].replaceAll("\"", "");
                        // 地区
                        county = decodeUnicode(county);
                        break;
                    case 11:
                        isp = (temp[i].split(":"))[1].replaceAll("\"", "");
                        // ISP公司
                        isp = decodeUnicode(isp);
                        break;
                }
            }

            System.out.println(country + area + region + city + county + isp);
            return new StringBuffer(country).append(area).append(region).append(city).append(county).append(isp).toString();
        }
        return null;
    }

    /**
     * @param urlStr   请求的地址
     * @param content  请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    private static String getResult(String urlStr, String content, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            // 新建连接实例
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间，单位毫秒
            connection.setConnectTimeout(2000);
            // 设置读取数据超时时间，单位毫秒
            connection.setReadTimeout(33000);
            // 是否打开输出流 true|false
            connection.setDoOutput(true);
            // 是否打开输入流true|false
            connection.setDoInput(true);
            // 提交方法POST|GET
            connection.setRequestMethod("POST");
            // 是否缓存true|false
            connection.setUseCaches(false);
            // 打开连接端口
            connection.connect();
            // 打开输出流往对端服务器写数据
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.writeBytes(content);
            // 刷新
            out.flush();
            // 关闭输出流
            out.close();
            // 往对端写完数据对端服务器返回数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    /**
     * unicode 转换成 中文
     *
     * @param theString
     * @return
     * @author fanhui 2007-3-15
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }
}

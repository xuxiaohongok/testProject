package com.zhidian3g.dsp.adPostback.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2015/12/9.
 */
public class HttpUtil {

    public static String doGet(String toUrl) throws IOException {
        return doGet(toUrl, 5000);
    }

    public static String doGet(String toUrl, int timeout) throws IOException {
        String result = "";
        URL url = new URL(toUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(timeout);
        conn.connect();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream in = conn.getInputStream();
            result = IOUtils.readStr(in);
        }
        conn.disconnect();
        return result;
    }

    public static String doPost(String toUrl, String params) throws IOException {
        return doPost(toUrl, params, 5000);
    }

    public static String doPost(String toUrl, String params, int timeout) throws IOException {
        URL url = new URL(toUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(timeout);
        conn.setUseCaches(false);
        conn.setDoInput(true);     
        conn.setDoOutput(true);
        conn.connect();
        
        StringBuilder buffer = new StringBuilder();
        
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        if (params != null && !"".equals(params.trim())) {
            out.writeBytes(params);
        }
        out.flush();
        out.close();
        
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
        }
        conn.disconnect();
        return buffer.toString();
    }

    /**
     * 根据url 抓取文件到指定目录
     * @param downloadUrl
     * @param destFileFolder
     * @param destFileName
     * @param suffix 默认后缀名（）
     * @param timeout
     * @return
     */
    public static File downloadFile(String downloadUrl, String destFileFolder, String destFileName, String suffix, int timeout) {
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String field = conn.getHeaderField("Content-Disposition");
                String contentType = conn.getHeaderField("content-type");
                if (StringUtils.isNotBlank(field)) {
                    suffix = field.substring(field.lastIndexOf(".") + 1);
                } else if (StringUtils.isNotBlank(contentType)) {
                    if (contentType.contains("jpeg")) {
                        suffix = "jpg";
                    } else if (contentType.contains("png")) {
                        suffix = "png";
                    }
                }

                InputStream in = conn.getInputStream();
                File folderFile = new File(destFileFolder);
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }
                File destFile = new File(destFileFolder, destFileName + "." + suffix);
                IOUtils.writeIntoFile(in, destFile);
                return destFile;
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String uploadFile(String uploadUrl, String filePath) {
        StringBuffer result = new StringBuffer();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }

            URL url = new URL(uploadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Disposition", "attachment;filename=" + file.getName());

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            out.flush();
            out.close();
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public static void main(String[] args) {
        HttpUtil.downloadFile("http://a1.semlos.cn/download/apks/30620140120150629178/wyyy2015092102.jpg", "E:/download", "aa", "png", 60000);
        //HttpUtil.uploadFile("http://localhost:8080/adPutInDemo.shtml", "E:/2015-01-24.log");
    }
}

package com.zhidian3g.dsp.adPostback.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by chenjianliang on 2015/12/9.
 */
public class IOUtils {

    public static final transient Logger LOG = LoggerFactory.getLogger(IOUtils.class);

    public static String readStr(InputStream stream) throws UnsupportedEncodingException,
            IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                sb.append(buf);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
//        System.out.println("..."+URLDecoder.decode(sb.toString(),"UTF-8"));
        return sb.toString();
    }

    public static String writeIntoString(InputStream in) {
        return IOUtils.writeIntoString(in, "UTF-8");
    }

    public static String writeIntoString(InputStream in, String charsetName) {
        String result = "";
        BufferedReader reader = null;
        StringWriter writer = new StringWriter();

        try {
            reader = new BufferedReader(new InputStreamReader(in, charsetName));
            char[] cbuf = new char[1024];
            int length = 0;
            while ((length = reader.read(cbuf)) != -1) {
                writer.write(cbuf, 0, length);
            }
            result = writer.getBuffer().toString();
        } catch (IOException e) {
            LOG.error("InputStream write into string fail!", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void writeIntoFile(String text, File file) {
        BufferedWriter buffered = null;
        try {
            buffered = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            buffered.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffered != null) {
                    buffered.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeIntoFile(InputStream in, File file) {
        BufferedInputStream buffered = null;
        FileOutputStream fOut = null;
        try {
            buffered = new BufferedInputStream(in);
            fOut = new FileOutputStream(file);
            int length = 0;
            byte[] b = new byte[1024];
            while ((length = buffered.read(b)) != -1) {
                fOut.write(b, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffered != null) {
                    buffered.close();
                }
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeIntoFile(InputStream in, File file, String charsetName) {
        BufferedWriter bWriter = null;
        BufferedReader bReader = null;
        try {
            bReader = new BufferedReader(new InputStreamReader(in, charsetName));
            bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName));
            char[] cbuf = new char[1024];
            int length = 0;
            while ((length = bReader.read()) != -1) {
                bWriter.write(cbuf, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bWriter != null) {
                    bWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String convertCharset(String text, String charsetName) throws UnsupportedEncodingException {
        return new String(text.getBytes(text), charsetName);
    }

    public static void main(String[] args) throws IOException {

    }

}

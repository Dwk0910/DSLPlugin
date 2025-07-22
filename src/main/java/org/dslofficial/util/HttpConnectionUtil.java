package org.dslofficial.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpConnectionUtil {
    /**
     * @param pURL : 요청 URL
     * @param pList : 파라미터 객체 (HashMap<String,String>)
     *
     * Created by 닢향
     * <a href="http://niphyang.tistory.com">http://niphyang.tistory.com</a>
     */
    public static String postRequest(String pURL, Map<String, String> pList) throws ConnectException {
        String myResult = "";

        try {
            //SSL 무시하기
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            //   URL 설정하고 접속하기
            URL url = new URL(pURL); // URL 설정

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속

            //--------------------------
            //   전송 모드 설정 - 기본적인 설정
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setConnectTimeout(30000); // 타임아웃 판단 시간을 30000ms(30초)로 설정
            http.setDoInput(true); // 서버에서 읽기 모드 지정
            http.setDoOutput(true); // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST"); // 전송 방식은 POST



            //--------------------------
            // 헤더 세팅
            //--------------------------
            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");


            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuilder buffer = new StringBuilder();

            //HashMap으로 전달받은 파라미터가 null이 아닌경우 버퍼에 넣어준다
            boolean isFirst = true;
            if (pList != null) {
                for (String key : pList.keySet()) {
                    String valueName = pList.get(key);
                    if (isFirst) {
                        buffer.append(key).append("=").append(valueName);
                        isFirst = false;
                    } else {
                        buffer.append("&").append(key).append("=").append(valueName);
                    }
                }
            }

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();


            //--------------------------
            //   Response Code
            //--------------------------
            //http.getResponseCode();


            //--------------------------
            //   서버에서 전송받기
            //--------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str).append("\n");
            }
            myResult = builder.toString();

            outStream.close();
            writer.close();
            tmp.close();
            reader.close();
            return myResult;
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }
        return myResult;
    }
}
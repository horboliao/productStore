package clientServerApp.handlers;

import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static clientServerApp.Main.SECRET_KEY;
import static clientServerApp.Main.dbc;

public class HandlerTools {
    static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        Optional.ofNullable(query).ifPresent(q -> {
            for (String param : q.split("&")) {
                String[] entry = param.split("=");
                result.put(entry[0], entry.length > 1 ? entry[1] : "");
            }
        });
        return result;
    }

    static boolean isGroupExists(String id) {
        return dbc.groupExists(dbc.getGroupName(Integer.parseInt(id)));
    }

    static String getIdFromPath(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    static String getPrevId(String path){
        String newId = path.substring(0, path.lastIndexOf('/'));
        return newId.substring(newId.lastIndexOf('/') + 1);
    }

    public static String requestBodyToString(HttpExchange exchange) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = exchange.getRequestBody().read(buffer)) != -1) {
            sb.append(new String(buffer, 0, bytesRead));
        }
        return sb.toString();
    }

    static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }

    public static String generateToken(String user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(user)
                .signWith(SECRET_KEY);
        return builder.compact();
    }

    static boolean verifyToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject() != null;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    static String getLoginFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
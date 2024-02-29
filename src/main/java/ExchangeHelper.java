import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExchangeHelper {

    private static final String INVALID_REQUEST = "Invalid request or missing required values";

    public static void respondAndClose(@NotNull HttpExchange exchange, @Nullable String body, int code) {
        try (exchange; var output = exchange.getResponseBody()) {
            var responseBytes = null == body ? null : body.getBytes();
            exchange.sendResponseHeaders(code, null == responseBytes ? -1 : responseBytes.length);
            if (null != responseBytes) {
                output.write(responseBytes);
                output.flush();
            }
        } catch (IOException exception) {
            System.err.println("Error writing Response for: " + exchange.getRequestURI());
            System.err.println("Body: " + body);
            System.err.println("Code: " + code);
        }
    }

    public static void respondInvalidRequest(@NotNull HttpExchange exchange) {
        respondAndClose(exchange, INVALID_REQUEST, 400);
    }

    @Nullable
    public static Map<String, String> parseQueryParams(@NotNull HttpExchange exchange) {
        var query = exchange.getRequestURI().getQuery();
        if (null == query) return null;

        var params = new HashMap<String, String>();
        for (var param : query.split("&")) {
            var entry = param.split("=");
            params.put(entry[0], entry.length > 1 ? entry[1] : "");
        }
        return params;
    }

    @Nullable
    public static Map<String, String> parseBody(@NotNull HttpExchange exchange) {
        try (var stream = exchange.getRequestBody()) {
            return new Gson().fromJson(readInputStream(stream), new TypeToken<>() {});
        } catch (IOException ignore) {}
        return null;
    }

    public static boolean hasEmptyValues(@Nullable String... values) {
        if (null != values) {
            for (var value : values) {
                if (null == value || value.isBlank()) return true;
            }
        }
        return false;
    }

    @NotNull
    private static String readInputStream(@NotNull InputStream stream) throws IOException {
        var result = new ByteArrayOutputStream();
        var buffer = new byte[2048];
        var length = 0;

        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString();
    }
}
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public class TicketAPIController {

    private final PassengerRepository mPassengerRepository = new PassengerRepository();
    private final TicketRepository mTicketRepository = new TicketRepository();

    public TicketAPIController(@NotNull HttpServer server) {
        server.createContext("/api", this::greetings);
        server.createContext("/api/purchase", this::purchase);
        server.createContext("/api/ticket/receipt", this::ticketReceipt);
        server.createContext("/api/ticket/modify", this::modifySeat);
        server.createContext("/api/ticket/cancel", this::cancelTicket);
        server.createContext("/api/passengers", this::seatDetailsBySection);
    }

    private void greetings(@NotNull HttpExchange exchange) {
        if (isNotMethod(exchange, "GET")) return;

        var responseText = """
                Welcome to framework-less JAVA Ticketer API
                Available Endpoints:
                /api
                /api/purchase
                /api/ticket/receipt
                /api/ticket/modify
                /api/ticket/cancel
                /api/passengers
                """;
        ExchangeHelper.respondAndClose(exchange, responseText, 200);
    }

    private void purchase(@NotNull HttpExchange exchange) {
        if (isNotMethod(exchange, "POST")) return;

        var mappedBody = ExchangeHelper.parseBody(exchange);
        if (null == mappedBody) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        var firstName = mappedBody.get("firstname");
        var lastName = mappedBody.get("lastname");
        var email = mappedBody.get("email");
        var from = mappedBody.get("from");
        var to = mappedBody.get("to");
        var seatSection = mappedBody.get("seat_section");

        if (ExchangeHelper.hasEmptyValues(firstName, lastName, email, from, to, seatSection)) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        var passenger = mPassengerRepository.create(firstName, lastName, email);
        var ticket = mTicketRepository.create(from, to, 20, passenger.id(), seatSection.charAt((0)));

        if (null == ticket) {
            // remove passenger as ticket is not available
            mPassengerRepository.delete(passenger.id());
            ExchangeHelper.respondAndClose(exchange, "No seats left", 200);
            return;
        }

        var receiptJson = new Gson().toJson(new Receipt(ticket, passenger));
        ExchangeHelper.respondAndClose(exchange, receiptJson, 200);
    }

    private void ticketReceipt(@NotNull HttpExchange exchange) {
        if (isNotMethod(exchange, "GET")) return;

        var queryParams = ExchangeHelper.parseQueryParams(exchange);
        if (null == queryParams) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        var ticketIdStr = queryParams.get("ticketId");
        if (ExchangeHelper.hasEmptyValues(ticketIdStr)) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        try {
            var ticket = mTicketRepository.getById(Integer.parseInt(ticketIdStr));
            if (ticket != null) {
                var passenger = mPassengerRepository.getById(ticket.passengerId());
                if (passenger != null) {
                    var receiptJson = new Gson().toJson(new Receipt(ticket, passenger));
                    ExchangeHelper.respondAndClose(exchange, receiptJson, 200);
                }
            }
            exchange.close();
        } catch (NumberFormatException e) {
            ExchangeHelper.respondInvalidRequest(exchange);
        }
    }

    private void cancelTicket(@NotNull HttpExchange exchange) {
        if (isNotMethod(exchange, "DELETE")) return;

        var queryParams = ExchangeHelper.parseQueryParams(exchange);
        if (null == queryParams) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        var ticketIdStr = queryParams.get("ticketId");
        if (ExchangeHelper.hasEmptyValues(ticketIdStr)) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        try (exchange) {
            var ticket = mTicketRepository.cancel(Integer.parseInt(ticketIdStr));
            if (null != ticket) {
                mPassengerRepository.delete(ticket.passengerId());
                ExchangeHelper.respondAndClose(exchange, "Success", 200);
            }
        } catch (NumberFormatException e) {
            ExchangeHelper.respondInvalidRequest(exchange);
        }
    }

    private void modifySeat(@NotNull HttpExchange exchange) {
        if (isNotMethod(exchange, "PUT")) return;

        var queryParams = ExchangeHelper.parseQueryParams(exchange);
        if (null == queryParams) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        var ticketId = queryParams.get("ticketId");
        var section = queryParams.get("seat_section");
        if (ExchangeHelper.hasEmptyValues(ticketId, section)) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        try (exchange) {
            var ticket = mTicketRepository.getById(Integer.parseInt(ticketId));
            if (null != ticket) {
                var updatedTicket = mTicketRepository.modifySeat(ticket, section.charAt(0));
                if (null != updatedTicket) {
                    var receiptJson = new Gson().toJson(updatedTicket);
                    ExchangeHelper.respondAndClose(exchange, receiptJson, 200);
                }
            }
        } catch (NumberFormatException exception) {
            ExchangeHelper.respondInvalidRequest(exchange);
        }
    }

    private void seatDetailsBySection(@NotNull HttpExchange exchange) {
        if (isNotMethod(exchange, "GET")) return;

        var queryParams = ExchangeHelper.parseQueryParams(exchange);
        if (null == queryParams) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        var section = queryParams.get("seat_section");
        if (ExchangeHelper.hasEmptyValues(section)) {
            ExchangeHelper.respondInvalidRequest(exchange);
            return;
        }

        var tickets = mTicketRepository.ticketsBySection(section.charAt(0));
        var details = new HashMap<Passenger, Seat>(tickets.size());
        for (var ticket : tickets) {
            details.put(mPassengerRepository.getById(ticket.passengerId()), ticket.seat());
        }
        var responseJson = new Gson().toJson(details);
        ExchangeHelper.respondAndClose(exchange, responseJson, 200);
    }

    private boolean isNotMethod(@NotNull HttpExchange exchange, @NotNull String method) {
        try {
            if (!method.equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return true;
            }
        } catch (IOException ignore) {}
        return false;
    }
}
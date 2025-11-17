package ru.checkdev.mock.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ru.checkdev.mock.exception.ItemNotFoundException;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            throw new ItemNotFoundException("Item not found");
        }
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                && httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ItemNotFoundException("Item not found");
        }
    }
}
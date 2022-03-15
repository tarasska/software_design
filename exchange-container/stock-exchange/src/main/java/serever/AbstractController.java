package serever;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import model.Company;
import rx.Observable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractController {

    protected <T> Observable<String> observableToStr(Observable<T> observable) {
        return observable.map(Objects::toString).onErrorReturn(Throwable::getMessage);
    }

    protected String getCompanyName(Map<String, List<String>> params) {
        return params.get(Company.COMPANY_KEY).get(0);
    }

    protected int getIntParam(Map<String, List<String>> params, String name) {
        return Integer.parseInt(params.get(name).get(0));
    }

    protected Observable<String> withCheckedParams(
        Function<Map<String, List<String>>, Observable<String>> action,
        Map<String, List<String>> params,
        String ...paramNames
    ) {
        StringBuilder missingParams = new StringBuilder();
        for (String name : paramNames) {
            if (!params.containsKey(name)) {
                missingParams.append(name).append(';');
            }
        }
        if (missingParams.length() > 0) {
            return Observable.just(String.format("Params %s not found", missingParams.toString()));
        }

        return action.apply(params);
    }

    public static <T extends AbstractController> Observable<Void> handle(
        T controller,
        HttpServerRequest<ByteBuf> request,
        HttpServerResponse<ByteBuf> response
    ) {
        String requestUri = request.getDecodedPath();
        if (requestUri == null || requestUri.isBlank()) {
            response.setStatus(HttpResponseStatus.BAD_REQUEST);
            return response.writeString(Observable.just("Invalid request."));
        } else {
            Observable<String> result = controller.handle(
                requestUri.substring(1),
                request.getQueryParameters()
            );
            response.setStatus(HttpResponseStatus.OK);
            return response.writeString(result);
        }
    }

    public abstract Observable<String> handle(String endpoint, Map<String, List<String>> params);
}

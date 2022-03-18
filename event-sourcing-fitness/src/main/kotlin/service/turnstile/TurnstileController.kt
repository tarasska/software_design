package service.turnstile

import rx.Observable
import service.AbstractController

class TurnstileController : AbstractController() {
    override fun handle(endpoint: String, params: Map<String, List<String>>): Observable<String> {
        TODO("Not yet implemented")
    }
}
package service.manager

import rx.Observable
import service.AbstractController

class ManagerController : AbstractController() {
    override fun handle(endpoint: String, params: Map<String, List<String>>): Observable<String> {
        TODO("Not yet implemented")
    }
}
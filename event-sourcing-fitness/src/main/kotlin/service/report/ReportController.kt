package service.report

import rx.Observable
import service.AbstractController

class ReportController : AbstractController() {
    override fun handle(endpoint: String, params: Map<String, List<String>>): Observable<String> {
        TODO("Not yet implemented")
    }
}
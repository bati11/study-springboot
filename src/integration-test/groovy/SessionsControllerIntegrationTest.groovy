class SessionsControllerIntegrationTest extends AbstractSpecification {
    def "should get input"() {
        expect:
        with(get("/login")) {
            viewName == "sessions/input"
        }
    }
}

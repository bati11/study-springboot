class UsersControllerIntegrationTest extends AbstractSpecification {

    def "should get input"() {
        expect:
        with(get("/signup")) {
            viewName == "users/input"
        }
    }
}

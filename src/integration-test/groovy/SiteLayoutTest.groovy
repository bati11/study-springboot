class SiteLayoutTest extends AbstractSpecification {

    def "layout links"() {
        expect:
        with(get("/")) {
            viewName == "static_pages/home"
            select('a[href="/"').size() == 2
            select('a[href="/help"').size() == 1
            select('a[href="/about"').size() == 1
            select('a[href="/contact"').size() == 1
        }
    }
}

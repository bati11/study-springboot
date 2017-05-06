import example.Application
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.context.web.WebDelegatingSmartContextLoader
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@WebAppConfiguration
@TestPropertySource("/application-integrationtest.properties")
@ContextConfiguration(loader = WebDelegatingSmartContextLoader, classes = Application)
class SiteLayoutTest extends Specification {
    @Autowired
    WebApplicationContext context

    @Shared
    MockMvc mvc

    def setup() throws Exception {
        mvc = webAppContextSetup(context).build()
    }

    def "layout links"() {
        expect:
        def result = mvc.perform(get("/")).andReturn()
        result.modelAndView.viewName == "static_pages/home"

        def doc = Jsoup.parse(result.response.contentAsString)
        with(doc) {
            select('a[href="/"').size() == 2
            select('a[href="/help"').size() == 1
            select('a[href="/about"').size() == 1
            select('a[href="/contact"').size() == 1
        }
    }
}

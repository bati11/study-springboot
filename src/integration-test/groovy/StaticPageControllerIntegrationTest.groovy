import example.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.context.web.WebDelegatingSmartContextLoader
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebAppConfiguration
@ContextConfiguration(loader = WebDelegatingSmartContextLoader, classes = Application)
class StaticPageControllerIntegrationTest extends Specification {

    @Autowired
    WebApplicationContext context;

    @Shared
    MockMvc mvc;

    def setup() throws Exception {
        mvc = webAppContextSetup(context).build()
    }

    def "should get home"() {
        expect:
        mvc.perform(get("/static_pages/"))
            .andExpect(status().isOk())
            .andExpect(view().name("static_pages/home"))
    }

    def "should get help"() {
        expect:
        mvc.perform(get("/static_pages/help"))
                .andExpect(status().isOk())
                .andExpect(view().name("static_pages/help"))
    }

    def "should get about"() {
        expect:
        mvc.perform(get("/static_pages/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("static_pages/about"))
    }
}

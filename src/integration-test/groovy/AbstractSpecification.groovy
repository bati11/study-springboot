import example.Application
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.context.web.WebDelegatingSmartContextLoader
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

@WebAppConfiguration
@TestPropertySource("/application-integrationtest.properties")
@ContextConfiguration(loader = WebDelegatingSmartContextLoader, classes = Application)
abstract class AbstractSpecification extends Specification {
    @Autowired
    private WebApplicationContext context

    @Shared
    private MockMvc mvc

    def setup() throws Exception {
        mvc = webAppContextSetup(context).build()
    }

    def get(String path) {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(path)).andReturn()
        return new MyMvcResult(mvcResult)
    }

    def view(MyMvcResult myMvcResult) {
        return myMvcResult.mvcResult.modelAndView.viewName
    }
}

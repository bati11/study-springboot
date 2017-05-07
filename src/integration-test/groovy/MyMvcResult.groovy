import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.test.web.servlet.MvcResult

class MyMvcResult {
    MvcResult mvcResult
    Document doc

    MyMvcResult(MvcResult mvcResult) {
        this.mvcResult = mvcResult
        this.doc = Jsoup.parse(mvcResult.response.contentAsString)
    }

    def getViewName() {
        return mvcResult.modelAndView.viewName
    }

    def select(String cssSelector) {
        return doc.select(cssSelector)
    }
}

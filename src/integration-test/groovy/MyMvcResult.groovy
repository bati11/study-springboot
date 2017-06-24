import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.test.web.servlet.MvcResult

class MyMvcResult {
    MvcResult mvcResult
    Document doc
    String lastLoginUserEmail
    String lastLoginUserPassword

    MyMvcResult(MvcResult mvcResult) {
        this.mvcResult = mvcResult
        this.doc = Jsoup.parse(mvcResult.response.contentAsString)
    }

    def getViewName() {
        assert mvcResult != null
        assert mvcResult.response.status == 200
        assert mvcResult.modelAndView != null
        return mvcResult.modelAndView.viewName
    }

    def getRedirectLocation() {
        assert mvcResult != null
        assert mvcResult.response.status == 302
        return mvcResult.getResponse().getHeader("location")
    }

    def select(String cssSelector) {
        return doc.select(cssSelector)
    }
}

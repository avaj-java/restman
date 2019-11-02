import jaemisseo.man.RestMan
import org.junit.Ignore
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RestManTest {

    final Logger logger = LoggerFactory.getLogger(this.getClass());



    @Test
    @Ignore
    void test(){
        String response = new RestMan().setType('application/json').post('http://localhost:8080/server/login', [
                email: "111",
                userPublicKey: "@222",
                signature: "333"
        ])
        println response
    }

    @Test
    @Ignore
    void get(){
        def data = new RestMan('http://localhost:28080/jelly').parseGet('/api/search-product', [query:'A4', from:'0', size:'20'])
        println data
    }

}

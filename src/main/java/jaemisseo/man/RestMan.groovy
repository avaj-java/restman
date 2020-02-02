package jaemisseo.man

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by sujkim on 2017-03-10.
 */
class RestMan {

    RestMan(){
    }

    RestMan(String baseURL){
        setBaseURL(baseURL)
    }

    static final Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String GET = 'GET'
    static final String POST = 'POST'
    static final String PUT = 'PUT'
    static final String DELETE = 'DELETE'

    String baseURL = ""
    String url = ""
    String method = GET
    String type = "*/*"
    String accept = "*/*"
    Map<String, String> headerMap = [:]
    Map<String, List<String>> parameterMap = [:]




    RestMan setBaseURL(baseURL){
        this.baseURL = baseURL
        return this
    }

    RestMan setType(String type){
        this.type = type
        return this
    }

    RestMan setAccept(String accept){
        this.accept = accept
        return this
    }

    RestMan setMethod(String method){
        this.method = method
        return this
    }

    RestMan addParameter(Map parameterMapToAdd){
        parameterMapToAdd.each{
            addParameter(it.key, it.value)
        }
        return this
    }

    RestMan addParameter(String key, String value){
        if (parameterMap[key]){
            if (parameterMap[key] instanceof List)
                parameterMap[key] << value
            else
                parameterMap[key] << [parameterMap[key], value]
        }else{
            parameterMap[key] = [value]
        }
        return this
    }

    RestMan addHeader(Map headerMapToAdd){
        headerMapToAdd.each{
            addHeader(it.key, it.value)
        }
        return this
    }

    RestMan addHeader(String key, String value){
        //Map
        headerMap[key] = value
        //MultiValueMap
//        if (headerMap[key]){
//            if (headerMap[key] instanceof List)
//                headerMap[key] << value
//            else
//                headerMap[key] << [headerMap[key], value]
//        }else{
//            headerMap[key] = [value]
//        }
        return this
    }



    /**************************************************
     *
     * GET
     *
     **************************************************/
    def get(){
        return request(null, GET, null, null)
    }

    def get(String url){
        return request(url, GET, null, null)
    }

    def get(String url, Map paramMap){
        return request(url, GET, paramMap, null)
    }

    def get(String url, Map paramMap, Map headerMap){
        return request(url, GET, paramMap, headerMap)
    }

    def parseGet(){
        return parseGet(null, null, null)
    }

    def parseGet(String url){
        return parseGet(url, null, null)
    }

    def parseGet(String url, Map paramMap){
        return parseGet(url, paramMap, null)
    }

    def parseGet(String url, Map paramMap, Map headerMap){
        String json = get(url, paramMap, headerMap)
        return new JsonSlurper().parseText(json)
    }



    /**************************************************
     *
     * POST
     *
     **************************************************/
    def post(){
        return request(null, POST, null, null)
    }

    def post(String url){
        return request(url, POST, null, null)
    }

    def post(String url, Map paramMap){
        return request(url, POST, paramMap, null)
    }

    def post(String url, Map paramMap, Map headerMap){
        return request(url, POST, paramMap, headerMap)
    }

    def parsePost(){
        return parsePost(null, null, null)
    }

    def parsePost(String url){
        return parsePost(url, null, null)
    }

    def parsePost(String url, Map paramMap){
        return parsePost(url, paramMap, null)
    }

    def parsePost(String url, Map paramMap, Map headerMap){
        String json = post(url, paramMap, headerMap)
        return new JsonSlurper().parseText(json)
    }



    /**************************************************
     *
     * PUT
     *
     **************************************************/
    def put(){
        return request(null, PUT, null, null)
    }

    def put(String url){
        return request(url, PUT, null, null)
    }

    def put(String url, Map paramMap){
        return request(url, PUT, paramMap, null)
    }

    def put(String url, Map paramMap, Map headerMap){
        return request(url, PUT, paramMap, headerMap)
    }

    def parsePut(){
        return parsePut(null, null, null)
    }

    def parsePut(String url){
        return parsePut(url, null, null)
    }

    def parsePut(String url, Map paramMap){
        return parsePut(url, paramMap, null)
    }

    def parsePut(String url, Map paramMap, Map headerMap){
        String json = put(url, paramMap, headerMap)
        return new JsonSlurper().parseText(json)
    }



    /**************************************************
     *
     * DELETE
     *
     **************************************************/
    def delete(){
        return request(null, DELETE, null, null)
    }

    def delete(String url){
        return request(url, DELETE, null, null)
    }

    def delete(String url, Map paramMap){
        return request(url, DELETE, paramMap, null)
    }

    def delete(String url, Map paramMap, Map headerMap){
        return request(url, DELETE, paramMap, headerMap)
    }

    def parseDelete(){
        return parseDelete(null, null, null)
    }

    def parseDelete(String url){
        return parseDelete(url, null, null)
    }

    def parseDelete(String url, Map paramMap){
        return parseDelete(url, paramMap, null)
    }

    def parseDelete(String url, Map paramMap, Map headerMap){
        String json = delete(url, paramMap, headerMap)
        return new JsonSlurper().parseText(json)
    }



    /**************************************************
     *
     * REQUEST
     *
     **************************************************/
    String request(String url, Map paramMap, Map headerMap){
        return request(url, method, paramMap, headerMap)
    }

    String request(String url, String method, Map paramMap, Map headerMap){
        //- Request
        String responseString
        WebResource.Builder builder
        ClientResponse response
        method = method ?: POST
        //TODO: how to log
        logger.debug "${url}"
        switch (method){
            case GET:
                builder = build(url, paramMap, headerMap)
                response = builder.get(ClientResponse.class)
                break
            case POST:
                String requestBody = JsonOutput.toJson(paramMap)
                builder = build(url, [:], headerMap)
                response = builder.post(ClientResponse.class, requestBody)
                break
            case PUT:
                String requestBody = JsonOutput.toJson(paramMap)
                builder = build(url, [:], headerMap)
                response = builder.post(ClientResponse.class, requestBody)
                break
            case DELETE:
                addParameter(paramMap)
                builder = build(url, parameterMap, headerMap)
                response = builder.post(ClientResponse.class)
                break
        }
        //- CHECK ERROR
        checkError(response)
        //- RESPONSE
        responseString = response.getEntity(String.class)
        return responseString
    }

    WebResource.Builder build(String url, Map<String, Object> paramMap, Map<String, String> headerMap){
        try {
            Client client = Client.create()
            url = baseURL+ (baseURL.endsWith('/')?'':'/') +(url?:'')
            WebResource webResource = client.resource(url)
            webResource = parameter(webResource, getParameterMap())
            webResource = parameter(webResource, paramMap)
            WebResource.Builder builder = webResource.type(type).accept(accept)
            header(builder, getHeaderMap())
            header(builder, headerMap)
            return builder
        }catch (Exception e){
            throw e
        }
    }

    private WebResource parameter(WebResource webResource, Map parameterMap){
        parameterMap?.each{
            List valueList = (it.value instanceof List) ? it.value : [it.value]
            valueList.each{ value ->
                value = (value instanceof String) ? value : String.valueOf(value)
                webResource = webResource.queryParam(it.key, value)
            }
        }
        return webResource
    }

    private header(WebResource.Builder builder, Map headerMap){
        headerMap?.each{
            builder.header(it.key, it.value)
        }
    }

    private boolean checkError(ClientResponse response){
        if (response.getStatus() != 200){
            throw new RuntimeException("Failed : HTTP error code : ${response.getStatus()}")
        }
        return true
    }

}

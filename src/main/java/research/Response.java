package research;

import java.io.Serializable;

public class Response implements Serializable {
    private String getResponse;
    private String postResponse;
    private String putResponse;
    private String deleteResponse;
    private String redirectionUrl;

    public Response(){
    }

    public Response(Response response) {
        this.getResponse = response.getGetResponse();
        this.postResponse = response.getPostResponse();
        this.putResponse = response.getPutResponse();
        this.deleteResponse = response.getDeleteResponse();
        this.redirectionUrl = response.getRedirectionUrl();
    }

    @Override
    public String toString(){
        return "{" +
                " get : " + getResponse + ",\n" +
                " post : " + postResponse + ",\n" +
                " put : " + putResponse + ",\n" +
                " delete : " + deleteResponse + ",\n" +
                " redirectUri : " + redirectionUrl + ",\n" +
                "}";
    }

    public String getGetResponse() {
        return getResponse;
    }

    public Response setGetResponse(String response){
        this.getResponse = response;
        return this;
    }

    public String getPostResponse() {
        return postResponse;
    }

    public Response setPostResponse(String response){
        this.postResponse = response;
        return this;
    }

    public String getPutResponse() {
        return putResponse;
    }

    public Response setPutResponse(String response){
        this.putResponse = response;
        return this;
    }

    public String getDeleteResponse() {
        return deleteResponse;
    }

    public Response setDeleteResponse(String response){
        this.deleteResponse = response;
        return this;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public Response setRedirectionUrl(String forwardUrl){
        this.redirectionUrl = forwardUrl;
        return this;
    }
}

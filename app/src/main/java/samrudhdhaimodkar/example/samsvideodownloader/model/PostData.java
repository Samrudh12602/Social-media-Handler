package samrudhdhaimodkar.example.samsvideodownloader.model;

public class PostData {
    private String postUrl;
    private String caption;
    private String post_type;
    private String shortCode;

    public PostData(String postUrl, String caption, String post_type, String shortCode) {
        this.postUrl = postUrl;
        this.caption = caption;
        this.post_type = post_type;
        this.shortCode = shortCode;
    }
    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}

package samrudhdhaimodkar.example.samsvideodownloader.model;

public class MediaData {
    private String mediaType;
    private String mediaUrl;

    public MediaData(String mediaUrl,String mediaType){
        this.mediaType=mediaType;
        this.mediaUrl=mediaUrl;
    }
    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}

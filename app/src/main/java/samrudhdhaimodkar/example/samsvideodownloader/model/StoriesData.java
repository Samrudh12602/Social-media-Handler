package samrudhdhaimodkar.example.samsvideodownloader.model;

public class StoriesData {
    private String StoryType;
    private String StoryUrl;
    private String StoryId;
    private double postedTime;
    private double expiryTime;
    private Taggedusers tags;

    public StoriesData(String storyType, String storyUrl, String storyId, double postedTime, double expiryTime, Taggedusers tags) {
        StoryType = storyType;
        StoryUrl = storyUrl;
        StoryId = storyId;
        this.postedTime = postedTime;
        this.expiryTime = expiryTime;
        this.tags=tags;
    }

    public String getStoryType() {
        return StoryType;
    }

    public void setStoryType(String storyType) {
        StoryType = storyType;
    }

    public String getStoryUrl() {
        return StoryUrl;
    }

    public void setStoryUrl(String storyUrl) {
        StoryUrl = storyUrl;
    }

    public String getStoryId() {
        return StoryId;
    }

    public void setStoryId(String storyId) {
        StoryId = storyId;
    }

    public double getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(double postedTime) {
        this.postedTime = postedTime;
    }

    public double getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(double expiryTime) {
        this.expiryTime = expiryTime;
    }
}

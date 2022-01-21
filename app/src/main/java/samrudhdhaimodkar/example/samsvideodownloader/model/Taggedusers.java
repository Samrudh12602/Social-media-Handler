package samrudhdhaimodkar.example.samsvideodownloader.model;

public class Taggedusers {
    String taggedUser;
    String taggedUserName;

    public Taggedusers(String taggedUser, String taggedUserName) {
        this.taggedUser = taggedUser;
        this.taggedUserName = taggedUserName;
    }

    public String getTaggedUser() {
        return taggedUser;
    }

    public void setTaggedUser(String taggedUser) {
        this.taggedUser = taggedUser;
    }

    public String getTaggedUserName() {
        return taggedUserName;
    }

    public void setTaggedUserName(String taggedUserName) {
        this.taggedUserName = taggedUserName;
    }
}

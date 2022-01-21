package samrudhdhaimodkar.example.samsvideodownloader.model;

import java.util.Map;

public class UserData {
        String name,username,id,profilepicUrl;
        Map<String,String> user;

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }

    public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProfilepicUrl() {
            return profilepicUrl;
        }

        public void setProfilepicUrl(String profilepicUrl) {
            this.profilepicUrl = profilepicUrl;
        }
    }

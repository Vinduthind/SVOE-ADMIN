package co.techxpose.firebaseui;

/**
 * Created by vindu-thind on 17/3/17.
 */

public class Blog {
    private  String Title,Desc,image;

    public Blog()
    {


    }


    public Blog(String title, String desc, String image) {
        Title = title;
        Desc = desc;
        this.image = image;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}

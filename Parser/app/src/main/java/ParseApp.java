import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by מנהל on 12/12/2015.
 */
public class ParseApp extends Application {

    public void onCreate(){
        super.onCreate();

        Parse.initialize(this, "VxNKZoBXZHTyFmx3cRhLFpGOQNMl9NMySrE6PiLP", "lZaGELhSL2Fon7Kd7TyouMBaA4zrBPg1Hm5GQYu2");
        ParseUser.enableAutomaticUser();
        ParseACL defaultAcl = new ParseACL();
        defaultAcl.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultAcl,true);
    }

}

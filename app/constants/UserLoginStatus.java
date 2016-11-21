package constants;

/**
 * Created by eduardo on 1/11/14.
 */
public enum UserLoginStatus
{
    ACTIVE(0),
    EXPIRED(1),
    NEW(2),
    OUT(3);

    public Integer name;

    UserLoginStatus (Integer n){
        this.name = n;
    }
}

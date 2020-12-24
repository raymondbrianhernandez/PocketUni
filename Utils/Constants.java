package raymondhernandez.pocketuniv.Utils;
import raymondhernandez.pocketuniv.BuildConfig;

/**
 * Created 3/26/2016.
 */
public final class Constants {

    public static final String KEY_SIGNUP_EMAIL = "SIGNUP_EMAIL";

    public static final String FIREBASE_PROPERTY_TIMESTAMP="timestamp";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";
    public static final String FIREBASE_PROPERTY_USER_HAS_LOGGED_IN_WITH_PASSWORD = "hasLoggedInWithPassword";

    public static final String FIREBASE_LOCATION_USERS="Students";
    public static final String FIREBASE_LOCATION_UID_MAPPINGS = "Student_UID_Mappings";
    public static final String FIREBASE_LOCATION_PROFESSORS="Professors";
    public static final String FIREBASE_LOCATION_BOOKS="Books";
    public static final String FIREBASE_LOCATION_COURSES="Courses";
    public static final String FIREBASE_LOCATION_RATINGS="Ratings";
    public static final String FIREBASE_LOCATION_RATINGS_PROFESSORS="Professors";
    public static final String FIREBASE_LOCATION_RATINGS_BOOKS="Books";
    public static final String FIREBASE_LOCATION_CHATS="Chats";
    public static final String FIREBASE_LOCATION_CHATROOMS="Chatrooms";
    public static final String FIREBASE_LOCATION_FRIENDS="Friends";
    public static final String FIREBASE_LOCATION_ASSIGNMENTS="Assignments";
    public static final String FIREBASE_LOCATION_MARKET = "Market";

    public static final String KEY_PROVIDER = "PROVIDER";
    public static final String KEY_ENCODED_EMAIL = "ENCODED_EMAIL";


    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_URL_PROFESSORS = FIREBASE_URL + "/" + FIREBASE_LOCATION_PROFESSORS;
    public static final String FIREBASE_URL_BOOKS = FIREBASE_URL + "/" + FIREBASE_LOCATION_BOOKS;
    public static final String FIREBASE_URL_COURSES = FIREBASE_URL + "/" + FIREBASE_LOCATION_COURSES;
    public static final String FIREBASE_URL_RATINGS = FIREBASE_URL + "/" + FIREBASE_LOCATION_RATINGS;
    public static final String FIREBASE_URL_CHATROOMS = FIREBASE_URL + "/" + FIREBASE_LOCATION_CHATROOMS;
    public static final String FIREBASE_URL_FRIENDS = FIREBASE_URL + "/" + FIREBASE_LOCATION_FRIENDS;
    public static final String FIREBASE_URL_ASSIGNMENTS=FIREBASE_URL+ "/" + FIREBASE_LOCATION_ASSIGNMENTS;
    public static final String FIREBASE_URL_MARKET=FIREBASE_URL+ "/" + FIREBASE_LOCATION_MARKET;

    public static final String PASSWORD_PROVIDER = "password";

    public static final String INTENT_EXTRA_STUDENT_NAME = "StudentName";
}

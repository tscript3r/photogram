package pl.tscript3r.photogram.api.v1.controllers;

public class MappingsConsts {

    private static final String BASE_MAPPING = "/api/v1";

    static final String ID_VARIABLE_MAPPING = "{id}";
    static final String ID_POST_VARIABLE_MAPPING = "{postId}";
    static final String ID_POST_VARIABLE = "postId";
    static final String ID_VARIABLE = "id";

    public static final String OWN_PARAM = "own";
    public static final String USERNAME_PARAM = "username";
    public static final String EMAIL_PARAM = "email";
    public static final String ID_PARAM = "id";

    public static final String USER_MAPPING = BASE_MAPPING + "/users";
    public static final String LOGIN_MAPPING = USER_MAPPING + "/login";
    public static final String POST_MAPPING = BASE_MAPPING + "/posts";
    public static final String COMMENT_MAPPING = POST_MAPPING + "/" + ID_POST_VARIABLE_MAPPING + "/comments";

    private MappingsConsts() {
    }

}

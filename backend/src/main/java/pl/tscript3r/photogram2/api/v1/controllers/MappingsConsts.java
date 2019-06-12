package pl.tscript3r.photogram2.api.v1.controllers;

public class MappingsConsts {

    private static final String BASE_MAPPING = "/api/v1";

    public static final String USER_MAPPING = BASE_MAPPING + "/users";
    public static final String POST_MAPPING = BASE_MAPPING + "/posts";
    public static final String COMMENT_MAPPING = POST_MAPPING + "/{postId}/comments";

    static final String ID_VARIABLE_MAPPING = "{id}";
    static final String ID_VARIABLE = "id";

    public static final String OWN_PARAM = "own";
    public static final String USERNAME_PARAM = "username";
    public static final String EMAIL_PARAM = "email";
    public static final String ID_PARAM = "id";

    private MappingsConsts() {
    }

}

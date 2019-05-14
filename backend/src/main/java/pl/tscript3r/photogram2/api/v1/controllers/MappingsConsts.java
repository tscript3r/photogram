package pl.tscript3r.photogram2.api.v1.controllers;

public class MappingsConsts {

    private static final String BASE_MAPPING = "/api/v1";

    public static final String USER_MAPPING = BASE_MAPPING + "/users";
    static final String POST_MAPPING = USER_MAPPING + "/{userId}/posts";

    private MappingsConsts() {
    }

}

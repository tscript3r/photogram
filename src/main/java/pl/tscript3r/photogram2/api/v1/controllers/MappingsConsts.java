package pl.tscript3r.photogram2.api.v1.controllers;

class MappingsConsts {

    private static final String BASE_MAPPING = "/api/v1";

    static final String USER_MAPPING = BASE_MAPPING + "/users";
    static final String POST_MAPPING = BASE_MAPPING + "/posts";
    static final String COMMENT_POST_MAPPING = POST_MAPPING + "/{id}/comments";

    private MappingsConsts() {
    }

}

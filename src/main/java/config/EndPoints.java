package config;

public enum EndPoints {

    CREATE_CUSTOMERS("/customers"),
    GET_CUSTOMERS_BY_ID("/customers/{id}"),
    GET_CUSTOMERS_BY_PHONE_NUMBER("/customers/filter"),

    DELETE_CUSTOMERS("/customers/{id}");
    private final String path;

    EndPoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

package hexlet.code.utilit;

public class NamedRoutes {
    public static String mainPage() {
        return "/";
    }

    public static String urlsPage() {
        return "/urls";
    }

    public static String urlPage(String id) {
        return "/urls/" + id;
    }

    public static String urlPage(long id) {
        return urlPage(String.valueOf(id));
    }

    public static String urlChecks(String id) {
        return "/urls/" + id + "/checks";
    }

    public static String urlChecks(long id) {
        return urlChecks(String.valueOf(id));
    }
}


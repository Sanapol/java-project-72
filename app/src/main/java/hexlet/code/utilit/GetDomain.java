package hexlet.code.utilit;

public class GetDomain {
    public static String get(String url) {
        String[] domainStart = url.split("//");
        String[] domainEnd = domainStart[1].split("/");
        return domainStart[0] + "//" + domainEnd[0];
    }
}

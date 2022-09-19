package dwuthk.search.external;

public interface DefaultClient {
    default Throwable defaultRetryFallbackException(Throwable e) {
//
//        if (e.getMessage().contains("Circuit")) {
//            return new RuntimeException(e);
//        }

        System.out.println("When Retry Callback Exception : " + e.getClass().getSimpleName());

        return new RuntimeException("Api Client Call Retry Failed.");
    }

    default Throwable defaultCircuitBreakFallbackException(Throwable e) {
        //return new RuntimeException("Api Client Call Circuit Broke");
        System.out.println("When Circuit Break Callback Exception : " + e.getClass().getSimpleName());
        return e;
    }

}

package vector.lkawa.year_estimator.util;

import java.util.ResourceBundle;

public interface Message {

    ResourceBundle bundle = ResourceBundle.getBundle("messages");

    interface API {
        interface Exception {
            static String noContentException() {
                return bundle.getString("api.exception.noContentException");
            }

            static String externalServiceError() {
                return bundle.getString("api.exception.externalServiceError");
            }
        }
    }
}

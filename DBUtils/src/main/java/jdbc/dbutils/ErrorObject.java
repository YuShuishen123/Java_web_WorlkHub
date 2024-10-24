package jdbc.dbutils;

import lombok.Data;

@Data
public class ErrorObject {
    private int errorCode;
    private String errorMessage;
}

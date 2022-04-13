package test.rest_template_test.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Author: khamza@nightwell-logistics.com
 * Date: 2/8/2022
 * Time: 6:17 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private boolean success;
    private String message;
    private Object data;
    private List<Object> dataList;
    private HttpStatus status;


    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
        if(success)
            this.status = HttpStatus.OK;
    }

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        if(data instanceof List){
            this.dataList = (List) data;
        }else {
            this.data = data;
        }
        if(success)
            this.status = HttpStatus.OK;
    }

    public Response(boolean success, String message, Object data, HttpStatus status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
    }
}

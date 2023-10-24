package com.ali.pay.entity;
import lombok.Data;
import lombok.NoArgsConstructor;

//<T>是一种泛型占位符，表示定义一个泛型类型。在RestBean类中，<T>表示RestBean是一个泛型类，T是泛型的占位符，可以在实例化RestBean对象时替换成具体的类型。这样可以让RestBean类适用于不同类型的数据处理，提高了代码的可重用性和灵活性。
@Data
@NoArgsConstructor
public class RestBean<T> {
    private int status;
    private boolean success;
    private String message;
    private int code;

    private Object data;

    public RestBean(int status, boolean success, String message , Object data){
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = status*100;
    }

    public static <T> RestBean<T> success(){
        return new RestBean<>(200,true,null,null);
    }

    public static <T> RestBean<T> error(String msg){
        RestBean restBean = new RestBean();
        restBean.message = msg;
        restBean.status = 0;//表示失败
        return restBean;
    }

    public  static <T> RestBean<T> success(String message){
        return new RestBean<>(200,true,message,null);
    }

    public  static <T> RestBean<T> success(String message,Object data){
        return new RestBean<>(200,true,message,data);
    }
    public  static <T> RestBean<T> success(Object data){
        return new RestBean<>(200,true,null,data);
    }

    public  static <T> RestBean<T> failure(int status){
        return new RestBean<>(status,false,null,null);
    }

    public  static <T> RestBean<T> failure(int status,String message){
        return new RestBean<>(status,false,message,null);
    }

}

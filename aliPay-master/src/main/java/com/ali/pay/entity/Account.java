package com.ali.pay.entity;

import lombok.Data;

//@Data注解实际上是一个组合注解，包含了@Getter、@Setter、@ToString、@EqualsAndHashCode和@RequiredArgsConstructor等注解的功能
///Lombok会自动生成JavaBean所需的getter、setter、toString等方法的代码
@Data
public class Account {
    int id;
    String username;
    String Password;
}

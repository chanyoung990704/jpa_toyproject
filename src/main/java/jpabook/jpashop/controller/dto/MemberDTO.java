package jpabook.jpashop.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberDTO {


    @NotEmpty(message = "이름은 필수적입니다.")
    private String name;

    private String city;

    private String street;

    private String zipcode;


}

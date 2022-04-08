package com.liseInfotech.reqresTest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class DataField {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}


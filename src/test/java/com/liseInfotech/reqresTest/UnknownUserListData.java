package com.liseInfotech.reqresTest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnknownUserListData {

    private int page;
    private int per_page;
    private int total;
    private int total_pages;

    private List<UnknownUserList> data;
    private SupportData support;
}

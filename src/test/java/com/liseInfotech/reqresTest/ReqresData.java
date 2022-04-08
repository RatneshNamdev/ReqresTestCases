package com.liseInfotech.reqresTest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ReqresData {
    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private ArrayList<DataField> data;
    private SupportData support;
}

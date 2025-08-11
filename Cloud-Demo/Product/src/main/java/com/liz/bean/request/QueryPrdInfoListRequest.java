package com.liz.bean.request;

import lombok.Data;

@Data
public class QueryPrdInfoListRequest extends BasePageRequest{
    String keyFuzzy;
    int keyId;
}

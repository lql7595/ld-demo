package com.liz.bean.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class BasePageRequest extends BaseRequest {
    private Integer size = 10;  // 默认每页10条
    private Integer cur = 1;    // 默认第1页

    public Page getPage() {
        Page<Object> page = new Page<>();
        page.setCurrent(cur);
        page.setSize(size);
        return page;
    }
}

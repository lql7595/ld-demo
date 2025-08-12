package com.liz.feign;

import com.liz.bean.request.BaseRequest;
import com.liz.bean.response.BaseResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "demo-user", path = "/demo/user/sys")
public interface UserServiceFeign {

    @GetMapping("/internal/role/rela")
    Set<String> getRoleRela(@RequestParam("role") String role);
}

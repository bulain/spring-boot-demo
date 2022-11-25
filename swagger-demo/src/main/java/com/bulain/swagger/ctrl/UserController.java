package com.bulain.swagger.ctrl;

import com.bulain.core.pojo.BaseResp;
import com.bulain.core.pojo.DataResp;
import com.bulain.core.pojo.ListResp;
import com.bulain.swagger.pojo.UserInfo;
import com.bulain.swagger.pojo.UserSearch;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("/swagger")
@Controller
public class UserController {

    @Operation(tags = "用户列表", summary = "根据查询条件获得用户列表")
    @GetMapping(value = "list")
    @ResponseBody
    public ListResp list(UserSearch search) {
        return ListResp.ok(new ArrayList<UserInfo>());
    }

    @Operation(tags = "用户保存", summary = "保存用户信息")
    @PostMapping(value = "save")
    @ResponseBody
    public DataResp save(UserInfo data) {
        return DataResp.ok(new UserInfo());
    }

    @Operation(tags = "用户查看", summary = "根据用户ID查看用户信息")
    @GetMapping(value = "view/{id}")
    @ResponseBody
    public DataResp view(@PathVariable("id") Long id) {
        return DataResp.ok(new UserInfo());
    }

    @Operation(tags = "用户删除", summary = "根据用户ID删除用户信息")
    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    public BaseResp delete(@PathVariable("id") Long id) {
        return BaseResp.ok();
    }

}

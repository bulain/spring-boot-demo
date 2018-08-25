package com.bulain.swagger.ctrl;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bulain.core.pojo.BaseResp;
import com.bulain.core.pojo.DataResp;
import com.bulain.core.pojo.ListResp;
import com.bulain.swagger.pojo.UserInfo;
import com.bulain.swagger.pojo.UserSearch;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/swagger")
@Controller
public class UserController {

	@ApiOperation(value = "用户列表", notes = "根据查询条件获得用户列表")
	@RequestMapping(value = {"list"}, method = {RequestMethod.GET})
	@ResponseBody
	public ListResp list(UserSearch search) {
		return ListResp.ok(new ArrayList<UserInfo>());
	}

	@ApiOperation(value = "用户保存", notes = "保存用户信息")
	@RequestMapping(value = "save", method = {RequestMethod.POST})
	@ResponseBody
	public DataResp save(UserInfo data) {
		return DataResp.ok(new UserInfo());
	}

	@ApiOperation(value = "用户查看", notes = "根据用户ID查看用户信息")
	@RequestMapping(value = "view/{id}", method = {RequestMethod.GET})
	@ResponseBody
	public DataResp view(@PathVariable("id") Long id) {
		return DataResp.ok(new UserInfo());
	}

	@ApiOperation(value = "用户删除", notes = "根据用户ID删除用户信息")
	@RequestMapping(value = "delete/{id}", method = {RequestMethod.DELETE})
	@ResponseBody
	public BaseResp delete(@PathVariable("id") Long id) {
		return BaseResp.ok();
	}

}

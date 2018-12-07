package com.nantian.employeeinfo.controller;

import com.nantian.employeeinfo.domain.User;
import com.nantian.employeeinfo.service.UserService;
import com.nantian.employeeinfo.util.DateUtils;
import com.nantian.employeeinfo.util.ExcelUtils;
import com.nantian.employeeinfo.util.RandomWordUtils;
import com.nantian.employeeinfo.util.UUIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/addForm")
	public String addForm() throws Exception {
		return "employeeAdd";
	}

	@RequestMapping("/editForm")
	public String editForm(String id, Model model) throws Exception {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "employeeEdit";
	}

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public User insert(User user) {
		try {
			user.setId(UUIdUtils.next());
			userService.insert(user);
			return userService.findById(user.getId());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//导入
	@PostMapping(value = "/batchImport")
	public String batchImportUserKnowledge(@RequestParam(value="filename") MultipartFile file,
		   HttpServletRequest request) {
		HttpSession session = request.getSession();
		//判断文件是否为空
		if(file==null){
			session.setAttribute("msg","文件不能为空！");
			return "redirect:/user/list";
		}
		//获取文件名
		String fileName=file.getOriginalFilename();
		//验证文件名是否合格
		if(!ExcelUtils.validateExcel(fileName)){
			session.setAttribute("msg","文件必须是excel格式！");
			return "redirect:/user/list";
		}
		//进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
		long size=file.getSize();
		if(StringUtils.isEmpty(fileName) || size==0){
			session.setAttribute("msg","文件不能为空！");
			return "redirect:/user/list";
		}
		//批量导入
		String message = userService.batchImport(fileName,file);
		session.setAttribute("msg",message);
		return "redirect:/user/list";
	}


	/**
	 * 用户列表
	 * @param user
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(User user, Model model, HttpServletRequest request) throws Exception {
		List<User> list = userService.findList(user);
		model.addAttribute("list", list);
		String beginTime = "", endTime = "";
		if(user.getBeginTime() != null)
			beginTime = DateUtils.dateToString(user.getBeginTime());
		if(user.getEndTime() != null)
			endTime = DateUtils.dateToString(user.getEndTime());

		//判断session中是否有msg，有就放入model
		String msg = (String) request.getSession().getAttribute("msg");
		model.addAttribute("msg", msg == null ? "" : msg);
		model.addAttribute("beginTime", beginTime);
		model.addAttribute("endTime", endTime);
		return "employeeList";
	}

	@RequestMapping("/update")
	public String update(User user) {
		userService.update(user);
		return "redirect:/user/list";
	}

	/**
	 * 删除用户
	 * @param ids 待删除用户id串
	 * @return
	 */
	@RequestMapping("/delete")
	public String delete(String ids) {
		String[] idArr = ids.split(",");
		if(idArr != null) {
			for (int i = 0; i < idArr.length; i++) {
				userService.delete(idArr[i]);
			}
		}
		return "redirect:/user/list";
	}

	@RequestMapping("/addSomeUser")
	public void addSomeUser() throws Exception {
		for(int i = 0; i< 100; i ++) {
			Date beginTime = RandomWordUtils.nextDate();
			Date endTime = RandomWordUtils.nextDate();
			if(beginTime.getTime() > endTime.getTime()) {
				Date temp = new Date();
				temp = endTime;
				endTime = beginTime;
				beginTime = temp;
			}
			int workStatus = (int)(Math.random() * 2);
			User user = new User(UUIdUtils.next(), RandomWordUtils.nextName(),
					beginTime, endTime, workStatus);
			userService.insert(user);
		}
	}

}

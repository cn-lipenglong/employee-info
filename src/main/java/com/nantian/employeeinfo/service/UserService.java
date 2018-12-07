package com.nantian.employeeinfo.service;

import com.nantian.employeeinfo.domain.User;
import com.nantian.employeeinfo.persistence.UserPersistence;
import com.nantian.employeeinfo.util.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserPersistence userPersistence;

	public int insert(User user) throws Exception {
		return userPersistence.insert(user);
	}

	public User findById(String id) throws Exception {
		return userPersistence.selectById(id);
	}

	public List<User> findList(User user) throws Exception {
		return userPersistence.selectList(user);
	}

	public int delete(String id) {
		return userPersistence.deleteById(id);
	}

	public int update(User user) {
		return userPersistence.updateByPrimaryKeySelective(user);
	}

	/**
	 * 上传excel文件到临时目录后并开始解析
	 * @param fileName
	 * @return
	 */
	public String batchImport(String fileName,MultipartFile mfile){
		File uploadDir = new  File("D:\\fileupload");
		//创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
		if (!uploadDir.exists()) uploadDir.mkdirs();
		//新建一个文件
		File tempFile = new File("D:\\fileupload\\" + new Date().getTime() + ".xlsx");
		//初始化输入流
		InputStream is = null;
		try{
			//将上传的文件写入新建的文件中
			mfile.transferTo(tempFile);

			//根据新建的文件实例化输入流
			is = new FileInputStream(tempFile);

			//根据版本选择创建Workbook的方式
			Workbook wb = null;
			//根据文件名判断文件是2003版本还是2007版本
			if(ExcelUtils.isExcel2007(fileName)){
				wb = new XSSFWorkbook(is);
			}else{
				wb = new HSSFWorkbook(is);
			}
			//根据excel里面的内容读取知识库信息
			return readExcelValue(wb,tempFile);
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			if(is !=null)
			{
				try{
					is.close();
				}catch(IOException e){
					is = null;
					e.printStackTrace();
				}
			}
		}
		return "导入出错！请检查数据格式！";
	}


	/**
	 * 解析Excel里面的数据
	 * @param wb
	 * @return
	 */
	private String readExcelValue(Workbook wb, File tempFile) throws Exception{
		//错误信息接收器
		String errorMsg = "";
		//得到第一个shell
		Sheet sheet=wb.getSheetAt(0);
		//得到Excel的行数
		int totalRows=sheet.getPhysicalNumberOfRows();
		//总列数
		int totalCells = 0;
		//得到Excel的列数(前提是有行数)，从第二行算起
		if(totalRows>=2 && sheet.getRow(1) != null){
			totalCells=sheet.getRow(1).getPhysicalNumberOfCells();
		}
		List<User> userList=new ArrayList<User>();
		User user;
		String br = "<br/>";
		//循环Excel行数,从第二行开始。标题不入库
		for(int r=1;r<totalRows;r++){
			String rowMessage = "";
			Row row = sheet.getRow(r);
			if (row == null){
				errorMsg += br+"第"+(r+1)+"行数据有问题，请仔细检查！";
				continue;
			}
			user = new User();
			String username = "";
			String beginTime = "";
			String endTime = "";
			Integer workStatus;
			//循环Excel的列
			for(int c = 0; c <totalCells; c++){
				Cell cell = row.getCell(c);
				if (null != cell){
					//cell.setCellType(Cell.CELL_TYPE_STRING);
					if(c==0){
						username = cell.getStringCellValue();
						if(StringUtils.isEmpty(username)){
							rowMessage += "名字不能为空；";
						}else if(username.length()>10){
							rowMessage += "名字的字数不能超过10；";
						}
						user.setUsername(username);
					}else if(c==1){
						//System.out.println(cell.getCellStyle().getDataFormatString());
						beginTime = cell.getStringCellValue();
						if(StringUtils.isEmpty(beginTime)){
							rowMessage += "开始时间不能为空；";
						}
						user.setBeginTime(new Date(beginTime));
					}else if(c==2){
						endTime = cell.getStringCellValue();
						if(StringUtils.isEmpty(endTime)){
							rowMessage += "结束时间不能为空；";
						}
						user.setEndTime(new Date(endTime));
					}else if(c==3){
						workStatus = Integer.parseInt(cell.getStringCellValue());
						if(workStatus == null){
							rowMessage += "结束时间不能为空；";
						}
						user.setWorkStatus(workStatus);
					}
				}else{
					rowMessage += "第"+(c+1)+"列数据有问题，请仔细检查；";
				}
			}
			//拼接每行的错误提示
			if(!StringUtils.isEmpty(rowMessage)){
				errorMsg += br+"第"+(r+1)+"行，"+rowMessage;
			}else{
				userList.add(user);
			}
		}
		//删除上传的临时文件
		if(tempFile.exists()){
			tempFile.delete();
		}
		//全部验证通过才导入到数据库
		if(StringUtils.isEmpty(errorMsg)){
			for(User users : userList){
				this.insert(users);
			}
			errorMsg = "导入成功，共"+userList.size()+"条数据！";
		}
		return errorMsg;
	}

}

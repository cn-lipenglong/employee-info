# employee-info

### 运行方式
1. clone到本地，导入idea
2. 本地MySQL创建名为employee_info的数据库
3. 运行sql文件夹下的数据库初始化文件
3. 启动项目
4. 访问接口http://localhost:8088/user/addSomeUser
    - 该接口往数据库插入100条测试数据
5. 访问http://localhost:8088/user/list
    - 查看列表页
6. 剩下的别的功能可以自己体验

### 目前还存在以下问题
- 导入Excel时日期格式待处理，已经找到解决方法
- 员工列表分页没做
- 员工列表名称模糊查询没做（如果有需要）
- 导出功能没有做

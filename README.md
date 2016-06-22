## 温州大学城市学院教务系统助手 v1.0


### 实现功能

1. 模拟登陆教务系统
2. 查看当前学期课程信息
3. 查看历年成绩信息
4. 给我留言

---

### 实现流程

1. 向服务器发送get请求，获取cookie，在网页中提取出__VIEWSTATE值
2. 带上cookie向服务器验证码地址发送get请求，下载验证码
3. 带上txtUserName、TextBox2、txtSecretCode、__VIEWSTATE、RadioButtonList1、Button1，设置Origin、cookie向服务器发送post请求
4. 登陆成功后跳转到主页面，若为第一次登陆则进行数据初始化操作（数据的获取、解析和储存到数据库）
5. 获取成绩信息时，需要先向 http://jw1.wucc.cn/xscjcx.aspx?xh=用户学号xsxh&xm=用户姓名&gnmkdm=N121605发送get请求，获取到查询成绩的__VIEWSTATE值
6. 课表查询、成绩查询如果有需要都可手动更新

---

### 版本预览 v2.0

1. 解决2G网络下连接、下载验证码、登陆可能出错问题
2. 实现可离线查看功能
3. 实现自动更新数据
4. 实现教室申请、网上选课、一键教师评分
5. 完善课程、成绩查询（可根据学年、学期查询）
6. 实现图书查询功能
7. 实现群聊天功能

---

### 页面截图

- 登陆页面

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/%E7%99%BB%E9%99%86.png" width = "40%" />

- 首页

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/%E9%A6%96%E9%A1%B5.png" width = "40%" />

- NavigationView

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/NavigationView.png" width = "40%" />

- 课表查询

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/%E8%AF%BE%E8%A1%A8%E6%9F%A5%E8%AF%A2.png" width = "40%" />

- 课表更新

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/%E8%AF%BE%E8%A1%A8%E6%9B%B4%E6%96%B0.png" width = "40%" />

- 成绩查询

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/%E6%88%90%E7%BB%A9%E6%9F%A5%E8%AF%A2.png" width = "40%" />

- 成绩更新

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/%E6%88%90%E7%BB%A9%E6%9B%B4%E6%96%B0.png" width = "40%" />

- 成绩具体信息

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/%E6%88%90%E7%BB%A9%E5%85%B7%E4%BD%93%E4%BF%A1%E6%81%AF.png" width = "40%" />

- 给我留言

<img src="https://github.com/cyjthink/jwzs/blob/master/images-folder/%E7%BB%99%E6%88%91%E7%95%99%E8%A8%80.png" width = "40%" />

---
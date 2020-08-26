#ACCESSIP
CREATE TABLE ACCESSIP (
	id INT PRIMARY KEY AUTO_INCREMENT,
	access_ip VARCHAR(40) NOT NULL, 
	auth_date DATETIME NOT NULL, 
	authorizationer VARCHAR(40) NOT NULL
);

#HOMEPAGE
CREATE TABLE HOMEPAGE (
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	favicon VARCHAR(150) NOT NULL,
	main_url VARCHAR(100) NOT NULL,
	info VARCHAR(100) NOT NULL,
	logo VARCHAR(150) NOT NULL,
	mail VARCHAR(50) NOT NULL,
	phone VARCHAR(50) NOT NULL,
	fax VARCHAR(50) NOT NULL,
	address VARCHAR(150) NOT NULL,
	representative VARCHAR(50) NOT NULL,
	mobile_use INT NOT NULL
);

#MENU
CREATE TABLE MENU (
	menu_idx INT PRIMARY KEY AUTO_INCREMENT,
	add_date DATE NOT NULL,
	group_idx INT NOT NULL, 
	menu_name VARCHAR(100) NOT NULL, 
	menu_type INT NOT NULL,
	modify_date DATE NOT NULL, 
	parent_menu_idx INT NOT NULL, 
	print_seq INT NOT NULL, 
	view_yn INT NOT NULL,
	use_yn INT NOT NULL,
	upload_file_size INT NOT NULL DEFAULT 0,
	menu_info VARCHAR(2000),
	menu_icon VARCHAR(500),
	menu_url VARCHAR(200) NOT NULL
);

#MENUHTML
CREATE TABLE MENUHTML (
	id INT PRIMARY KEY AUTO_INCREMENT,
	menu_idx INT NOT NULL, 
	menu_html LONGTEXT NOT NULL,
	mobile_menu_html LONGTEXT NOT NULL,
	FOREIGN KEY (`menu_idx`) REFERENCES `menu` (`menu_idx`) on delete cascade
);

#CATEGORY
CREATE TABLE CATEGORY (
	category_idx INT PRIMARY KEY AUTO_INCREMENT,
	add_date DATE NOT NULL,
	group_idx INT NOT NULL, 
	category_name VARCHAR(50) NOT NULL, 
	modify_date DATE NOT NULL, 
	parent_category_idx INT NOT NULL, 
	print_seq INT NOT NULL, 
	view_yn INT NOT NULL,
	use_yn INT NOT NULL,
	category_url VARCHAR(500) NOT NULL,
	upload_file_size INT NOT NULL DEFAULT 0,
	level INT NOT NULL DEFAULT 0
);	

#POST
CREATE TABLE POST (
	post_id INT PRIMARY KEY AUTO_INCREMENT,
	menu_id INT NOT NULL,
	main_img VARCHAR(100),
	title VARCHAR(500) NOT NULL,
	contents LONGTEXT NOT NULL,
	mobile_contents LONGTEXT NOT NULL,
	comment_acc INT NOT NULL,
	view_count INT NOT NULL,
	write_date DATE NOT NULL,
	modify_date DATE NOT NULL,
	explanation VARCHAR(2000) DEFAULT NULL
	
	FOREIGN KEY (`menu_id`) REFERENCES `MENU` (`menu_idx`) on delete cascade
);

#POSTCOMMENT
CREATE TABLE POSTCOMMENT (
	comment_id INT PRIMARY KEY AUTO_INCREMENT,
	post_id INT NOT NULL,
	writter_ip_add VARCHAR(50) NOT NULL,
	modify_ip_add VARCHAR(50) NOT NULL,
	reply_to INT NOT NULL,
	contents VARCHAR(2000) NOT NULL,
	write_date DATE NOT NULL,
	modify_date DATE NOT NULL,
	
	FOREIGN KEY (`post_id`) REFERENCES `POST` (`post_id`) on delete cascade
);

#POSTFILE
CREATE TABLE POSTFILE (
	post_file_id INT PRIMARY KEY AUTO_INCREMENT,
	post_id INT NOT NULL,
	file VARCHAR(100) NOT NULL,
	
	FOREIGN KEY (`post_id`) REFERENCES `POST` (`post_id`) on delete cascade
);

#ADMIN
CREATE TABLE ADMIN (
	admin_id INT PRIMARY KEY AUTO_INCREMENT,
	admin_level INT NOT NULL DEFAULT 1,
	admin_account VARCHAR(30) NOT NULL,
	admin_password VARCHAR(500) NOT NULL,
	admin_name VARCHAR(30)  NOT NULL,
	admin_email VARCHAR(100)  NOT NULL,
	admin_phone VARCHAR(20)  NOT NULL,
	admin_create VARCHAR(30)  NOT NULL,
	admin_modify VARCHAR(30)  NOT NULL,
	uuid VARCHAR(150)
);

#최고관리자 비밀번호 supersuper00!
INSERT INTO Admin (admin_level, admin_account, admin_password, admin_name, admin_email, admin_create, admin_modify, uuid, admin_phone) VALUES (10, 'superadmin', '16EC9BF12878DE53068C192284A71AA0', '최고관리자', 'nomail@nomail.com', '1999-01-01 00:00:00', '1999-01-01 00:00:00', null, '0111235678');
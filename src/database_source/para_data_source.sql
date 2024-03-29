#Hire
CREATE TABLE HIRE (
	id INT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(500) NOT NULL, 
	info MEDIUMTEXT NOT NULL, 
	personnel INT NOT NULL,
	start_date DATE NOT NULL, 
	end_date DATE NOT NULL, 
	phone_certified INT NOT NULL, 
	interview INT NOT NULL,
	portfolio INT NOT NULL,
	gender_use INT NOT NULL,
	email_use INT NOT NULL,
	address_use INT NOT NULL,
	activation INT NOT NULL
);

#QUESTION
CREATE TABLE QUESTION (
	id INT PRIMARY KEY AUTO_INCREMENT,
	type INT NOT NULL,
	content VARCHAR(500) NOT NULL,
	example VARCHAR(2000)
);

#ANSWER
CREATE TABLE ANSWER (
	id INT PRIMARY KEY AUTO_INCREMENT,
	question_id INT NOT NULL,
	content VARCHAR(2000) NOT NULL,
	reply INT NOT NULL,
	FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) on delete cascade
);

#QnHJOIN
CREATE TABLE QnHJOIN (
	id INT PRIMARY KEY AUTO_INCREMENT,
	question_id INT NOT NULL,
	hire_id INT NOT NULL,
	FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) on delete cascade,
	FOREIGN KEY (`hire_id`) REFERENCES `hire` (`id`) on delete cascade
);

#VOLUNTEER
CREATE TABLE VOLUNTEER (
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	phone VARCHAR(30) NOT NULL,
	gender INT,
	email VARCHAR(50),
	address VARCHAR(100),
	post VARCHAR(20)
); 

#VOLUNTEER RESULT
CREATE TABLE VOLUNTEERRESULT (
	id INT PRIMARY KEY AUTO_INCREMENT,
	hire_id INT NOT NULL,
	volunteer_id INT NOT NULL,
	file VARCHAR(500),
	result INT NOT NULL,
	insert_date DATE NOT NULL,
	modify_date DATE NOT NULL,
	
	FOREIGN KEY (`volunteer_id`) REFERENCES `VOLUNTEER` (`id`) on delete cascade,
	FOREIGN KEY (`hire_id`) REFERENCES `hire` (`id`) on delete cascade
);

#HIRERESULT
CREATE TABLE HIRERESULT (
	id INT PRIMARY KEY AUTO_INCREMENT,
	volunteer_result_id INT NOT NULL,
	question_id INT NOT NULL,
	answer_id INT DEFAULT 0,
	open_answer MEDIUMTEXT,
	
	FOREIGN KEY (`volunteer_result_id`) REFERENCES `VOLUNTEERRESULT` (`id`) on delete cascade,
	FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) on delete cascade
);

#LANDINGQUESTION
CREATE TABLE LANDINGQUESTION (
	question_id INT PRIMARY KEY AUTO_INCREMENT,
	question_title VARCHAR(2000) NOT NULL,
	question_contents MEDIUMTEXT
);
INSERT INTO LANDINGQUESTION (question_title, question_contents) VALUES ('default', null);
UPDATE LANDINGQUESTION SET question_id = 0;


#LANDINGANSWER
CREATE TABLE LANDINGANSWER (
	answer_id INT PRIMARY KEY AUTO_INCREMENT,
	question_id INT NOT NULL,
	pring_seq INT NOT NULL,
	answer_title VARCHAR(2000),
	answer_img VARCHAR(150),
	event_function VARCHAR(100),
	event_img VARCHAR(200),
	
	FOREIGN KEY (`question_id`) REFERENCES `LANDINGQUESTION` (`question_id`) on delete cascade
);

INSERT INTO LANDINGANSWER (question_id, pring_seq, answer_title, answer_img, event_function, event_img) VALUES (0,  0, 'default', null, null, null);
UPDATE LANDINGANSWER SET answer_id = 0;

#LANDINGQUESTIONCHAIN
CREATE TABLE LANDINGQUESTIONCHAIN (
	chain_id INT PRIMARY KEY AUTO_INCREMENT,
	question_id INT NOT NULL,
	answer_id INT NOT NULL,
	
	FOREIGN KEY (`question_id`) REFERENCES `LANDINGQUESTION` (`question_id`) on delete cascade,
	FOREIGN KEY (`answer_id`) REFERENCES `LANDINGANSWER` (`answer_id`) on delete cascade
);

#LANDINGPARTICIPANT
CREATE TABLE LANDINGPARTICIPANT (
	participant_id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(200) NOT NULL,
	company VARCHAR(300) NOT NULL,
	ip_address VARCHAR(500) NOT NULL,
	record VARCHAR(700) NOT NULL,
	start_time VARCHAR(1000) NOT NULL,
	end_time VARCHAR(1000) NOT NULL
);


#CONTACT
CREATE TABLE CONTACT (
	contact_id INT PRIMARY KEY AUTO_INCREMENT,
	participant_id INT NOT NULL,
	reservation_date VARCHAR(50) NOT NULL,
	reservation_type INT NOT NULL,
	reservation_memo MEDIUMTEXT,
	reservation_pw   VARCHAR(20) NOT NULL,
	contact_Date DATE NOT NULL,
	
	FOREIGN KEY (`participant_id`) REFERENCES `LANDINGPARTICIPANT` (`participant_id`) on delete cascade
);

#MENU
CREATE TABLE WORKMENU (
	menu_idx INT PRIMARY KEY AUTO_INCREMENT,
	add_date DATE NOT NULL,
	group_idx INT NOT NULL, 
	menu_name VARCHAR(50) NOT NULL, 
	modify_date DATE NOT NULL, 
	parent_menu_idx INT NOT NULL, 
	print_seq INT NOT NULL, 
	view_yn INT NOT NULL,
	use_yn INT NOT NULL
);

CREATE TABLE WORKPOST (
	post_id INT PRIMARY KEY AUTO_INCREMENT,
	menu_idx INT NOT NULL,
	post_password VARCHAR(20),
	post_title VARCHAR(150) NOT NULL,
	post_contents LONGTEXT NOT NULL,
	writter VARCHAR(100) NOT NULL,
	write_date VARCHAR(50) NOT NULL,
	modify_date VARCHAR(50) NOT NULL,
	
	FOREIGN KEY (`menu_idx`) REFERENCES `WORKMENU` (`menu_idx`) on delete cascade
);
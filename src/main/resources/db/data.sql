insert into University (univ_id, name, email_domain) values (1, '홍익대', 'g.hongik.ac.kr');
insert into University (univ_id, name, email_domain) values (2, '숙명여대', 'g.sookmyung.ac.kr');
insert into University (univ_id, name, email_domain) values (3, '명지대', 'g.mju.ac.kr');
insert into University (univ_id, name, email_domain) values (4, '성신여대', 'g.sungshin.ac.kr');

insert into user (user_id, birth, email, gender, password, phone_number, univ) values (10, "2000-03-16", "chlwogns316", "M", "1234", "01035810915", "홍익대학교");
insert into user (user_id, birth, email, gender, password, phone_number, univ) values (20, "2000-05-10", "email", "F", "1234", "01012341234", "숙명여대");

insert into category(category_id, name) values (0, "카테고리1");

insert into Post (post_id, context, like_number, tag1, tag2, tag3, tag4, tag5, title, user_id, hits, category_id) values (10000, "게시글1-1", 0, "태그1", "태그2", "태그3", "태그4", "태그5", "게시글 1번", 1, 0, 0);

insert into chatroom(chatroom_id, post_user_email, chatroom_uuid, post_id, request_user_id) values (1000, "gyun1712@gmail.com", "chatTest", 10000, 20);

insert into user_chatroom(user_chatroom_id, opponent_user_email, chatroom_id, user_id, created_date) values(5000, "gyun1712@gmail.com", 1000, 20, "2023-01-01T01:01:12");
insert into user_chatroom(user_chatroom_id, opponent_user_email, chatroom_id, user_id, created_date) values(5001, "email", 1000, 1, "2023-01-01T01:01:12");

insert into message(message_id, created_date, content, sender_email, chatroom_id) values (100, "2023-01-01T01:01:12", "ㅎㅇ", "email", 1000);
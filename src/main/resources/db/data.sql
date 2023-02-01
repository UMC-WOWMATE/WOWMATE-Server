insert into User (user_id, birth, email, gender, password, phone_number, univ) values (10, "2000-03-16", "chlwogns316", "M", "1234", "01035810915", "홍익대학교");
insert into User (user_id, birth, email, gender, password, phone_number, univ) values (20, "2000-05-10", "email", "F", "1234", "01012341234", "숙명여대");

insert into University (univ_id, name, email_domain) values (1, '홍익대', 'g.hongik.ac.kr');
insert into University (univ_id, name, email_domain) values (2, '숙명여대', 'g.sookmyung.ac.kr');
insert into University (univ_id, name, email_domain) values (3, '명지대', 'g.mju.ac.kr');
insert into University (univ_id, name, email_domain) values (4, '성신여대', 'g.sungshin.ac.kr');

-- insert into Post (post_id, context, like_number, tag1, tag2, tag3, tag4, tag5, title, user_id, hits) values (10000, "게시글1-1", 0, "태그1", "태그2", "태그3", "태그4", "태그5", "게시글 1번", 10, 0);
--
--
-- insert into create_chatroom(create_chatroom_id, post_user_email, post_id, user_id) values (1, "chlwogns316", 10000, 20);
--
-- insert into chatroom(chatroom_id, opponent_user_email, user_email, chatroom_uuid, create_chatroom_id) values (1, "chlwogns316", "email", "chatTest1", 1);
-- insert into chatroom(chatroom_id, opponent_user_email, user_email, chatroom_uuid, create_chatroom_id) values (2, "email", "chlwogns316", "chatTest2", 1);


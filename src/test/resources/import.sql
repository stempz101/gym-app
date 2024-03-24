insert into _user (id, first_name, last_name, username, password, is_active) values (1, 'Michael', 'Patel', 'Michael.Patel', 'uTlOfjgZg2', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (2, 'Michael', 'Patel', 'Michael.Patel1', 'QZ2TWVlyFX', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (3, 'Christopher', 'Lee', 'Christopher.Lee4', 'mVsYvgvjmQ', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (4, 'Sarah', 'Nguyen', 'Sarah.Nguyen', 'RQBNpYJ3Yb', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (5, 'John', 'Doe', 'John.Doe', 'zpxZzJQ3gv', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (6, 'Jessica', 'Rodriguez', 'Jessica.Rodriguez2', '94c7sHTeY0', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (7, 'Daniel', 'Thompson', 'Daniel.Thompson', 'ZKj9nuryNC', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (8, 'Max', 'Simons', 'Max.Simons', 'YIUYdasd23', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (9, 'Rodrigo', 'Jimenez', 'Rodrigo.Jimenez', 'JLfgsh89aP', false);

alter sequence _user_seq restart with (select max(id) + 50 from _user);

insert into jwt_token (id, token, is_revoked, user_id) values (1, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNaWNoYWVsLlBhdGVsIiwiaWF0IjoxNzExMTM5Mzg1LCJleHAiOjE3MTExODI1ODV9.ca6b0zn92mHL7OYuvXxbg-8I6Tr0fIiQ7860pXPXRkU', true, 1);
insert into jwt_token (id, token, is_revoked, user_id) values (2, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNaWNoYWVsLlBhdGVsIiwiaWF0IjoxNzExMTM5MzgyLCJleHAiOjE3MTExODI1ODV9.wRGyQJcx_vJxuypQvpK_rzPFg-lVBfYs7puJwfp3668', false, 1);
insert into jwt_token (id, token, is_revoked, user_id) values (3, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNaWNoYWVsLlBhdGVsIiwiaWF0IjoxNzExMTM5MzgxLCJleHAiOjE3MTExODI1ODV9.TgJW3s_yc6QPjpAjzME_1fgoMEmoObjd_KgcY5iov9s', false, 1);

alter sequence jwt_token_seq restart with (select max(id) + 50 from jwt_token);

insert into training_type (id, training_type) values (1, 'Bodybuilding');
insert into training_type (id, training_type) values (2, 'CrossFit');
insert into training_type (id, training_type) values (3, 'Strength Training');

alter sequence training_type_seq restart with (select max(id) + 50 from training_type);

insert into trainee (id, user_id, date_of_birth, address) values (1, 1, '2001-07-09', 'Deribasovska St, 1, Odesa');
insert into trainee (id, user_id, date_of_birth, address) values (2, 2, '2000-03-20', 'Hrets''ka St, 56, Odesa');
insert into trainee (id, user_id, date_of_birth, address) values (3, 3, '1999-09-05', 'Kanatna St, 4, Odesa');
insert into trainee (id, user_id, date_of_birth, address) values (4, 4, '1996-10-08', 'Preobrazhens''ka St, 33, Odesa');

alter sequence trainee_seq restart with (select max(id) + 50 from trainee);

insert into trainer (id, user_id, training_type_id) values (1, 5, 1);
insert into trainer (id, user_id, training_type_id) values (2, 6, 2);
insert into trainer (id, user_id, training_type_id) values (3, 7, 3);
insert into trainer (id, user_id, training_type_id) values (4, 8, 1);

alter sequence trainer_seq restart with (select max(id) + 50 from trainer);

insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (1, 1, 2, 2, 'Cross #1', '2024-02-06', 60);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (2, 2, 3, 3, 'Strength #1', '2024-02-06', 90);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (3, 3, 1, 1, 'Bodybuilding #1', '2024-02-07', 80);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (4, 4, 3, 3, 'Strength #2', '2024-02-07', 110);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (5, 2, 1, 1, 'Bodybuilding #2', '2024-02-10', 110);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (6, 3, 2, 2, 'Cross #2', '2024-02-12', 110);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (7, 3, 4, 1, 'Bodybuilding #3', '2024-02-13', 110);

alter sequence training_seq restart with (select max(id) + 50 from training);

insert into trainee_trainer (trainee_id, trainer_id) values (1, 2);
insert into trainee_trainer (trainee_id, trainer_id) values (2, 3);
insert into trainee_trainer (trainee_id, trainer_id) values (3, 1);
insert into trainee_trainer (trainee_id, trainer_id) values (4, 3);
insert into trainee_trainer (trainee_id, trainer_id) values (2, 1);
insert into trainee_trainer (trainee_id, trainer_id) values (3, 2);
insert into trainee_trainer (trainee_id, trainer_id) values (3, 4);

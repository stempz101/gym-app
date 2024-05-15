insert into _user (id, first_name, last_name, username, password, is_active) values (1, 'Michael', 'Patel', 'Michael.Patel', '$2a$10$DyR01231PwkSd8nPN6cRguYPBAx6gyfv3gMEcLdggSHV/d1fhYvhG', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (2, 'Michael', 'Patel', 'Michael.Patel1', '$2a$10$d6H9SWzRBybxywH4Qli3V.Ekbjep4yr/mawMrJvd7TBwoYL3FWqC6', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (3, 'Christopher', 'Lee', 'Christopher.Lee4', '$2a$10$qs9rviwCZR8UIYwUmSA85.7jBVPmrQh4fAl.mUoKThGQJG/OVUBri', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (4, 'Sarah', 'Nguyen', 'Sarah.Nguyen', '$2a$10$G3ZmpVzQMFa8Cr8KvdZN/OTYKheYDUiujRxVFWYrZwv9WOHAdEDKm', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (5, 'John', 'Doe', 'John.Doe', '$2a$10$xxJe4Zl2I6fjtAdI/vbgZu4s//nCo2EqxTdbZaqDlcFXKsIj15fke', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (6, 'Jessica', 'Rodriguez', 'Jessica.Rodriguez2', '$2a$10$YAJS0a2DgvJqznlWuy9FruMvlLXXp1NrP47qqLxixxxmtHGzGqGIi', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (7, 'Daniel', 'Thompson', 'Daniel.Thompson', '$2a$10$SN0uJ60yovfzrPpugrkTSuOHpkjwO66p7SFj2iJPV59g1k9CzGLvG', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (8, 'Max', 'Simons', 'Max.Simons', '$2a$10$BE8dz9a6F9/WJAfsTrrHc.FWC5kw5mwfajWAC7sUfXfdvS36ejIYu', true);
insert into _user (id, first_name, last_name, username, password, is_active) values (9, 'Rodrigo', 'Jimenez', 'Rodrigo.Jimenez', '$2a$10$1KCJJt2P6aAT5qiP2/NV5eHVe6OZTSJC7KVzzovAfu1b5nnfl.Htm', false);

alter sequence _user_seq restart with 60;

insert into training_type (id, training_type) values (1, 'Bodybuilding');
insert into training_type (id, training_type) values (2, 'CrossFit');
insert into training_type (id, training_type) values (3, 'Strength Training');

alter sequence training_type_seq restart with 54;

insert into trainee (id, user_id, date_of_birth, address) values (1, 1, '2001-07-09', 'Deribasovska St, 1, Odesa');
insert into trainee (id, user_id, date_of_birth, address) values (2, 2, '2000-03-20', 'Hrets''ka St, 56, Odesa');
insert into trainee (id, user_id, date_of_birth, address) values (3, 3, '1999-09-05', 'Kanatna St, 4, Odesa');
insert into trainee (id, user_id, date_of_birth, address) values (4, 4, '1996-10-08', 'Preobrazhens''ka St, 33, Odesa');

alter sequence trainee_seq restart with 54;

insert into trainer (id, user_id, training_type_id) values (1, 5, 1);
insert into trainer (id, user_id, training_type_id) values (2, 6, 2);
insert into trainer (id, user_id, training_type_id) values (3, 7, 3);
insert into trainer (id, user_id, training_type_id) values (4, 8, 1);

alter sequence trainer_seq restart with 54;

insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (1, 1, 2, 2, 'Cross #1', '2024-02-06', 60);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (2, 2, 3, 3, 'Strength #1', '2024-02-06', 90);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (3, 3, 1, 1, 'Bodybuilding #1', '2024-02-07', 80);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (4, 4, 3, 3, 'Strength #2', '2024-02-07', 110);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (5, 2, 1, 1, 'Bodybuilding #2', '2024-02-10', 110);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (6, 3, 2, 2, 'Cross #2', '2024-02-12', 110);
insert into training (id, trainee_id, trainer_id, training_type_id, training_name, training_date, training_duration) values (7, 3, 4, 1, 'Bodybuilding #3', '2024-02-13', 110);

alter sequence training_seq restart with 58;

insert into trainee_trainer (trainee_id, trainer_id) values (1, 2);
insert into trainee_trainer (trainee_id, trainer_id) values (2, 3);
insert into trainee_trainer (trainee_id, trainer_id) values (3, 1);
insert into trainee_trainer (trainee_id, trainer_id) values (4, 3);
insert into trainee_trainer (trainee_id, trainer_id) values (2, 1);
insert into trainee_trainer (trainee_id, trainer_id) values (3, 2);
insert into trainee_trainer (trainee_id, trainer_id) values (3, 4);

insert into vine (abv, amount, color, country, grape_name, is_sparkling, manufacturer, name, price, region, sugar, vine.`'year'`, sold_wine)
values (10, 100, 'RED', 'UKRAINE', 'MERLOT', true, 'KOLONIST', 'MERLOT_VINE', 1000, 'KHERSON', 'SWEET', '2021', 12);
insert into vine (abv, amount, color, country, grape_name, is_sparkling, manufacturer, name, price, region, sugar, vine.`'year'`, sold_wine)
values (10, 100, 'ORANGE', 'UKRAINE', 'ZINFANDEL', true, 'KOLONIST', 'ZINFANDEL_VINE', 1000, 'KHERSON', 'DRY', '2000', 12);
insert into vine (abv, amount, color, country, grape_name, is_sparkling, manufacturer, name, price, region, sugar, vine.`'year'`, sold_wine)
values (10, 100, 'WHITE', 'UKRAINE', 'CABERNET SAUVIGNON', true, 'KOLONIST', 'CABERNET_VINE', 1000, 'KHERSON', 'SWEET', '2020', 12);
insert into vine (abv, amount, color, country, grape_name, is_sparkling, manufacturer, name, price, region, sugar, vine.`'year'`, sold_wine)
values (10, 100, 'RED', 'UKRAINE', 'CABERNET SAUVIGNON', true, 'KOLONIST', 'CABERNET_VINE', 1000, 'KHERSON', 'SEMI-SWEET', '2013', 12);
insert into vine (abv, amount, color, country, grape_name, is_sparkling, manufacturer, name, price, region, sugar, vine.`'year'`, sold_wine)
values (10, 100, 'WHITE', 'UKRAINE', 'CABERNET SAUVIGNON', true, 'KOLONIST', 'MERLOT_VINE', 1000, 'KHERSON', 'SWEET', '2024', 12);
insert into vine (abv, amount, color, country, grape_name, is_sparkling, manufacturer, name, price, region, sugar, vine.`'year'`, sold_wine)
values (10, 100, 'ROSE', 'UKRAINE', 'CABERNET SAUVIGNON', true, 'KOLONIST', 'MERLOT_VINE', 1000, 'KHERSON', 'DRY', '2001', 12);

insert into post_office (city, office_address, office_number)
values ('Lviv', 'Pakova, 9', 1);
insert into post_office (city, office_address, office_number)
values ('Kyiv', 'Naukova, 2', 10);
insert into post_office (city, office_address, office_number)
values ('Ternopil', 'Opilska, 4', 4);

insert into client_order (datetime, home_address, phone_number, post_office_id, user_email, user_first_name, user_last_name, sum, user_id)
values ('2024-06-18T17:35:36.520764', 'Lviv, Kopernyka str, 90', '+38056893456', null, 'colombo@gmail.com', 'John', 'Bybko', 10000, 1);
insert into order_vine (vine_amount, order_id, vine_id)
values (10, 1, 1);

insert into client_order (datetime, home_address, phone_number, post_office_id, user_email, user_first_name, user_last_name, sum, user_id)
values ('2024-06-18T17:38:30.989434', 'Lutsk, Shevchenko str, 14', '+38056893456', null, 'colombo@gmail.com', 'John', 'Bybko', 5000, 1);
insert into order_vine (vine_amount, order_id, vine_id)
values (5, 2, 2);

insert into client_order (datetime, home_address, phone_number, post_office_id, user_email, user_first_name, user_last_name, sum, user_id)
values ('2024-06-18T17:39:39.100984', null, '+38056893456', 1, 'colombo@gmail.com', 'John', 'Bybko', 7000, 1);
insert into order_vine (vine_amount, order_id, vine_id)
values (7, 3, 1);

insert into client_order (datetime, home_address, phone_number, post_office_id, user_email, user_first_name, user_last_name, sum, user_id)
values ('2024-06-18T17:40:38.526087', null, '+38056893456', 2, 'colombo@gmail.com', 'John', 'Bybko', 1000, 1);
insert into order_vine (vine_amount, order_id, vine_id)
values (1, 4, 1);
-- Creating a Reports View
CREATE VIEW `reports_history_view` AS
	SELECT sender AS 'user_id', id AS 'report_id' , message AS 'report_msg', timestamp
		FROM `wms`.`reports`;

-- Creating a Requests View
CREATE VIEW `requests_view` AS
           SELECT
               `requests`.`id` AS `request_id`,
               `requests`.`sender` AS `sender`,
               `requests`.`receiver` AS `receiver`,
               `requests`.`product_id` AS `product_id`,
               `requests`.`quantity` AS `quantity`,
               `requests`.`timestamp` AS `timestamp`,
               `requests`.`status` AS `status`
           FROM
               `requests`

-- Creating an Approved Requests View
CREATE VIEW `approved_requests_view` AS
	SELECT id as 'request_id', sender, receiver, product_id, quantity, timestamp
		FROM `wms`.`requests`
        WHERE status='APPROVED'

-- Creating a Declined Requests View
CREATE VIEW `denied_requests_view` AS
	SELECT id as 'request_id', sender, receiver, product_id, quantity, timestamp
		FROM `wms`.`requests`
        WHERE status='DENIED'

-- Creating a Pending Requests View
CREATE VIEW `pending_requests_view` AS
	SELECT id as 'request_id', sender, receiver, product_id, quantity, timestamp
		FROM `wms`.`requests`
        WHERE status='PENDING'

-- Creating a Daily Stats View
CREATE VIEW `daily_stats` AS
	SELECT count('req') AS "Number Of Requests", count('rep') AS "Number Of Reports", count('app_req') AS "Number Of Approved Requests", count('den_req') AS "Number Of Denied Requests"
    FROM (SELECT id FROM `wms`.`requests` WHERE timestamp=CURDATE()) AS req,
    (SELECT id FROM `wms`.`reports` WHERE timestamp=CURDATE()) AS rep,
    (SELECT id FROM `wms`.`requests` WHERE timestamp=CURDATE() AND status='APPROVED') AS app_req,
    (SELECT id FROM `wms`.`requests` WHERE timestamp=CURDATE() AND status='DENIED') AS den_req

-- Creating a Current Users View
CREATE VIEW `current_users_view` AS
	SELECT id AS 'user_id', display_name AS 'name', position
    FROM `wms`.`users`
    WHERE status='APPROVED'

-- Creating a Pending Users View
CREATE VIEW `pending_users_view` AS
	SELECT id AS 'user_id', display_name AS 'Name', position
    FROM `wms`.`users`
    WHERE status='PENDING'

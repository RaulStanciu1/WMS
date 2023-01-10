-- GENERAL STATISTICS VIEW
CREATE VIEW `daily_statistics` AS
	SELECT  now() as "Current Date",
		count(case when status='APPROVED' then 1 else null end) as "Approved Requests",
		count(case when status='PENDING' then 1 else null end) as "Pending Requests",
		count(case when status='DENIED' then 1 else null end) as "Denied Requests",
		count(case when true then 1 else null end) as "Total Requests"
	FROM wms.requests


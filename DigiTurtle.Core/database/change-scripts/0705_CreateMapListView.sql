CREATE VIEW [CW].[MapList] AS (
	SELECT MapTitle, MapId FROM DigiTurtle.[CW].[WorldMapping] GROUP BY MapTitle, MapId
);

GO
-----------------------------------------------------------
-- Create the Matchmaking tables
--   CW.Match(Id, Type, Time, Status, Filename) [Type in {OnDemand, Scheduled}, Status in {Pending, New, InProgress, Complete}]
--   CW.MatchUser(MatchId, UserId, Team, UserState) [State in {Pending, Accepted, Declined}]
--   CW.MatchLobby(UserId, State) [State in {OutOfLobby, Random}]
-----------------------------------------------------------

PRINT '-- Creating table CW.Match';

CREATE TABLE DigiTurtle.[CW].[Match] (
	Id UNIQUEIDENTIFIER default NEWID(),
	Type nvarchar(100),
	Time smalldatetime,
	Status nvarchar(100),
	Filename nvarchar(256)
);

PRINT '-- Creating table CW.MatchUser';

CREATE TABLE DigiTurtle.[CW].[MatchUser] (
	MatchId UNIQUEIDENTIFIER NOT NULL,
	UserId UNIQUEIDENTIFIER NOT NULL,
	Team int,
	UserState nvarchar(100)
);

PRINT '-- Creating table CW.MatchLobby';

CREATE TABLE DigiTurtle.[CW].[MatchLobby] (
	UserId UNIQUEIDENTIFIER NOT NULL,
	State nvarchar(100)
);

PRINT '-- Creating view CW.MatchesWithUser';

GO

CREATE VIEW [CW].[MatchesWithUser] AS SELECT * FROM (
	SELECT * FROM DigiTurtle.[CW].[Match] Match INNER JOIN DigiTurtle.[CW].[MatchUser] MatchUser ON Match.Id = MatchUser.MatchId
) AS MatchesWithUser;

GO
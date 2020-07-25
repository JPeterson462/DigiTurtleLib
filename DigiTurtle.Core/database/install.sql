PRINT '-- Setting up Database Dev';
USE DigiTurtle;
GO
-----------------------------------------------------------
-- Create the schema and tables
--   CW.User (Id, Username, Coins)
--   CW.Tile (Id, OwnerId, Filename)
--   CW.WorldMapping (MapId, TileId)
-----------------------------------------------------------
IF EXISTS (SELECT name FROM sys.schemas WHERE name = N'CW')
	BEGIN
		PRINT '-- Removing existing schema CW'
		DROP SCHEMA [CW]
	END
GO

PRINT '-- Creating schema CW'
GO

CREATE SCHEMA [CW] AUTHORIZATION [dbo]
GO

IF EXISTS (SELECT name from sys.tables WHERE name = 'User')
	BEGIN
		PRINT '-- Removing existing table CW.User'
		DROP TABLE DigiTurtle.[CW].[User]
	END
GO

PRINT '-- Creating table CW.User';

CREATE TABLE DigiTurtle.[CW].[User] (
	Id UNIQUEIDENTIFIER PRIMARY KEY default NEWID(),
	Username nvarchar(100),
	Coins int default 0
);

IF EXISTS (SELECT name from sys.tables WHERE name = 'Tile')
	BEGIN
		PRINT '-- Removing existing table CW.Tile'
		DROP TABLE DigiTurtle.[CW].[Tile]
	END
GO

PRINT '-- Creating table CW.Tile';

CREATE TABLE DigiTurtle.[CW].[Tile] (
	Id UNIQUEIDENTIFIER PRIMARY KEY default NEWID(),
	OwnerId UNIQUEIDENTIFIER,
	Filename nvarchar(100), 
);

IF EXISTS (SELECT name from sys.tables WHERE name = 'WorldMapping')
	BEGIN
		PRINT '-- Removing existing table CW.WorldMapping'
		DROP TABLE DigiTurtle.[CW].[WorldMapping]
	END
GO

PRINT '-- Creating table CW.WorldMapping';

CREATE TABLE DigiTurtle.[CW].[WorldMapping] (
	MapTitle nvarchar(100),
	MapId UNIQUEIDENTIFIER,
	TileId UNIQUEIDENTIFIER,
	PRIMARY KEY (MapId, TileId)
);

PRINT '-- Creating table CW.AccessRole';

CREATE TABLE DigiTurtle.[CW].[AccessRole] (
	Name nvarchar(100),
	Level int,
	Password nvarchar(64),
	Salt nvarchar(32)
);

PRINT '-- Creating table CW.Permission';

CREATE TABLE DigiTurtle.[CW].[Permission] (
	Name nvarchar(100),
	Level int
);

GO

INSERT INTO DigiTurtle.[CW].[User] (Id, Username, Coins) VALUES ('67AFB64D-2436-4104-840B-8FEC389C6C41', 'JPeterson', 5000);
INSERT INTO DigiTurtle.[CW].[User] (Id, Username, Coins) VALUES (NEWID(), 'Admin', 0);
INSERT INTO DigiTurtle.[CW].[User] (Id, Username, Coins) VALUES ('F62D70B7-0A02-496C-8565-4976FF96FB48', 'Test', 5000);

INSERT INTO DigiTurtle.[CW].[Resources] (UserId) VALUES ('67AFB64D-2436-4104-840B-8FEC389C6C41');
INSERT INTO DigiTurtle.[CW].[Resources] (UserId) VALUES ('F62D70B7-0A02-496C-8565-4976FF96FB48');

INSERT INTO DigiTurtle.[CW].[WorldMapping] (MapId, TileId, MapTitle)
	VALUES ('E36681EC-41B1-4A09-BB4C-4525561B6659', '20088D01-8B85-41EE-98F7-1CC803E31990', 'Default');
INSERT INTO DigiTurtle.[CW].[Tile] (Id, OwnerId, Filename, Q, R)
	VALUES ('20088D01-8B85-41EE-98F7-1CC803E31990', '7AFB64D-2436-4104-840B-8FEC389C6C41', 'tile_0_0.json', 0, 0);
	
INSERT INTO DigiTurtle.[CW].[WorldMapping] (MapId, TileId, MapTitle)
	VALUES ('E36681EC-41B1-4A09-BB4C-4525561B6659', 'D3E961B1-E2C2-47DA-B396-8E4e8DADB0C8', 'Default');
INSERT INTO DigiTurtle.[CW].[Tile] (Id, OwnerId, Filename, Q, R)
	VALUES ('D3E961B1-E2C2-47DA-B396-8E4e8DADB0C8', 'F62D70B7-0A02-496C-8565-4976FF96FB48', 'tile_1_0.json', 1, 0);

PRINT '-- Adding columns Q (int) and R (int) to CW.Tile';

ALTER TABLE DigiTurtle.[CW].[Tile] ADD Q int;
ALTER TABLE DigiTurtle.[CW].[Tile] ADD R int;

-----------------------------------------------------------
-- Create the Resources tables
--   CW.Resources (UserId,
--					BatteryBasic, BatteryHighCapacity,
--					WeaponryAmmunition, WeaponryArmorPlating,
--					TechnologySpeed1, ...,
--					TechnologyFiringRate1, ...,
--					TechnologyAccuracy1, ...,
--					BotInfantry, BotArmored, BotCommander, BotSpecialOps)
-----------------------------------------------------------

PRINT '-- Creating table CW.Resources';

CREATE TABLE DigiTurtle.[CW].[Resources] (
	UserId UNIQUEIDENTIFIER,
	BatteryBasic int default 0,
	BatteryHighCapacity int default 0,
	WeaponryAmmunition int default 0,
	WeaponryArmorPlating int default 0,
	TechnologySpeed1 int default 0,
	TechnologyFiringRate1 int default 0,
	TechnologyAccuracy1 int default 0,
	BotInfantry int default 0,
	BotArmored int default 0,
	BotCommander int default 0,
	BotSpecialOps int default 0,
);

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

CREATE PROCEDURE RandomMatch @Teams int, @UsersPerTeam int
AS
BEGIN TRAN

DECLARE @NumUsers int;
SET @NumUsers = @Teams * @UsersPerTeam;

DECLARE @TargetState nvarchar(100);
SET @TargetState = 'Random_' + CAST(@Teams AS varchar(16)) + '_' + CAST(@UsersPerTeam as varchar(16));

SELECT TOP (@NumUsers) * FROM DigiTurtle.[CW].[MatchLobby] WHERE State = @TargetState;
IF @@ROWCOUNT = @NumUsers
BEGIN
	DECLARE @MatchId UNIQUEIDENTIFIER;
	SET @MatchId = NEWID();
	
	INSERT INTO DigiTurtle.[CW].[Match] (Id, Time, Type, Status, Filename) VALUES (@MatchId, GETDATE(), 'OnDemand', 'Pending', NULL);
	
	DECLARE @TeamId int;
	SET @TeamId = 1;

	DECLARE @DefaultUserStatus nvarchar(100);
	SET @DefaultUserStatus = 'Pending';
	
	DECLARE @UsersInTeam TABLE(MatchId UNIQUEIDENTIFIER, UserId UNIQUEIDENTIFIER, Team int, UserStatus nvarchar(100));
	
	WHILE @TeamId <= @Teams
	BEGIN
		DELETE FROM @UsersInTeam;
		INSERT INTO @UsersInTeam(MatchId, UserId, Team, UserStatus) (
			SELECT TOP (@UsersPerTeam) @MatchId AS MatchId, UserId, @TeamId as Team, @DefaultUserStatus as UserStatus FROM (
				SELECT UserId, State FROM (
					SELECT TOP (@NumUsers) UserId, State FROM DigiTurtle.[CW].[MatchLobby] ORDER BY NEWID()
				) AS UsersOnTeam WHERE State = @TargetState
			) AS UsersInLobby
		);
		INSERT INTO DigiTurtle.[CW].[MatchUser] (MatchId, UserId, Team, UserState) (SELECT * FROM @UsersInTeam);
		UPDATE DigiTurtle.[CW].[MatchLobby] SET State = 'Matched' WHERE UserId IN (SELECT UserId FROM @UsersInTeam);

		SET @TeamId = @TeamId + 1;
	END
	
END

COMMIT TRAN
GO;

PRINT '-- Creating table CW.Permission';

CREATE TABLE DigiTurtle.[CW].[Permission] (
	Name nvarchar(100),
	Level int
);

PRINT '-- Populating table CW.Permission';

INSERT INTO DigiTurtle.[CW].[Permission] (Name, Level) VALUES ('PublicData', 1);
INSERT INTO DigiTurtle.[CW].[Permission] (Name, Level) VALUES ('User.Read', 10);
INSERT INTO DigiTurtle.[CW].[Permission] (Name, Level) VALUES ('User.Write', 15);
INSERT INTO DigiTurtle.[CW].[Permission] (Name, Level) VALUES ('System.Write', 25);

PRINT '-- Creating table CW.AccessRole';

CREATE TABLE DigiTurtle.[CW].[AccessRole] (
	Name nvarchar(100),
	Level int,
	Password nvarchar(64),
	Salt nvarchar(32)
);

PRINT '-- Populating table CW.AccessRole';

INSERT INTO DigiTurtle.[CW].[AccessRole] (Name, Level, Password, Salt) VALUES (
	'Client', 15, 'a01656a6b3f0d6aa37f883b411c9a18496c57cb35b41df03fa44ce9c09558adc', 'c2e494ff91be927424d89b68f92505ad'
);
INSERT INTO DigiTurtle.[CW].[AccessRole] (Name, Level, Password, Salt) VALUES (
	'ProducerServer', 25, '7824da3b79f3eb7570082e9f2f4746062801cb1cb97e75c8e844f9da5e5842cf', '0b426e75c7a1732478b5a5f333da4aba'
);
INSERT INTO DigiTurtle.[CW].[AccessRole] (Name, Level, Password, Salt) VALUES (
	'BattleServer', 25, '095a89f699b4da3c2fb1eea6d35330a2c4d1b1fe26664f2b6397e6f693c8cf88', 'ef3245ecf098d09f3a8d48b6b479f983'
);

PRINT '-- Creating view CW.RolePermission';

GO

CREATE VIEW [CW].[RolePermission] AS (
	SELECT 
	Access.Name AS RoleName, Access.Password AS Password, Access.Salt AS Salt, Permission.Name AS PermissionName
	FROM DigiTurtle.[CW].[AccessRole] Access INNER JOIN DigiTurtle.[CW].[Permission] Permission ON Access.Level >= Permission.Level
);

GO

CREATE PROCEDURE ComputeMatchState @MatchId UNIQUEIDENTIFIER
AS
BEGIN TRAN

DECLARE @NumUsers int;
DECLARE @NumPending int;
DECLARE @NumAccepted int;
DECLARE @NumDeclined int;

SELECT * FROM DigiTurtle.[CW].[MatchUser] WHERE MatchId = @MatchId;
SET @NumUsers = @@RowCount;

SELECT * FROM DigiTurtle.[CW].[MatchUser] WHERE MatchId = @MatchId AND UserState = 'Pending';
SET @NumPending = @@RowCount;

SELECT * FROM DigiTurtle.[CW].[MatchUser] WHERE MatchId = @MatchId AND UserState = 'Accepted';
SET @NumAccepted = @@RowCount;

SELECT * FROM DigiTurtle.[CW].[MatchUser] WHERE MatchId = @MatchId AND UserState = 'Declined';
SET @NumDeclined = @@RowCount;

DECLARE @ComputedStatus nvarchar(100);

IF @NumAccepted = @NumUsers
BEGIN
	SET @ComputedStatus = 'New';
END

IF @NumPending > 0
BEGIN
	SET @ComputedStatus = 'Pending';
END

IF @NumDeclined > 0
BEGIN
	SET @ComputedStatus = 'Cancelled';
END

UPDATE DigiTurtle.[CW].[Match] SET Status = @ComputedStatus WHERE Id = @MatchId;

COMMIT TRAN
GO

CREATE VIEW [CW].[MapList] AS (
	SELECT MapTitle, MapId FROM DigiTurtle.[CW].[WorldMapping] GROUP BY MapTitle, MapId
);

GO

PRINT '-- Populating CW.AccessRole';

INSERT INTO DigiTurtle.[CW].AccessRole (Name, Level, Password, Salt)
	VALUES ('Client', 15, 'a01656a6b3f0d6aa37f883b411c9a18496c57cb35b41df03fa44ce9c09558adc', 'c2e494ff91be927424d89b68f92505ad');
	
INSERT INTO DigiTurtle.[CW].AccessRole (Name, Level, Password, Salt)
	VALUES ('ProducerServer', 25, '7824da3b79f3eb7570082e9f2f4746062801cb1cb97e75c8e844f9da5e5842cf', '0b426e75c7a1732478b5a5f333da4aba');
	
INSERT INTO DigiTurtle.[CW].AccessRole (Name, Level, Password, Salt)
	VALUES ('BattleServer', 25, '095a89f699b4da3c2fb1eea6d35330a2c4d1b1fe26664f2b6397e6f693c8cf88', 'ef3245ecf098d09f3a8d48b6b479f983');

PRINT '-- Populating CW.Permission';

INSERT INTO DigiTurtle.[CW].Permission (Name, Level)
	VALUES ('PublicData', 1);
	
INSERT INTO DigiTurtle.[CW].Permission (Name, Level)
	VALUES ('User.Read', 10);
	
INSERT INTO DigiTurtle.[CW].Permission (Name, Level)
	VALUES ('User.Write', 15);
	
INSERT INTO DigiTurtle.[CW].Permission (Name, Level)
	VALUES ('System.Write', 25);

